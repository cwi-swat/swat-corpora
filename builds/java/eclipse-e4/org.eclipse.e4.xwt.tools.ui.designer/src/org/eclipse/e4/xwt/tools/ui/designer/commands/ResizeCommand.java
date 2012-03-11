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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ResizeCommand extends Command {

	private EditPart editPart;
	private Dimension growth;
	private CompoundCommand command;

	public ResizeCommand(EditPart editPart, Dimension growth) {
		setLabel("Resize");
		this.editPart = editPart;
		this.growth = growth;
	}

	public boolean canExecute() {
		if (editPart == null || growth == null || (growth.width == 0 && growth.height == 0)) {
			return false;
		}
		Object model = editPart.getModel();
		return model != null && model instanceof XamlNode;
	}

	public void execute() {
		int growWidth = 0;
		int growHeight = 0;
		if (growth.width != 0) {
			growWidth = growth.width;
		}
		if (growth.height != 0) {
			growHeight = growth.height;
		}

		Dimension size = getSize();
		command = new CompoundCommand();
		XamlNode model = (XamlNode) editPart.getModel();
		if (growWidth != 0) {
			command.add(createCommand(model, "width", growWidth + size.width));
		}
		if (growHeight != 0) {
			command.add(createCommand(model, "height", growHeight + size.height));
		}
		if (command.canExecute()) {
			command.execute();
		}
	}

	private Command createCommand(XamlNode control, String attrName, int value) {
		String newValue = Integer.toString(value);
		CompoundCommand command = new CompoundCommand();
		XamlAttribute layoutDataAttr = control.getAttribute("layoutData");
		if (layoutDataAttr != null && !layoutDataAttr.getChildNodes().isEmpty()) {
			XamlElement layoutData = layoutDataAttr.getChild(0);
			XamlAttribute attribute = layoutData.getAttribute(attrName);
			if (attribute == null) {
				attribute = layoutData.getAttribute(attrName + "Hint");// GridData
			}
			if (attribute != null) {
				command.add(new ApplyAttributeSettingCommand(null, attribute, newValue));
			}
		}
		if (command.isEmpty()) {
			command.add(new ApplyAttributeSettingCommand(control, attrName, IConstants.XWT_NAMESPACE, newValue));
		} else {
			XamlAttribute attribute = control.getAttribute(attrName);
			if (attribute != null) {
				command.add(new ApplyAttributeSettingCommand(null, attribute, newValue));
			}
		}
		return command.unwrap();
	}

	private Dimension getSize() {
		IFigure contentPane = ((GraphicalEditPart) editPart).getContentPane();
		return contentPane.getSize();
	}

	public boolean canUndo() {
		return command != null && command.canUndo();
	}

	public void undo() {
		command.undo();
	}
}
