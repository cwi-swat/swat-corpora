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

import org.eclipse.e4.xwt.converters.StringToInteger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class RadioGroupFieldEditor extends FieldEditor {

	private String[][] labelsAndValues;
	private Button[] radioButtons;
	private String value;
	private String groupName;

	public RadioGroupFieldEditor(String fieldName, String[][] labelsAndValues, Composite parent, String groupName) {
		init(fieldName, "");
		this.labelsAndValues = labelsAndValues;
		this.groupName = groupName;
		createControl(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.FieldEditor#createEditor(org.eclipse.swt.widgets.Composite, java.lang.String)
	 */
	protected Control createEditor(Composite parent) {
		Composite radioBox;
		if (groupName == null) {
			radioBox = new Composite(parent, SWT.NONE);
		} else {
			radioBox = new Group(parent, SWT.NONE);
			((Group) radioBox).setText(groupName);
		}
		adapt(radioBox);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		radioBox.setLayout(layout);
		if (labelsAndValues != null) {
			Listener listener = new Listener() {
				public void handleEvent(Event event) {
					String oldValue = value;
					value = (String) event.widget.getData();
					dispatchEvent(oldValue, value);
				}
			};
			radioButtons = new Button[labelsAndValues.length];
			for (int i = 0; i < labelsAndValues.length; i++) {
				Button radio = new Button(radioBox, SWT.RADIO | SWT.LEFT);
				radioButtons[i] = radio;
				String[] labelAndValue = labelsAndValues[i];
				radio.setText(labelAndValue[0]);
				radio.setData(labelAndValue[1]);
				radio.addListener(SWT.Selection, listener);
				adapt(radio);
			}
		}
		return radioBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.FieldEditor#refresh(java.lang.Object)
	 */
	protected void update(String selected) {
		this.value = selected;
		if (radioButtons == null) {
			return;
		}

		if (value != null) {
			boolean found = false;
			for (int i = 0; i < radioButtons.length; i++) {
				Button radio = radioButtons[i];
				boolean selection = false;
				String data = (String) radio.getData();
				Object intValue = StringToInteger.instance.convert(data);
				if (data.equals(value) || value.equals(intValue.toString())) {
					selection = true;
					found = true;
				}
				radio.setSelection(selection);
			}
			if (found) {
				return;
			}
		}

		// We weren't able to find the value. So we select the first
		// radio button as a default.
		if (radioButtons.length > 0) {
			radioButtons[0].setSelection(true);
			value = (String) radioButtons[0].getData();
		}
	}

}
