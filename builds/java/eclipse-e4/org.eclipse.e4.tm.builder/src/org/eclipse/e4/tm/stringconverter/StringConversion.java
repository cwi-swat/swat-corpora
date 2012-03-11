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

import java.net.URL;
import java.util.HashMap;

import org.eclipse.e4.tm.stringconverters.ArrayStringConverter;
import org.eclipse.e4.tm.stringconverters.ClassStringConverter;
import org.eclipse.e4.tm.stringconverters.FontStringConverter;
import org.eclipse.e4.tm.stringconverters.ImageStringConverter;
import org.eclipse.e4.tm.stringconverters.NewInstanceStringConverter;
import org.eclipse.e4.tm.stringconverters.PointStringConverter;
import org.eclipse.e4.tm.stringconverters.RGBStringConverter;
import org.eclipse.e4.tm.stringconverters.RectangleStringConverter;
import org.eclipse.e4.tm.stringconverters.SWTColorsStringConverter;
import org.eclipse.e4.tm.stringconverters.StaticMethodStringConverter;
import org.eclipse.e4.tm.stringconverters.StringStringConverter;
import org.eclipse.e4.tm.stringconverters.ValueOfStringConverter;
import org.eclipse.e4.tm.stringconverters.WebColorStringConverter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

public class StringConversion {

	private StringConverterContext stringConverterContext;

	public void setStringConverterContext(StringConverterContext stringConverterContext) {
		this.stringConverterContext = stringConverterContext;
	}

	private HashMap<Object, StringConverter> stringConverters = new HashMap<Object, StringConverter>();

	public StringConversion(boolean addStandardConverters) {

		if (addStandardConverters) {
			registerStringConverter(Integer.TYPE, new StaticMethodStringConverter(Integer.class, "decode"));
			registerStringConverter(Byte.TYPE, new StaticMethodStringConverter(Byte.class, "decode"));

			registerStringConverter(String.class, new StringStringConverter());

			registerStringConverter(RGB.class, new RGBStringConverter());
			registerStringConverter(Point.class, new PointStringConverter());
			registerStringConverter(Rectangle.class, new RectangleStringConverter());
			registerStringConverter(Font.class, new FontStringConverter());
			addStringConverter(Color.class, new WebColorStringConverter());
			addStringConverter(Color.class, new SWTColorsStringConverter());
			
			addStringConverter(URL.class, new NewInstanceStringConverter());
			addStringConverter(URI.class, new StaticMethodStringConverter(URI.class, "createURI"));
			addStringConverter(Image.class, new ImageStringConverter());
		}
	}
	public StringConversion() {
		this(true);
	}

	/**
	 * Register a class that can convert from a String to an object of klass.
	 * @param klass The Class object representing the type that will be returned
	 * @param stringConverter The object that can convert a String to that type
	 */
	public <T> void registerStringConverter(Class<T> klass, StringConverter stringConverter) {
		stringConverters.put(klass, stringConverter);
		stringConverters.put(klass.getName(), stringConverter);
	}

	/**
	 * Register an additional class that can convert from a
	 * String to Java type represented by Class klass. Does not clear an existing one,
	 * but makes them operate together, trying each in order until one returns an object.
	 * @param klass The Class object representing the type that will be returned
	 * @param stringConverter The object that can convert a String to that type
	 */
	public <T> void addStringConverter(Class<T> klass, StringConverter stringConverter) {
		StringConverter oldConverter = (StringConverter)stringConverters.get(klass);
		if (oldConverter == null) {
			registerStringConverter(klass, stringConverter);
		} else {
			if (! (oldConverter instanceof CompositeStringConverter)) {
				oldConverter = new CompositeStringConverter(oldConverter);
				stringConverters.put(klass, oldConverter);
			}
			((CompositeStringConverter)oldConverter).addStringConverter(stringConverter);
		}
	}

	private boolean trySuperclassConverters = true;

	public void setTrySuperclassConverters(boolean trySuperclassConverters) {
		this.trySuperclassConverters = trySuperclassConverters;
	}

	private Class<?> upperSuperclass = null;

	public void setUpperSuperclass(Class<?> upperSuperclass) {
		this.upperSuperclass = upperSuperclass;
	}

	/**
	 * Parse a String value into an object of type klass
	 * @param value The value to convert
	 * @param klass The Class into which value should be converted
	 * @return Object the converted value as a klass object
	 */
	public <T> T convert(String value, Class<T> klass) {

		String stringValue = value;
		while (stringValue != null) {

			Class<?> superClass = klass;
			Class<T> objectClass = ClassStringConverter.getObjectClass(klass);
			Object lastResult = null;
			do {
				Object result = null;
				StringConverter converter = getStringConverter(superClass);
				if (converter != null) {
					try {
						result = converter.convert(stringValue, klass, stringConverterContext);
					} catch (Exception e) {
						throw new RuntimeException("Exception when converting " + value + " for " + superClass, e);
					}
				} else if (! trySuperclassConverters) {
					throw new RuntimeException("Unable to find a converter for type: " + klass.getName());
				}
				if (result != null) {
					if (objectClass.isInstance(result)) {
						return (T)result;
					} else {
						lastResult = result;
					}
				}
				superClass = superClass.getSuperclass();
			} while (trySuperclassConverters && superClass != null && superClass != upperSuperclass);

			throw new RuntimeException(value + " (" + stringValue + ")" + " could not be parsed into " + klass + ", but to " + lastResult);
		}
		return null;
	}

	private StringConverter getStringConverter(Class<?> klass) {
		StringConverter converter = (StringConverter)stringConverters.get(klass);
		if (converter == null) {
			converter = (StringConverter)stringConverters.get(klass.getName());
		}
		if (converter == null) {
			if (klass.isArray()) {
				converter = new ArrayStringConverter();
			} else if (ValueOfStringConverter.supportsClass(klass)) {
				converter = new ValueOfStringConverter();
			}
			if (converter != null) {
				stringConverters.put(klass, converter);
			} else {
				converter = (StringConverter)stringConverters.get(Object.class);
			}
		}
		return converter;
	}
}
