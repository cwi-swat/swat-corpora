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

import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.core.problems.Problem;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DomHelper;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTElementChecker extends XWTAbstractNodeChecker {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.editor.problems.XWTNodeChecker#checkNode(org.w3c.dom.Element, java.util.List)
	 */
	protected void checkNode(Node node, List<Problem> problems) {
		if (Node.TEXT_NODE == node.getNodeType()) {
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
		if (nodeName.indexOf(":") != -1) {
			nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
		}
		if (nodeName.indexOf(".") != -1 || "Array".equals(nodeName)) {
			return;
		}
		if (node instanceof Element) {
			IMetaclass metaclass = XWTUtility.getMetaclass(nodeName, ns);
			if (metaclass == null) {
				problems.add(createProblem(node, "\"" + nodeName + "\" can not be resolved to a Type.", Problem.ERROR));
			}
		}
	}

}
