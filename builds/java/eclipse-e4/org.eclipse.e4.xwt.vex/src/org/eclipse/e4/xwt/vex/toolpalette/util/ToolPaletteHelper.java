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
package org.eclipse.e4.xwt.vex.toolpalette.util;

import org.eclipse.e4.xwt.vex.toolpalette.Entry;

public class ToolPaletteHelper {

	public static String getSimpleName(Entry entry) {
		String name = entry.getName();
		if (name == null) {
			return null;
		}
		int index = name.lastIndexOf('.');
		if (index != -1) {
			name = name.substring(index + 1);
		}
		return name;
	}
}
