/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.graphics.util;

public class Util {

	public static double[] valueOf(String s, String toStringPrefix, String separatorRegexp, String toStringSuffix) {
		if (s.startsWith(toStringPrefix)) {
			s = s.substring(toStringPrefix.length());
		}
		if (s.endsWith(toStringSuffix)) {
			s = s.substring(0, s.length() - toStringSuffix.length());
		}
		String[] tokens = s.split(separatorRegexp);
		int emptyCount = 0;
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].trim();
			if (tokens[i].length() == 0) {
				emptyCount++;
			}
		}
		double[] doubles = new double[tokens.length - emptyCount];
		for (int i = 0, j = 0; i < tokens.length; i++) {
			if (tokens[i].length() > 0) {
				doubles[j] = Double.valueOf(tokens[i]);
				j++;
			}
		}
		return doubles;
	}
}
