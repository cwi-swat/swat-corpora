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
package org.eclipse.e4.languages.javascript.debug.rhino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.EventPacket;
import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;

/**
 * Rhino implementation of thread data
 * 
 * @since 1.0
 */
public class ThreadData {

	private final LinkedList contexts = new LinkedList();
	private final Long threadId;
	private final RhinoDebugger debugger;

	private long currentContextId = 0L;
	private long currentFrameId = 0L;

	/**
	 * Constructor
	 * 
	 * @param threadId
	 * @param debugger
	 */
	public ThreadData(Long threadId, RhinoDebugger debugger) {
		this.threadId = threadId;
		this.debugger = debugger;
	}

	/**
	 * @return a JSON object for this data
	 */
	public synchronized Object toJSON() {
		Map result = new HashMap();
		result.put(JSONConstants.THREAD_ID, threadId);
		if (contexts.isEmpty()) {
			result.put(JSONConstants.STATE, JSONConstants.RUNNING);
		} else {
			Context top = (Context) contexts.getFirst();
			result.put(JSONConstants.STATE, ((ContextData) top.getDebuggerContextData()).getState());
		}
		ArrayList contextIds = new ArrayList(contexts.size());
		for (Iterator iterator = contexts.iterator(); iterator.hasNext();) {
			Context context = (Context) iterator.next();
			ContextData data = (ContextData) context.getDebuggerContextData();
			contextIds.add(data.getId());
		}
		result.put(JSONConstants.CONTEXTS, contextIds);
		return result;
	}

	/**
	 * Caches that the given context has been created
	 * 
	 * @param context
	 */
	public synchronized void contextCreated(Context context) {
		context.setGeneratingDebug(true);
		context.setOptimizationLevel(-1);
		ContextData contextData = new ContextData(threadId, new Long(currentContextId++), debugger);
		context.setDebugger(debugger, contextData);
		contexts.addFirst(context);
		if (contexts.size() == 1) {
			sendThreadEvent(JSONConstants.ENTER);
		}
	}

	/**
	 * Removes the context from the top of the stack
	 * 
	 * @param context
	 */
	public synchronized void contextReleased(Context context) {
		contexts.removeFirst();
		if (contexts.isEmpty()) {
			sendThreadEvent(JSONConstants.EXIT);
		}
	}

	/**
	 * Sends a thread event for the given type
	 * 
	 * @param type
	 * 
	 * @see JSONConstants#ENTER
	 * @see JSONConstants#EXIT
	 */
	private void sendThreadEvent(String type) {
		EventPacket threadEvent = new EventPacket(JSONConstants.THREAD);
		Map body = threadEvent.getBody();
		body.put(JSONConstants.TYPE, type);
		body.put(JSONConstants.THREAD_ID, threadId);
		debugger.sendEvent(threadEvent);
	}

	/**
	 * @return true if there are any known contexts to this thread data
	 */
	public synchronized boolean hasContext() {
		return !contexts.isEmpty();
	}

	/**
	 * Returns all of the frame ids from all of the known contexts in this thread data or an empty list, never <code>null</code>
	 * 
	 * @return the complete list of frame ids from all known contexts
	 */
	public synchronized List getFrameIds() {
		ArrayList result = new ArrayList(contexts.size());
		for (Iterator iterator = contexts.iterator(); iterator.hasNext();) {
			Context context = (Context) iterator.next();
			ContextData data = (ContextData) context.getDebuggerContextData();
			result.addAll(data.getFrameIds());
		}
		return result;
	}

	/**
	 * Suspends the first context on the stack. has no effect if there are no known contexts for this thread data
	 */
	public synchronized void suspend() {
		Context context = (Context) contexts.getFirst();
		if (context == null) {
			return;
		}
		ContextData data = (ContextData) context.getDebuggerContextData();
		data.suspend();
	}

	/**
	 * Resumes the top context for the given type. Has no effect if there are no known contexts
	 * 
	 * @param stepType
	 * 
	 * @see JSONConstants#STEP_IN
	 * @see JSONConstants#STEP_NEXT
	 * @see JSONConstants#STEP_OUT
	 * @see JSONConstants#STEP_ANY
	 */
	public synchronized void resume(String stepType) {
		Context context = (Context) contexts.getFirst();
		if (context == null) {
			return;
		}
		ContextData data = (ContextData) context.getDebuggerContextData();
		data.resume(stepType);
	}

	/**
	 * Returns the {@link DebugFrame} with the given id from the first context containing such a frame. Returns <code>null</code> if no such frame exists with the given id.
	 * 
	 * @param frameId
	 * @return the {@link DebugFrame} with the given id or <code>null</code>
	 */
	public DebugFrameImpl getFrame(Long frameId) {
		for (Iterator iterator = contexts.iterator(); iterator.hasNext();) {
			Context context = (Context) iterator.next();
			ContextData data = (ContextData) context.getDebuggerContextData();
			DebugFrameImpl frame = data.getFrame(frameId);
			if (frame != null)
				return frame;
		}
		return null;
	}

	/**
	 * Creates a new {@link DebugFrame} for the given attributes.
	 * 
	 * @param context
	 * @param debuggableScript
	 * @param script
	 * @return a new {@link DebugFrame}
	 */
	public synchronized DebugFrame getFrame(Context context, DebuggableScript debuggableScript, ScriptImpl script) {
		return new DebugFrameImpl(new Long(currentFrameId++), context, debuggableScript, script);
	}
}
