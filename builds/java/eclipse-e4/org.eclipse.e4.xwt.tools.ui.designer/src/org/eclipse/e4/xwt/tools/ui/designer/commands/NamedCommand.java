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
package org.eclipse.e4.xwt.tools.ui.designer.commands;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.internal.core.ScopeKeeper;
import org.eclipse.e4.xwt.internal.core.ScopeManager;
import org.eclipse.e4.xwt.internal.utils.UserData;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class NamedCommand extends Command {

	private XamlNode node;
	private String newName;
	private String oldName;

	private XamlAttribute nameAttr;

	public NamedCommand(XamlNode node, String newName) {
		this.node = node;
		if (newName != null) {
			this.newName = newName;
		}
	}

	public NamedCommand(Widget widget, String newName) {
		this(XWTProxy.getModel(widget), newName);
		if (newName == null) {
			this.newName = generateName(widget);
		}
	}

	public NamedCommand(Widget widget) {
		this(widget, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (node == null || newName == null) {
			return false;
		}
		nameAttr = node.getAttribute("name");
		if (nameAttr == null) {
			nameAttr = node.getAttribute("name", IConstants.XWT_X_NAMESPACE);
		}
		if (nameAttr != null && newName.equals(nameAttr.getValue())) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		if (nameAttr == null) {
			nameAttr = XamlFactory.eINSTANCE.createAttribute("name", IConstants.XWT_NAMESPACE);
		}
		oldName = nameAttr.getValue();
		nameAttr.setValue(newName);
		if (!node.getAttributes().contains(nameAttr)) {
			node.getAttributes().add(nameAttr);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		if (oldName == null) {
			node.getAttributes().remove(nameAttr);
		} else {
			nameAttr.setValue(oldName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return node != null && nameAttr != null;
	}

	public static String generateName(Widget widget) {
		String typeName = widget.getClass().getSimpleName();
		typeName = typeName.length() > 1 ? Character.toLowerCase(typeName.charAt(0)) + typeName.substring(1) : typeName.toLowerCase();
		ScopeKeeper nameContext = UserData.findScopeKeeper(widget);
		if (nameContext != null) {
			String name = null;
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				name = typeName + i;
				if (!nameContext.containsName(name)) {
					break;
				}
			}
			return name;
		}
		return typeName + "0";
	}
}
