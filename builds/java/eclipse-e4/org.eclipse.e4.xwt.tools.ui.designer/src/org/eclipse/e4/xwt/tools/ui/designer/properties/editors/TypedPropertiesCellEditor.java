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

import org.eclipse.e4.xwt.tools.ui.designer.properties.TypedProperties;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

public class TypedPropertiesCellEditor extends ComboBoxCellEditor {

	private String type;

	public TypedPropertiesCellEditor(Composite parent, String type) {
		super(parent, TypedProperties.getProperties(type));
		this.type = type;
	}

	/**
	 * @see org.eclipse.jface.viewers.ComboBoxCellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		Object value = super.doGetValue();
		String[] items = getItems();
		try {
			if (value instanceof Integer) {
				value = items[(Integer) value];
			}
		} catch (Exception e) {
			CCombo control = (CCombo) getControl();
			if (control != null && !control.isDisposed()) {
				value = control.getText();
			}
		}
		return value;
	}

	/**
	 * @see org.eclipse.jface.viewers.ComboBoxCellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) {
		int intValue = -1;
		String textValue = null;
		String[] items = getItems();
		if (value instanceof Integer) {
			textValue = TypedProperties.getLabelProvider(type).getText((Integer) value);
		} else {
			textValue = value.toString();
		}
		if (textValue != null) {
			for (int i = 0; i < items.length; i++) {
				if (textValue.equals(items[i])) {
					intValue = i;
					break;
				}
			}
			if (intValue == -1) {
				((CCombo) getControl()).setText(textValue);
			} else {
				super.doSetValue(intValue);
			}
		}
	}
}
