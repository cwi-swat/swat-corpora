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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.mozilla.javascript.Scriptable;

class ResourceWrapper extends ContentsWrapper {

	protected ResourceWrapper(JavascriptSupport javascriptSupport, Scriptable scope, Resource res, Class<?> staticType) {
		super(javascriptSupport, scope, res, staticType, EcorePackage.eINSTANCE.getEResource());
	}

	public boolean has(String name, Scriptable start) {
		for (Iterator<EObject> it = getContents().iterator(); it.hasNext();) {
			EObject content = (EObject)it.next();
			if (this.javascriptSupport.hasName(name, content)) {
				return true;
			}
		}
		return super.has(name, start);
	}

	public Object get(String name, Scriptable start) {
		for (Iterator<EObject> it = getContents().iterator(); it.hasNext();) {
			EObject content = (EObject)it.next();
			if (this.javascriptSupport.hasName(name, content)) {
				EStructuralFeature feature = this.javascriptSupport.getNameFeature(content);
				EmfContext.noteDependency(content, feature, this.javascriptSupport.getName(content));
				return this.javascriptSupport.wrap(content);
			}
		}
		return super.get(name, start);
	}

	public List<EObject> getContents() {
		return ((Resource)javaObject).getContents();
	}

	public Object[] getIds() {
		List<Object> ids = getIds(! isEmfOnly(), true);
		addNameIds(getContents(), ids);
		return ids.toArray();
	}
}
