/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.core.editor;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public interface ISelectionSynchronizer {

	/**
	 * Adds a provider to the set of synchronized providers.
	 * 
	 * @param provider
	 *            the provider
	 */
	public abstract void addProvider(ISelectionProvider provider);

	/**
	 * Removes the provider from the set of synchronized providers
	 * 
	 * @param provider
	 *            the provider to remove
	 */
	public abstract void removeProvider(ISelectionProvider provider);

	/**
	 * Receives notification from one provider, and maps selection to all other
	 * members.
	 * 
	 * @param event
	 *            the selection event
	 */
	public abstract void selectionChanged(SelectionChangedEvent event);

	/**
	 * Enables or disabled synchronization between viewers.
	 * 
	 * @since 3.1
	 * @param value
	 *            <code>true</code> if synchronization should occur
	 */
	public abstract void setEnabled(boolean value);

}