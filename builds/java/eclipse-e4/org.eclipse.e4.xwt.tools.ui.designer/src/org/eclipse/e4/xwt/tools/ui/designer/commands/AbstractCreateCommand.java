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

import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.CreateReqHelper;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.InitializeHelper;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author jliu jin.liu@soyatec.com
 */
public abstract class AbstractCreateCommand extends Command {

	protected EditPart parent;
	protected CreateRequest createRequest;
	protected CreateReqHelper helper;
	protected Command addChildCommand;
	private XamlNode child;

	public AbstractCreateCommand(EditPart parent, CreateRequest createRequest) {
		this.parent = parent;
		this.createRequest = createRequest;
		helper = new CreateReqHelper(createRequest);
		setLabel("Creation Command");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		preExecute(child, createRequest);

		addChildCommand.execute();
	}

	protected Command createCreateCommand(XamlNode parent, XamlNode child) {
		return new AddNewChildCommand(parent, child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (child == null) {
			child = helper.getNewObject();
		}

		if (!InitializeHelper.checkValue(child)) {
			return false;
		}
		if (addChildCommand == null) {
			addChildCommand = createCreateCommand(getParentModel(), child);
		}

		if (addChildCommand == null || !addChildCommand.canExecute()) {
			return false;
		}
		return parent != null && getParentModel() != null && helper.canCreate(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return addChildCommand != null && addChildCommand.canUndo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		addChildCommand.undo();
	}

	/**
	 * @return the parent
	 */
	public EditPart getParent() {
		return parent;
	}

	public XamlElement getParentModel() {
		Object model = parent.getModel();
		if (!(model instanceof XamlElement)) {
			return null;
		}
		return (XamlElement) model;
	}

	protected abstract void preExecute(XamlNode newNode, CreateRequest createRequest);

}
