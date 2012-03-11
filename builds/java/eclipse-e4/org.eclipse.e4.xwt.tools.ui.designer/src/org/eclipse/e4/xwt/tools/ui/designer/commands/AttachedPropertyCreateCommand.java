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
import org.eclipse.e4.xwt.tools.ui.designer.parts.CoolItemEditPart;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class AttachedPropertyCreateCommand extends AbstractCreateCommand {

	private String attachedProperty;
	private XamlAttribute attachedAttr;

	public AttachedPropertyCreateCommand(EditPart parent, CreateRequest createRequest, String attachedProperty) {
		super(parent, createRequest);
		this.attachedProperty = attachedProperty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.commands.AbstractCreateCommand#preExecute (org.soyatec.tools.designer.xaml.XamlNode, org.eclipse.gef.requests.CreateRequest)
	 */
	protected void preExecute(XamlNode newNode, CreateRequest createRequest) {
		attachedAttr = getParentModel().getAttribute(attachedProperty);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.soyatec.xwt.tools.ui.designer.commands.AbstractCreateCommand# createCreateCommand(org.soyatec.tools.designer.xaml.XamlNode, org.soyatec.tools.designer.xaml.XamlNode)
	 */
	protected Command createCreateCommand(XamlNode parent, XamlNode child) {
		XamlNode newParent = parent;
		XamlNode newChild = child;
		if (attachedAttr == null) {
			newChild = attachedAttr = XamlFactory.eINSTANCE.createAttribute(attachedProperty, IConstants.XWT_NAMESPACE);
			if (child instanceof XamlElement) {
				attachedAttr.getChildNodes().add((XamlElement) child);
			}
		} else {
			newParent = attachedAttr;
			attachedAttr.getChildNodes().clear();
		}
		return new AddNewChildCommand(newParent, newChild);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.commands.AbstractCreateCommand#canExecute ()
	 */
	public boolean canExecute() {
		if (parent instanceof CoolItemEditPart) {
			return parent != null && getParentModel() != null && helper.canCreate(parent) && attachedProperty != null;
		}
		return parent != null && getParentModel() != null && helper.canCreate(parent.getParent()) && attachedProperty != null;
	}
}
