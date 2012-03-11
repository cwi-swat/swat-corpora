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

import org.eclipse.e4.tm.builder.IClassResolver;


/**
 * A context for the StringConverter implementations.
 * Used for parsing sub-strings and resolving classes.
 * @author Hallvard Traetteberg
 *
 */
public interface StringConverterContext extends IClassResolver {

	/**
	 * Asks the context to convert a string as an object of the specified class.
	 * @param <T> The desired class of the result.
	 * @param value The string to convert.
	 * @param klass The desired class of the result.
	 * @return The resulting object.
	 * @throws Exception
	 */
	public <T> T convert(String value, Class<T> klass) throws Exception;

	public <T> T adapt(Object value, Class<T> c);

	/**
	 * Sets a property on an object, used for experimental object syntax.
	 * @param object The object to set the property of.
	 * @param propertyName The property name.
	 * @param value The new value of the property.
	 * @throws Exception
	 */
	public void setProperty(Object object, String propertyName, Object value) throws Exception;

	/**
	 * Registers an object that must be explicitly disposed,
	 * by calling its dispose() method (duck typing).
	 * @param disposable The object that must be disposed.
	 */
	public void registerDisposable(Object disposable);
}
