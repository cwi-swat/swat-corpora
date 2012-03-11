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
package org.eclipse.e4.xwt.ui.editor.checkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.e4.xwt.vex.dom.DomHelper;
import org.eclipse.e4.xwt.vex.problems.Problem;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author jliu
 * 
 */
public class ElementPropertiesChecker extends AbstractProblemChecker {

	private static final String XMLNS_TAG = "xmlns";

	protected void checkProblems(StructuredTextEditor textEditor, String javaName, Node node, List<Problem> problems, boolean includeChindren) {
		if (!isValid(node)) {
			return;
		}
		NodeList childNodes = node.getChildNodes();
		if (includeChindren && childNodes != null) {
			int length = childNodes.getLength();
			for (int i = 0; i < length; i++) {
				Node item = childNodes.item(i);
				if (includeChindren) {
					checkProblems(textEditor, javaName, item, problems, includeChindren);
				}
			}
		}

		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			return;
		}
		int attrLength = attributes.getLength();
		if (attrLength == 0) {
			return;
		}
		String nodeName = getTagName(node);
		HashMap<String, List<String>> map = null;
		List<String> properties = getProperties(nodeName, DomHelper.lookupNamespaceURI(node), true);
		if (properties == null) {
			properties = Collections.EMPTY_LIST;
		}

		for (int j = 0; j < attrLength; j++) {
			Node attr = attributes.item(j);
			String attrName = getTagName(attr);
			if (attrName == null || isLoadingTag(attrName)) {
				continue;
			}
			int index = attrName.indexOf('.');
			if (index != -1) {
				String type = attrName.substring(0, index);
				String propertyName = attrName.substring(index + 1);
				if (map == null) {
					map = new HashMap<String, List<String>>();
				}
				collectAttachedProperties(type, DomHelper.lookupNamespaceURI(node, attr), map, true);
				List<String> attachedProperties = map.get(type);
				if (attachedProperties == null) {
					attachedProperties = Collections.EMPTY_LIST;
				}
				if (!attachedProperties.contains(propertyName)) {
					Problem error = createProblem(textEditor, node, "Attached Property \"" + propertyName + "\" of \"" + type + "\" doesn't exist", Problem.ERROR);
					problems.add(error);
				}
			} else if (!properties.contains(attrName)) {
				Problem error = createProblem(textEditor, node, "Property \"" + attrName + "\" of \"" + nodeName + "\" doesn't exist", Problem.ERROR);
				problems.add(error);
			}
		}
	}

	private boolean isLoadingTag(String tagName) {
		if (tagName == null || tagName.equals("")) {
			return false;
		}
		if (tagName.indexOf(":") != -1) {
			return true;
		}
		return XMLNS_TAG.endsWith(tagName) || tagName.startsWith(XMLNS_TAG);
	}

	private List<String> getProperties(String nodeName, String namespace, boolean includedEvents) {
		int index = nodeName.indexOf(':');
		String elementName = nodeName.substring(index + 1);

		// Metaclass metaclass = UPF.getMetaclass(elementName, namespace);
		// if (metaclass == null) {
		// // user defined class
		// if (index != -1) {
		// String packageName = namespace;
		// if (namespace.startsWith(IConstants.XAML_CLR_NAMESPACE_PROTO)) {
		// packageName = namespace.substring(IConstants.XAML_CLR_NAMESPACE_PROTO.length());
		// }
		// String qualidiedName = packageName + "." + elementName;
		// try {
		// Class kclass = UPF.getLoadingContext().getClassLoader().loadClass(qualidiedName);
		// metaclass = UPF.bindsMetaclass(kclass);
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
		// if (metaclass == null) {
		// return Collections.EMPTY_LIST;
		// }
		// }
		List<String> properties = new ArrayList<String>();
		// Collection<Property> allProperties = metaclass.getAllProperties();
		// for (Property property : allProperties) {
		// properties.add(property.getName());
		// }
		// if (includedEvents) {
		// for (Event event : metaclass.getAllEvents()) {
		// properties.add(event.getName());
		// }
		// }
		return properties;
	}

	private void collectAttachedProperties(String nodeName, String namespace, HashMap<String, List<String>> map, boolean includedEvents) {
		if (map.containsKey(nodeName)) {
			return;
		}
		// Metaclass metaclass = UPF.getMetaclass(nodeName, namespace);
		// if (metaclass == null) {
		// return;
		// }
		// List<String> properties = new ArrayList<String>();
		// map.put(nodeName, properties);
		// Collection<Property> allProperties = metaclass.getAllAttachedProperties();
		// for (Property property : allProperties) {
		// properties.add(property.getName());
		// }
		// if (includedEvents) {
		// for (Event event : metaclass.getAllAttachedEvents()) {
		// properties.add(event.getName());
		// }
		// }
	}
}
