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
package org.eclipse.e4.languages.javascript.jsdi.rhino.request;

import org.eclipse.e4.languages.javascript.jsdi.ThreadReference;
import org.eclipse.e4.languages.javascript.jsdi.request.SuspendRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

public class SuspendRequestImpl extends EventRequestImpl implements SuspendRequest {

	private final ThreadReference thread;

	public SuspendRequestImpl(VirtualMachineImpl vm, ThreadReference thread) {
		super(vm);
		this.thread = thread;
	}

	public ThreadReference thread() {
		return thread;
	}
}
