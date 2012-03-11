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
package org.eclipse.e4.languages.javascript.jsdi.rhino;

import org.eclipse.e4.languages.javascript.jsdi.Mirror;
import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;

/**
 * Rhino implementation of {@link Mirror}
 * 
 * @since 1.0
 */
public class MirrorImpl implements Mirror {

	protected final VirtualMachineImpl vm;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param description
	 */
	public MirrorImpl(VirtualMachineImpl vm) {
		this.vm = vm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Mirror#virtualMachine()
	 */
	public VirtualMachine virtualMachine() {
		return this.vm;
	}

}
