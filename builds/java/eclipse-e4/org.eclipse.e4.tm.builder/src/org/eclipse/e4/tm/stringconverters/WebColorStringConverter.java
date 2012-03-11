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

import org.eclipse.e4.tm.stringconverter.StringConverterContext;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class WebColorStringConverter extends RGBStringConverter {

	public Object convert(String source, StringConverterContext context) throws Exception {
		String colorDef = null;
		if (source.length() > 0 && Character.isJavaIdentifierStart(source.charAt(0))) {
			colorDef = findColorDef(source);
		}
		Object result = super.convert(colorDef != null ? colorDef : source, context);
		if (! (result instanceof RGB)) {
			return null;
		}
		Display display = context.adapt(null, Display.class);
		Color color = new Color(display, (RGB)result);
		context.registerDisposable(color);
		return color;
	}

	public static RGB findRGB(String source) throws Exception {
		String colorDef = findColorDef(source);
		return RGBStringConverter.convertHexToRGB(colorDef != null ? colorDef : source);
	}
	public static String findColorDef(String colorName) {
		String colorDef = findColorDef(colorName, colorNames);
		if (colorDef == null) {
			colorName = fixColorName(colorName);
			colorDef = findColorDef(colorName, colorNames);
		}
		return colorDef;
	}

	public static String findColorName(String colorDef) {
		for (int i = 0; i < colorNames.length; i += 2) {
			if (colorDef.equals(colorNames[i + 1])) {
				return colorNames[i];
			}
		}
		return null;
	}
	
	private static char toHexDigit(int n) {
		return (char)(n < 10 ? '0' + n : 'A' + n - 10);
	}
	private static String toHex(byte n) {
		int n1 = n / 16, n2 = n % 16;
		char[] buf = new char[] {toHexDigit(n1), toHexDigit(n2)};
		return new String(buf);
	}
	public static String findColorName(int red, int green, int blue) {
		return findColorName("#" + toHex((byte)red) + toHex((byte)green) + toHex((byte)blue));
	}

	private static String fixColorName(String colorName) {
		StringBuffer buffer = new StringBuffer(colorName.length());
		boolean upcase = true;
		for (int i = 0; i < colorName.length(); i++) {
			char c = colorName.charAt(i);
			if ("-_".indexOf(c) >= 0) {
				upcase = true;
			} else {
				c = (upcase ? Character.toUpperCase(c) : Character.toLowerCase(c));
				buffer.append(c);
				upcase = false;
			}
		}
		colorName = buffer.toString();
		return colorName;
	}
			
	private static String findColorDef(String name, String table[]) {
		int slotsPrLine = 2, nameSlot = 0, colorSlot = 1;
    	int low = 0, high = (table.length / slotsPrLine) - 1;
    	while (low <= high) {
    	    int mid = (low + high) / 2;
    	    int cmp = table[mid * slotsPrLine + nameSlot].compareTo(name);
    	    if (cmp < 0) {
    	    	low = mid + 1;
    	    } else if (cmp > 0) {
    	    	high = mid - 1;
    	    } else {
    	    	return table[mid * slotsPrLine + colorSlot];
    	    }
    	}
    	return null;
	}

	// from http://www.w3schools.com/HTML/html_colornames.asp
	private static String[] colorNames = {
			"AliceBlue", "#F0F8FF",
			"AntiqueWhite", "#FAEBD7",
			"Aqua", "#00FFFF", 	 
			"Aquamarine", "#7FFFD4", 	 
			"Azure", "#F0FFFF", 	 
			"Beige", "#F5F5DC", 	 
			"Bisque", "#FFE4C4", 	 
			"Black", "#000000", 	 
			"BlanchedAlmond", "#FFEBCD", 	 
			"Blue", "#0000FF", 	 
			"BlueViolet", "#8A2BE2", 	 
			"Brown", "#A52A2A", 	 
			"BurlyWood", "#DEB887", 	 
			"CadetBlue", "#5F9EA0", 	 
			"Chartreuse", "#7FFF00", 	 
			"Chocolate", "#D2691E", 	 
			"Coral", "#FF7F50", 	 
			"CornflowerBlue", "#6495ED", 	 
			"Cornsilk", "#FFF8DC", 	 
			"Crimson", "#DC143C", 	 
			"Cyan", "#00FFFF", 	 
			"DarkBlue", "#00008B", 	 
			"DarkCyan", "#008B8B", 	 
			"DarkGoldenRod", "#B8860B", 	 
			"DarkGray", "#A9A9A9", 	 
			"DarkGreen", "#006400", 	 
			"DarkKhaki", "#BDB76B", 	 
			"DarkMagenta", "#8B008B", 	 
			"DarkOliveGreen", "#556B2F", 	 
			"Darkorange", "#FF8C00", 	 
			"DarkOrchid", "#9932CC", 	 
			"DarkRed", "#8B0000", 	 
			"DarkSalmon", "#E9967A", 	 
			"DarkSeaGreen", "#8FBC8F", 	 
			"DarkSlateBlue", "#483D8B", 	 
			"DarkSlateGray", "#2F4F4F", 	 
			"DarkTurquoise", "#00CED1", 	 
			"DarkViolet", "#9400D3", 	 
			"DeepPink", "#FF1493", 	 
			"DeepSkyBlue", "#00BFFF", 	 
			"DimGray", "#696969", 	 
			"DodgerBlue", "#1E90FF", 	 
			"FireBrick", "#B22222", 	 
			"FloralWhite", "#FFFAF0", 	 
			"ForestGreen", "#228B22", 	 
			"Fuchsia", "#FF00FF", 	 
			"Gainsboro", "#DCDCDC", 	 
			"GhostWhite", "#F8F8FF", 	 
			"Gold", "#FFD700", 	 
			"GoldenRod", "#DAA520", 	 
			"Gray", "#808080", 	 
			"Green", "#008000", 	 
			"GreenYellow", "#ADFF2F", 	 
			"HoneyDew", "#F0FFF0", 	 
			"HotPink", "#FF69B4", 	 
			"IndianRed", "#CD5C5C",
			"Indigo", "#4B0082",
			"Ivory", "#FFFFF0", 	 
			"Khaki", "#F0E68C", 	 
			"Lavender", "#E6E6FA", 	 
			"LavenderBlush", "#FFF0F5", 	 
			"LawnGreen", "#7CFC00", 	 
			"LemonChiffon", "#FFFACD", 	 
			"LightBlue", "#ADD8E6", 	 
			"LightCoral", "#F08080", 	 
			"LightCyan", "#E0FFFF", 	 
			"LightGoldenRodYellow", "#FAFAD2", 	 
			"LightGrey", "#D3D3D3", 	 
			"LightGreen", "#90EE90", 	 
			"LightPink", "#FFB6C1", 	 
			"LightSalmon", "#FFA07A", 	 
			"LightSeaGreen", "#20B2AA", 	 
			"LightSkyBlue", "#87CEFA", 	 
			"LightSlateGray", "#778899", 	 
			"LightSteelBlue", "#B0C4DE", 	 
			"LightYellow", "#FFFFE0", 	 
			"Lime", "#00FF00", 	 
			"LimeGreen", "#32CD32", 	 
			"Linen", "#FAF0E6", 	 
			"Magenta", "#FF00FF", 	 
			"Maroon", "#800000", 	 
			"MediumAquaMarine", "#66CDAA", 	 
			"MediumBlue", "#0000CD", 	 
			"MediumOrchid", "#BA55D3", 	 
			"MediumPurple", "#9370D8", 	 
			"MediumSeaGreen", "#3CB371", 	 
			"MediumSlateBlue", "#7B68EE", 	 
			"MediumSpringGreen", "#00FA9A", 	 
			"MediumTurquoise", "#48D1CC", 	 
			"MediumVioletRed", "#C71585", 	 
			"MidnightBlue", "#191970", 	 
			"MintCream", "#F5FFFA", 	 
			"MistyRose", "#FFE4E1", 	 
			"Moccasin", "#FFE4B5", 	 
			"NavajoWhite", "#FFDEAD", 	 
			"Navy", "#000080", 	 
			"OldLace", "#FDF5E6", 	 
			"Olive", "#808000", 	 
			"OliveDrab", "#6B8E23", 	 
			"Orange", "#FFA500", 	 
			"OrangeRed", "#FF4500", 	 
			"Orchid", "#DA70D6", 	 
			"PaleGoldenRod", "#EEE8AA", 	 
			"PaleGreen", "#98FB98", 	 
			"PaleTurquoise", "#AFEEEE", 	 
			"PaleVioletRed", "#D87093", 	 
			"PapayaWhip", "#FFEFD5", 	 
			"PeachPuff", "#FFDAB9", 	 
			"Peru", "#CD853F", 	 
			"Pink", "#FFC0CB", 	 
			"Plum", "#DDA0DD", 	 
			"PowderBlue", "#B0E0E6", 	 
			"Purple", "#800080", 	 
			"Red", "#FF0000", 	 
			"RosyBrown", "#BC8F8F", 	 
			"RoyalBlue", "#4169E1", 	 
			"SaddleBrown", "#8B4513", 	 
			"Salmon", "#FA8072", 	 
			"SandyBrown", "#F4A460", 	 
			"SeaGreen", "#2E8B57", 	 
			"SeaShell", "#FFF5EE", 	 
			"Sienna", "#A0522D", 	 
			"Silver", "#C0C0C0", 	 
			"SkyBlue", "#87CEEB", 	 
			"SlateBlue", "#6A5ACD", 	 
			"SlateGray", "#708090", 	 
			"Snow", "#FFFAFA", 	 
			"SpringGreen", "#00FF7F", 	 
			"SteelBlue", "#4682B4", 	 
			"Tan", "#D2B48C", 	 
			"Teal", "#008080", 	 
			"Thistle", "#D8BFD8", 	 
			"Tomato", "#FF6347", 	 
			"Turquoise", "#40E0D0", 	 
			"Violet", "#EE82EE", 	 
			"Wheat", "#F5DEB3", 	 
			"White", "#FFFFFF", 	 
			"WhiteSmoke", "#F5F5F5", 	 
			"Yellow", "#FFFF00", 	 
	};
}
