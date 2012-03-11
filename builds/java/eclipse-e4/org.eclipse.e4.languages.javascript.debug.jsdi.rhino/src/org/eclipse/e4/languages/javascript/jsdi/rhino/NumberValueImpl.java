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
import org.eclipse.e4.languages.javascript.jsdi.NumberValue;

/**
 * Rhino implementation of {@link NumberValue}
 * 
 * @since 0.9
 */
public class NumberValueImpl extends MirrorImpl implements NumberValue {

	/**
	 * The underlying value
	 */
	private Number value;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param body
	 */
	public NumberValueImpl(VirtualMachineImpl vm, Map body) {
		this(vm, (Number) body.get(JSONConstants.VALUE));
	}

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param number
	 */
	public NumberValueImpl(VirtualMachineImpl vm, Number number) {
		super(vm);
		this.value = number;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.NumberValue#value()
	 */
	public Number value() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Value#getValueTypeName()
	 */
	public String getValueTypeName() {
		return JSONConstants.NUMBER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.NumberValue#isNaN()
	 */
	public boolean isNaN() {
		return value == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Value#valueString()
	 */
	public String valueString() {
		if (value == null) {
			return NAN;
		}
		return value.toString();
	}
}
