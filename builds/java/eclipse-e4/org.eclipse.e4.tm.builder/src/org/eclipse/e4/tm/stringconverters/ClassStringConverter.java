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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.tm.builder.IClassResolver;

public class ClassStringConverter extends AbstractClassStringConverter implements IClassResolver {

	public ClassStringConverter() {
		super.trim = true;
		importPackage("java.lang");
		resolvedClasses.putAll(primitiveTypes);
	}

	private static Map<String, Class<?>> primitiveTypes = new HashMap<String, Class<?>>();
	static {
		primitiveTypes.put("int", Integer.TYPE);
		primitiveTypes.put("byte", Byte.TYPE);
		primitiveTypes.put("short", Short.TYPE);
		primitiveTypes.put("long", Long.TYPE);
		primitiveTypes.put("double", Double.TYPE);
		primitiveTypes.put("float", Float.TYPE);
		primitiveTypes.put("boolean", Boolean.TYPE);
		primitiveTypes.put("char", Character.TYPE);
	}

	public static Class<?> getPrimitiveType(String s) {
		return primitiveTypes.get(s);
	}
	
	private static Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();

	private static void addPrimitiveType(Class<?> primitive, Class<?> wrapper) {
		ClassStringConverter.wrapperPrimitiveMap.put(primitive, wrapper);
		ClassStringConverter.wrapperPrimitiveMap.put(wrapper, primitive);
	}

	static {
		ClassStringConverter.addPrimitiveType(Integer.TYPE, Integer.class);
		ClassStringConverter.addPrimitiveType(Byte.TYPE, Byte.class);
		ClassStringConverter.addPrimitiveType(Short.TYPE, Short.class);
		ClassStringConverter.addPrimitiveType(Long.TYPE, Long.class);
		ClassStringConverter.addPrimitiveType(Double.TYPE, Double.class);
		ClassStringConverter.addPrimitiveType(Float.TYPE, Float.class);
		ClassStringConverter.addPrimitiveType(Boolean.TYPE, Boolean.class);
		ClassStringConverter.addPrimitiveType(Character.TYPE, Character.class);
	}

	public static Class<?> getPrimitiveType(Class<?> c) {
		if (c.isPrimitive()) {
			return c;
		}
		Class<?> pc = ClassStringConverter.wrapperPrimitiveMap.get(c);
		return (pc != null && pc.isPrimitive() ? pc : null);
	}

	public static <T> Class<T> getObjectClass(Class<T> c) {
		if (! c.isPrimitive()) {
			return c;
		}
		Class<?> wc = ClassStringConverter.wrapperPrimitiveMap.get(c);
		return (Class<T>)wc;
	}

	//
	
	private ClassLoader classLoader = getClass().getClassLoader();

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classloader) {
		this.classLoader = classloader;
		resolvedClasses = new HashMap<String, Class<?>>(primitiveTypes);
	}

	protected Class<?> loadClass(String name) throws Exception {
		return classLoader.loadClass(name);
	}
}
