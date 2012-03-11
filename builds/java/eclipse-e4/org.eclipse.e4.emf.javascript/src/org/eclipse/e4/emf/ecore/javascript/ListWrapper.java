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
import org.mozilla.javascript.Scriptable;

class ListWrapper extends ContentsWrapper {

	public ListWrapper(JavascriptSupport javascriptSupport, Scriptable scope, List<?> list, Class<?> staticType) {
		super(javascriptSupport, scope, list, staticType, EcorePackage.eINSTANCE.getEEList());
	}

	public List<?> getContents() {
		return (List<?>)javaObject;
	}

	public Object[] getIds() {
		return getIds(true, false).toArray();
	}
}
