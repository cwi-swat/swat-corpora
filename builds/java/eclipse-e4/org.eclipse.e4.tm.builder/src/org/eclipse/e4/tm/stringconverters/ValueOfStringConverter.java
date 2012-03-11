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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.tm.builder.AbstractBuilder;
import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;

public class ValueOfStringConverter extends AbstractStringConverter {

	private static Map<Object, Object> parseMethods = new HashMap<Object, Object>();

	private static Class<?>[] argClasses = {String.class};
	private static Object[] args = new Object[1];

	public static Method getParseMethod(Class<?> klass) {
		Object o = ValueOfStringConverter.parseMethods.get(klass);
		if (o instanceof Method) {
			return (Method)o;
		} else if (o != null) {
			return null;
		}
		Method m = null;
		try {
			m = ClassStringConverter.getObjectClass(klass).getMethod("valueOf", ValueOfStringConverter.argClasses);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		if (m == null) {
			try {
				String name = klass.getName();
				int pos = name.lastIndexOf('.');
				if (pos >= 0) {
					name = name.substring(pos + 1);
				}
				m = ClassStringConverter.getObjectClass(klass).getMethod("parse" + AbstractBuilder.casify(name, Boolean.TRUE), ValueOfStringConverter.argClasses);
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			}
		}
		if (m != null) {
			ValueOfStringConverter.parseMethods.put(klass, m);
		}
		return m;
	}

	private static boolean hasParseMethod(Class<?> klass) {
		return ValueOfStringConverter.getParseMethod(klass) != null;
	}

	public static <T> Constructor<T> getConstructor(Class<T> klass) {
		Object o = ValueOfStringConverter.parseMethods.get(klass);
		if (o instanceof Constructor) {
			return (Constructor<T>)o;
		} else if (o != null) {
			return null;
		}
		Constructor<T> cons = null;
		try {
			cons = ClassStringConverter.getObjectClass(klass).getConstructor(ValueOfStringConverter.argClasses);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		if (cons != null) {
			ValueOfStringConverter.parseMethods.put(klass, cons);
		}
		return cons;
	}

	private static boolean hasConstructor(Class<?> klass) {
		return ValueOfStringConverter.getConstructor(klass) != null;
	}

	public static boolean supportsClass(Class<?> klass) {
		return ValueOfStringConverter.hasParseMethod(klass) || ValueOfStringConverter.hasConstructor(klass);
	}

	public <T> T convert(String source, Class<T> klass, StringConverterContext stringConverter) {
		ValueOfStringConverter.args[0] = source;
		try {
			Method m = ValueOfStringConverter.getParseMethod(klass);
			if (m != null) {
				return (T)m.invoke(null, ValueOfStringConverter.args);
			} else if (klass.getName().startsWith("java.")) {
				Constructor<T> cons = ValueOfStringConverter.getConstructor(klass);
				if (cons != null) {
					return cons.newInstance(ValueOfStringConverter.args);
				}
			}
		}
		catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (InstantiationException e) {
		}
		return null;
	}
}
