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
 * Abstract description of a stack frame with-respect-to JavaScript debugging
 * 
 * @see Location
 * @see Mirror
 * @see Value
 * @see Variable
 * @since 1.0
 */
public interface StackFrameReference extends Mirror {

	/**
	 * Returns the value of 'this' for the current frame.
	 * 
	 * @return the 'this' ObjectReference
	 * 
	 */
	public Variable thisObject();

	/**
	 * The visible variables for this {@link StackFrameReference}
	 * 
	 * @return the list of {@link Variable} objects for this {@link StackFrameReference}
	 */
	public List/* <Variable> */variables();

	/**
	 * The current {@link Location} associated with this {@link StackFrameReference} <br>
	 * or <code>null</code> if an exception occurs trying to resolve the location.
	 * 
	 * @return the {@link Location} associated with this {@link StackFrameReference} or <code>null</code>
	 */
	public Location location();

	/**
	 * Evaluates the given expression in the context of this {@link StackFrameReference} <br>
	 * and returns the resulting {@link Value} or <code>null</code>.
	 * 
	 * @return the {@link Value} result of evaluating the expression or <code>null</code>
	 */
	public Value evaluate(String expression);
}
