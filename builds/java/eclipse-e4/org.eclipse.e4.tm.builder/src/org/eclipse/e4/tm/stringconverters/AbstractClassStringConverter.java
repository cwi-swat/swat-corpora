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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.tm.builder.AbstractBuilder;
import org.eclipse.e4.tm.builder.IClassResolver;
import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;

public abstract class AbstractClassStringConverter extends AbstractStringConverter implements IClassResolver {

	public AbstractClassStringConverter() {
		super.trim = true;
	}

	private final String arrayTypeSuffix = "[]";

	public Object convert(String source, StringConverterContext stringConverterContext) {
		boolean isArray = source.endsWith(arrayTypeSuffix);
		if (isArray) {
			source = source.substring(0, source.length() - arrayTypeSuffix.length());
		}
		Class<?> klass = stringConverterContext.resolve(source);
		if (klass != null && isArray) {
			klass = Array.newInstance(klass, 0).getClass();
		}
		if (klass == null && Character.isLowerCase(source.charAt(0))) {
			Object c = convert(AbstractBuilder.casify(source, Boolean.TRUE), stringConverterContext);
			if (c instanceof Class<?>) {
				klass = (Class<?>)c;
			}
		}
		return klass;
	}
	
	protected Map<String, Class<?>> resolvedClasses = new HashMap<String, Class<?>>();

	private List<String> imports = new ArrayList<String>();

	public void importPackage(String packageName) {
		if (packageName.charAt(packageName.length() - 1) != '.') {
			packageName += ".";
		}
		imports.add(packageName);
	}

	private StringBuilder qualifiedName = new StringBuilder();

	protected abstract Class<?> loadClass(String name) throws Exception;
	
	public Class<?> resolve(String className) {
		Class<?> result = resolvedClasses.get(className);
		if (result != null) {
			return result;
		}
		result = resolveLoading(className);
		if (result != null) {
			resolvedClasses.put(className, result);
		}
		return result;
	}

	protected  Class<?> resolveLoading(String className) {
		Class<?> result = null;
		if (className.indexOf('.') > 0) {
			try {
				result = loadClass(className);
			} catch (Exception e) {
			}
		}
		if (result == null) {	
			for (String prefix: imports) {
				qualifiedName.setLength(0);
				qualifiedName.append(prefix);
				qualifiedName.append(className);
				try {
					result = loadClass(qualifiedName.toString());
					break;
				} catch (Exception e) {
				}
			}
		}
		return result;
	}
}
