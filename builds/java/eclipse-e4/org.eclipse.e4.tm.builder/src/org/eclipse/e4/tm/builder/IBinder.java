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

public interface IBinder {
	public Object update(EObject eObject, Object object, IBinderContext context);
	public void dispose(EObject eObject, Object object, IBinderContext context);
	public void updateStyle(EObject eObject, Object object, IBinderContext context);

	public boolean validateFeature(EObject eObject, Object object, String featureName, IBinderContext context);

	public <T> T adapt(Object value, Class<T> c);
}
