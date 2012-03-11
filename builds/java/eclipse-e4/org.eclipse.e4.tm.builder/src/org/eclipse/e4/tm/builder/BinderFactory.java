/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

public abstract class BinderFactory {

	protected AbstractBuilder builder;
	
	protected BinderFactory(AbstractBuilder builder) {
		super();
		this.builder = builder;
	}

	protected Map<EClass, IBinder> binderMap;

	public IBinder getBinder(EObject eObject) {
		EClass eClass = eObject.eClass();
		if (binderMap == null) {
			binderMap = new HashMap<EClass, IBinder>();
		}
		IBinder binder = binderMap.get(eClass);
		if (binder == null) {
			binder = createBinder(eClass);
			binderMap.put(eClass, binder);
		}
		return binder;
	}

	protected String getBinderClassName(EClass eClass) {
		String className = AbstractBuilder.getClassAnnotation(eClass, AbstractBinder.ANNOTATION_URI, "binderClass", null);
		if (className == null) {
			className = eClass.getName() + "Binder";
		}
		String packageName = AbstractBuilder.getClassAnnotation(eClass, AbstractBinder.ANNOTATION_URI, "binderPackage", null);
		if (packageName == null && className.indexOf('.') < 0) {
			String name = builder.getClass().getName();
			packageName = name.substring(0, name.lastIndexOf('.'));
		}
		if (packageName != null && className != null) {
			className = packageName + "." + className;
		}
		return className;
	}
	
	protected Class<?> getBinderClassForName(String className) {
		return builder.getClassResolver().resolve(className);
	}

	protected IBinder createBinder(EClass eClass) {
		String className = getBinderClassName(eClass);
		Class<?> c = getBinderClassForName(className);
		return (c != null && IBinder.class.isAssignableFrom(c) ? createBinderOfClass((Class<? extends IBinder>)c) : null);
	}

	protected IBinder createBinderOfClass(Class<? extends IBinder> c) {
		IBinder binder = null;
		if (c != null && IBinder.class.isAssignableFrom(c)) {
			try {
				binder = (IBinder)c.newInstance();
				builder.setProperty(binder, "builder", this);
			} catch (Exception e) {
			}
			if (binder == null) {
				Constructor<IBinder>[] constructors = (Constructor<IBinder>[])c.getConstructors();
				for (int i = 0; i < constructors.length; i++) {
					if (ReflectionSupport.hasSingleParameterForValue(constructors[i].getParameterTypes(), this, false)) {
						try {
							binder = constructors[i].newInstance(new Object[]{this});
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return binder;
	}
}
