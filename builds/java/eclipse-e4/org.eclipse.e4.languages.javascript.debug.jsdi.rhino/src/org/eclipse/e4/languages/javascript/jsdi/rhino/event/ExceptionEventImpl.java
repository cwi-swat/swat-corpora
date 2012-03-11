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
import org.eclipse.e4.languages.javascript.jsdi.event.ExceptionEvent;
import org.eclipse.e4.languages.javascript.jsdi.request.ExceptionRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

public class ExceptionEventImpl extends EventImpl implements ExceptionEvent {

	private final ThreadReference thread;
	private final Location location;
	private final String message;

	public ExceptionEventImpl(VirtualMachineImpl vm, ThreadReference thread, Location location, String message, ExceptionRequest request) {
		super(vm, request);
		this.thread = thread;
		this.location = location;
		this.message = message;
	}

	public ThreadReference thread() {
		return thread;
	}

	public Location location() {
		return location;
	}

	public String message() {
		return message;
	}
}
