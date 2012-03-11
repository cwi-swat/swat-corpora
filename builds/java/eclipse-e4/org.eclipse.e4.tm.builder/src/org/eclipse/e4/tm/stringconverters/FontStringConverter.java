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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class FontStringConverter extends AbstractStringConverter {

	public Object convert(String source, StringConverterContext context) throws Exception {
		String[] tokens = source.split("[;, ]");
		String family = null;
		int style = SWT.NONE, size = 0;
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if (token.length() == 0) {
				continue;
			}
			char c = token.charAt(0);
			if (Character.isDigit(c)) {
				size = Integer.valueOf(token);
			} else if (Character.isLowerCase(c)) {
				if (token.equals("bold")) {
					style |= SWT.BOLD;
				} else if (token.equals("italic")) {
					style |= SWT.ITALIC;
				} else if (token.equals("plain") || token.equals("normal")) {
					style |= SWT.NORMAL;
				}
			} else if (Character.isUpperCase(c)) {
				family = token;
			}
		}
		if (family == null || size <= 0) {
			return null;
		}
		Display display = context.adapt(null, Display.class);
		Font font = new Font(display, family, size, style);
		context.registerDisposable(font);
		return font;
	}
}
