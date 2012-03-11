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
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FormDataCreateCommand extends Command {

	private EditPart parent;
	private FormData formData;
	private Command command;
	private XamlNode forCreate;
	static int i = 0;
	private CompoundCommand commandList;

	public FormDataCreateCommand(EditPart parent, XamlNode forCreate, FormData formData) {
		this.parent = parent;
		this.forCreate = forCreate;
		this.formData = formData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return parent != null && formData != null && forCreate != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		System.out.println(formData.toString());
		commandList = new CompoundCommand();
		XamlNode parentNode = (XamlNode) parent.getModel();
		XamlAttribute layoutDataAttr = forCreate.getAttribute("layoutData", IConstants.XWT_NAMESPACE);
		if (layoutDataAttr == null) {
			layoutDataAttr = XamlFactory.eINSTANCE.createAttribute("layoutData", IConstants.XWT_NAMESPACE);
		}
		XamlElement formDataChild = layoutDataAttr.getChild("FormData", IConstants.XWT_NAMESPACE);
		if (formDataChild == null) {
			formDataChild = XamlFactory.eINSTANCE.createElement("FormData", IConstants.XWT_NAMESPACE);
		}
		// 1. width and height
		if (formData.width != SWT.DEFAULT) {
			ApplyAttributeSettingCommand command = new ApplyAttributeSettingCommand(formDataChild, "width", IConstants.XWT_NAMESPACE, Integer.toString(formData.width));
			addCommand(command);
		}
		if (formData.height != SWT.DEFAULT) {
			ApplyAttributeSettingCommand command = new ApplyAttributeSettingCommand(formDataChild, "height", IConstants.XWT_NAMESPACE, Integer.toString(formData.height));
			addCommand(command);
		}
		// 2. left, top, right and bottom.
		computeFormDataCommand(formDataChild, formData.left, "left");
		computeFormDataCommand(formDataChild, formData.top, "top");
		computeFormDataCommand(formDataChild, formData.right, "right");
		computeFormDataCommand(formDataChild, formData.bottom, "bottom");
		// compute commands.
		addNewChild(layoutDataAttr, formDataChild);
		addNewChild(forCreate, layoutDataAttr);
		addNewChild(parentNode, forCreate);
		command = commandList.unwrap();
		if (command.canExecute()) {
			command.execute();
		}
	}

	private void computeFormDataCommand(XamlNode formDataChild, FormAttachment formAttachment, String name) {
		XamlAttribute attachmentAttr = formDataChild.getAttribute(name, IConstants.XWT_NAMESPACE);
		if (formAttachment != null) {
			if (attachmentAttr == null) {
				attachmentAttr = XamlFactory.eINSTANCE.createAttribute(name, IConstants.XWT_NAMESPACE);
			}
			computeAttatchCommand(attachmentAttr, formAttachment);
			addNewChild(formDataChild, attachmentAttr);
		} else if (attachmentAttr != null) {
			addCommand(new DeleteCommand(attachmentAttr));
		}
	}

	private void computeAttatchCommand(XamlAttribute parent, FormAttachment attachment) {
		XamlElement attachmentChild = parent.getChild("FormAttachment", IConstants.XWT_NAMESPACE);
		if (attachmentChild == null) {
			attachmentChild = XamlFactory.eINSTANCE.createElement("FormAttachment", IConstants.XWT_NAMESPACE);
		}
		Control control = attachment.control;
		if (control != null) {
			String controlName = getName(control);
			if (controlName == null) {
				controlName = NamedCommand.generateName(control);
				addCommand(new NamedCommand(control, controlName));
			}
			XamlAttribute controlAttr = attachmentChild.getAttribute("control", IConstants.XWT_NAMESPACE);
			if (controlAttr == null) {
				controlAttr = XamlFactory.eINSTANCE.createAttribute("control", IConstants.XWT_NAMESPACE);
			}
			XamlElement bindChild = controlAttr.getChild("Binding", IConstants.XWT_NAMESPACE);
			if (bindChild == null) {
				bindChild = XamlFactory.eINSTANCE.createElement("Binding", IConstants.XWT_NAMESPACE);
			}
			ApplyAttributeSettingCommand command = new ApplyAttributeSettingCommand(bindChild, "ElementName", IConstants.XWT_NAMESPACE, controlName);
			addCommand(command);

			addNewChild(controlAttr, bindChild);
			addNewChild(attachmentChild, controlAttr);

			/* if (attachment.alignment != SWT.DEFAULT) */{
				String alignment = "SWT.DEFAULT";
				switch (attachment.alignment) {
				case SWT.TOP:
					alignment = "SWT.TOP";
					break;
				case SWT.BOTTOM:
					alignment = "SWT.BOTTOM";
					break;
				case SWT.RIGHT:
					alignment = "SWT.RIGHT";
					break;
				case SWT.CENTER:
					alignment = "SWT.CENTER";
					break;
				case SWT.LEFT:
					alignment = "SWT.LEFT";
					break;
				}
				command = new ApplyAttributeSettingCommand(attachmentChild, "alignment", IConstants.XWT_NAMESPACE, alignment);
				addCommand(command);
			}
		} else {
			addCommand(new DeleteCommand(attachmentChild.getAttribute("control")));
		}
		/* if (attachment.denominator != 100) */{
			ApplyAttributeSettingCommand command = new ApplyAttributeSettingCommand(attachmentChild, "denominator", IConstants.XWT_NAMESPACE, Integer.toString(attachment.denominator));
			addCommand(command);
		}
		/* if (attachment.numerator != 0) */{
			ApplyAttributeSettingCommand command = new ApplyAttributeSettingCommand(attachmentChild, "numerator", IConstants.XWT_NAMESPACE, Integer.toString(attachment.numerator));
			addCommand(command);
		}
		/* if (attachment.offset != 0) */{
			ApplyAttributeSettingCommand command = new ApplyAttributeSettingCommand(attachmentChild, "offset", IConstants.XWT_NAMESPACE, Integer.toString(attachment.offset));
			addCommand(command);
		}
		addNewChild(parent, attachmentChild);
	}

	private void addCommand(Command command) {
		if (command == null || !command.canExecute()) {
			return;
		}
		commandList.add(command);
	}

	private void addNewChild(XamlNode parent, XamlNode child) {
		if (child instanceof XamlAttribute && !parent.getAttributes().contains(child)) {
			commandList.add(new AddNewChildCommand(parent, child));
		} else if (child instanceof XamlElement && !parent.getChildNodes().contains(child)) {
			commandList.add(new AddNewChildCommand(parent, child));
		}
	}

	private String getName(Control control) {
		XamlNode model = XWTProxy.getModel(control);
		if (model == null) {
			return null;
		}
		XamlAttribute nameAttr = model.getAttribute("name", IConstants.XWT_NAMESPACE);
		if (nameAttr == null) {
			nameAttr = model.getAttribute("name", IConstants.XWT_X_NAMESPACE);
		}
		if (nameAttr != null) {
			return nameAttr.getValue();
		}
		return null;
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
