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

import org.eclipse.e4.xwt.tools.ui.designer.core.properties.LabelProviderFactory;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
		ColorDialog dialog = new ColorDialog(new Shell());
		RGB rgb = dialog.open();
		if (rgb != null) {
			return new Color(Display.getDefault(), rgb);
		}
		return color;
	}

}
