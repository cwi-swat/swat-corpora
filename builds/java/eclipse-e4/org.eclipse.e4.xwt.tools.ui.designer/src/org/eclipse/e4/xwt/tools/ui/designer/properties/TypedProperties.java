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
package org.eclipse.e4.xwt.tools.ui.designer.properties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class TypedProperties {

	private static List<String> types;
	private static ResourceBundle TYPED_PROPERTIES = ResourceBundle.getBundle("org.eclipse.e4.xwt.tools.ui.designer.properties.types");

	public static synchronized List<String> getTypes() {
		if (types == null) {
			types = new ArrayList<String>();
			Enumeration<String> enumeration = TYPED_PROPERTIES.getKeys();
			while (enumeration.hasMoreElements()) {
				types.add((String) enumeration.nextElement());
			}

			Collections.sort(types);
		}
		return types;
	}

	public static String[] getProperties(String type) {
		List<String> result = new ArrayList<String>();
		String value = TYPED_PROPERTIES.getString(type);
		StringTokenizer stk = new StringTokenizer(value, ",");
		while (stk.hasMoreElements()) {
			result.add(stk.nextToken().trim());
		}
		return result.toArray(new String[0]);
	}

	public static int getValue(String property) {
		try {
			Field field = SWT.class.getDeclaredField(property);
			return field.getInt(null);
		} catch (Exception e) {
		}
		return -1;
	}

	public static ILabelProvider getLabelProvider(final String type) {
		return new LabelProvider() {
			public String getText(Object element) {
				if (element instanceof Integer) {
					int intValue = (Integer) element;
					String[] properties = getProperties(type);
					for (String prop : properties) {
						if (intValue == getValue(prop)) {
							return prop;
						}
					}
				}
				return super.getText(element);
			}
		};
	}

}
