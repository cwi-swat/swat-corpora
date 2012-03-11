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

import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class InsertCreateCommand extends AbstractCreateCommand {

	private EditPart reference;
	private int index = -1;

	public InsertCreateCommand(EditPart parent, EditPart reference, CreateRequest createRequest, int index) {
		super(parent, createRequest);
		this.reference = reference;
		this.index = index;
	}

	public InsertCreateCommand(EditPart parent, EditPart reference, CreateRequest createRequest) {
		this(parent, reference, createRequest, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.commands.AbstractCreationCommand#createCreateCommand(org.soyatec.tools.designer.xaml.XamlNode, org.soyatec.tools.designer.xaml.XamlNode)
	 */
	protected Command createCreateCommand(XamlNode parent, XamlNode child) {
		return new AddNewChildCommand(parent, child, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.commands.AbstractCreationCommand#preExecute(org.soyatec.tools.designer.xaml.XamlNode, org.eclipse.gef.requests.CreateRequest)
	 */
	protected void preExecute(XamlNode newNode, CreateRequest createRequest) {
		if (reference != null) {
			index = getParentModel().getChildNodes().indexOf(reference.getModel());
			((AddNewChildCommand) addChildCommand).setIndex(index);
		}
	}

	public int getIndex() {
		return index;
	}
}
