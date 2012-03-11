/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.rhino;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.e4.languages.javascript.debug.connect.SocketTransportService;
import org.eclipse.e4.languages.javascript.debug.connect.DebugRuntime;
import org.eclipse.e4.languages.javascript.debug.connect.Connection;
import org.eclipse.e4.languages.javascript.debug.connect.DisconnectException;
import org.eclipse.e4.languages.javascript.debug.connect.EventPacket;
import org.eclipse.e4.languages.javascript.debug.connect.Request;
import org.eclipse.e4.languages.javascript.debug.connect.Response;
import org.eclipse.e4.languages.javascript.debug.connect.TimeoutException;
import org.eclipse.e4.languages.javascript.debug.connect.TransportService;
import org.eclipse.e4.languages.javascript.debug.connect.TransportService.ListenerKey;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

/**
 * Rhino implementation of {@link Debugger}
 * 
 * @since 1.0
 */
public class RhinoDebugger implements Debugger, ContextFactory.Listener, Runnable {

	private static final String SUSPEND = "suspend"; //$NON-NLS-1$
	private static final String ADDRESS = "address"; //$NON-NLS-1$
	private static final String SOCKET = "socket"; //$NON-NLS-1$
	private static final String TRANSPORT = "transport"; //$NON-NLS-1$

	private final Thread requestHandlerThread = new Thread(this, "RhinoDebugger - Request Handler"); //$NON-NLS-1$
	private final RequestHandler requestHandler = new RequestHandler(this);

	private volatile boolean shutdown = false;
	private DebugRuntime runtime;

	private final Map threadToThreadId = new HashMap();
	private final Map threadIdToData = new HashMap();
	private final Map scripts = new HashMap();
	private final Map debuggableScripts = new HashMap();
	private final Map breakpoints = new HashMap();

	private long currentThreadId = 0L;
	private long currentBreakpointId = 0L;
	private long currentScriptId = 0L;
	private ArrayList disabledThreads = new ArrayList();
	private final TransportService transportService;
	private final String address;
	private boolean startSuspended;
	private ListenerKey listenerKey;
	private volatile Connection connection;

	/**
	 * Constructor
	 * 
	 * @param configString
	 */
	public RhinoDebugger(String configString) {
		Map config = parseConfigString(configString);

		StringBuffer buffer = new StringBuffer();
		buffer.append("\nRhino attaching debugger\n"); //$NON-NLS-1$

		buffer.append("Start at time: ").append(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(Calendar.getInstance().getTime())); //$NON-NLS-1$
		buffer.append("\nListening to "); //$NON-NLS-1$
		String transport = (String) config.get(TRANSPORT);
		if (SOCKET.equals(transport)) {
			this.transportService = new SocketTransportService();
			buffer.append("socket on "); //$NON-NLS-1$
		} else {
			// TODO NLS this
			throw new IllegalArgumentException("transport: "+ transport); //$NON-NLS-1$
		}
		this.address = (String) config.get(ADDRESS);
		buffer.append("port ").append(this.address); //$NON-NLS-1$
		this.startSuspended = Boolean.valueOf((String) config.get(SUSPEND)).booleanValue();
		System.err.println(buffer.toString());
	}

