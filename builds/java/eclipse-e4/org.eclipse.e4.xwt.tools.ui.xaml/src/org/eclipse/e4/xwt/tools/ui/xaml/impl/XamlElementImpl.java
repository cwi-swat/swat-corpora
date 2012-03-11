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
package org.eclipse.e4.xwt.tools.ui.xaml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class XamlElementImpl extends XamlNodeImpl implements XamlElement {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected XamlElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return XamlPackage.Literals.XAML_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.xaml.Generation#generate(org.w3c.dom.Document, org.w3c.dom.Node, java.util.Map)
	 */
	public Node generate(Document document, Map<String, Object> options) {
		if (document == null) {
			return null;
		}
		String prefix = getPrefix();
		if (prefix != null) {
			Element root = document.getDocumentElement();
			Attr prefixNode = root.getAttributeNode(prefix);
			if (prefixNode == null) {
				root.setAttribute("xmlns:" + prefix, namespace);
				if (getOwnerDocument() != null) {
					getOwnerDocument().addDeclaredNamespace(prefix, namespace);
				}
			}
		}
		String qualifiedName = prefix == null ? getName() : prefix + ":"
				+ getName();
		String namespace = getNamespace();
		Element node = document.createElementNS(namespace, qualifiedName);
		for (XamlAttribute attribute : getAttributes()) {
			Node newAttr = attribute.generate(document, options);
			if (newAttr == null) {
				continue;
			}
			if (newAttr instanceof Attr) {
				node.setAttributeNode((Attr) newAttr);
			} else if (newAttr.getChildNodes().getLength() != 0
					|| newAttr.getAttributes().getLength() != 0) {
				node.appendChild(newAttr);
			}
		}
		for (XamlElement child : getChildNodes()) {
			Node newChild = child.generate(document, options);
			if (newChild != null) {
				node.appendChild(newChild);
			}
		}
		String value = getValue();
		if (value != null) {
			generateContent(document, node, value);
		}
		return node;
	}

	protected void generateContent(Document document, Node node, String value) {
		String content = getContent(node);
		if (equals(value, content)) {
			return;
		}
		value = value == null ? "" : value;
		if (content != null) {
			List<Text> contentNodes = getContentNodes(node);
			for (Text text : contentNodes) {
				String nodeValue = text.getNodeValue();
				if (nodeValue == null || filter(nodeValue).length() == 0) {
					continue;
				}
				text.setData(value);
			}
		} else {
			Text textNode = document.createTextNode(value == null ? "" : value);
			node.appendChild(textNode);
		}
	}

	/**
	 * Return content of a Node, "<j:String>hello world</j:String>"
	 */
	protected String getContent(Node parent) {
		List<Text> textNodes = getContentNodes(parent);
		if (textNodes.isEmpty()) {
			return null;
		}
		StringBuilder content = new StringBuilder();
		for (Text text : textNodes) {
			String value = text.getNodeValue();
			if (value == null) {
				continue;
			}
			value = filter(value);
			if (value.length() != 0) {
				content.append(value);
			}
		}
		return content.length() > 0 ? content.toString() : null;
	}

	protected List<Text> getContentNodes(Node node) {
		NodeList childNodes = node.getChildNodes();
		int length = childNodes.getLength();
		if (length == 0) {
			return Collections.emptyList();
		}
		List<Text> contentTexts = new ArrayList<Text>();
		for (int i = 0; i < length; i++) {
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.TEXT_NODE) {
				contentTexts.add((Text) item);
			}
		}
		return contentTexts;
	}

	protected String filter(String value) {
		value = value.replace("\n", "");
		value = value.replace("\t", "");
		value = value.replace("\r", "");
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.xaml.XamlNode#getFlatValue()
	 */
	public String getFlatValue() {
		StringBuilder result = new StringBuilder();
		String p = getPrefix();
		if (p != null) {
			result.append(p);
			result.append(":");
		}
		result.append(name);
		EList<XamlAttribute> attributes = getAttributes();
		EList<XamlElement> children = getChildNodes();
		if (!attributes.isEmpty()) {
			result.append(" ");
			for (int i = 0; i < attributes.size(); i++) {
				XamlAttribute attr = attributes.get(i);
				result.append(attr.getName());
				result.append("=");
				String flatValue = attr.getFlatValue();
				if (flatValue == null) {
					flatValue = "";
				}
				String value = flatValue.trim();
				if (value.indexOf(" ") != -1) {
					result.append("{" + value + "}");
				} else {
					result.append(value);
				}
				if (i + 1 < attributes.size()) {
					result.append(", ");
				}
			}
		} else if (!children.isEmpty()) {
			for (XamlElement child : children) {
				String flatValue = child.getFlatValue();
				if (flatValue == null) {
					continue;
				}
				result.append(" ");
				result.append(flatValue);
			}
		} else if (value != null) {
			result.append(" ");
			result.append(value);
		}
		return result.toString();
	}

} // XamlElementImpl
