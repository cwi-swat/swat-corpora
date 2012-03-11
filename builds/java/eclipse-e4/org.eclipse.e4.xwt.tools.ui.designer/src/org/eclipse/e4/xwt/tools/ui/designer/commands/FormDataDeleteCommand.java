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

import org.eclipse.e4.xwt.tools.ui.designer.core.util.Draw2dTools;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTTools;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ControlEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form.FormLayoutData;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form.FormLayoutHelper;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FormDataDeleteCommand extends Command {

	private EditPart parent;
	private EditPart child;
	private Command command;

	public FormDataDeleteCommand(EditPart parent, EditPart child) {
		this.parent = parent;
		this.child = child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (parent == null || child == null) {
			return false;
		}
		if (!(child instanceof ControlEditPart) || !(parent instanceof CompositeEditPart)) {
			return false;
		}
		return child.getModel() != null && parent.getModel() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		CompositeEditPart parentEp = (CompositeEditPart) parent;
		ControlEditPart childEp = (ControlEditPart) child;
		XamlNode childNode = childEp.getCastModel();
		if (child.getParent() == null || child.getParent() != parent) {
			childEp = (ControlEditPart) parentEp.getViewer().getEditPartRegistry().get(childNode);
		}
		CompoundCommand commandList = new CompoundCommand();
		commandList.add(new DeleteCommand(childEp.getCastModel()));
		Control control = (Control) childEp.getWidget();
		Control[] dependencies = FormLayoutHelper.getDependencies(control);
		FormLayoutHelper helper = new FormLayoutHelper(parentEp);
		control.dispose();
		if (dependencies.length != 0) {
			for (Control child : dependencies) {
				XamlNode model = XWTProxy.getModel(child);
				if (model == null) {
					continue;
				}
				FormLayoutData layoutData = helper.computeData(Draw2dTools.toDraw2d(SWTTools.getBounds(child)), child);
				if (layoutData != null) {
					commandList.add(new FormDataCreateCommand(parentEp, model, layoutData.data));
				}
			}
		}
		command = commandList.unwrap();
		if (command.canExecute()) {
			command.execute();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return command != null && command.canUndo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		command.undo();
	}
}