	/**
	 * Parses the command line configuration string
	 * 
	 * @param configString
	 * @return the map of command line args
	 */
	private static Map parseConfigString(String configString) {
		Map config = new HashMap();
		StringTokenizer tokenizer = new StringTokenizer(configString, ","); //$NON-NLS-1$
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			int equalsIndex = token.indexOf('=');
			if (equalsIndex == -1)
				config.put(token, null);
			else
				config.put(token.substring(0, equalsIndex), token.substring(equalsIndex + 1));
		}
		return config;
	}

	/**
	 * Constructor
	 * 
	 * @param transportService
	 * @param address
	 * @param startSuspended
	 */
	public RhinoDebugger(TransportService transportService, String address, boolean startSuspended) {
		this.transportService = transportService;
		this.address = address;
		this.startSuspended = startSuspended;
		try {
			if (startSuspended) {
				listenerKey = transportService.startListening(address);
				acceptConnection(300000);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return true if the <code>suspend=true</code> command line argument is set
	 */
	public boolean isStartSuspended() {
		return startSuspended;
	}

	/**
	 * Suspend the debugger waiting for a runtime to connect, polling at the given timeout interval
	 * 
	 * @param timeout
	 * @return true when a runtime has been found
	 */
	public synchronized boolean suspendForRuntime(long timeout) {
		while (runtime == null)
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				// TODO log this
				e.printStackTrace();
			}
		return runtime != null;
	}

	/**
	 * Starts the debugger
	 */
	public void start() {
		try {
			if (listenerKey == null) {
				listenerKey = transportService.startListening(address);
			}
		} catch (IOException e) {
			// TODO log this
			e.printStackTrace();
		}
		requestHandlerThread.start();
	}

	/**
	 * Stops the debugger
	 */
	public void stop() {
		shutdown = true;
		try {
			requestHandlerThread.interrupt();
			requestHandlerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			transportService.stopListening(listenerKey);
		} catch (IOException e) {
			// TODO log this
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while (!shutdown) {
				try {
					acceptConnection(10000);
				} catch (IOException e) {
					if (connection == null)
						continue;
				}
				while (!shutdown && connection.isOpen()) {
					try {
						Request request = runtime.receiveRequest(1000);
						Response response = requestHandler.handleRequest(request);
						runtime.sendResponse(response);
					} catch (TimeoutException e) {
						// ignore
					} catch (DisconnectException e) {
						break;
					}
				}
				closeConnection();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the active connection
	 * 
	 * @throws IOException
	 */
	private void closeConnection() throws IOException {
		if (connection != null) {
			runtime.dispose();
			setRuntime(null);
			connection.close();
			connection = null;
		}
	}

	/**
	 * Waits for a connection for the given timeout
	 * 
	 * @param timeout
	 * @throws IOException
	 */
	private void acceptConnection(long timeout) throws IOException {
		if (connection == null) {
			connection = transportService.accept(listenerKey, timeout, timeout);
			setRuntime(new DebugRuntime(connection));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.debug.Debugger#getFrame(org.mozilla.javascript.Context, org.mozilla.javascript.debug.DebuggableScript)
	 */
	public DebugFrame getFrame(Context context, DebuggableScript debuggableScript) {
		ScriptImpl script = getScript(debuggableScript);
		ContextData contextData = (ContextData) context.getDebuggerContextData();
		ThreadData thread = (ThreadData) threadIdToData.get(contextData.getThreadId());
		return thread.getFrame(context, debuggableScript, script);
	}

	/**
	 * Returns the debuggable script context
	 * 
	 * @param debuggableScript
	 * @return the debuggable script context
	 */
	private ScriptImpl getScript(DebuggableScript debuggableScript) {
		while (!debuggableScript.isTopLevel()) {
			debuggableScript = debuggableScript.getParent();
		}
		return (ScriptImpl) debuggableScripts.get(debuggableScript);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.debug.Debugger#handleCompilationDone(org.mozilla.javascript.Context, org.mozilla.javascript.debug.DebuggableScript, java.lang.String)
	 */
	public void handleCompilationDone(Context context, DebuggableScript debuggableScript, String source) {
		if (!debuggableScript.isTopLevel()) {
			return;
		}
		Long scriptId = nextScriptId();
		ScriptImpl script = new ScriptImpl(scriptId, debuggableScript, source);
		scripts.put(scriptId, script);
		debuggableScripts.put(debuggableScript, script);

		ContextData contextData = (ContextData) context.getDebuggerContextData();
		contextData.scriptLoaded(script);
	}

	/**
	 * Returns the next script id to use
	 * 
	 * @return the next id
	 */
	private synchronized Long nextScriptId() {
		return new Long(currentScriptId++);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ContextFactory.Listener#contextCreated(org.mozilla.javascript.Context)
	 */
	public synchronized void contextCreated(Context context) {
		Thread thread = Thread.currentThread();
		if (disabledThreads.contains(thread)) {
			return;
		}
		Long threadId = (Long) threadToThreadId.get(thread);
		if (threadId == null) {
			threadId = new Long(currentThreadId++);
			threadToThreadId.put(thread, threadId);
		}
		ThreadData threadData = (ThreadData) threadIdToData.get(threadId);
		if (threadData == null) {
			threadData = new ThreadData(threadId, this);
			threadIdToData.put(threadId, threadData);
		}
		threadData.contextCreated(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ContextFactory.Listener#contextReleased(org.mozilla.javascript.Context)
	 */
	public synchronized void contextReleased(Context context) {
		Thread thread = Thread.currentThread();
		if (disabledThreads.contains(thread)) {
			return;
		}
		Long threadId = (Long) threadToThreadId.get(thread);
		if (threadId == null) {
			return;
		}
		ThreadData threadData = (ThreadData) threadIdToData.get(threadId);
		threadData.contextReleased(context);
		if (!threadData.hasContext()) {
			threadToThreadId.remove(thread);
			threadIdToData.remove(threadId);
		}
	}

	/**
	 * Resumes a thread with the given id for the given step type. Has no effect if no such thread exists
	 * 
	 * @param threadId
	 * @param stepType
	 */
	public synchronized void resume(Long threadId, String stepType) {
		ThreadData threadData = (ThreadData) threadIdToData.get(threadId);
		if (threadData != null) {
			threadData.resume(stepType);
		}
	}

	/**
	 * Resumes all threads currently in the debugger
	 */
	public synchronized void resumeAll() {
		for (Iterator it = threadIdToData.keySet().iterator(); it.hasNext();) {
			Long threadId = (Long) it.next();
			resume(threadId, null);
		}
	}

	/**
	 * Suspend the thread with the given id. Has no effect if no such thread exists
	 * 
	 * @param threadId
	 */
	public synchronized void suspend(Long threadId) {
		ThreadData threadData = (ThreadData) threadIdToData.get(threadId);
		if (threadData != null) {
			threadData.suspend();
		}
	}

	/**
	 * Suspend all threads currently in the debugger
	 */
	public synchronized void suspendAll() {
		for (Iterator it = threadIdToData.keySet().iterator(); it.hasNext();) {
			Long threadId = (Long) it.next();
			suspend(threadId);
		}
	}

	/**
	 * Disconnects the debugger
	 */
	public void disconnect() {
	}

	/**
	 * Returns all of the stack frame ids for the thread with the given id. Returns an empty list if no such thread exists, never <code>null</code>
	 * 
	 * @param threadId
	 * @return the complete list of stack frame ids from the thread with the given id
	 */
	public synchronized List getFrameIds(Long threadId) {
		ThreadData threadData = (ThreadData) threadIdToData.get(threadId);
		if (threadData == null) {
			return Collections.EMPTY_LIST;
		}
		return threadData.getFrameIds();
	}

	/**
	 * Returns a {@link DebugFrame} with the given id from the thread with the given thread id. Returns <code>null</code> if the no such thread exists with the given id and / or no such {@link DebugFrame} exists with the given id
	 * 
	 * @param threadId
	 * @param frameId
	 * @return the {@link DebugFrame} with the given id from the thread with the given id
	 */
	public synchronized DebugFrameImpl getFrame(Long threadId, Long frameId) {
		ThreadData threadData = (ThreadData) threadIdToData.get(threadId);
		if (threadData != null) {
			return threadData.getFrame(frameId);
		}
		return null;
	}

	/**
	 * @return the ids of all of the scripts currently known to the debugger
	 */
	public synchronized List getScriptIds() {
		return new ArrayList(scripts.keySet());
	}

	/**
	 * Returns the script with the given id or <code>null</code> if no such script exists with the given id
	 * 
	 * @param scriptId
	 * @return the script with the given id or <code>null</code>
	 */
	public synchronized ScriptImpl getScript(Long scriptId) {
		return (ScriptImpl) scripts.get(scriptId);
	}

	/**
	 * @return the complete collection of breakpoints currently known to the debugger
	 */
	public synchronized Collection getBreakpoints() {
		return breakpoints.keySet();
	}

	/**
	 * Creates a breakpoint in the script with the given id and the given breakpoint attributes. Returns the new breakpoint or <code>null</code> if no such script exists with the given id
	 * 
	 * @param scriptId
	 * @param lineNumber
	 * @param functionName
	 * @param condition
	 * @param threadId
	 * @return the new breakpoint or <code>null</code> if no script exists with the given id
	 */
	public synchronized BreakpointImpl setBreakpoint(Long scriptId, Integer lineNumber, String functionName, String condition, Long threadId) {
		ScriptImpl script = (ScriptImpl) scripts.get(scriptId);
		if (!script.isValid(lineNumber, functionName)) {
			return null;
		}
		BreakpointImpl breakpoint = new BreakpointImpl(nextBreakpointId(), script, lineNumber, functionName, condition, threadId);
		breakpoints.put(breakpoint.getId(), breakpoint);
		script.addBreakpoint(breakpoint);
		return breakpoint;
	}

	/**
	 * @return the next unique breakpoint id to use
	 */
	private synchronized Long nextBreakpointId() {
		return new Long(currentBreakpointId++);
	}

	/**
	 * Clears the breakpoint out of the cache with the given id and returns it. Returns <code>null</code> if no breakpoint exists with the given id.
	 * 
	 * @param breakpointId
	 * @return the removed breakpoint or <code>null</code>
	 */
	public synchronized BreakpointImpl clearBreakpoint(Long breakpointId) {
		BreakpointImpl breakpoint = (BreakpointImpl) breakpoints.remove(breakpointId);
		if (breakpoint != null) {
			ScriptImpl script = breakpoint.getScript();
			script.removeBreakpoint(breakpoint);
		}
		return breakpoint;
	}

	/**
	 * Sends the given {@link EventPacket} using the underlying {@link DebugRuntime} and returns if it was sent successfully
	 * 
	 * @param event
	 * @return true if the event was sent successfully, false otherwise
	 */
	public synchronized boolean sendEvent(EventPacket event) {
		try {
			if (runtime != null) {
				runtime.sendEvent(event);
				return true;
			}
		} catch (DisconnectException e) {
			// ignore
			e.printStackTrace(); // for now for debugging purposes
		}
		return false;
	}

	/**
	 * Sets the {@link DebugRuntime} for this debugger to use
	 * 
	 * @param runtime
	 */
	private synchronized void setRuntime(DebugRuntime runtime) {
		this.runtime = runtime;
		notify();
	}

	/**
	 * Gets a breakpoint with the given id, returns <code>null</code> if no such breakpoint exists with the given id
	 * 
	 * @param breakpointId
	 * @return the breakpoint with the given id or <code>null</code>
	 */
	public BreakpointImpl getBreakpoint(Long breakpointId) {
		return (BreakpointImpl) breakpoints.get(breakpointId);
	}

	/**
	 * Gets the thread for the thread with the given id, returns <code>null</code> if no such thread exists with the goven id
	 * 
	 * @param threadId
	 * @return the thread data for the thread with the given id or <code>null</code>
	 */
	public ThreadData getThreadData(Long threadId) {
		return (ThreadData) threadIdToData.get(threadId);
	}

	/**
	 * @return the complete list of thread ids known to the debugger
	 */
	public List getThreadIds() {
		return new ArrayList(threadIdToData.keySet());
	}

	/**
	 * Caches the current thread as disabled
	 */
	public void disableThread() {
		disabledThreads.add(Thread.currentThread());
	}

	/**
	 * Removes the current thread as being disabled
	 */
	public void enableThread() {
		disabledThreads.remove(Thread.currentThread());
	}
}
