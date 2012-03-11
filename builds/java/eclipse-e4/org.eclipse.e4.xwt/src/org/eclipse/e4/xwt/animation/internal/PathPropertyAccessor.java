/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.animation.internal;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.metadata.ModelUtils;
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertyAccessor;

public class PathPropertyAccessor<T> implements PropertyAccessor<T> {
	private String path;
	private String[] segments;

	private Object cacheObject;
	private Object cacheHost;
	private IProperty property;

	public PathPropertyAccessor(String path) {
		this.path = path;
		segments = path.split("\\.");
	}

	public T get(Object object, String fieldName) {
		checkCache(object);
		try {
			return (T) property.getValue(cacheObject);
		} catch (Exception e) {
			throw new XWTException("Value access fails: property \""
					+ segments[segments.length - 1] + "\" of the path \""
					+ path + "\" is not found in the class \""
					+ object.getClass().getName() + "\".");
		}
	}

	public void set(Object object, String fieldName, T value) {
		checkCache(object);

		try {
			property.setValue(cacheObject, value);
		} catch (Exception e) {
			throw new XWTException("Value access fails: property \""
					+ segments[segments.length - 1] + "\" of the path \""
					+ path + "\" is not found in the class \""
					+ object.getClass().getName() + "\".");
		}
	}

	protected void checkCache(Object object) {
		if (object == cacheHost && cacheObject != null) {
			return;
		}
		Object target = object;
		IProperty currentProperty = null;
		IMetaclass metaclass = XWT.getMetaclass(target);
		for (int i = 0; i < segments.length; i++) {
			String segment = ModelUtils.normalizePropertyName(segments[i]);
			currentProperty = metaclass.findProperty(segment);
			if (currentProperty == null) {
				cacheObject = null;
				throw new XWTException("Property \"" + segment
						+ "\" of the path \"" + path
						+ "\" is not found in the class \""
						+ object.getClass().getName() + "\".");
			}
			try {
				cacheObject = target;
				target = currentProperty.getValue(target);
				metaclass = XWT.getMetaclass(target);
			} catch (Exception e) {
				cacheObject = null;
				throw new XWTException("Value access fails: property \""
						+ segment + "\" of the path \"" + path
						+ "\" is not found in the class \""
						+ object.getClass().getName() + "\".");
			}
		}
		property = currentProperty;
		cacheHost = object;
	}
}
