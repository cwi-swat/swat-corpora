/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.jsdi.rhino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.DebugSession;
import org.eclipse.e4.languages.javascript.debug.connect.DisconnectException;
import org.eclipse.e4.languages.javascript.debug.connect.EventPacket;
import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.debug.connect.Request;
import org.eclipse.e4.languages.javascript.debug.connect.Response;
import org.eclipse.e4.languages.javascript.debug.connect.TimeoutException;
import org.eclipse.e4.languages.javascript.jsdi.BooleanValue;
import org.eclipse.e4.languages.javascript.jsdi.NullValue;
import org.eclipse.e4.languages.javascript.jsdi.NumberValue;
import org.eclipse.e4.languages.javascript.jsdi.StringValue;
import org.eclipse.e4.languages.javascript.jsdi.UndefinedValue;
import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;
import org.eclipse.e4.languages.javascript.jsdi.event.EventQueue;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequestManager;
import org.eclipse.e4.languages.javascript.jsdi.rhino.event.EventQueueImpl;
import org.eclipse.e4.languages.javascript.jsdi.rhino.request.EventRequestManagerImpl;

/**
 * Rhino implementation of {@link VirtualMachine}
 * 
 * @since 1.0
 */
public class VirtualMachineImpl implements VirtualMachine {

	public final UndefinedValueImpl undefinedValue = new UndefinedValueImpl(this);
	public final NullValueImpl nullValue = new NullValueImpl(this);

	private final Map scripts = new HashMap();
	private final DebugSession session;
	private EventRequestManagerImpl eventRequestManager = new EventRequestManagerImpl(this);
	private final EventQueue eventQueue = new EventQueueImpl(this, eventRequestManager);
	private Map threads = new HashMap();
	private boolean disconnected = false;

	/**
	 * Constructor
	 * 
	 * @param debugSession
	 */
	public VirtualMachineImpl(DebugSession debugSession) {
		this.session = debugSession;
		initalizeScripts();
	}

