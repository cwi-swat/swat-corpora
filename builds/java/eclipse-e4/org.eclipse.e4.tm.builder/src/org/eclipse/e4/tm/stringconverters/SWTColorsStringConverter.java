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


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SWTColorsStringConverter extends StaticFieldsStringConverter {

	public SWTColorsStringConverter() {
		super(SWT.class, Integer.TYPE, "COLOR_");
	}

	public Object convert(String source) {
		Integer rgb = (Integer)super.convert(source);
		if (rgb != null) {
			Display display = context.adapt(null, Display.class);
			Color color = display.getSystemColor(rgb.intValue());
			if (color == display.getSystemColor(SWT.COLOR_BLACK)) {
				return null;
			}
			return color;
		}
		return null;
	}
}
