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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EcorePackage;
import org.mozilla.javascript.Scriptable;

class MapWrapper extends JavascriptSupportWrapper {

	public MapWrapper(JavascriptSupport javascriptSupport, Scriptable scope, Map<?, ?> map, Class<?> staticType) {
		super(javascriptSupport, scope, map, staticType, EcorePackage.eINSTANCE.getEMap());
	}
	public MapWrapper(JavascriptSupport javascriptSupport, Scriptable scope, EMap<?, ?> map, Class<?> staticType) {
		super(javascriptSupport, scope, map, staticType, EcorePackage.eINSTANCE.getEMap());
	}

	public Object[] getIds() {
		Collection<?> mapEntries = null;
		if (javaObject instanceof Map) {
			mapEntries = ((Map<?, ?>)javaObject).keySet();
		} else if (javaObject instanceof EMap) {
			mapEntries = ((EMap<?, ?>)javaObject).keySet();
		}
		List<Object> ids = new ArrayList<Object>(Arrays.asList(super.getIds()));
		if (mapEntries != null) {
			ids.addAll(mapEntries);
		}
		return ids.toArray();
	}

	public boolean has(String name, Scriptable start) {
		if (javaObject instanceof Map) {
			return ((Map<?, ?>)javaObject).containsKey(name);
		} else if (javaObject instanceof EMap) {
			return ((EMap<?, ?>)javaObject).containsKey(name);
		}
		return super.has(name, start);
	}

	public Object get(String name, Scriptable start) {
		if (javaObject instanceof Map) {
			return ((Map<?, ?>)javaObject).get(name);
		} else if (javaObject instanceof EMap) {
			return ((EMap<?, ?>)javaObject).get(name);
		}
		return null;
	}

	public void put(String name, Scriptable start, Object value) {
		if (javaObject instanceof Map) {
			((Map)javaObject).put(name, value);
		} else if (javaObject instanceof EMap) {
			((EMap)javaObject).put(name, value);
		} else {
			super.put(name, start, value);
		}
	}
}
