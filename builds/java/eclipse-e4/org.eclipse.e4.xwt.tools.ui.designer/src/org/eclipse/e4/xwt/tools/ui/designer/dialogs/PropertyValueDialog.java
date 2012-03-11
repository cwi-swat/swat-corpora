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

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.preference.Preferences;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PropertyValueDialog extends TitleAreaDialog {

	private IProperty property;
	private String result;
	private String initialize;

	public PropertyValueDialog(Shell parentShell, IProperty property, String initializeValue) {
		super(parentShell);
		this.property = property;
		this.initialize = initializeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Value Dialog");
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
		if (property != null) {
			label.setText(property.getName() + ": ");
		} else {
			label.setText("New Value: ");
		}

		final Text text = new Text(control, SWT.BORDER);
		if (initialize != null) {
			text.setText(initialize);
		}
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verify(text);
			}
		});
		verify(text);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		setTitle("Input a New Value.");
		if (property != null) {
			setMessage("Give a new value for \"" + property.getName() + "\" property");
		}

		new Label(control, SWT.NONE);
		new Label(control, SWT.NONE);
		new Label(control, SWT.NONE);
		final Button checkbutton = new Button(control, SWT.CHECK);
		checkbutton.setText("Do not ask again? You can change this from preference page.");
		checkbutton.setSelection(false);
		GridData griddata = new GridData(GridData.FILL_HORIZONTAL);
		// griddata.horizontalSpan = 2;
		checkbutton.setLayoutData(griddata);
		checkbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Preferences.getPreferenceStore().setValue(Preferences.PROMPT_DURING_CREATION, !checkbutton.getSelection());
			}
		});
		return composite;
	}

	private void verify(Text text) {
		result = text.getText();
		boolean validate = true;
		if (result == null || result.equals("")) {
			setErrorMessage("Result is empty.");
			validate = false;
		} else if (property != null) {
			Class<?> type = property.getType();
			// add the filter when the type of property is interger.
			if (type.isAssignableFrom(int.class)) {
				if (!isInt(result)) {
					setErrorMessage("Result is not a validate value,please input a interger.");
					validate = false;
				}
			}
			if (!type.isAssignableFrom(result.getClass())) {
				IConverter c = XWT.findConvertor(String.class, type);
				if (c == null) {
					setErrorMessage("This resilt is not applicable for the arguments (" + type.getSimpleName() + ")");
					validate = false;
				} else {
					Object newValue = c.convert(result);
					if (newValue == null) {
						setErrorMessage("This resilt is not applicable for the arguments (" + type.getSimpleName() + ")");
						validate = false;
					}
				}
			}
		}
		if (validate) {
			setErrorMessage(null);
			setFinished(true);
		} else {
			setFinished(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		setFinished(initialize != null);
	}

	public String getResult() {
		return result;
	}

	public void setFinished(boolean flag) {
		Button okButton = getButton(Window.OK);
		if (okButton != null) {
			okButton.setEnabled(flag);
		}
	}

	public Boolean isInt(String s) {
		return s.matches("^[0-9]+$");
	}

}
