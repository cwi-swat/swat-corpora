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

import org.eclipse.e4.languages.javascript.jsdi.event.Event;
import org.eclipse.e4.languages.javascript.jsdi.event.EventSet;

/**
 * Describes a listener that is interested in listening / responding to {@link EventSet}s from the underlying model.
 * 
 * @since 1.0
 */
public interface IJSDIEventListener {
	/**
	 * Handles the given event that this listener has registered for and returns whether the thread in which the event occurred should be resumed. All event handlers for the events in an event set are given a chance to vote on whether the thread should be resumed. If all agree, the thread is resumed by the event dispatcher. If any event handler returns <code>false</code> the thread in which the event originated is left in a suspended state.
	 * <p>
	 * Event listeners are provided with the current state of the suspend vote. For example, this could allow a conditional breakpoint to not bother running its evaluation since the vote is already to suspend (if it coincides with a step end).
	 * </p>
	 * 
	 * @param event
	 *            the event to handle
	 * @param target
	 *            the debug target in which the event occurred
	 * @param suspendVote
	 *            whether the current vote among event listeners is to suspend
	 * @param eventSet
	 *            the event set the event is contained in
	 * @return whether the thread in which the event occurred should be resumed
	 */
	public boolean handleEvent(Event event, JSDIDebugTarget target, boolean suspendVote, EventSet eventSet);

	/**
	 * Notification that all event handlers for an event set have handled their associated events and whether the event set will suspend.
	 * 
	 * @param event
	 *            event the listener was registered for/handled
	 * @param target
	 *            target in which the event occurred
	 * @param suspend
	 *            whether the event will cause the event thread to suspend
	 * @param eventSet
	 *            the event set the event is contained in
	 */
	public void eventSetComplete(Event event, JSDIDebugTarget target, boolean suspend, EventSet eventSet);
}
