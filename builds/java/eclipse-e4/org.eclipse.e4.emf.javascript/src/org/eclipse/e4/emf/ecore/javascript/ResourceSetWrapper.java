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
package org.eclipse.e4.emf.ecore.javascript;

import java.util.List;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.mozilla.javascript.Scriptable;

class ResourceSetWrapper extends ContentsWrapper {

	protected ResourceSetWrapper(JavascriptSupport javascriptSupport, Scriptable scope, ResourceSet resSet, Class<?> staticType) {
		super(javascriptSupport, scope, resSet, staticType, EcorePackage.eINSTANCE.getEResourceSet());
	}

	public List<?> getContents() {
		return ((ResourceSet)javaObject).getResources();
	}

	public Object[] getIds() {
		return getIds(! isEmfOnly(), true).toArray();
	}
}
