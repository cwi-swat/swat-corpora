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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;

public class NewInstanceStringConverter extends AbstractStringConverter {

	private static Map<Class<?>, Constructor<?>> constructors = new HashMap<Class<?>, Constructor<?>>();

	private static Class[] argClasses = {String.class};
	private static Object[] args = new Object[1];

	public static <T> Constructor<T> getConstructor(Class<T> klass) {
		Constructor<T> cons = (Constructor<T>)NewInstanceStringConverter.constructors.get(klass);
		if (cons != null) {
			return cons;
		}
		if (cons == null) {
			try {
				cons = ClassStringConverter.getObjectClass(klass).getConstructor(NewInstanceStringConverter.argClasses);
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			}
		}
		if (cons == null) {
			Constructor<T>[] conses = (Constructor<T>[])klass.getConstructors();
			for (int i = 0; i < conses.length; i++) {
				if (conses[i].getParameterTypes().length == 1) {
					cons = conses[i];
					break;
				}
			}
		}
		if (cons != null) {
			NewInstanceStringConverter.constructors.put(klass, cons);
		}
		return cons;
	}

	public static boolean hasConstructor(Class<?> klass) {
		return NewInstanceStringConverter.getConstructor(klass) != null;
	}

	public <T> T convert(String source, Class<T> klass, StringConverterContext stringConverter) {
		Constructor<T> cons = NewInstanceStringConverter.getConstructor(klass);
		if (cons != null) {
			try {
				NewInstanceStringConverter.args[0] = stringConverter.convert(source, cons.getParameterTypes()[0]);
				return cons.newInstance(NewInstanceStringConverter.args);
			} catch (Exception e) {
			}
		}
		return null;
	}
}
