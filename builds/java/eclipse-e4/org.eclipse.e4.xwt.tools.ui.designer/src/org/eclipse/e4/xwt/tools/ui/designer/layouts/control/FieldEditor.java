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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class FieldEditor {

	protected String fieldName;
	protected String labelText;
	private Control editor;
	private List<FieldEditorListener> listeners;

	private boolean updating;
	private boolean dispatching;

	protected FieldEditor() {
	}

	protected FieldEditor(String fieldName, String labelText, Composite parent) {
		init(fieldName, labelText);
		createControl(parent);
	}

	protected void createControl(Composite parent) {
		editor = createEditor(parent);
	}

	protected void init(String fieldName, String labelText) {
		Assert.isNotNull(fieldName);
		this.fieldName = fieldName;
		this.labelText = labelText;
	}

	public Control getEditor() {
		return editor;
	}

	public void apply(Object source) {
		if (!isValid() || source == null) {
			return;
		}
		try {
			Field field = source.getClass().getField(fieldName);
			Object value = field.get(source);
			doUpdate(value == null ? "" : value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean isValid() {
		return editor != null && !editor.isDisposed() && !dispatching;
	}

	protected void doUpdate(String newValue) {
		updating = true;
		update(newValue == null ? "" : newValue);
		updating = false;
	}

	protected void dispatchEvent(String oldValue, String newValue) {
		if (listeners == null || updating || (oldValue == null && newValue == null) || (oldValue != null && oldValue.equals(newValue))) {
			return;
		}
		dispatching = true;
		FieldEditorEvent event = new FieldEditorEvent(this, fieldName, oldValue, newValue);
		for (FieldEditorListener l : listeners) {
			if (event.doit) {
				break;
			}
			l.handleEvent(event);
		}
		dispatching = false;
	}

	public void addListener(FieldEditorListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<FieldEditorListener>();
		}
		listeners.add(listener);
	}

	public void removeListener(FieldEditorListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	protected void adapt(Control control) {
		control.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}

	protected abstract void update(String value);

	protected abstract Control createEditor(Composite parent);

}
