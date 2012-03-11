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


public interface IBinderContext {

	public <T> T adapt(Object value, Class<T> c);
	public <T> T convert(String source, Class<T> c) throws Exception;

	public Object update(EObject eObject);
	public void dispose(EObject eObject);
	public void updateStyle(EObject eObject);
	
	public Object getFieldProperty(Object object, String name);
	public Object getGetterProperty(Object object, String name);

	public Exception setFieldProperty(Object object, String name, Object value);
	public Exception setSetterProperty(Object object, String name, Object value);

	public Object getMethodProperty(Object object, String methodSpec, Object[] args);
	public Exception setMethodProperty(Object object, String methodSpec, Object[] args);

	public void setProperty(Object object, String name, Object value) throws Exception;

	public <T> T getRootObject(Class<T> c);
	public <T> T getObject(EObject eObject, Class<T> c);
	public void putObject(EObject eObject, Object object);

	public EObject getEObject(Object object);
	
	public void invalidateFeature(EObject eObject, String featureName);

	public void fireObjectHandled(int id, EObject eObject, Object object);
}
