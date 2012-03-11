/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.jsdi;

import org.eclipse.e4.languages.javascript.jsdi.event.Event;
import org.eclipse.e4.languages.javascript.jsdi.event.EventSet;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequest;

/**
 * Abstract representation of an object that can be mirrored in a {@link VirtualMachine}.
 * 
 * @see Event
 * @see EventRequest
 * @see EventSet
 * @see PropertyReference
 * @see ScriptReference
 * @see StackFrameReference
 * @see ThreadReference
 * @see Value
 * @since 0.9
 */
public interface Mirror {

	/**
	 * Returns the handle to the {@link VirtualMachine} that this mirror object was spawned from
	 * 
	 * @return the {@link VirtualMachine} handle that this mirror was spawned from
	 */
	public VirtualMachine virtualMachine();
}
