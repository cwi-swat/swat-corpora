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
package org.eclipse.e4.tm.stringconverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractStringConverter implements StringConverter {

	protected boolean trim = false;

	protected void throwConversionException(String source, Class<?> klass, Exception e) throws RuntimeException {
		throw new RuntimeException("Exception when converting " + source + " to " + klass + ": " + e, e);
	}

	private Pattern pattern;
	private Matcher matcher;

	protected String getRegex() {
		return null;
	}

	protected Matcher getMatcher() {
		return matcher;
	}

	protected String getSubstring(int i) {
		return (matcher != null ? matcher.group(i) : null);
	}

	protected Object getObject(int i, Class<?> klass, StringConverterContext context) throws Exception {
		String s = getSubstring(i);
		return (s != null ? context.convert(s, klass) : null);
	}

	protected boolean matches(String source) {
		if (getRegex() != null) {
			if (pattern == null) {
				pattern = Pattern.compile(getRegex(), Pattern.DOTALL);
			}
			matcher = pattern.matcher(source);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}

	protected StringConverterContext context = null;
	
	public <T> T convert(String source, Class<T> klass, StringConverterContext context) throws Exception {
		if (trim) {
			source = source.trim();
		}
		this.context = context; 
		try {
			return (T)convert(source, context);
		} finally {
			this.context = null;
		}
	}

	public Object convert(String source, StringConverterContext context) throws Exception {
		return convert(source);
	}

	public Object convert(String source) throws Exception {
		return null;
	}

	protected <T> T convertThrowing(String source, Class<T> klass, StringConverterContext context) {
		T result = null;
		try {
			result = context.convert(source, klass);
		} catch (Exception e) {
			throwConversionException(source, klass, e);
		}
		return result;
	}
}
