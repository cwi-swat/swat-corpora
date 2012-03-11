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

import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

/**
 * Rhino implementation of {@link EventRequest}
 * 
 * @since 1.0
 */
public class EventRequestImpl implements EventRequest {

	private boolean deleted = false;
	protected boolean enabled = false;
	protected final VirtualMachineImpl vm;

	public EventRequestImpl(VirtualMachineImpl vm) {
		this.vm = vm;
	}

	public synchronized boolean isEnabled() {
		return enabled;
	}

	public synchronized void setEnabled(boolean enabled) {
		checkDeleted();
		this.enabled = enabled;
	}

	protected void checkDeleted() {
		if (deleted) {
			throw new IllegalStateException("deleted"); //$NON-NLS-1$
		}
	}

	public VirtualMachine virtualMachine() {
		return vm;
	}

	public synchronized void delete() {
		setEnabled(false);
		deleted = true;
	}
}
