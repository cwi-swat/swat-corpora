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

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.jsdi.NullValue;

/**
 * Rhino implementation of {@link NullValue}
 * 
 * @since 1.0
 */
public class NullValueImpl extends MirrorImpl implements NullValue {

	static final String NULL_VALUE = "Null"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param description
	 */
	public NullValueImpl(VirtualMachineImpl vm) {
		super(vm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Value#getValueTypeName()
	 */
	public String getValueTypeName() {
		return JSONConstants.NULL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Value#valueString()
	 */
	public String valueString() {
		return NULL_VALUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return valueString();
	}
}
