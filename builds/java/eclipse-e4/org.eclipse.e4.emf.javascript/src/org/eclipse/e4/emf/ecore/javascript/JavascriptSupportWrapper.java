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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

public abstract class JavascriptSupportWrapper extends NativeJavaObject implements Adapter {

	protected final JavascriptSupport javascriptSupport;

	private boolean emfOnly = false;

	public boolean isEmfOnly() {
		return emfOnly;
	}

	public void setEmfOnly(boolean emfOnly) {
		this.emfOnly = emfOnly;
	}

	JavascriptSupportWrapper(JavascriptSupport javascriptSupport, Scriptable scope, Object object, Class<?> staticType, EClassifier prototypeClass) {
		super(scope, object, staticType);
		this.javascriptSupport = javascriptSupport;
		javascriptSupport.initWrapper(this, scope, object, prototypeClass);
	}

	static String toString(String wrapperName, Object o) {
		boolean isEObject = (o instanceof EObject);
		return "[" + wrapperName + " for " + (isEObject ? "eO" : "o") + "bject of " + (isEObject ? "eClass " + ((EObject)o).eClass().getName() : o.getClass()) + ": " + o;
	}

	public String toString() {
		return toString("JSWrapper", javaObject);
	}
	
	protected void addIndexIds(int n, List<Object> l) {
		for (int i = 0; i < n; i++) {
			l.add(i);
		}
	}

	protected void addParentId(EObject eObject, List<Object> result) {
		if (eObject.eContainingFeature() != null) {
			result.add(NameSupport.NAME_PREFIX);
		}
	}

	protected void addNameIds(List<EObject> contents, List<Object> result) {
		for (EObject content: contents) {
			String eName = this.javascriptSupport.getName(content);
			if (eName != null) {
				result.add(NameSupport.NAME_PREFIX + eName);
			}
		}
		for (int i = 0; i < result.size(); i++) {
			Object id = result.get(i);
			if (id instanceof ENamedElement) {
				result.set(i, ((ENamedElement)id).getName());
			}
		}
	}

	public boolean has(String name, Scriptable start) {
		return (isEmfOnly() ? false : super.has(name, start));
	}

	public Object get(String name, Scriptable start) {
		return (isEmfOnly() ? Scriptable.NOT_FOUND : super.get(name, start));
	}

	public void put(String name, Scriptable start, Object value) {
		if (! isEmfOnly()) {
			super.put(name, start, value);
		}
	}

	protected boolean has(List<?> list, int index, Scriptable start) {
		return list.size() > index;
	}

	protected Object get(List<?> list, int index, Scriptable start) {
		return this.javascriptSupport.wrap(list.get(index));
	}

	protected void put(List list, int index, Scriptable start, Object value) {
		value = this.javascriptSupport.unwrap(value);
		list.set(index, value);
	}

	// from Adapter

	public Notifier getTarget() {
		return (javaObject instanceof EObject ? (EObject)javaObject : null);
	}
	public void setTarget(Notifier newTarget) {
	}
	public boolean isAdapterForType(Object type) {
		return false;
	}

	public void notifyChanged(Notification notification) {
		if (javaObject instanceof EObject) {
			JavascriptNotificationSupport.notifyChanged(notification, (EObject)javaObject, javascriptSupport, false);
		}
	}
}
