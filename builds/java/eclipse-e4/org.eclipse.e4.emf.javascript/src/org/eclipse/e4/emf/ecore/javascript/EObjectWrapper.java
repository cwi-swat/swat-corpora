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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.mozilla.javascript.Scriptable;

class EObjectWrapper extends JavascriptSupportWrapper {

	private static Logger log = Logger.getLogger(EObjectWrapper.class.getName());

	protected EObjectWrapper(JavascriptSupport javascriptSupport, Scriptable scope, EObject eObject, Class<?> staticType, EClassifier prototypeClass) {
		super(javascriptSupport, scope, eObject, staticType, prototypeClass);
	}

	public EObjectWrapper(JavascriptSupport javascriptSupport, Scriptable scope, EObject eObject, Class<?> staticType) {
		this(javascriptSupport, scope, eObject, staticType, eObject.eClass());
	}

	private EStructuralFeature getFeature(String name, EObject eObject) {
		EClass eClass = eObject.eClass();
		if (name.length() == 0 || NameSupport.NAME_PREFIX.equals(name)) {
			EReference eContainmentFeature = eObject.eContainmentFeature();
			return (eContainmentFeature != null ? eContainmentFeature.getEOpposite() : null);
		}
		return eClass.getEStructuralFeature(name);
	}

	public boolean has(String name, Scriptable start) {
		EObject eObject = (EObject)javaObject;
		EStructuralFeature feature = getFeature(name, eObject);
		if (feature != null) {
			return true;
		}
		for (EObject content: eObject.eContents()) {
			if (this.javascriptSupport.hasName(name, content)) {
				return true;
			}
		}
		return super.has(name, start);
	}

	public Object get(String name, Scriptable start) {
		EObject eObject = (EObject)javaObject;
		EStructuralFeature feature = getFeature(name, eObject);
		if (feature != null) {
			Object value = eObject.eGet(feature);
			EmfContext.noteDependency(eObject, feature, value);
			return this.javascriptSupport.wrap(value);
		}
		for (EObject content: eObject.eContents()) {
			if (this.javascriptSupport.hasName(name, content)) {
				feature = this.javascriptSupport.getNameFeature(content);
				EmfContext.noteDependency(content, feature, this.javascriptSupport.getName(content));
				return this.javascriptSupport.wrap(content);
			}
		}
		return super.get(name, start);
	}

	public void put(String name, Scriptable start, Object value) {
		EObject eObject = (EObject)javaObject;
		EStructuralFeature feature = getFeature(name, eObject);
		if (feature != null) {
			EClassifier type = feature.getEType();
			Object unwrappedValue = (value != null ? this.javascriptSupport.unwrapTo(value, type.getInstanceClass()) : null);
			if (unwrappedValue != null && (! type.isInstance(unwrappedValue))) {
				log.log(Level.SEVERE, "Unsupported conversion from " + value + " to " + type + ": " + unwrappedValue);
			}
			eObject.eSet(feature, unwrappedValue);
			return;
		}
		super.put(name, start, value);
	}

	public Object[] getIds() {
		EObject eObject = (EObject)javaObject;
		EClass eClass = eObject.eClass();
		List<Object> ids = (isEmfOnly() ? new ArrayList<Object>() : new ArrayList<Object>(Arrays.asList(super.getIds())));
		ids.addAll(Arrays.asList(getPrototype().getIds()));
		ids.addAll(eClass.getEAllStructuralFeatures());
		addParentId(eObject, ids);
		addNameIds(eObject.eContents(), ids);
		addIndexIds(eObject.eContents().size(), ids);
		return ids.toArray();
	}

	public boolean has(int index, Scriptable start) {
		EObject eObject = (EObject)javaObject;
		return has(eObject.eContents(), index, start);
	}

	public Object get(int index, Scriptable start) {
		EObject eObject = (EObject)javaObject;
		return get(eObject.eContents(), index, start);
	}

	public void put(int index, Scriptable start, Object value) {
		EObject eObject = (EObject)javaObject;
		put(eObject.eContents(), index, start, value);
	}
}
