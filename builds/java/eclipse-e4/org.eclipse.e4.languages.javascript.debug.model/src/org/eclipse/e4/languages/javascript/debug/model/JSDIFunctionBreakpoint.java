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
 * JavaScript function breakpoint
 * 
 * @since 1.0
 */
public class JSDIFunctionBreakpoint extends JSDIBreakpoint {

	/**
	 * If this is a function entry breakpoint
	 */
	private static final String ENTRY = "ENTRY"; //$NON-NLS-1$

	/**
	 * if this is a function exit breakpoint
	 */
	private static final String EXIT = "EXIT"; //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public JSDIFunctionBreakpoint() {
		// needed for restoring breakpoints via extension point contributions
	}

	/**
	 * Constructor
	 * 
	 * @param resource
	 * @param name
	 * @param signature
	 * @param charstart
	 * @param charend
	 * @param attributes
	 * @param register
	 * @throws DebugException
	 */
	public JSDIFunctionBreakpoint(final IResource resource, final String name, final String signature, final int charstart, final int charend, final Map attributes, final boolean register) throws DebugException {
		IWorkspaceRunnable wr = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {

				// create the marker
				setMarker(resource.createMarker(JSDIBreakpoint.JSDI_FUNCTION_BREAKPOINT));

				// add attributes
				attributes.put(IBreakpoint.ID, getModelIdentifier());
				attributes.put(IBreakpoint.ENABLED, Boolean.valueOf(true));
				attributes.put(JSDIBreakpoint.FUNCTION_NAME, name);
				attributes.put(JSDIBreakpoint.FUNCTION_SIGNAURE, signature);
				attributes.put(IMarker.CHAR_START, new Integer(charstart));
				attributes.put(IMarker.CHAR_END, new Integer(charend));
				attributes.put(ENTRY, Boolean.valueOf(true));

				ensureMarker().setAttributes(attributes);

				// add to breakpoint manager if requested
				register(register);
			}
		};
		run(getMarkerRule(resource), wr);
	}

	/**
	 * Returns the function name set in the breakpoint.
	 * 
	 * @return the function name or <code>null</code> if none.
	 * @throws CoreException
	 */
	public String getFunctionName() throws CoreException {
		return ensureMarker().getAttribute(JSDIBreakpoint.FUNCTION_NAME, null);
	}

	/**
	 * Returns the function signature set in the breakpoint.
	 * 
	 * @return the function signature or <code>null</code> if none.
	 * @throws CoreException
	 */
	public String getSignature() throws CoreException {
		return ensureMarker().getAttribute(JSDIBreakpoint.FUNCTION_SIGNAURE, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint#createRequest(org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, org.eclipse.e4.languages.javascript.jsdi.ScriptReference)
	 */
	protected boolean createRequest(JSDIDebugTarget target, ScriptReference script) throws CoreException {
		Location loc = script.functionLocation(getFunctionName());
		if (loc == null) {
			return false;
		}
		BreakpointRequest request = target.getEventRequestManager().createBreakpointRequest(loc);
		registerRequest(target, request);
		configureRequest(request);
		request.setEnabled(isEnabled());
		return true;
	}

	/**
	 * Returns if this function breakpoint will suspend when execution enters the function it is set on
	 * 
	 * @return true if this is a function entry breakpoint
	 * @throws CoreException
	 */
	public boolean isEntry() throws CoreException {
		return ensureMarker().getAttribute(ENTRY, false);
	}

	/**
	 * Sets if this will be a function entry breakpoint
	 * 
	 * @param isentry
	 * @throws CoreException
	 */
	public void setEntry(boolean isentry) throws CoreException {
		if (isentry != isEntry()) {
			ensureMarker().setAttribute(ENTRY, isentry);
			recreateBreakpoint();
		}
	}

	/**
	 * Returns if this function breakpoint will suspend when execution leaves the function it is set on
	 * 
	 * @return true if this is a function exit breakpoint
	 * @throws CoreException
	 */
	public boolean isExit() throws CoreException {
		return ensureMarker().getAttribute(EXIT, false);
	}

	/**
	 * Sets if this will be a function exit breakpoint
	 * 
	 * @param isexit
	 * @throws CoreException
	 */
	public void setExit(boolean isexit) throws CoreException {
		if (isexit != isExit()) {
			ensureMarker().setAttribute(EXIT, isexit);
			recreateBreakpoint();
		}
	}
}
