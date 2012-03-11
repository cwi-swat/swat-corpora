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
package org.eclipse.e4.xwt.tools.ui.designer.properties.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BooleanCellEditor extends AbstractCellEditor {
	private Button checkBox;

	public BooleanCellEditor(Composite parent) {
		super(parent);
	}

	protected Control createControl(Composite parent) {
		if (checkBox == null || checkBox.isDisposed()) {
			checkBox = new Button(parent, SWT.CHECK);
		}
		return checkBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CheckboxCellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object newValue) {
		boolean booleanValue = false;
		if (newValue instanceof Boolean) {
			booleanValue = ((Boolean) newValue).booleanValue();
		} else if (newValue instanceof String) {
			booleanValue = "true".equals(newValue);
		}
		checkBox.setSelection(booleanValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CheckboxCellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		return checkBox.getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		checkBox.setFocus();
	}

}
