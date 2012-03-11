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
package org.eclipse.e4.xwt.tools.ui.designer.core.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class IntArrayDialog extends Dialog {

	private int[] initValue;
	private String[] titles;
	private int[] result;

	public IntArrayDialog(Shell parent, int[] initValue, String[] titles) {
		super(parent);
		this.initValue = initValue;
		this.titles = titles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(3, false));
		result = new int[titles.length];
		if (initValue != null) {
			System.arraycopy(initValue, 0, result, 0, Math.min(initValue.length, titles.length));
		}
		for (int i = 0; i < titles.length; i++) {
			createItem(composite, titles[i], i);
		}
		return composite;
	}

	protected Spinner createItem(Composite parent, String title, final int index) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(title);

		final Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setMaximum(9999);// set max before set selection.
		spinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spinner.setSelection(result[index]);
		spinner.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				result[index] = spinner.getSelection();
			}
		});

		label = new Label(parent, SWT.NONE);
		label.setText("pixels");
		return spinner;
	}

	public int[] getResult() {
		return result;
	}
}
