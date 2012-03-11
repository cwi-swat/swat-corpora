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
import org.eclipse.e4.languages.javascript.jsdi.request.ExceptionRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

public class ExceptionRequestImpl extends EventRequestImpl implements ExceptionRequest {

	private ThreadReference thread;

	public ExceptionRequestImpl(VirtualMachineImpl vm) {
		super(vm);
	}

	public synchronized void addThreadFilter(ThreadReference thread) {
		checkDeleted();
		this.thread = thread;
	}

	public synchronized ThreadReference thread() {
		return thread;
	}
}
