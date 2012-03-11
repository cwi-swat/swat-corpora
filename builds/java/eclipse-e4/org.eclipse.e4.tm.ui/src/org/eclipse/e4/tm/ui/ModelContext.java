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
package org.eclipse.e4.tm.ui;

import org.eclipse.emf.ecore.EObject;

public interface ModelContext {

	public interface Listener {
		public void contextChanged(ModelContext source);
	}

	public EObject getModel();

	public void dispose();

	public void addModelContextListener(Listener listener);
	public void removeModelContextListener(Listener listener);
}
