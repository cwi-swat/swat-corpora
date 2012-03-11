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
package org.eclipse.e4.xwt.tools.ui.designer.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 * 
 */
public class ExternalizeTextDialog extends Dialog {

	private String oldValue;
	private String textValue;
	private Text text;
	private boolean canExternalize;
	private boolean isExternalized;
	private Button externalizeButton;
	private Button updateButton;
	private String propertyName;

	/**
	 * @param parentShell
	 */
	public ExternalizeTextDialog(Shell parentShell, String oldValue, boolean canExternalize, String propertyName) {
		super(parentShell);
		this.oldValue = oldValue;
		this.canExternalize = canExternalize;
		this.isExternalized = !canExternalize;
		this.propertyName = propertyName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Set " + (propertyName.substring(0, 1)).toUpperCase() + propertyName.substring(1) + " Dialog");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) dialogArea.getLayout();
		layout.numColumns = 3;

		Label label = new Label(dialogArea, SWT.NONE);
		label.setText(propertyName);

		text = new Text(dialogArea, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 200;
		text.setText(oldValue == null ? "" : oldValue);
		text.setLayoutData(gridData);
		textValue = text.getText();
		text.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				textValue = text.getText();
				if (!textValue.equals(oldValue)) {
					updateButton.setEnabled(true);
					canExternalize = false;
					externalizeButton.setEnabled(canExternalize);
				} else if (!isExternalized) {
					updateButton.setEnabled(false);
					canExternalize = true;
					externalizeButton.setEnabled(canExternalize);
				} else {
					updateButton.setEnabled(false);
				}
			}
		});

		return dialogArea;
	}

	/**
	 * Adds buttons to this dialog's button bar.
	 * 
	 * @param parent
	 *            the button bar composite
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.YES_ID, "Externalize Value", false);
		externalizeButton = getButton(IDialogConstants.YES_ID);
		externalizeButton.setEnabled(!isExternalized);
		createButton(parent, IDialogConstants.OK_ID, "Update value", false);
		updateButton = getButton(IDialogConstants.OK_ID);
		updateButton.setEnabled(false);

		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
	}

	/**
	 * Notifies that this dialog's button with the given id has been pressed.
	 * 
	 * @param buttonId
	 *            the id of the button that was pressed (see <code>IDialogConstants.*_ID</code> constants)
	 */
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			okPressed();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		} else if (IDialogConstants.YES_ID == buttonId) {
			externalizePressed();
		}
	}

	/**
	 * Notifies that the cancel button of this dialog has been pressed.
	 */
	protected void externalizePressed() {
		setReturnCode(IDialogConstants.YES_ID);
		close();
	}

	public String getTextValue() {
		return textValue;
	}

}
