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
package org.eclipse.e4.xwt.tools.ui.designer.layouts.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class CheckBoxFieldEditor extends FieldEditor {

	private Button checkBox;
	private boolean wasSelected = false;

	public CheckBoxFieldEditor(String fieldName, String labelText, Composite parent) {
		super(fieldName, labelText, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.FieldEditor#refresh(java.lang.Object)
	 */
	protected void update(String value) {
		if (checkBox == null || checkBox.isDisposed()) {
			return;
		}
		if (value != null) {
			wasSelected = Boolean.parseBoolean(value.toString());
		}
		checkBox.setSelection(wasSelected);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.FieldEditor#createEditor(org.eclipse.swt.widgets.Composite, java.lang.String)
	 */
	protected Control createEditor(Composite parent) {
		checkBox = new Button(parent, SWT.CHECK);
		if (labelText != null) {
			checkBox.setText(labelText);
		}
		checkBox.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean isSelected = checkBox.getSelection();
				dispatchEvent(Boolean.toString(wasSelected), Boolean.toString(isSelected));
				wasSelected = isSelected;
			}
		});
		adapt(checkBox);
		return checkBox;
	}

}
