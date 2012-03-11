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

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.e4.languages.javascript.jsdi.ScriptReference;
import org.eclipse.e4.languages.javascript.jsdi.event.Event;
import org.eclipse.e4.languages.javascript.jsdi.event.EventSet;
import org.eclipse.e4.languages.javascript.jsdi.event.ScriptLoadEvent;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequest;
import org.eclipse.e4.languages.javascript.jsdi.request.ScriptLoadRequest;

/**
 * Breakpoint that suspends on {@link ScriptLoadEvent}s
 * 
 * @since 0.9
 */
public class JSDIScriptLoadBreakpoint extends JSDIBreakpoint {

	/**
	 * Constructor
	 */
	public JSDIScriptLoadBreakpoint() {
		// used for persistence / restoration
	}

	/**
	 * Constructor
	 * 
	 * @param charstart
	 * @param charend
	 * @param attributes
	 * @param register
	 * @throws DebugException
	 */
	public JSDIScriptLoadBreakpoint(final int charstart, final int charend, final Map attributes, final boolean register) throws DebugException {
		IWorkspaceRunnable wr = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {

				// create the marker
				setMarker(ResourcesPlugin.getWorkspace().getRoot().createMarker(JSDIBreakpoint.JSDI_SCRIPT_LOAD_BREAKPOINT));

				// add attributes
				attributes.put(IBreakpoint.ID, getModelIdentifier());
				attributes.put(IBreakpoint.ENABLED, Boolean.valueOf(true));
				attributes.put(IMarker.CHAR_START, new Integer(charstart));
				attributes.put(IMarker.CHAR_END, new Integer(charend));

				ensureMarker().setAttributes(attributes);

				// add to breakpoint manager if requested
				register(register);
			}
		};
		run(getMarkerRule(ResourcesPlugin.getWorkspace().getRoot()), wr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint#handleEvent(org.eclipse.e4.languages.javascript.jsdi.event.Event, org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, boolean, org.eclipse.e4.languages.javascript.jsdi.event.EventSet)
	 */
	public boolean handleEvent(Event event, JSDIDebugTarget target, boolean suspendVote, EventSet eventSet) {
		try {
			if (event instanceof ScriptLoadEvent) {
				ScriptLoadEvent sevent = (ScriptLoadEvent) event;
				ScriptReference script = sevent.script();
				JSDIThread thread = target.findThread((sevent).thread());
				if (thread != null) {
					thread.suspendForBreakpoint(this, suspendVote);
					return !getScriptPath().equals(script.sourceURI().getPath());
				}
			}
		} catch (CoreException ce) {
			// TODO log this
			ce.printStackTrace();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint#registerRequest(org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, org.eclipse.e4.languages.javascript.jsdi.request.EventRequest)
	 */
	protected void registerRequest(JSDIDebugTarget target, EventRequest request) {
		ArrayList requests = getRequests(target);
		if (requests.isEmpty()) {
			// only add it once per target
			addRequestForTarget(target, request);
			try {
				if (isRegistered())
					incrementInstallCount();
			} catch (CoreException ce) {
				// TODO log this
				ce.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint#deregisterRequest(org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, org.eclipse.e4.languages.javascript.jsdi.request.EventRequest)
	 */
	protected void deregisterRequest(JSDIDebugTarget target, EventRequest request) {
		target.removeJSDIEventListener(this, request);
		try {
			decrementInstallCount();
		} catch (CoreException ce) {
			// TODO log this
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint#eventSetComplete(org.eclipse.e4.languages.javascript.jsdi.event.Event, org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, boolean, org.eclipse.e4.languages.javascript.jsdi.event.EventSet)
	 */
	public void eventSetComplete(Event event, JSDIDebugTarget target, boolean suspend, EventSet eventSet) {
		if (event instanceof ScriptLoadEvent) {
			JSDIThread thread = target.findThread(((ScriptLoadEvent) event).thread());
			if (thread != null) {
				thread.suspendForBreakpointComplete(this, suspend, eventSet);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint#createRequest(org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, org.eclipse.e4.languages.javascript.jsdi.ScriptReference)
	 */
	protected boolean createRequest(JSDIDebugTarget target, ScriptReference script) throws CoreException {
		ScriptLoadRequest request = target.getEventRequestManager().createScriptLoadRequest();
		registerRequest(target, request);
		request.setEnabled(isEnabled());
		return false;
	}

}
