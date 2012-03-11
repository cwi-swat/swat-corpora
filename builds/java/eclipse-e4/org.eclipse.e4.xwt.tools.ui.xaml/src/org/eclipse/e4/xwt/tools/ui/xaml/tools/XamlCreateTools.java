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
package org.eclipse.e4.xwt.tools.ui.xaml.tools;

import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XamlCreateTools {

	public static XamlAttribute addAttribute(XamlNode parent, String name,
			String namespace, String value) {
		if (parent == null || name == null || namespace == null) {
			throw new NullPointerException();
		}
		XamlAttribute attribute = parent.getAttribute(name, namespace);
		boolean isNew = false;
		if (attribute == null) {
			attribute = XamlFactory.eINSTANCE.createXamlAttribute();
			attribute.setName(name);
			attribute.setNamespace(namespace);
			isNew = true;
		}
		if (value != null) {
			attribute.setValue(value);
		}
		if (isNew) {
			parent.getAttributes().add(attribute);
		}
		return attribute;
	}

	public static XamlAttribute addAttribute(XamlNode parent, String name,
			String namespace) {
		return addAttribute(parent, name, namespace, null);
	}

	public static XamlAttribute addComplexAttribute(XamlElement parent,
			String name, String namespace, String childName,
			String childNamespace) {
		return addComplexAttribute(parent, name, namespace, childName,
				childNamespace, false);
	}

	public static XamlAttribute addComplexAttribute(XamlElement parent,
			String name, String namespace, String childName,
			String childNamespace, boolean allowMutilChild) {
		if (parent == null || name == null || namespace == null
				|| childName == null || childNamespace == null) {
			throw new NullPointerException();
		}
		XamlAttribute attribute = parent.getAttribute(name, namespace);
		if (attribute == null) {
			attribute = createAttribute(name, namespace);
			XamlElement child = createElement(childName, childNamespace);
			attribute.getChildNodes().add(child);
			parent.getAttributes().add(attribute);
		} else if (attribute.getChild(childName, childNamespace) == null) {
			XamlElement child = createElement(childName, childNamespace);
			if (!allowMutilChild) {
				attribute.getChildNodes().clear();
			}
			attribute.getChildNodes().add(child);
		}
		return attribute;
	}

	public static XamlAttribute createAttribute(String name, String namespace) {
		if (name == null || namespace == null) {
			throw new NullPointerException();
		}
		XamlAttribute attribute = XamlFactory.eINSTANCE.createXamlAttribute();
		attribute.setName(name);
		attribute.setNamespace(namespace);
		return attribute;
	}

	public static XamlElement createElement(String name, String namespace) {
		if (name == null || namespace == null) {
			throw new NullPointerException();
		}
		XamlElement element = XamlFactory.eINSTANCE.createXamlElement();
		element.setName(name);
		element.setNamespace(namespace);
		return element;
	}

	private XamlCreateTools() {
	}

}
