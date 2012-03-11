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
package org.eclipse.e4.xwt.tools.ui.designer.editor.model;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.internal.xml.Attribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * Parse the XAML extension Markup: {StaticResource test} {StaticResource RessourceKey=test} {DynamicResource {x:Static SystemColors.ControlBrushKey}}
 * 
 * @author jliu jin.liu@soyatec.com
 */
public class BraceHandler {
	private XamlElement root;
	private XamlNode current;
	private XamlNode forParse;
	private XamlDocument document;

	public BraceHandler(XamlDocument document) {
		this.document = document;
	}

	public XamlDocument getDocument() {
		return document;
	}

	public XamlNode parse(XamlNode element, String text) {
		this.forParse = element;
		if (root != null) {
			BraceHandler parser = new BraceHandler(document);
			return parser.parse(element, text);
		}
		current = element;

		StringTokenizer stringTokenizer = new StringTokenizer(text, "{}", true);
		String previous = null;
		String nextPrevious = null;
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();

			if (previous != null) {
				if (previous.equals("{")) {
					if (token.equals("}")) {
						// escape sequence
						if (stringTokenizer.hasMoreTokens()) {
							handleBlock(stringTokenizer.nextToken(" \t\n\r\f"), false);
						}
					} else {
						startBlock();
					}
				} else if (previous.equals("}")) {
					endBlock();
				} else {
					StringBuffer block = new StringBuffer(previous);
					if (token.equals("{")) {
						int level = 1;
						block.append(token);
						while (stringTokenizer.hasMoreTokens() && level >= 0) {
							String value = stringTokenizer.nextToken();
							if (value.equals("{")) {
								level++;
							} else if (value.equals("}")) {
								level--;
							}
							if (level >= 0) {
								block.append(value);
							}
						}
					}
					handleBlock(block.toString(),
							(nextPrevious == null || !nextPrevious.equals("}")));
				}
			}
			nextPrevious = previous;
			previous = token;
		}
		XamlNode result = root;
		root = null;
		current = null;
		return result;
	}

	protected void startBlock() {
	}

	protected void endBlock() {
		if (current != null) {
			EObject container = current.eContainer();
			current = (XamlNode) container.eContainer();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.tools.ui.designer.core.editor.builder.BraceHandler#expendNamespaces(org.eclipse.e4.xwt.tools.ui.xaml.XamlNode, java.lang.String)
	 */
	protected String expendNamespaces(XamlNode element, String value) {
		if (value.indexOf(':') == -1) {
			return value;
		}
		EMap<String, String> declaredNamespaces = getDocument().getDeclaredNamespaces();
		int length = IConstants.XAML_CLR_NAMESPACE_PROTO.length();
		for (String prefix : declaredNamespaces.keySet()) {
			String namespace = declaredNamespaces.get(prefix);
			if (namespace.startsWith(IConstants.XAML_CLR_NAMESPACE_PROTO)) {
				String packageName = namespace.substring(length);
				value = value.replace(prefix + ":", packageName + '.');
			}
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.tools.ui.designer.core.editor.builder.BraceHandler#handleContent(org.eclipse.e4.xwt.tools.ui.xaml.XamlNode, java.lang.String)
	 */
	protected void handleContent(XamlNode element, String text) {
		if (text.startsWith("{") && text.endsWith("}")) {
			parse(element, text);
			return;
		} else {
			// handle the case: <x:Array Type="ns:Type" >
			if (IConstants.XAML_X_TYPE.equals(element.getName()) || IConstants.XAML_X_STATIC.equals(element.getName())) {
				int index = text.indexOf(':');
				if (index != -1) {
					String ns = text.substring(0, index);
					String content = text.substring(index + 1);
					String namespace = getDocument().getDeclaredNamespace(ns);
					if (namespace != null) {
						XamlElement childElement = element.getChild(content, namespace);
						if (childElement == null) {
							childElement = XamlFactory.eINSTANCE.createElement(content, namespace);
							childElement.getChildNodes().add(childElement);
						}
						return;
					}
				}
			}
		}
		if (element instanceof Attribute && IConstants.XWT_X_NAMESPACE.equals(element.getNamespace())
				&& IConstants.XAML_STYLE.equalsIgnoreCase(element.getName())) {
			// handle the expansion of x:Style = "(j:class).variable"
			text = expendNamespaces(element, text);
		}
		element.setValue(text);
	}

	protected XamlElement createElement(String token) {
		int index = token.indexOf(':');
		String namespace = null;
		String name = token;
		if (index != -1) {
			String prefix = token.substring(0, index);
			name = token.substring(index + 1);
			namespace = document.getDeclaredNamespace(prefix);
		}
		if (namespace == null) {
			namespace = document.getDeclaredNamespace(null);
		}
		XamlElement element = null;
		if (current != null) {
			element = current.getChild(name, namespace);
		}
		if (element == null) {
			element = XamlFactory.eINSTANCE.createElement(name, namespace);
			element.setId(XWTModelBuilder.generateID(name));
		}
		if (current != null && current != forParse) {
			// There's no need to add the children for given parent at the beginning of parser.
			current.getChildNodes().add(element);
		} else {
			if (root == null) {
				root = element;
			}
		}
		current = element;
		return element;
	}

	private XamlAttribute createAttribute(XamlNode parent, String name, String namespace) {
		XamlAttribute attribute = parent.getAttribute(name, namespace);
		if (attribute == null) {
			attribute = XamlFactory.eINSTANCE.createAttribute(name, namespace);
			attribute.setId(XWTModelBuilder.generateID(name));
		}
		return attribute;
	}

	protected void handleBlock(String content, boolean newElement) {
		String rootPattern = " \t\n\r\f=,";
		StringTokenizer tokenizer = new StringTokenizer(content, rootPattern, true);
		String attributeName = null;
		String attributeValue = null;
		boolean equals = false;
		XamlElement element = null;
		if (!newElement && current instanceof XamlElement) {
			element = (XamlElement) current;
		}

		boolean skip = false;
		String token = null;
		while (skip || tokenizer.hasMoreTokens()) {
			if (!skip) {
				token = tokenizer.nextToken(rootPattern).trim();
			}
			skip = false;
			if (token.length() == 0) {
				continue;
			}
			if (element == null) {
				element = createElement(token);
			} else {
				if (token.equals("=")) {
					equals = true;
					if ("xpath".equalsIgnoreCase(attributeName)) {
						attributeValue = tokenizer.nextToken(",");
					}
					continue;
				}
				if (token.equals(",")) {
					if (attributeName != null) {
						if (attributeValue != null) {
							XamlAttribute attribute = createAttribute(element, attributeName, element.getNamespace());
							if ("path".equalsIgnoreCase(attributeName) && "Binding".equalsIgnoreCase(element.getName())) {
								attributeValue = expendNamespaces(element, attributeValue);
							}
							handleContent(attribute, attributeValue);
							if (!element.getAttributes().contains(attribute)) {
								element.getAttributes().add(attribute);
							}
							current = attribute;
						} else {
							element.setValue(attributeName);
						}
						attributeName = null;
						attributeValue = null;
						equals = false;
					}
				} else {
					if (attributeName == null) {
						attributeName = token;
					} else {
						// String block = token;
						StringBuffer block = new StringBuffer(token);
						if (token.startsWith("{")) {
							int level = 1;
							while (tokenizer.hasMoreTokens() && level > 0) {
								String value = tokenizer.nextToken("{}");
								if (value.equals("{")) {
									level++;
								} else if (value.equals("}")) {
									level--;
								}
								block.append(value);
							}
						}
						attributeValue = block.toString();

						try {
							token = tokenizer.nextToken(rootPattern).trim();
							skip = true;
							continue;
						} catch (NoSuchElementException e) {
						}
					}
				}
			}
			skip = false;
		}

		if (equals) {
			XamlAttribute attribute = createAttribute(element, attributeName, element.getNamespace());
			if ("path".equalsIgnoreCase(attributeName) && "Binding".equalsIgnoreCase(element.getName())) {
				attributeValue = expendNamespaces(element, attributeValue);
			}
			if (attributeValue != null) {
				handleContent(attribute, attributeValue);
			} else {
				current = attribute;
			}
			if (!element.getAttributes().contains(attribute)) {
				element.getAttributes().add(attribute);
			}
		} else if (attributeName != null) {
			int index = attributeName.indexOf(":");
			if (index != -1) {
				element = createElement(attributeName);
				current = (XamlNode) current.eContainer();
			} else {
				current.setValue(attributeName);
			}
		}
	}

}
