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
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.ExternalizeStringsWizard;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueEntry;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueModel;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 */
public class OpenExternalizeStringsAction extends WorkbenchPartAction {

	int index = 0;
	XWTDesigner designer = (XWTDesigner) getWorkbenchPart();
	public static final String ID = OpenExternalizeStringsAction.class.getName();

	public OpenExternalizeStringsAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText("Externalize Strings");
	}

	@Override
	public void run() {
		TextValueModel textValueEntrys = getTextValue();
		if (textValueEntrys.elements().length == 0) {
			String dialogMessage = "No Strings to externalize found in file.";
			String[] dialogButtonLabels = { "Ok" };
			MessageDialog messageDialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Externalize Strings", null, dialogMessage, MessageDialog.INFORMATION, dialogButtonLabels, 0);
			messageDialog.open();
		} else {
			ExternalizeStringsWizard wizard = new ExternalizeStringsWizard(textValueEntrys, designer);
			wizard.init(PlatformUI.getWorkbench(), null);
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
			dialog.open();
		}
	}

	/**
	 * Get the input to the table viewer.
	 * 
	 * @return textValueEntrys
	 */
	protected TextValueModel getTextValue() {
		TextValueModel textValueEntrys = new TextValueModel();
		index = 0;
		RootEditPart rootEditPart = designer.getGraphicalViewer().getRootEditPart();
		EditPart contents = rootEditPart.getContents();
		getTextContents(contents, textValueEntrys, "text");
		getTextContents(contents, textValueEntrys, "tooltipText");
		return textValueEntrys;
	}

	/**
	 * Get the text attribute values.
	 * 
	 * @param editPart
	 * @param textValueEntrys
	 */
	private void getTextContents(EditPart editPart, TextValueModel textValueEntrys, String type) {
		if (editPart instanceof WidgetEditPart) {
			XamlNode node = ((WidgetEditPart) editPart).getCastModel();
			XamlAttribute attribute = node.getAttribute(type, IConstants.XWT_NAMESPACE);
			if (attribute != null && attribute.getChildNodes().size() == 0) {
				textValueEntrys.add(new TextValueEntry(attribute.getValue(), "" + index));
				index++;
			}
			if (editPart.getChildren().size() > 0) {
				getChildTextContents(editPart, textValueEntrys, type);
			}
		} else if (editPart.getChildren().size() > 0) {
			getChildTextContents(editPart, textValueEntrys, type);
		}
	}

	/**
	 * Get the editPart children's text value.
	 * 
	 * @param editPart
	 * @param textValueEntrys
	 */
	private void getChildTextContents(EditPart editPart, TextValueModel textValueEntrys, String type) {
		if (editPart.getChildren().size() > 0) {
			List child = editPart.getChildren();
			for (int i = 0; i < child.size(); i++) {
				if (child.get(i) instanceof EditPart) {
					getTextContents((EditPart) (child.get(i)), textValueEntrys, type);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return true;
	}

}
