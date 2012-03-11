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
package org.eclipse.e4.xwt.tools.ui.designer.editor.dnd;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.DropContext;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DomHelper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Node;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTDropContext extends DropContext {

	private String getNodeName(Node node) {
		String name = node.getNodeName();
		int index = name.indexOf(":");
		if (index != -1) {
			name = name.substring(index + 1);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.dnd.DropContext#isKindOf(org.w3c.dom.Node, java.lang.String)
	 */
	protected boolean isKindOf(Node node, String targetType) {
		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			return false;
		}
		String namespace = DomHelper.lookupNamespaceURI(node);
		String tagName = getNodeName(node);
		if (tagName.indexOf(".") > 0) {
			tagName = (tagName.substring(tagName.lastIndexOf(".") + 1));
			tagName = Character.toUpperCase(tagName.charAt(0)) + tagName.substring(1);
			namespace = IConstants.XWT_NAMESPACE;
		}
		try {
			return isKindOf(tagName, namespace, targetType);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isKindOf(String nodeName, String namespace, String targetType) throws Exception {
		if (nodeName == null || namespace == null || targetType == null) {
			return false;
		}
		if ("Composite".equalsIgnoreCase(targetType)) {
			IMetaclass metaclass = XWT.getMetaclass(nodeName, namespace);
			if (metaclass == null) {
				return false;
			} else {
				return Composite.class.isAssignableFrom(metaclass.getType());
			}
		} else if ("Control".equalsIgnoreCase(targetType)) {
			IMetaclass metaclass = XWT.getMetaclass(nodeName, namespace);
			if (metaclass == null) {
				return false;
			} else {
				return Control.class.isAssignableFrom(metaclass.getType());
			}
		}
		return targetType.equalsIgnoreCase(nodeName);
	}
}
