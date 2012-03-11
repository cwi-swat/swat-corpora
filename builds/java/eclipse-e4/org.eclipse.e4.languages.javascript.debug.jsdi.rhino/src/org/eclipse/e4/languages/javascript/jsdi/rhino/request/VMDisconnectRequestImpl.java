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

import org.eclipse.e4.languages.javascript.jsdi.request.VMDisconnectRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

/**
 * Default implementation of {@link VMDisconnectRequest}
 * 
 * @since 1.0
 */
public class VMDisconnectRequestImpl extends EventRequestImpl implements VMDisconnectRequest {

	/**
	 * Constructor
	 * 
	 * @param vm
	 */
	public VMDisconnectRequestImpl(VirtualMachineImpl vm) {
		super(vm);
	}

}
