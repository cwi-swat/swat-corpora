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

import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.commands.Command;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ApplyAttributeSettingCommand extends Command {

	private XamlNode parent;
	private String attributeName;
	private String namespace;
	private String oldValue;
	private String newValue;
	private boolean isCreation = false;
	private XamlAttribute attr;

	public ApplyAttributeSettingCommand(XamlNode parent, String attributeName, String namespace, String newValue) {
		super("Set " + attributeName);
		this.parent = parent;
		this.attributeName = attributeName;
		this.namespace = namespace;
		this.newValue = newValue;
	}

	public ApplyAttributeSettingCommand(XamlNode parent, XamlAttribute attr, String value) {
		this(parent, attr.getName(), attr.getNamespace(), value);
		this.attr = attr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (attr == null) {
			if (parent != null && attributeName != null) {
				attr = parent.getAttribute(attributeName, namespace);
			}
		}
		if (attr != null) {
			return newValue == null ? attr.getValue() != null : !newValue
					.equals(attr.getValue());
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		if (isCreation && parent != null) {
			return attr != null;
		}
		return attr != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		if (attr == null) {
			attr = parent.getAttribute(attributeName, namespace);
		}
		if (attr == null) {
			attr = XamlFactory.eINSTANCE.createAttribute(attributeName, namespace);
		} else {
			oldValue = attr.getValue();
		}
		if (newValue == null || newValue.length() == 0) {
			if (parent != null) {
				parent.getAttributes().remove(attr);
				return;
			}
		}
		attr.setValue(newValue);
		isCreation = attr.eContainer() == null || (parent != null && attr.eContainer() != parent);
		if (parent != null && isCreation) {
			parent.getAttributes().add(attr);
		}
	}

	public XamlAttribute getAttribute() {
		return attr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		if (isCreation && parent != null) {
			parent.getAttributes().remove(attr);
		}
		if (attr != null) {
			if (oldValue != null) {
				attr.setValue(oldValue);
			} else {
				attr.setValue(null);
				attr.setUseFlatValue(true);
				parent.getAttributes().remove(attr);
				parent.getAttributes().add(attr);
			}
		}
	}

	public static Command createCommand(XamlNode parent, String attrName, String namespace, String newValue) {
		return new ApplyAttributeSettingCommand(parent, attrName, namespace, newValue);
	}
}
