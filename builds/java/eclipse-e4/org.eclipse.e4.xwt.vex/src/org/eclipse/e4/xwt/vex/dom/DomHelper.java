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
package org.eclipse.e4.xwt.vex.dom;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DomHelper {
	public static String XMLNS = "xmlns";

	/**
	 * resolve the namespace
	 * 
	 * @param node
	 * @return
	 */
	public static String lookupNamespaceURI(Node node) {
		String string = node.getNamespaceURI();
		if (string != null) {
			return string;
		}
		return lookupNamespaceURI(node, node.getPrefix());
	}

	public static String lookupNamespaceURI(Node node, Node attr) {
		String string = attr.getNamespaceURI();
		if (string != null) {
			return string;
		}
		return lookupNamespaceURI(node, attr.getPrefix());
	}

	public static String lookupNamespaceURI(Node node, String prefix) {
		NamedNodeMap map = node.getAttributes();
		if (map != null && map.getLength() > 0) {
			for (int i = 0; i < map.getLength(); i++) {
				Node attr = map.item(i);
				String name = attr.getNodeName();
				if (name.startsWith(XMLNS)) {
					int index = name.indexOf(':');
					if (index == -1 || prefix == null) {
						if (index == -1 && prefix == null) {
							return attr.getNodeValue();
						}
						continue;
					}
					String prefixName = name.substring(index + 1);
					if (prefix.equals(prefixName)) {
						return attr.getNodeValue();
					}
				}
			}
		}

		Node nodeParent = node.getParentNode();
		if (nodeParent != null) {
			return lookupNamespaceURI(nodeParent, prefix);
		}
		return null;
	}
}
