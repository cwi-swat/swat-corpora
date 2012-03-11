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
package org.eclipse.e4.xwt.tools.ui.designer.editor.problems;

import java.util.List;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.core.problems.Problem;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DomHelper;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTAttributeChecker extends XWTAbstractNodeChecker {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.editor.problems.XWTAbstractNodeChecker#checkNode(org.w3c.dom.Node, java.util.List)
	 */
	protected void checkNode(Node node, List<Problem> problems) {
		if (node == null || Node.TEXT_NODE == node.getNodeType()) {
			return;
		}
		NodeList childNodes = node.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				checkNode(childNodes.item(i), problems);
			}
		}
		String nodeName = node.getNodeName();
		String prefix = node.getPrefix();
		String ns = DomHelper.lookupNamespaceURI(node, prefix);
		int index = nodeName.indexOf(":");
		if (index != -1) {
			nodeName = nodeName.substring(index + 1);
		}
		String attrName = null;
		index = nodeName.indexOf(".");
		if (index != -1) {
			attrName = nodeName.substring(index + 1);
			nodeName = nodeName.substring(0, index);
		}
		IMetaclass metaclass = XWTUtility.getMetaclass(nodeName, ns);
		if (attrName != null) {
			index = attrName.indexOf(".");
			while (index != -1) {
				String newAttr = attrName.substring(0, index);
				attrName = attrName.substring(index + 1);
				index = attrName.indexOf(".");
				if (newAttr != null && !"Resources".equalsIgnoreCase(newAttr) && !"DataContext".equalsIgnoreCase(newAttr)) {
					IProperty p = metaclass.findProperty(newAttr);
					if (p == null && metaclass.findEvent(attrName) == null) {
						problems.add(createError(node, "\"" + newAttr + "\" can not be resolved to a property of \"" + nodeName + "\""));
					}
					if (p != null) {
						metaclass = XWT.getMetaclass(p.getType());
					}
				}
			}
			if (metaclass != null && attrName != null && !"Resources".equalsIgnoreCase(attrName) && !"DataContext".equalsIgnoreCase(attrName)) {
				IProperty p = metaclass.findProperty(attrName);
				if (p == null && metaclass.findEvent(attrName) == null) {
					problems.add(createError(node, "\"" + attrName + "\" can not be resolved to a property of \"" + nodeName + "\""));
				}
				if (p != null) {
					metaclass = XWT.getMetaclass(p.getType());
				}
			}
		}
		NamedNodeMap attributes = node.getAttributes();
		if (metaclass == null || attributes == null || attributes.getLength() == 0) {
			return;
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			Node item = attributes.item(i);
			attrName = item.getNodeName();
			if (attrName.startsWith("xmlns")) {
				continue;
			}
			if (attrName.indexOf(":") != -1) {
				attrName = attrName.substring(attrName.indexOf(":") + 1);
			}
			if ("Key".equalsIgnoreCase(attrName) || "Name".equalsIgnoreCase(attrName) || "Class".equalsIgnoreCase(attrName) || "style".equalsIgnoreCase(attrName)) {
				continue;
			}
			IMetaclass attachedMetaclass = null;
			index = attrName.indexOf(".");
			while (index != -1) {
				String attachedName = attrName.substring(0, index);
				attrName = attrName.substring(index + 1);
				index = attrName.indexOf(".");
				if (attachedName != null) {
					attachedMetaclass = XWTUtility.getMetaclass(attachedName, ns);
				}
			}
			if (metaclass.findProperty(attrName) == null && metaclass.findEvent(attrName) == null) {
				if (attachedMetaclass != null && attachedMetaclass.findProperty(attrName) == null && attachedMetaclass.findEvent(attrName) == null) {
					problems.add(createError(node, "\"" + attrName + "\" can not be resolved to a property of \"" + nodeName + "\""));
				}
			}
		}
	}
}
