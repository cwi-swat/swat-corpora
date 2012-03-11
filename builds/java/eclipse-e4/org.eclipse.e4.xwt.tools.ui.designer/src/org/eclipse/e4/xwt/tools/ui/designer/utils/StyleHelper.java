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

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.converters.StringToInteger;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class StyleHelper {

	public static boolean isMenuBar(Menu menu) {
		return checkStyle(menu, SWT.BAR);
	}

	public static boolean isOnMenubar(MenuItem menuItem) {
		return isMenuBar(menuItem.getParent());
	}

	public static boolean checkStyle(Widget widget, int style) {
		return widget != null && !widget.isDisposed() && checkStyle(widget.getStyle(), style);
	}

	public static boolean checkStyle(int styles, String style) {
		if ("SHELL_TRIM".equalsIgnoreCase(style) || "SWT.SHELL_TRIM".equalsIgnoreCase(style)) {
			return checkStyle(styles, SWT.CLOSE) && checkStyle(styles, SWT.MIN) && checkStyle(styles, SWT.MAX) && checkStyle(styles, SWT.TITLE) && checkStyle(styles, SWT.RESIZE);
		} else if ("DIALOG_TRIM".equalsIgnoreCase(style) || "SWT.DIALOG_TRIM".equalsIgnoreCase(style)) {
			return checkStyle(styles, SWT.CLOSE) && checkStyle(styles, SWT.TITLE) && checkStyle(styles, SWT.BORDER);
		}
		int value = (Integer) StringToInteger.instance.convert(style);
		return checkStyle(styles, value);
	}

	public static boolean checkStyle(int styles, int style) {
		return (styles & style) != 0;
	}

	public static boolean checkStyle(XamlNode node, int style) {
		return checkStyle(getStyle(node), style);
	}

	public static int getStyle(XamlNode node) {
		if (node == null) {
			return 0;
		}
		XamlAttribute attribute = node.getAttribute("style", IConstants.XWT_X_NAMESPACE);
		if (attribute != null && attribute.getValue() != null) {
			String styleValue = attribute.getValue();
			IConverter c = XWT.findConvertor(String.class, Integer.class);
			if (c != null) {
				return (Integer) c.convert(styleValue);
			}
		}
		return 0;
	}

}
