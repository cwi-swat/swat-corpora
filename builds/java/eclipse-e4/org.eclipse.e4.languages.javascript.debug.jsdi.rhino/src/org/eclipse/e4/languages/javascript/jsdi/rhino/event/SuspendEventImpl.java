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
package org.eclipse.e4.languages.javascript.jsdi.rhino.event;

import org.eclipse.e4.languages.javascript.jsdi.Location;
import org.eclipse.e4.languages.javascript.jsdi.ThreadReference;
import org.eclipse.e4.languages.javascript.jsdi.event.BreakpointEvent;
import org.eclipse.e4.languages.javascript.jsdi.request.SuspendRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

public class SuspendEventImpl extends EventImpl implements BreakpointEvent {

	private final ThreadReference thread;
	private final Location location;

	public SuspendEventImpl(VirtualMachineImpl vm, ThreadReference thread, Location location, SuspendRequest request) {
		super(vm, request);
		this.thread = thread;
		this.location = location;
	}

	public ThreadReference thread() {
		return thread;
	}

	public Location location() {
		return location;
	}
}
