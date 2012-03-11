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

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.e4.languages.javascript.jsdi.Location;
import org.eclipse.e4.languages.javascript.jsdi.ScriptReference;
import org.eclipse.e4.languages.javascript.jsdi.request.BreakpointRequest;

/**
 * Default implementation of a line breakpoint
 * 
 * @since 1.0
 */
public class JSDILineBreakpoint extends JSDIBreakpoint {

	/**
	 * Constructor
	 */
	public JSDILineBreakpoint() {
		// needed for restoring breakpoints via extension point contributions
	}

	/**
	 * Constructor
	 * 
	 * @param resource
	 * @param linenumber
	 * @param charstart
	 * @param charend
	 * @param attributes
	 * @param register
	 * @throws DebugException
	 */
	public JSDILineBreakpoint(final IResource resource, final int linenumber, final int charstart, final int charend, final Map attributes, final boolean register) throws DebugException {
		IWorkspaceRunnable wr = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {

				// create the marker
				setMarker(resource.createMarker(JSDIBreakpoint.JSDI_LINE_BREAKPOINT));

				// add attributes
				attributes.put(IBreakpoint.ID, getModelIdentifier());
				attributes.put(IBreakpoint.ENABLED, Boolean.valueOf(true));
				attributes.put(IMarker.LINE_NUMBER, new Integer(linenumber));
				attributes.put(IMarker.CHAR_START, new Integer(charstart));
				attributes.put(IMarker.CHAR_END, new Integer(charend));

				ensureMarker().setAttributes(attributes);

				// add to breakpoint manager if requested
				register(register);
			}
		};
		run(getMarkerRule(resource), wr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint#createRequest(org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, org.eclipse.e4.languages.javascript.jsdi.ScriptReference)
	 */
	protected boolean createRequest(JSDIDebugTarget target, ScriptReference script) throws CoreException {
		Location loc = script.lineLocation(getLineNumber());
		if (loc == null) {
			return false;
		}
		BreakpointRequest request = target.getEventRequestManager().createBreakpointRequest(loc);
		registerRequest(target, request);
		configureRequest(request);
		request.setEnabled(isEnabled());
		return true;
	}
}
