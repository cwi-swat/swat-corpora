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

import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.jsdi.StringValue;

/**
 * Rhino implementation of {@link StringValue}
 * 
 * @since 1.0
 */
public class StringValueImpl extends MirrorImpl implements StringValue {

	private String value;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param value
	 */
	public StringValueImpl(VirtualMachineImpl vm, String value) {
		super(vm);
		this.value = value;
	}

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param body
	 */
	public StringValueImpl(VirtualMachineImpl vm, Map body) {
		this(vm, (String) body.get(JSONConstants.VALUE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.StringReference#value()
	 */
	public String value() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Value#getValueTypeName()
	 */
	public String getValueTypeName() {
		return JSONConstants.STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Value#valueString()
	 */
	public String valueString() {
		return value;
	}
}
