/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package org.eclipse.e4.tm.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public abstract class AbstractModelContext implements ModelContext, Runnable {

	private List<Listener> listeners = new ArrayList<Listener>();

	public void addModelContextListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeModelContextListener(Listener listener) {
		listeners.remove(listener);
	}

	protected void fireContextChanged() {
		run();
	}

	public void run() {
		for (Iterator<Listener> it = listeners.iterator(); it.hasNext();) {
			Listener listener = it.next();
			listener.contextChanged(this);
		}
	}

	public void dispose() {
	}

	protected EObject getModel(Resource res) {
		EList<EObject> contents = res.getContents();
		return (contents.size() > 0 ? contents.get(0) : null);
	}
}
