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
 * Abstract representation of a variable value with-respect-to Javascript debugging.
 * 
 * @see Mirror
 * @since 0.9
 */
public interface Value extends Mirror {

	/**
	 * Returns the name of the type of this value. For example the name <code>function</code> could be the type name to describe a {@link FunctionReference}.
	 * 
	 * @return the name of the type of this {@link Value}
	 */
	public String getValueTypeName();

	/**
	 * Returns the string representation of the value.
	 * 
	 * @return the string representation of the value.
	 */
	public String valueString();
}
