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
import org.eclipse.swt.graphics.RGB;

public class RGBStringConverter extends AbstractStringConverter {

	private int value(int[] values, int index) {
		if (values.length <= index) {
			return 0;
		}
		int value = values[index];
		return (value < 0 ? 0 : (value > 255 ? 255 : value));
	}
	
	private static int fromHex(String source, int n) {
		int pos = n * 2 + 1;
		return Integer.parseInt(source.substring(pos, pos + 2), 16);
	}

	public Object convert(String source, StringConverterContext context) throws Exception {
		if (source.startsWith("#") && source.length() == 7) {
			return convertHexToRGB(source);
		}
		int[] intArray = context.convert(source, int[].class);
		return new RGB(value(intArray, 0), value(intArray, 1), value(intArray, 2));
	}

	protected static RGB convertHexToRGB(String source) {
		return new RGB(fromHex(source, 0), fromHex(source, 1), fromHex(source, 2));
	}
}
