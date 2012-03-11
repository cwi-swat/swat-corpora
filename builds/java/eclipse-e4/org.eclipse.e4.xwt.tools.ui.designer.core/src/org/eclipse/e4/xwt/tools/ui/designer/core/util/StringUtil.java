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
package org.eclipse.e4.xwt.tools.ui.designer.core.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class StringUtil {
	public static final String COMMA = ",";

	/**
	 * String Utility.
	 */
	private StringUtil() {
	}

	public static String format(Object[] args, String sep) {
		if (args == null || sep == null) {
			return "";
		}
		int length = args.length;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(args[i].toString());
			if (i + 1 < length) {
				sb.append(sep);
			}
		}
		return sb.toString();
	}

	public static String format(Object[] args) {
		return format(args, COMMA);
	}

	public static String format(Rectangle r) {
		return format(new Object[] { r.x, r.y, r.width, r.height });
	}

	public static String format(Point p) {
		return format(new Object[] { p.x, p.y });
	}

	public static String format(org.eclipse.draw2d.geometry.Rectangle r) {
		return format(new Object[] { r.x, r.y, r.width, r.height });
	}

	public static String format(org.eclipse.draw2d.geometry.Point p) {
		return format(new Object[] { p.x, p.y });
	}

	public static String format(org.eclipse.draw2d.geometry.Dimension d) {
		return format(new Object[] { d.width, d.height });
	}
}
