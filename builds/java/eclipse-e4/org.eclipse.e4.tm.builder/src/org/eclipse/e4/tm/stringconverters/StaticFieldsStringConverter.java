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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;

public abstract class StaticFieldsStringConverter extends AbstractStringConverter {

	private String prefix;
	private Class fieldsClass;
	private Class valueClass;

	protected StaticFieldsStringConverter(Class fieldsClass, Class valueClass, String prefix) {
		super();
		super.trim = true;
		this.fieldsClass = fieldsClass;
		this.valueClass = valueClass;
		this.prefix = prefix;
	}

	private static int modifiers = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;

	protected void fillValueMap(Map map) {
		Field[] fields = fieldsClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (StaticFieldsStringConverter.testField(field, valueClass)) {
				String name = field.getName();
				if (prefix == null || name.startsWith(prefix)) {
					try {
						Object o = field.get(null);
						map.put(name, o);
						if (prefix != null) {
							map.put(name.substring(prefix.length()), o);
						}
					} catch (IllegalAccessException eIllegalAccess) {
					}
				}
			}
		}
	}

	private static <T> boolean testField(Field field, T valueClass) {
		return field != null && valueClass != null && valueClass.equals(field.getType()) && (field.getModifiers() & StaticFieldsStringConverter.modifiers) == StaticFieldsStringConverter.modifiers;
	}

	public Object convert(String source) {
		return StaticFieldsStringConverter.convert(fieldsClass, valueClass, prefix, source, null);
	}

	public static <T> T convert(Class fieldsClass, Class<T> valueClass, String prefix, String source, T def) {
		Field field = null;
		try {
			field = fieldsClass.getField(source);
		} catch (Exception e) {
		}
		if (field == null && prefix != null) {
			try {
				field = fieldsClass.getField(prefix + source);
			} catch (Exception e) {
			}
		}
		T result = null;
		if (StaticFieldsStringConverter.testField(field, valueClass) || StaticFieldsStringConverter.testField(field, ClassStringConverter.getPrimitiveType(valueClass))) {
			try {
				result = (T)field.get(null);
			} catch (Exception e) {
			}
		}
		return (result != null ? result : def);
	}
}
