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

public interface StringConverter {

	/**
	 * Parse the value as an object of the specified class.
	 * 
	 * @param source The string to be parsed.
	 * @return The resulting object, or null, of no object could be parsed.
	 * @throws Exception Must be thrown only if parser knows it is the right one, but fails.
	 */
	public <T> T convert(String source, Class<T> klass, StringConverterContext context) throws Exception;
}
