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
import java.util.StringTokenizer;

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;

/**
 * Class ArrayStringConverter.
 * 
 * @author hallvard
 */
public class ArrayStringConverter extends AbstractStringConverter {

	private String delimiters = " \t\r\n";

	public ArrayStringConverter() {
	}
	public ArrayStringConverter(String delimiters) {
		this();
		this.delimiters = delimiters;
	}

	private String parenthesis = "(,)[ ]{;}<|>";

	public <T> T convert(String source, Class<T> klass, StringConverterContext stringConverter) {
		Class<?> elementClass = klass.getComponentType();
		String delimiters = this.delimiters;
		if (source.length() > 1) {
			char first = source.charAt(0), last = source.charAt(source.length() - 1);
			for (int i = 0; i < parenthesis.length(); i += 3) {
				if (first == parenthesis.charAt(i) && last == parenthesis.charAt(i + 2)) {
					delimiters = parenthesis.substring(i + 1, i + 2);
					source = source.substring(1, source.length() - 1);
					break;
				}
			}
		}
		StringTokenizer stringTokenizer = new StringTokenizer(source, delimiters);
		int tokens = stringTokenizer.countTokens();
		T array = (T)Array.newInstance(elementClass, tokens);
		for (int i = 0; i < tokens; i++) {
			String token = stringTokenizer.nextToken();
			if (delimiters == this.delimiters) {
				token = token.trim();
			}
			try {
				Object o = stringConverter.convert(token, elementClass);
				Array.set(array, i, o);
			} catch (Exception e) {
				throw new RuntimeException("Couldn't create array of " + array.getClass(), e);
			}
		}
		return array;
	}
}
