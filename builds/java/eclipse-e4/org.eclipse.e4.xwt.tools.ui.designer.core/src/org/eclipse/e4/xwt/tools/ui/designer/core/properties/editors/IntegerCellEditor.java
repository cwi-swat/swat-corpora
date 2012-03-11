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
package org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class IntegerCellEditor extends AbstractCellEditor {

	private Spinner spinner;

	/**
	 * @param parent
	 */
	public IntegerCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createControl(Composite cell) {
		if (spinner == null || spinner.isDisposed()) {
			spinner = new Spinner(cell, SWT.NONE);
			spinner.setMinimum(-1);
			spinner.setMaximum(Integer.MAX_VALUE);
		}
		return spinner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) {
		if (spinner == null || spinner.isDisposed()) {
			return;
		}
		int intValue = 0;
		if (value instanceof Integer) {
			intValue = ((Integer) value).intValue();
		} else if (value instanceof String) {
			try {
				intValue = Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
			}
		}
		spinner.setSelection(intValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		if (spinner == null || spinner.isDisposed()) {
			return 0;
		}
		return spinner.getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		if (spinner != null && !spinner.isDisposed()) {
			spinner.setFocus();
		}
	}
}
