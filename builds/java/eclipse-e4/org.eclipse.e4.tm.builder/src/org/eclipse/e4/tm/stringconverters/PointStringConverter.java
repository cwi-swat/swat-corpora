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

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;

import org.eclipse.swt.graphics.Point;

public class PointStringConverter extends AbstractStringConverter {

	public Object convert(String source, StringConverterContext stringConverter) throws Exception {
		int[] intArray = stringConverter.convert(source, int[].class);
		return new Point(intArray.length >= 1 ? intArray[0] : 0, intArray.length >= 2 ? intArray[1] : 0);
	}
}
