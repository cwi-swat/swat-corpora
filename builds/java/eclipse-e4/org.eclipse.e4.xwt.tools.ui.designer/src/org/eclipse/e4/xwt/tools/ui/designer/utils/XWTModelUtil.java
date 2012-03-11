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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTModelUtil {

	/**
	 * This method is use to get another attribute for given attrName, the name of another attribute should be contained in adapters.
	 */
	public static XamlAttribute getAdaptableAttribute(XamlNode parent, String[] adapters, String attrName, String namespace) {
		XamlAttribute attribute = null;
		if (adapters == null || adapters.length == 0) {
			return null;
		}
		List<String> copyAdapters = new ArrayList<String>(Arrays.asList(adapters));
		if (copyAdapters.remove(attrName)) {
			for (String other : copyAdapters) {
				attribute = parent.getAttribute(other, namespace);
				if (attribute != null) {
					break;
				}
			}
		}
		return attribute;
	}

	/**
	 * This method is use to retrieve another attribute for given attrName, for TableViewer of JFace, both control and table are same objects.
	 */
	public static XamlAttribute getAdaptableAttribute(XamlNode parent, String attrName, String namespace) {
		if (parent == null || attrName == null) {
			return null;
		}
		XamlAttribute attribute = parent.getAttribute(attrName, namespace);
		if (attribute == null) {
			IMetaclass metaclass = XWTUtility.getMetaclass(parent);
			if (metaclass == null) {
				return null;
			}
			Class<?> type = metaclass.getType();
			if (TableViewer.class.isAssignableFrom(type)) {
				attribute = getAdaptableAttribute(parent, new String[] { "table", "control" }, attrName, namespace);
			} else if (ListViewer.class.isAssignableFrom(type)) {
				attribute = getAdaptableAttribute(parent, new String[] { "list", "control" }, attrName, namespace);
			} else if (TreeViewer.class.isAssignableFrom(type)) {
				attribute = getAdaptableAttribute(parent, new String[] { "tree", "control" }, attrName, namespace);
			} else if (ComboViewer.class.isAssignableFrom(type)) {
				attribute = getAdaptableAttribute(parent, new String[] { "combo", "control" }, attrName, namespace);
			}
		}
		return attribute;
	}

	/**
	 * This method is use to retrieve attribute from parent, for viewers of JFace, some attribute should be set to its control object.
	 */
	public static XamlAttribute getChildAttribute(XamlNode parent, String attrName, String namespace) {
		if (parent == null || attrName == null) {
			return null;
		}
		XamlAttribute attribute = parent.getAttribute(attrName, namespace);
		if (attribute == null) {
			IMetaclass metaclass = XWTUtility.getMetaclass(parent);
			if (metaclass == null) {
				return null;
			}
			Class<?> type = metaclass.getType();
			if (TableViewer.class.isAssignableFrom(type)) {
				attribute = getChildAttribute(parent, new String[] { "table", "control" }, attrName, namespace);
			} else if (ListViewer.class.isAssignableFrom(type)) {
				attribute = getChildAttribute(parent, new String[] { "list", "control" }, attrName, namespace);
			} else if (TreeViewer.class.isAssignableFrom(type)) {
				attribute = getChildAttribute(parent, new String[] { "tree", "control" }, attrName, namespace);
			} else if (ComboViewer.class.isAssignableFrom(type)) {
				attribute = getChildAttribute(parent, new String[] { "combo", "control" }, attrName, namespace);
			}
		}
		return attribute;
	}

	public static XamlAttribute getChildAttribute(XamlNode parent, String[] adapters, String attrName, String namespace) {
		XamlAttribute attribute = null;
		if (adapters == null || adapters.length == 0) {
			return null;
		}
		List<String> copyAdapters = new ArrayList<String>(Arrays.asList(adapters));
		for (String other : copyAdapters) {
			XamlAttribute adapter = parent.getAttribute(other, namespace);
			if (adapter == null) {
				continue;
			}
			attribute = adapter.getAttribute(attrName, namespace);
			if (attribute != null) {
				break;
			}
		}
		return attribute;
	}
}
