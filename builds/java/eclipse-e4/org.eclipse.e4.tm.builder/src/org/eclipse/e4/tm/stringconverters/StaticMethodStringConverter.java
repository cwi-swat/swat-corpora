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
package org.eclipse.e4.tm.stringconverters;

import java.lang.reflect.Method;

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;

public class StaticMethodStringConverter extends AbstractStringConverter {

	private Class methodClass, parameterTypes[];
	private String methodName;
	private Object[] args = null;
	
	private StaticMethodStringConverter() {
		super.trim = true;
	}

	public StaticMethodStringConverter(Class methodClass, String methodName, Class[] parameterTypes, Object[] args) {
		this();
		this.methodClass = methodClass;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
	}

	public StaticMethodStringConverter(Class methodClass, String methodName) {
		this(methodClass, methodName, new Class[]{String.class}, new Object[]{null});
	}

	private Method method = null;
	
	public Object convert(String source) {
		if (method == null) {
			try {
				method = methodClass.getMethod(methodName, parameterTypes);
			} catch (Exception e) {
			}
		}
		if (method == null) {
			return null;
		}
		int sourcePos = 0;
		for (int i = 0; i < parameterTypes.length; i++) {
			if (parameterTypes[i] == String.class && args[i] == null) {
				sourcePos = i;
				break;
			}
		}
		args[sourcePos] = source;
		Object result = null;
		try {
			result = method.invoke(null, args);
		} catch (Exception e) {
		} finally {
			args[sourcePos] = null;
		}
		return result;
	}
}
