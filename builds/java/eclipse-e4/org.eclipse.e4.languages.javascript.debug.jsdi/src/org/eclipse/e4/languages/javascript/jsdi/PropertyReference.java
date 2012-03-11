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
 * Abstract description of an object property with-respect-to Javascript debugging
 * 
 * @see Mirror
 * @see Value
 * @since 1.0
 */
public interface PropertyReference extends Mirror {

	/**
	 * Returns the name of this {@link PropertyReference} or <code>null</code> if one could not be determined.
	 * 
	 * @return the name of this {@link PropertyReference} or <code>null</code>.
	 */
	public String name();

	/**
	 * Returns the underlying {@link Value} of this {@link PropertyReference} or <code>null</code> if one could not be determined.
	 * 
	 * @return the underlying {@link Value} for this {@link PropertyReference} or <code>null</code>
	 */
	public Value value();
}
