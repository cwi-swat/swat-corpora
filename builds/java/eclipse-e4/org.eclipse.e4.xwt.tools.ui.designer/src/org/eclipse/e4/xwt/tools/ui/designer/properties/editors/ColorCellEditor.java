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

import org.eclipse.e4.xwt.tools.ui.designer.dialogs.ColorChooser;
import org.eclipse.e4.xwt.tools.ui.designer.providers.LabelProviderFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ColorCellEditor extends AbstractCellEditor {

	/**
	 * constructor
	 */
	public ColorCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		String color = null;
		Object value = getValue();
		if (value instanceof Color) {
			color = LabelProviderFactory.getLabelProvider(Color.class).getText(value);
		} else if (value instanceof String) {
			color = (String) value;
		}
		ColorChooser dialog = new ColorChooser(cellEditorWindow.getShell(), color);
		if (dialog.open() == Window.OK) {
			color = dialog.getColor();
		}
		return color;
	}

}
