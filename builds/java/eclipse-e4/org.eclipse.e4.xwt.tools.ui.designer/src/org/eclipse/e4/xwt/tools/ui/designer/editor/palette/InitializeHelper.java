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
package org.eclipse.e4.xwt.tools.ui.designer.editor.palette;

import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.dialogs.PropertyValueDialog;
import org.eclipse.e4.xwt.tools.ui.designer.preference.Preferences;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.tools.AnnotationTools;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class InitializeHelper {

	public static boolean checkValue(XamlNode node) {
		if (!XWTDesignerPlugin.getDefault().getPreferenceStore().getBoolean(Preferences.PROMPT_DURING_CREATION)) {
			return true;
		}
		if (node == null) {
			return false;
		}
		XamlNode cursorNode = getCursorNode(node);
		if (cursorNode != null && cursorNode instanceof XamlAttribute) {
			XamlAttribute a = (XamlAttribute) cursorNode;
			IProperty property = XWTUtility.getProperty(a);
			if (property != null) {
				String init = "";
				if ("text".equals(property.getName())) {
					String name = node.getName();
					init = "New" + name;
				}
				PropertyValueDialog dialog = new PropertyValueDialog(new Shell(), property, init);
				if (dialog.open() == Window.OK) {
					a.setValue(dialog.getResult());
				} else {
					return false;
				}
			}
		}
		return true;
	}

	private static XamlNode getCursorNode(XamlNode parent) {
		if (parent == null) {
			return null;
		}
		boolean isAnnotated = AnnotationTools.isAnnotated(parent, EntryHelper.ANN_CURSOR_DATA);
		if (isAnnotated) {
			return parent;
		}
		EList<XamlAttribute> attributes = parent.getAttributes();
		for (XamlAttribute a : attributes) {
			XamlNode node = getCursorNode(a);
			if (node != null) {
				return node;
			}
		}
		EList<XamlElement> childNodes = parent.getChildNodes();
		for (XamlElement c : childNodes) {
			XamlNode node = getCursorNode(c);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

}
