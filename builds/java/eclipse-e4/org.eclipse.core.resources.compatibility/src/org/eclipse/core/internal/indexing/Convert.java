/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.indexing;

import java.io.UnsupportedEncodingException;

class Convert {

	/**
	 * Converts the string argument to a byte array.
	 */
	static String fromUTF8(byte[] b) {
		String result;
		try {
			result = new String(b, "UTF8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			result = new String(b);
		}
		return result;
	}

	/**
	 * Converts the string argument to a byte array.
	 */
	static byte[] toUTF8(String s) {
		byte[] result;
		try {
			result = s.getBytes("UTF8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			result = s.getBytes();
		}
		return result;
	}
}
