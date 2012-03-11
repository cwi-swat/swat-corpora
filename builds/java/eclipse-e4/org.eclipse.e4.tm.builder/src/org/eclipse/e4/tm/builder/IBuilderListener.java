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
package org.eclipse.e4.tm.builder;

import org.eclipse.emf.ecore.EObject;

public interface IBuilderListener {
	public int BUILD = 1, UPDATE = 2, DISPOSE = 3, STYLED = 4;
	public void objectHandled(int id, EObject eObject, Object object);
}
