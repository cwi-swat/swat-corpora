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

/**
 * Abstract representation of a {@link Number} value with-respect-to JavaScript debugging.
 * 
 * @see Value
 * @since 0.9
 */
public interface NumberValue extends Value {

	/**
	 * Represents the value for 'Not A Number'
	 */
	public static final String NAN = "NaN"; //$NON-NLS-1$
	/**
	 * Represents the value for negative infinity
	 */
	public static final String NEG_INFINITY = "-Infinity"; //$NON-NLS-1$
	/**
	 * Represents the value for positive infinity
	 */
	public static final String INFINITY = "Infinity"; //$NON-NLS-1$

	/**
	 * Returns the underlying {@link Number} value for this value.<br>
	 * <br>
	 * This method can return <code>null</code> if its value is not a number.
	 * 
	 * @return the underlying {@link Number} value
	 * @see #NAN
	 * @see #NEG_INFINITY
	 * @see #INFINITY
	 */
	public Number value();

	/**
	 * Returns if the this {@link NumberValue} is a valid number or not.
	 * 
	 * @return true if this is a valid number false otherwise.
	 * @see #NAN
	 * @see #NEG_INFINITY
	 * @see #INFINITY
	 */
	boolean isNaN();
}
