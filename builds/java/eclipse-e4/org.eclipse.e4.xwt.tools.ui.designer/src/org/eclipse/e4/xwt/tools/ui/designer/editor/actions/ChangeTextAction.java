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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import java.util.List;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.dialogs.TextValueDialog;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ChangeTextAction extends SelectionAction {

	public static final String ID = "org.eclipse.e4.xwt.tools.ui.designer.editor.actions.ChageTextAction";

	public ChangeTextAction(IWorkbenchPart part) {
		super(part);
		setText("Change Text");
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		EditPart editPart = (EditPart) getSelectedObjects().get(0);
		Object model = editPart.getModel();
		if (model instanceof XamlElement) {
			// Check text property of a control is or not exist.
			XamlElement element = (XamlElement) model;
			if (element == null) {
				return;
			}
			String name = element.getName();
			if (name == null || name.equals("")) {
				return;
			}
			String namespace = element.getNamespace();
			IMetaclass metaclass = XWT.getMetaclass(Character.toUpperCase(name.charAt(0)) + name.substring(1), namespace);
			IProperty property = metaclass.findProperty("text");
			if (property == null) {
				return;
			}

			XamlAttribute attribute = element.getAttribute("text");
			boolean isNew = false;
			if (attribute == null) {
				attribute = XamlFactory.eINSTANCE.createAttribute("text", IConstants.XWT_NAMESPACE);
				isNew = true;
			}

			String oldValue = attribute.getValue() != null ? attribute.getValue().toString() : null;
			TextValueDialog dialog = new TextValueDialog(new Shell(), oldValue);
			dialog.create();
			dialog.getShell().setText("Set New Text");
			dialog.setTitle("New Text");
			dialog.setMessage("Input a new text value.");
			if (dialog.open() == Window.OK) {
				String newValue = dialog.getNewValue();
				Command cmd = createCommand((XamlElement) model, attribute, isNew, oldValue, newValue);
				EditDomain.getEditDomain(editPart).getCommandStack().execute(cmd);
			}

		}
	}

	private Command createCommand(final XamlElement element, final XamlAttribute attribute, final boolean isNew, final String oldValue, final String newValue) {
		Command command = new Command() {
			public void execute() {
				attribute.setValue(newValue);
				if (isNew) {
					element.getAttributes().add(attribute);
				}
			}

			public void undo() {
				attribute.setValue(oldValue);
				if (isNew) {
					element.getAttributes().add(attribute);
				}
			}
		};
		command.setLabel("Set Text");
		return command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		List<?> selectedObjects = getSelectedObjects();
		if (selectedObjects.size() != 1) {
			return false;
		}
		Object object = selectedObjects.get(0);
		if (!(object instanceof EditPart) || !(((EditPart) object).getModel() instanceof XamlElement)) {
			return false;
		}
		XamlElement element = (XamlElement) ((EditPart) object).getModel();
		IMetaclass metaclass = XWTUtility.getMetaclass(element);
		if (metaclass == null || metaclass.findProperty("text") == null) {
			return false;
		}
		return true;
	}
}
