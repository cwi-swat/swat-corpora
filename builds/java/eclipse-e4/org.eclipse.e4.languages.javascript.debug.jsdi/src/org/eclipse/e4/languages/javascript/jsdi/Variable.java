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
 * Abstract description of a variable with-respect-to JavaScript debugging
 * 
 * @since 1.0
 */
public interface Variable extends PropertyReference {

	/**
	 * Returns true if this variable is an argument or not
	 * 
	 * @return true if this variable is an argument
	 */
	public boolean isArgument();

	/**
	 * Returns true if this variable is externally visible in the context of the given {@link StackFrameReference}.
	 * 
	 * @param frame
	 * @return true if this variable is externally visible, false otherwise
	 */
	public boolean isVisible(StackFrameReference frame);

}
