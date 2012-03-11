/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.animation;

public class ParseNumbers {
	
	public static String intToString(int n, int m, int digits, char ch, int d) {
		String value = Integer.toString(n, m);

		if (ch != 0) {
			String prefix = Character.toString(ch);

			for (int i = digits - value.length(); i > 0; i--) {
				value = prefix + value;
			}
		}

		return value;
	}
}
