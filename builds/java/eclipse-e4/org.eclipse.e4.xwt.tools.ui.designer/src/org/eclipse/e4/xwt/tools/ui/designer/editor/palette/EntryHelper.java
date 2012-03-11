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
package org.eclipse.e4.xwt.tools.ui.designer.editor.palette;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.internal.utils.LoggerManager;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.preference.Preferences;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.tools.AnnotationTools;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.requests.CreateRequest;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class EntryHelper {

	public static final String CURSOR_CONSTANTS = "${cursor}";
	public static final String ANN_CURSOR_DATA = "CURSOR_DATA_ANN";

	private static Map<Entry, XamlNode> nodes = new HashMap<Entry, XamlNode>();

	public static XamlNode getNode(Entry entry) {
		if (entry == null) {
			return null;
		}
		XamlNode node = nodes.get(entry);
		if (node == null || node.eContainer() != null) {
			try {
				node = new EntryHelper().createNode(entry);
				if ("Composite".equals(node.getName())) {
					initLayoutAttribute(node);
				}
				nodes.put(entry, node);
			} catch (Exception e) {
				LoggerManager.log(e);
			}
		}
		return (XamlNode) EcoreUtil.copy(node);
	}

	public static void initLayoutAttribute(XamlNode model) {
		XamlAttribute layoutAttr = XamlFactory.eINSTANCE.createAttribute("layout", IConstants.XWT_NAMESPACE);
		String layout = Preferences.getPreferenceStore().getString(Preferences.DEFAULT_LAYOUT);
		LayoutType layoutType = LayoutsHelper.getLayoutType(layout);
		if (layoutType != null && LayoutType.NullLayout != layoutType && LayoutType.Unknown != layoutType) {
			XamlElement newLayout = XamlFactory.eINSTANCE.createElement(layoutType.value(), IConstants.XWT_NAMESPACE);
			layoutAttr.getChildNodes().add(newLayout);
			model.getAttributes().add(layoutAttr);
		}
	}

	public static XamlNode getNode(CreateRequest createReq) {
		Object newObject = createReq.getNewObject();
		if (newObject instanceof Entry) {
			return getNode((Entry) newObject);
		}
		return null;
	}

	private XamlNode createNode(Entry entry) {
		if (entry == null || entry.getContent() == null) {
			return null;
		}
		String content = entry.getContent();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			try {
				org.w3c.dom.Document document = domBuilder.parse(new ByteArrayInputStream(content.getBytes()));
				if (document != null) {
					org.w3c.dom.Element element = document.getDocumentElement();
					String name = element.getNodeName();
					String ns = element.getNamespaceURI();
					String prefix = element.getPrefix();
					if (ns == null) {
						ns = getNamespace(prefix);
					}
					XamlNode node = null;
					if (name.indexOf(".") != -1) {
						name = name.substring(name.indexOf(".") + 1);
						node = XamlFactory.eINSTANCE.createXamlAttribute();
						node.setName(name);
						node.setNamespace(ns);
					} else {
						node = XamlFactory.eINSTANCE.createXamlElement();
						node.setName(name);
						node.setNamespace(ns);
					}
					node.setPrefix(prefix);
					createModel(node, element);
					return node;
				}
			} catch (Exception e) {
				throw new XWTException(e);
			}

		} catch (ParserConfigurationException e) {
			throw new XWTException(e);
		}

		return null;
	}

	private String getNamespace(String prefix) {
		if ("x".equals(prefix)) {
			return IConstants.XWT_X_NAMESPACE;
		}
		return IConstants.XWT_NAMESPACE;
	}

	private void createModel(XamlNode parent, org.w3c.dom.Element text) {
		NamedNodeMap attributes = text.getAttributes();
		if (attributes != null) {
			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				Attr attr = (Attr) attributes.item(i);
				createAttribute(parent, attr);
			}
		}
		NodeList childNodes = text.getChildNodes();
		if (childNodes == null) {
			return;
		}
		int length = childNodes.getLength();
		for (int i = 0; i < length; i++) {
			Node item = childNodes.item(i);
			if (item instanceof Element) {
				Element child = (Element) item;
				createChild(parent, child);
			}
		}
	}

	private void createChild(XamlNode parent, Element child) {
		String name = child.getNodeName();
		String ns = child.getNamespaceURI();
		String prefix = child.getPrefix();
		if (ns == null) {
			ns = getNamespace(prefix);
		}
		if (name.indexOf(".") != -1) {
			name = name.substring(name.indexOf(".") + 1);
			XamlAttribute a = XamlFactory.eINSTANCE.createXamlAttribute();
			a.setName(name);
			a.setNamespace(ns);
			createModel(a, child);
			parent.getAttributes().add(a);
		} else {
			XamlElement e = XamlFactory.eINSTANCE.createXamlElement();
			e.setName(name);
			e.setNamespace(ns);
			createModel(e, child);
			parent.getChildNodes().add(e);
		}
	}

	/**
	 * @param parent
	 * @param attr
	 */
	private void createAttribute(XamlNode parent, Attr attr) {
		if (attr == null) {
			return;
		}
		String name = attr.getName();
		String ns = attr.getNamespaceURI();
		String value = attr.getNodeValue();
		boolean containsCursor = false;
		if (value != null && CURSOR_CONSTANTS.equals(value)) {
			value = "";
			containsCursor = true;
		}
		String prefix = attr.getPrefix();
		int index = name.indexOf(":");
		if (index != -1) {
			prefix = name.substring(0, index);
			name = name.substring(index + 1);
		}
		if (ns == null) {
			ns = getNamespace(prefix);
		}
		XamlAttribute a = parent.getAttribute(name, ns);
		if (a == null) {
			a = XamlFactory.eINSTANCE.createXamlAttribute();
			a.setName(name);
			a.setNamespace(ns);
			a.setPrefix(prefix);
			a.setValue(value);
			parent.getAttributes().add(a);
		} else if (value == null && a.getValue() != null) {
			a.setValue(null);
		} else if (value != null && value != a.getValue()) {
			a.setValue(value);
		}
		if (containsCursor) {
			AnnotationTools.addAnnotation(a, ANN_CURSOR_DATA, name);
		}
	}

}
