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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class LayoutCellEditor extends ComboBoxCellEditor {
	private static List<String> items = new ArrayList<String>();
	static {
		LayoutType[] values = LayoutType.values();
		for (int i = 0; i < values.length; i++) {
			String value = values[i].value();
			if (value != null) {
				items.add(value);
			}
		}
	}

	public LayoutCellEditor(Composite parent) {
		super(parent, items.toArray(new String[0]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.properties.editors.ComboCellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) {
		if (!(value instanceof Integer)) {
			String realValue = LayoutsHelper.getLayoutType(value).value();
			value = items.indexOf(realValue);
		}
		super.doSetValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ComboBoxCellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		Object value = super.doGetValue();
		try {
			if (value instanceof Integer) {
				value = items.get((Integer) value);
			}
		} catch (Exception e) {
			CCombo control = (CCombo) getControl();
			if (control != null && !control.isDisposed()) {
				value = control.getText();
			}
		}
		return value;
	}

}
