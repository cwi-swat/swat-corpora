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

import java.util.List;

import org.eclipse.e4.xwt.vex.problems.Problem;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IntegerPropertyChecker extends AbstractProblemChecker {

	protected void checkProblems(StructuredTextEditor textEditor, String javaName, Node node, List<Problem> problems, boolean includeChildren) {
		if (!isValid(node)) {
			return;
		}

		NodeList childNodes = node.getChildNodes();
		if (includeChildren && childNodes != null) {
			int length = childNodes.getLength();
			for (int i = 0; i < length; i++) {
				Node child = childNodes.item(i);
				checkProblems(textEditor, javaName, child, problems, includeChildren);
			}
		}

		String nodeName = getTagName(node);

		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			return;
		}
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			Node attr = attributes.item(i);
			String attrName = getTagName(attr);
			if (attrName == null) {
				continue;
			}
			boolean isDb = isInteger(node, attrName);
			if (isDb) {
				String nodeValue = attr.getNodeValue();
				double value = -1;
				try {
					value = Integer.parseInt(nodeValue);
				} catch (NumberFormatException e) {
					Problem problem = createProblem(textEditor, node, "Error Integer Format for Property \"" + attrName + "\" of \"" + nodeName + "\".", Problem.ERROR);
					problems.add(problem);
				}
				if (value <= 0) {
					if (attrName.equals("ColumnSpan")) {
						Problem problem = createProblem(textEditor, node, "\"ColumnSpan\" must more than 0.", Problem.ERROR);
						problems.add(problem);
					} else if (attrName.equals("RowSpan")) {
						Problem problem = createProblem(textEditor, node, "\"RowSpan\" must more than 0.", Problem.ERROR);
						problems.add(problem);
					}
				}
			}
		}
	}

	private boolean isInteger(Node node, String attrName) {
		String nodeName = getTagName(node);
		if (nodeName == null) {
			return false;
		}
		// Metaclass metaclass = UPF.getMetaclass(nodeName, DomHelper.lookupNamespaceURI(node));
		// if (metaclass == null) {
		// return false;
		// }
		// Property property = metaclass.findProperty(attrName);
		// if (property == null) {
		// property = metaclass.findAttachedProperty(attrName);
		// }
		// Node parentNode = node.getParentNode();
		// if (property == null && parentNode != null) {
		// String parent = getTagName(parentNode);
		// Metaclass pMetaclass = UPF.getMetaclass(parent, DomHelper.lookupNamespaceURI(parentNode));
		// if (pMetaclass != null && pMetaclass.isSubclassOf(Panel.metaclass)) {
		// return isInteger(parentNode, attrName);
		// }
		// }
		// if (property == null) {
		// return false;
		// }
		// return property instanceof IntegerProperty;
		return false;
	}
}
