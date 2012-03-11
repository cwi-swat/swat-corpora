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
package org.eclipse.e4.languages.javascript.jsdi;

import java.util.List;

/**
 * Abstract description of a thread reference with-respect-to Javascript debugging
 * 
 * @since 1.0
 */
public interface ThreadReference extends Mirror {

	/**
	 * Thread status constants
	 */
	public static final int THREAD_STATUS_UNKNOWN = -1;
	public static final int THREAD_STATUS_ZOMBIE = 0;
	public static final int THREAD_STATUS_RUNNING = 1;
	public static final int THREAD_STATUS_SLEEPING = 2;
	public static final int THREAD_STATUS_MONITOR = 3;
	public static final int THREAD_STATUS_WAIT = 4;
	public static final int THREAD_STATUS_NOT_STARTED = 5;

	/**
	 * Returns the total stack frame count for this thread
	 * 
	 * @return the total stack frame count
	 */
	public int frameCount();

	/**
	 * Returns the stack frame for this index on this thread
	 * 
	 * @return the indexed stack frame
	 */

	public StackFrameReference frame(int index);

	/**
	 * Returns the live list of stack frames for this thread
	 * 
	 * @return the list of stack frames from this thread
	 */
	public List frames();

	/**
	 * Send the request to interrupt this threads' execution
	 */
	public void interrupt();

	/**
	 * Sends the request to resume this thread, iff it is in the suspended state
	 */
	public void resume();

	/**
	 * Sends the request to suspend this thread, iff it is not already in a suspended state
	 */
	public void suspend();

	/**
	 * Returns the status of this thread.
	 * 
	 * @see ThreadReference for a complete listing of thread statuses
	 * 
	 * @return the status of this thread
	 */
	public int status();

	/**
	 * Returns whether or not this thread is currently suspended on a breakpoint, false otherwise
	 * 
	 * @return if the thread is suspended on a breakpoint, false otherwise
	 */
	public boolean isAtBreakpoint();

	/**
	 * Returns if this thread is currently in a suspended state
	 * 
	 * @return true if the thread is suspended, false otherwise
	 */
	public boolean isSuspended();

	/**
	 * Returns the simple name of this thread
	 * 
	 * @return the name of the thread
	 */
	public String name();
}
