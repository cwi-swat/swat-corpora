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

import java.util.StringTokenizer;

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;

public class StringArrayStringConverter extends AbstractStringConverter {

	public Object convert(String source) {
		StringTokenizer stringTokenizer = new StringTokenizer(source, " \t\r\n");
		int count = stringTokenizer.countTokens();
		String[] tokenArray = new String[count];
		for (int i = 0; i < count; i++) {
			tokenArray[i] = stringTokenizer.nextToken();
		}
		return tokenArray;
	}
}
