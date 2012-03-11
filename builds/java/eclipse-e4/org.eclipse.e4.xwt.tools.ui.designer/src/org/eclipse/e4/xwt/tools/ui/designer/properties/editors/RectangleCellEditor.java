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

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.tools.ui.designer.core.dialogs.IntArrayDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class RectangleCellEditor extends AbstractCellEditor {

	public RectangleCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		Rectangle rect = new Rectangle(0, 0, 0, 0);
		Object value = getValue();
		if (value instanceof Rectangle) {
			Rectangle r = (Rectangle) value;
			rect.x = r.x;
			rect.y = r.y;
			rect.width = r.width;
			rect.height = r.height;
		} else {
			IConverter c = XWT.findConvertor(value.getClass(), Rectangle.class);
			if (c != null) {
				rect = (Rectangle) c.convert(value);
			}
		}
		IntArrayDialog dialog = new IntArrayDialog(cellEditorWindow.getShell(), new int[] { rect.x, rect.y, rect.width,
				rect.height }, new String[] { "x:", "y:", "width:", "height:" });
		if (dialog.open() == Window.OK) {
			int[] result = dialog.getResult();
			rect.x = result[0];
			rect.y = result[1];
			rect.width = result[2];
			rect.height = result[3];
		}
		return rect;
	}

}