	/**
	 * 
	 */
	private void initalizeScripts() {
		Request request = new Request(JSONConstants.SCRIPTS);
		try {
			Response response = sendRequest(request);
			List scriptIds = (List) response.getBody().get(JSONConstants.SCRIPTS);

			for (Iterator iterator = scriptIds.iterator(); iterator.hasNext();) {
				Long scriptId = new Long(((Number) iterator.next()).longValue());
				ScriptReferenceImpl script = createScriptReference(scriptId);
				scripts.put(scriptId, script);
			}
		} catch (DisconnectException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param scriptId
	 * @return
	 */
	private ScriptReferenceImpl createScriptReference(Long scriptId) {
		Request request = new Request(JSONConstants.SCRIPT);
		request.getArguments().put(JSONConstants.SCRIPT_ID, scriptId);
		try {
			Response response = sendRequest(request, 30000);
			Map jsonScript = (Map) response.getBody().get(JSONConstants.SCRIPT);
			return new ScriptReferenceImpl(this, jsonScript);
		} catch (DisconnectException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#allScripts()
	 */
	public synchronized List allScripts() {
		return new ArrayList(scripts.values());
	}

	/**
	 * @param scriptId
	 * @param script
	 */
	public synchronized ScriptReferenceImpl addScript(Long scriptId) {
		ScriptReferenceImpl script = (ScriptReferenceImpl) scripts.get(scriptId);
		if (script != null)
			return script;

		script = createScriptReference(scriptId);
		if (script != null)
			scripts.put(scriptId, script);
		return script;
	}

	/**
	 * @param scriptId
	 * @return
	 */
	public synchronized ScriptReferenceImpl getScript(Long scriptId) {
		return (ScriptReferenceImpl) scripts.get(scriptId);
	}

	/**
	 * @param timeout
	 * @return
	 * @throws TimeoutException
	 * @throws DisconnectException
	 */
	public EventPacket receiveEvent(int timeout) throws TimeoutException, DisconnectException {
		return session.receiveEvent(timeout);
	}

	/**
	 * Sends a request to the backing {@link DebugSession}
	 * 
	 * @param request
	 * @param timeout
	 * @return the {@link Response} for the request
	 * @throws TimeoutException
	 * @throws DisconnectException
	 */
	public Response sendRequest(Request request, int timeout) throws TimeoutException, DisconnectException {
		session.sendRequest(request);
		return session.receiveResponse(request.getSequence(), timeout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#sendRequest(org .eclipse.e4.languages.javascript.debug.connect.Request)
	 */
	public Response sendRequest(Request request) throws TimeoutException, DisconnectException {
		return sendRequest(request, 30000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#allThreads()
	 */
	public synchronized List allThreads() {
		Request request = new Request(JSONConstants.THREADS);
		try {
			Response response = sendRequest(request);
			List threadIds = (List) response.getBody().get(JSONConstants.THREADS);
			HashMap allThreads = new HashMap(threadIds.size());
			for (Iterator iterator = threadIds.iterator(); iterator.hasNext();) {
				Long threadId = new Long(((Number) iterator.next()).longValue());
				ThreadReferenceImpl thread = (ThreadReferenceImpl) threads.get(threadId);
				if (thread == null) {
					thread = createThreadReference(threadId);
				}
				allThreads.put(threadId, thread);
			}
			threads = allThreads;
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return new ArrayList(threads.values());
	}

	public synchronized ThreadReferenceImpl addThread(Long threadId) {
		ThreadReferenceImpl thread = (ThreadReferenceImpl) threads.get(threadId);
		if (thread != null)
			return thread;

		thread = createThreadReference(threadId);
		if (thread != null)
			threads.put(threadId, thread);
		return thread;
	}

	public synchronized ThreadReferenceImpl removeThread(Long threadId) {
		return (ThreadReferenceImpl) threads.remove(threadId);
	}

	/**
	 * @param threadId
	 * @return
	 */
	private ThreadReferenceImpl createThreadReference(Long threadId) {
		Request request = new Request(JSONConstants.THREAD);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		try {
			Response response = sendRequest(request, 30000);
			Map jsonThread = (Map) response.getBody().get(JSONConstants.THREAD);
			if (jsonThread == null)
				return ThreadReferenceImpl.zombie(this, threadId);
			return new ThreadReferenceImpl(this, jsonThread);
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param threadId
	 * @return
	 */
	public synchronized ThreadReferenceImpl getThread(Long threadId) {
		ThreadReferenceImpl thread = (ThreadReferenceImpl) threads.get(threadId);
		if (thread == null) {
			thread = createThreadReference(threadId);
			if (thread != null)
				threads.put(threadId, thread);
		}
		return thread;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#description()
	 */
	public String description() {
		Request request = new Request(JSONConstants.VERSION);
		try {
			Response response = sendRequest(request);
			StringBuffer buffer = new StringBuffer();
			buffer.append((String) response.getBody().get(JSONConstants.VM_VENDOR)).append(JSONConstants.SPACE);
			buffer.append(response.getBody().get(JSONConstants.VM_NAME)).append(JSONConstants.SPACE);
			buffer.append(response.getBody().get(JSONConstants.VM_VERSION));
			return buffer.toString();
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#dispose()
	 */
	public void dispose() {
		Request request = new Request(JSONConstants.DISPOSE);
		try {
			sendRequest(request);
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} finally {
			disconnectVM();
			this.eventRequestManager.createVMDeathRequest();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#name()
	 */
	public String name() {
		Request request = new Request(JSONConstants.VERSION);
		try {
			Response response = sendRequest(request);
			return (String) response.getBody().get(JSONConstants.VM_NAME);
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#resume()
	 */
	public void resume() {
		Request request = new Request(JSONConstants.CONTINUE);
		try {
			sendRequest(request);
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#suspend()
	 */
	public void suspend() {
		Request request = new Request(JSONConstants.SUSPEND);
		try {
			sendRequest(request);
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#terminate()
	 */
	public void terminate() {
		dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#version()
	 */
	public String version() {
		Request request = new Request(JSONConstants.VERSION);
		try {
			Response response = sendRequest(request);
			return (String) response.getBody().get(JSONConstants.VM_VERSION);
		} catch (DisconnectException e) {
			disconnectVM();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * disconnects the VM and creates the correct requests
	 */
	public void disconnectVM() {
		if (this.disconnected) {
			// no-op it is already disconnected
			return;
		}
		try {
			this.session.dispose();
			this.eventRequestManager.createVMDisconnectRequest();
		} finally {
			this.disconnected = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#mirrorOf(boolean)
	 */
	public BooleanValue mirrorOf(boolean bool) {
		return new BooleanValueImpl(this, Boolean.valueOf(bool));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#mirrorOf(int)
	 */
	public NumberValue mirrorOf(Number number) {
		return new NumberValueImpl(this, number);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#mirrorOf(java .lang.String)
	 */
	public StringValue mirrorOf(String string) {
		return new StringValueImpl(this, string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#mirrorOfNull()
	 */
	public NullValue mirrorOfNull() {
		return nullValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#mirrorOfUndefined ()
	 */
	public UndefinedValue mirrorOfUndefined() {
		return undefinedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#eventRequestManager ()
	 */
	public EventRequestManager eventRequestManager() {
		return eventRequestManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.VirtualMachine#eventQueue()
	 */
	public EventQueue eventQueue() {
		return eventQueue;
	}

	public ScriptReferenceImpl script(Long scriptId) {
		return (ScriptReferenceImpl) scripts.get(scriptId);
	}
}
