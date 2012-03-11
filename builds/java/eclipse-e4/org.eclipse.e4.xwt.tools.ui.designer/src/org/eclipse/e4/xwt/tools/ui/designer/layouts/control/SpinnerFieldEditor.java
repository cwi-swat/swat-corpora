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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class SpinnerFieldEditor extends FieldEditor {

	private Spinner spinner;
	private int wasSelected = SWT.DEFAULT;

	public SpinnerFieldEditor(String fieldName, String labelText, Composite parent) {
		super(fieldName, labelText, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createEditor(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		if (labelText != null) {
			label.setText(labelText);
		}
		adapt(label);

		spinner = new Spinner(parent, SWT.BORDER);
		spinner.setMinimum(-1);
		spinner.setMaximum(9999);
		spinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spinner.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int selection = spinner.getSelection();
				dispatchEvent(Integer.toString(wasSelected), Integer.toString(selection));
				wasSelected = selection;
			}
		});
		adapt(spinner);
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditor#update(java.lang.String)
	 */
	protected void update(String value) {
		try {
			wasSelected = Integer.parseInt(value);
			spinner.setSelection(wasSelected);
		} catch (NumberFormatException e) {
		}
	}

}
