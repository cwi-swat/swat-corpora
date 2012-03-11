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
package org.eclipse.e4.xwt.tools.ui.designer.dialogs;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IEvent;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EventHandlerDialog extends AbstractEventHandlerDialog {
	public EventHandlerDialog(StructuredTextViewer textViewer, String oldHandler, String attrValue, String[] handlers) {
		super(textViewer, oldHandler, attrValue, handlers);
	}

	protected boolean usedByOther(Node node, String oldHandler) {
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		if (node == null) {
			return false;
		}
		String name = node.getNodeName();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			IMetaclass metaclass = XWT.getMetaclass(name, node.getNamespaceURI());
			if (metaclass != null) {
				NamedNodeMap nodeMap = node.getAttributes();
				for (int j = nodeMap.getLength() - 1; j >= 0; j--) {
					IDOMAttr attrNode = (IDOMAttr) nodeMap.item(j);
					String attrName = attrNode.getName();
					String attrValue = attrNode.getValue();
					IEvent event = metaclass.findEvent(attrName);
					if (event != null) {
						if (attrValue.equals(oldHandler)) {
							count++;
						}
					}
					if (count >= 1) {
						return true;
					}

				}
			}
		}
		NodeList nodes = node.getChildNodes();
		int length = nodes.getLength();
		for (int i = 0; i < length; i++) {
			Node childNode = nodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				usedByOther(childNode, oldHandler);
			}
		}
		if (count >= 1) {
			return true;
		} else {
			return false;
		}
	}
}
