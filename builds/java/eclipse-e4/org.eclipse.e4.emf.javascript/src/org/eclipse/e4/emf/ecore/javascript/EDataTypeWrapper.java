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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.mozilla.javascript.Scriptable;

public class EDataTypeWrapper extends EObjectWrapper {

	public EDataTypeWrapper(JavascriptSupport javascriptSupport, Scriptable scope, EObject eObject, Class<?> staticType) {
		super(javascriptSupport, scope, eObject, staticType, EcorePackage.eINSTANCE.getEDataType());
	}
}
