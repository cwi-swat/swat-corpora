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
 * Represents a boolean value with-respect-to JavaScript debugging.
 * 
 * @see Value
 * @since 0.9
 */
public interface BooleanValue extends Value {

	/**
	 * Returns the boolean value for this {@link Value}
	 * 
	 * @return the boolean value
	 */
	boolean value();

}
