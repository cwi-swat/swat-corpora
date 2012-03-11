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

import org.eclipse.e4.languages.javascript.jsdi.ScriptReference;
import org.eclipse.e4.languages.javascript.jsdi.ThreadReference;
import org.eclipse.e4.languages.javascript.jsdi.event.ScriptLoadEvent;
import org.eclipse.e4.languages.javascript.jsdi.request.ScriptLoadRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.ScriptReferenceImpl;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

public class ScriptLoadEventImpl extends EventImpl implements ScriptLoadEvent {

	private final ThreadReference thread;
	private final ScriptReferenceImpl script;

	public ScriptLoadEventImpl(VirtualMachineImpl vm, ThreadReference thread, ScriptReferenceImpl script, ScriptLoadRequest request) {
		super(vm, request);
		this.thread = thread;
		this.script = script;
	}

	public ScriptReference script() {
		return script;
	}

	public ThreadReference thread() {
		return thread;
	}
}
