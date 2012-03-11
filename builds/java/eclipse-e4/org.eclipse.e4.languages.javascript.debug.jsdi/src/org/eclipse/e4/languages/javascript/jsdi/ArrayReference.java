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

import java.util.List;

/**
 * Abstract representation of an array with-respect-to Javascript debugging.
 * 
 * @see ObjectReference
 * @see Value
 * @since 0.9
 */
public interface ArrayReference extends ObjectReference {

	/**
	 * Returns the current number of {@link Value}s in this array
	 * 
	 * @return the number of {@link Value}s in the array
	 */
	public int length();

	/**
	 * Returns the {@link Value} at the given index in this array.
	 * 
	 * @param index
	 *            the index of the {@link Value} to retrieve
	 * @return the {@link Value} at the given index
	 * @see Value
	 * @throws IndexOutOfBoundsException
	 *             if the index is outside the bounds of this array. For example if <code>index &lt; 0</code> or <code>index &gt; {@link #length()}</code>
	 */
	public Value getValue(int index) throws IndexOutOfBoundsException;

	/**
	 * Returns the complete list of {@link Value}s in the array
	 * 
	 * @return the complete list of {@link Value}s in this array or an empty array if this {@link ArrayReference} has no {@link Value}s, never <code>null</code>
	 * @see Value
	 */
	public List/* <Value> */getValues();
}
