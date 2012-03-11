/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder.jface;

import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public abstract class ControlEditingSupport extends EditingSupport {

	private CellEditor editor;
	private Control control;

	public ControlEditingSupport(Control control, ColumnViewer viewer, CellEditor editor) {
		super(viewer);
		this.control = control;
		org.eclipse.swt.widgets.Control swtControl = viewer.getControl();
		if (editor == null) {
			new TextCellEditor(swtControl instanceof Composite ? (Composite)swtControl : swtControl.getParent());
		}
		this.editor = editor;
	}

	protected void prepareControl(Object inputElement) {
		control.setDataObject(inputElement);
	}

	protected boolean canEdit(Object element) {
		prepareControl(element);
		return control.isEnabled();
	}

	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	protected void setValue(Object element, Object value) {
		doSetValue(element, value);
		getViewer().update(element, null);
	}

	protected abstract void doSetValue(Object element, Object value);
}
