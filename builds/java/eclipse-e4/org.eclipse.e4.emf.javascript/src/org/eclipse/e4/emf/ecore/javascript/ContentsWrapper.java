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

import org.eclipse.emf.ecore.EClassifier;
import org.mozilla.javascript.Scriptable;

abstract class ContentsWrapper extends JavascriptSupportWrapper {

	public ContentsWrapper(JavascriptSupport javascriptSupport, Scriptable scope, Object javaObject, Class<?> staticType, EClassifier prototypeClass) {
		super(javascriptSupport, scope, javaObject, staticType, prototypeClass);
	}

	public abstract List<?> getContents();

	protected void addContentsIds(List<Object> result) {
		addIndexIds(getContents().size(), result);
	}

	protected List<Object> getIds(boolean addSuperIds, boolean addPrototypeIds) {
		List<Object> ids = new ArrayList<Object>();
		if (addSuperIds) {
			ids.addAll(Arrays.asList(super.getIds()));
		}
		if (addPrototypeIds) {
			ids.addAll(Arrays.asList(getPrototype().getIds()));
		}
		addContentsIds(ids);
		return ids;
	}

	public boolean has(int index, Scriptable start) {
		return has(getContents(), index, start);
	}

	public Object get(int index, Scriptable start) {
		return get(getContents(), index, start);
	}

	public void put(int index, Scriptable start, Object value) {
		put(getContents(), index, start, value);
	}
}
