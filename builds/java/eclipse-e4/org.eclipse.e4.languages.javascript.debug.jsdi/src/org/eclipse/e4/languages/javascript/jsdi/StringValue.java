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
package org.eclipse.e4.languages.javascript.jsdi;

/**
 * Abstract representation of a String literal with-respect-to JavaScript debugging
 * 
 * @since 1.0
 */
public interface StringValue extends Value {

	/**
	 * Returns the underlying value of the string
	 * 
	 * @return the value of the string
	 */
	public String value();
}
