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
package org.eclipse.e4.languages.javascript.debug.model;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;
import org.eclipse.e4.languages.javascript.jsdi.event.Event;
import org.eclipse.e4.languages.javascript.jsdi.event.EventQueue;
import org.eclipse.e4.languages.javascript.jsdi.event.EventSet;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequest;

/**
 * Event dispatcher that re-fires event up to any model elements that have registered
 * 
 * @since 1.0
 */
public class EventDispatcher implements Runnable {

	HashMap listeners = null;
	boolean shutdown = false;
	JSDIDebugTarget target = null;

	/**
	 * Constructor
	 * 
	 * @param jsdiDebugTarget
	 */
	public EventDispatcher(JSDIDebugTarget jsdiDebugTarget) {
		target = jsdiDebugTarget;
		listeners = new HashMap();
	}

	/**
	 * Adds the given listener to the listing for the given request. Only one listener can be registered per request.
	 * 
	 * @param listener
	 * @param request
	 */
	public synchronized void addJSDIEventListener(IJSDIEventListener listener, EventRequest request) {
		listeners.put(request, listener);
	}

	/**
	 * Removes all listeners for the given request
	 * 
	 * @param listener
	 * @param request
	 */
	public synchronized void removeJSDIEventListener(EventRequest request) {
		listeners.remove(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		if (shutdown) {
			return; // no-op
		}
		VirtualMachine vm = target.getVM();
		if (vm != null) {
			EventQueue queue = vm.eventQueue();
			EventSet eventset = null;
			while (!shutdown) {
				eventset = queue.remove();
				if (eventset != null) {
					dispatch(eventset);
				}
			}
		}
	}

	/**
	 * performs the re-firing of jsdi events up to any model elements listening
	 * 
	 * @param eventset
	 */
	void dispatch(EventSet eventSet) {
		Event event = null;
		IJSDIEventListener listener = null;
		boolean resume = true;
		for (Iterator iter = eventSet.iterator(); iter.hasNext();) {
			event = (Event) iter.next();
			if (event == null) {
				continue;
			}
			listener = (IJSDIEventListener) listeners.get(event.request());
			if (listener != null) {
				resume &= listener.handleEvent(event, target, false, eventSet);
			}
		}

		for (Iterator iter = eventSet.iterator(); iter.hasNext();) {
			event = (Event) iter.next();
			if (event == null) {
				continue;
			}
			listener = (IJSDIEventListener) listeners.get(event.request());
			if (listener != null) {
				listener.eventSetComplete(event, target, !resume, eventSet);
			}
		}
		if (resume) {
			eventSet.resume();
		}
	}

	/**
	 * Shut down and clean up the dispatcher
	 */
	public void shutdown() {
		shutdown = true;
		listeners.clear();
	}

}
