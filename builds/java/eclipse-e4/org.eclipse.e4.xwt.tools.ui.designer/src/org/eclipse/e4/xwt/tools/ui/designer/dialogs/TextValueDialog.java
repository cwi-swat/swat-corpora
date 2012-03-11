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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class TextValueDialog extends TitleAreaDialog {

	private String oldValue;
	private String newValue;

	/**
	 * @param parentShell
	 */
	public TextValueDialog(Shell parentShell, String oldValue) {
		super(parentShell);
		this.oldValue = oldValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite control = new Composite(composite, SWT.NONE);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		control.setLayout(new GridLayout(2, false));
		Label label = new Label(control, SWT.NONE);
		label.setText("Text:");

		final Text text = new Text(control, SWT.BORDER);
		if (oldValue != null) {
			text.setText(oldValue);
		}
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				newValue = text.getText();
				if (newValue == null || newValue.equals("")) {
					setErrorMessage("Text input is empty.");
					setButtonVisuble(false);
				} else if (newValue.equals(oldValue)) {
					setErrorMessage("Text input is equals the old one.");
				} else {
					setErrorMessage(null);
					getButton(Window.OK).setEnabled(true);
				}

			}
		});
		newValue = text.getText();
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return composite;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setButtonVisuble(boolean flag) {
		getButton(Window.OK).setEnabled(flag);
	}
}
