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
package org.eclipse.e4.languages.javascript.jsdi.request;

import java.util.List;

import org.eclipse.e4.languages.javascript.jsdi.Location;
import org.eclipse.e4.languages.javascript.jsdi.ThreadReference;
import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;

/**
 * Description of a manager for creating requests for the model
 * 
 * @since 0.9
 */
public interface EventRequestManager {

	/**
	 * Creates a new breakpoint request for the given script location
	 * 
	 * @param linenumber
	 * @param location
	 * @return the new breakpoint request for the given script
	 */
	public BreakpointRequest createBreakpointRequest(Location location);

	/**
	 * Returns the live un-modifiable list of breakpoint requests currently queued in the manager
	 * 
	 * @return the list of breakpoint requests
	 */
	public List breakpointRequests();

	public DebuggerStatementRequest createDebuggerStatementRequest();

	public List debuggerStatementRequests();

	public ExceptionRequest createExceptionRequest();

	public List exceptionRequests();

	public ScriptLoadRequest createScriptLoadRequest();

	public List scriptLoadRequests();

	public StepRequest createStepRequest(ThreadReference thread, int step);

	public List stepRequests();

	public SuspendRequest createSuspendRequest(ThreadReference thread);

	public List suspendRequests();

	public ThreadEnterRequest createThreadEnterRequest();

	public List threadEnterRequests();

	public ThreadExitRequest createThreadExitRequest();

	public List threadExitRequests();

	public void deleteEventRequest(EventRequest eventRequest);

	public void deleteEventRequest(List eventRequests);

	/**
	 * Creates a new request indicating that the associated {@link VirtualMachine} should be shutdown / disposed.
	 * 
	 * @see VMDeathRequest
	 */
	public VMDeathRequest createVMDeathRequest();

	/**
	 * Returns the complete cache of {@link VMDeathRequest}s currently in the manager or an empty list, never <code>null</code>.
	 * 
	 * @return the un-modifiable listing of {@link VMDeathRequest}s
	 */
	public List vmDeathRequests();

	/**
	 * Creates a new request indicating that the associated {@link VirtualMachine} should disconnect.
	 * 
	 * @see VMDisconnectRequest
	 */
	public VMDisconnectRequest createVMDisconnectRequest();

	/**
	 * Returns the complete cache of {@link VMDisconnectRequest}s currently in the manager or an empty list, never <code>null</code>.
	 * 
	 * @return the un-modifiable listing of {@link VMDisconnectRequest}s
	 */
	public List vmDisconnectRequests();
}
