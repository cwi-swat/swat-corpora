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
import java.util.List;

import org.eclipse.e4.xwt.vex.problems.Problem;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author jliu
 */
public class ElementNameChecker extends AbstractProblemChecker {

	public List<Problem> checkProblems(StructuredTextEditor textEditor, String javaName) {

		List<Problem> problems = new ArrayList<Problem>();

		Node root = getRoot(textEditor);
		checkProblems(textEditor, root, problems, new ArrayList<String>());

		return problems;
	}

	private void checkProblems(StructuredTextEditor textEditor, Node node, List<Problem> problems, List<String> existedNames) {
		if (!isValid(node)) {
			return;
		}

		NodeList childNodes = node.getChildNodes();
		if (childNodes != null) {
			int length = childNodes.getLength();
			for (int i = 0; i < length; i++) {
				Node item = childNodes.item(i);
				checkProblems(textEditor, item, problems, existedNames);
			}
		}

		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			return;
		}
		int attrLength = attributes.getLength();
		for (int j = 0; j < attrLength; j++) {
			Node attr = attributes.item(j);
			String nodeName = attr.getNodeName();
			if (nodeName == null) {
				continue;
			}
			if ("x:Name".equalsIgnoreCase(nodeName)) {
				String nodeValue = attr.getNodeValue();
				if (nodeValue == null) {
					continue;
				}
				if (existedNames.contains(nodeValue)) {
					Problem problem = createProblem(textEditor, node, "Element Name \"" + nodeValue + "\" is already used.", Problem.ERROR);
					problems.add(problem);
				} else {
					existedNames.add(nodeValue);
				}
			}
		}
	}

	@Override
	protected void checkProblems(StructuredTextEditor textEditor, String javaName, Node node, List<Problem> problems, boolean includeChildren) {
		// Do nothing
	}

}
