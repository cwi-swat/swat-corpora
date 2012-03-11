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
package org.eclipse.e4.core.di.suppliers;

/**
 * The base class for an "object supplier" - something that knows how to instantiate objects
 * corresponding to the object descriptor.
 * <p> 
 * If supplier is asked to track changes, it should notify requestor whenever any of the objects
 * produced by the {@link #get(IObjectDescriptor[], Object[], IRequestor, boolean, boolean, boolean)} method change.
 * The supplier can do this by performing calls to the {@link IRequestor#resolveArguments(boolean)} and
 * {@link IRequestor#execute()}.
 * </p>
 *  @see IRequestor#resolveArguments(boolean)
 *  @see IRequestor#execute()
 */
abstract public class PrimaryObjectSupplier {

	/**
	 * Default constructor
	 */
	public PrimaryObjectSupplier() {
		// placeholder
	}

	/**
	 * This method is called by the dependency injection mechanism to obtain objects corresponding
	 * to the object descriptors. If the supplier is asked to track changes, it should notify requestor 
	 * whenever it detects a change that would result in a different result produced by this method.
	 * @param descriptors descriptors to the objects requested by the requestor
	 * @param actualValues the values of actual arguments computed so far for the descriptors (in/out)
	 * @param requestor the requestor originating this request
	 * @param initial <code>true</code> true if this is the initial request from the requestor
	 * @param track <code>true</code> if the object suppliers should notify requestor of
	 * changes to the returned objects; <code>false</code> otherwise
	 * @param group <code>true</code> if the change notifications can be grouped; 
	 * <code>false</code> otherwise
	 */
	abstract public void get(IObjectDescriptor[] descriptors, Object[] actualValues, IRequestor requestor, boolean initial, boolean track, boolean group);

	/**
	 * Pause tracking access to the supplier's objects.
	 */
	abstract public void pauseRecording();

	/**
	 * Resume tracking access to the supplier's objects.
	 */
	abstract public void resumeRecoding();

}
