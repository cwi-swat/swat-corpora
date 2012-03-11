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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class XWTUtility {

	private static Map<String, IMetaclass> metaclassCache = new HashMap<String, IMetaclass>();

	public static IMetaclass getMetaclass(XamlNode node) {
		if (node == null) {
			return null;
		}
		if (node instanceof XamlElement) {
			XamlElement e = (XamlElement) node;
			String name = e.getName();
			String ns = e.getNamespace();
			return getMetaclass(name, ns);
		}
		return null;
	}

	public static IMetaclass getMetaclass(String tagName, String ns) {
		if ("#comment".equals(tagName) || "Array".equals(tagName)) {
			return null;
		}
		if (tagName != null && tagName.length() > 1) {
			tagName = Character.toUpperCase(tagName.charAt(0)) + tagName.substring(1);
		}
		String key = tagName + "=&&=" + ns;
		IMetaclass metaclass = metaclassCache.get(key);
		if (metaclass == null) {
			try {
				metaclass = XWT.getMetaclass(tagName, ns);
				metaclassCache.put(key, metaclass);
			} catch (Exception e) {
			}
		}
		return metaclass;
	}

	public static IProperty getProperty(XamlNode node, String name) {
		IMetaclass metaclass = getMetaclass(node);
		if (metaclass == null) {
			return null;
		}
		return metaclass.findProperty(name);
	}

	public static IProperty getProperty(XamlAttribute attribute) {
		if (attribute == null) {
			return null;
		}
		XamlNode node = (XamlNode) attribute.eContainer();
		return getProperty(node, attribute.getName());
	}
}
