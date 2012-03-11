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
package org.eclipse.e4.xwt.tools.ui.designer.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.e4.xwt.converters.StringToFont;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.StringUtil;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class FontUtil {
	private static String[] FONT_NAMES;

	public static final String COMMA = ",";

	/**
	 * array of font sizes
	 */
	protected static final String[] FONT_SIZES = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" };

	/**
	 * @return String array of font names as String objects for the default display.
	 */
	static public String[] getFontNames() {
		if (FONT_NAMES != null)
			return FONT_NAMES;

		// add the names into a set to get a set of unique names
		Set<String> stringItems = new HashSet<String>();
		FontData[] fontDatas = getDisplay().getFontList(null, true);
		for (int i = 0; i < fontDatas.length; i++) {
			if (fontDatas[i].getName() != null) {
				stringItems.add(fontDatas[i].getName());
			}
		}

		// add strings into the array
		String strings[] = new String[stringItems.size()];
		int i = 0;
		for (String item : stringItems) {
			strings[i++] = item;
		}

		// sort the array
		Arrays.sort(strings);

		return FONT_NAMES = strings;
	}

	/**
	 * @return - array of fomt sizes
	 */
	public static final String[] getFontSizes() {
		return FONT_SIZES;
	}

	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null && PlatformUI.isWorkbenchRunning()) {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		return display != null ? display : Display.getDefault();
	}

	public static FontData getFontData(String str) {
		if (str == null) {
			return null;
		}
		Font font = (Font) StringToFont.instance.convert(str);
		if (font != null) {
			return font.getFontData()[0];
		}
		FontData fontData = null;
		if (str != null) {
			fontData = new FontData();
			String[] data = str.split(COMMA);
			if (data.length >= 3) {
				fontData.setName(data[0]);
				fontData.setHeight(Integer.valueOf(data[1]));
				fontData.setStyle(getIntFontStyle(data[2]));
			}
		}
		return fontData;
	}

	public static int getIntFontStyle(String style) {
		if ("NORMAL".equals(style)) {
			return 0;
		} else if ("BOLD".equals(style)) {
			return 1;
		} else if ("ITALIC".equals(style)) {
			return 2;
		} else if ("BOLD|ITALIC".equals(style)) {
			return 3;
		}
		return 4;
	}

	public static String getStringFontStyle(int style) {
		if (style == 0) {
			return "NORMAL";
		} else if (style == 1) {
			return "BOLD";
		} else if (style == 2) {
			return "ITALIC";
		} else if (style == 3) {
			return "BOLD|ITALIC";
		}
		return "";
	}

	public static String getFontStr(FontData fontData) {
		return StringUtil.format(new Object[] { fontData.getName(), fontData.getHeight(), getStringFontStyle(fontData.getStyle()) });
	}
}
