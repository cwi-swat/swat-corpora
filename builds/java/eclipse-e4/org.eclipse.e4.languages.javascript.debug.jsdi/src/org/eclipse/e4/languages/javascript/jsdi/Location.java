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
 * Abstract representation of a given location in a {@link ScriptReference}.<br>
 * <br>
 * A {@link Location} can be a line number and / or a function name.
 * 
 * @see ScriptReference
 * @since 0.9
 */
public interface Location {

	/**
	 * Returns the underlying {@link ScriptReference} for this {@link Location} or <code>null</code> if there was an exception creating the location.
	 * 
	 * @return the underlying {@link ScriptReference} or <code>null</code>.
	 */
	public ScriptReference scriptReference();

	/**
	 * Returns the line number of this location within its underlying {@link ScriptReference}
	 * 
	 * @return the line number in the {@link ScriptReference}
	 */
	public int lineNumber();

	/**
	 * Returns the name of the function this location resides in, if any
	 * 
	 * @return the function name this location resides in or <code>null</code> if this location is not enclosed by a function
	 */
	public String functionName();
}
