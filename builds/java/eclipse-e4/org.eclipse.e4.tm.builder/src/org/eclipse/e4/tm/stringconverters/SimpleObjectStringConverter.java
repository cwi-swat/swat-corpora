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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;

public class SimpleObjectStringConverter extends AbstractStringConverter {

	// Support this kind of syntax: layout="<class>{<propertyClass>:<propertyName>=<valueString>}"

	private char classNamePropertyNameSeparator = ':';
	private char propertyNameValueSeparator = '=';
	private char propertyDelimiter = ';';

	private String separator(char separator) {
		return "\\s*" + "\\" + separator + "\\s*";
	}

	private String nameSyntax = "\\w+";
	private String classNameSyntax = nameSyntax + "(?:\\." + nameSyntax + ")*";
	private String valueSyntax = "[^" + propertyDelimiter + "]*";

	private String propertySyntax = "(?:(" + classNameSyntax + ")" + separator(classNamePropertyNameSeparator) + ")?(" + nameSyntax + ")" + separator(propertyNameValueSeparator) + "(" + valueSyntax + ")" + separator(propertyDelimiter);
	private Pattern propertyPattern = Pattern.compile(propertySyntax);

	public <T> T convert(String source, Class<T> klass, StringConverterContext context) {
		int pos1 = source.indexOf("{"), pos2 = source.lastIndexOf("}");
		if (pos1 < 0 || pos2 < 0 || pos2 < source.length() - 1) {
			return null;
		}
		Class<?> resultClass = convertThrowing(source.substring(0, pos1).trim(), Class.class, context);
		T result = null;
		if (klass.isAssignableFrom(resultClass)) {
			try {
				Object o = resultClass.newInstance();
				result = (klass.isInstance(o) ? (T)o : null);
			} catch (Exception e1) {
			}
		}
		if (result == null) {
			return null;
		}
		String propertiesString = source.substring(pos1 + 1, pos2);
		Matcher matcher = propertyPattern.matcher(propertiesString);
		int start = 0;
		while (matcher.find(start)) {
			if (matcher.start() != start) {
				return null;
			}
			int count = matcher.groupCount();
			if (count == 0) {
				return null;
			}
			String valueClassName = (count > 2 ? matcher.group(1) : null);
			String propertyName = matcher.group(valueClassName != null ? 2 : 1);
			String valueString = matcher.group(valueClassName != null ? 3 : 2);
			try {
				Class<?> valueClass = context.convert(valueClassName, Class.class);
				Object value = context.convert(valueString, valueClass);
				context.setProperty(result, propertyName, value);
			} catch (Exception e) {
				throwConversionException(source, klass, e);
			}
			start = matcher.end();
		}
		return (start < propertiesString.length() ? null : result);
	}
}
