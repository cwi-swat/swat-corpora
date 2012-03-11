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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PointCellEditor extends AbstractCellEditor {

	public PointCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		Point point = new Point(0, 0);
		Object value = getValue();
		if (value instanceof Point) {
			point.x = ((Point) value).x;
			point.y = ((Point) value).y;
		} else {
			IConverter c = XWT.findConvertor(value.getClass(), Point.class);
			if (c != null) {
				point = (Point) c.convert(value);
			}
		}
		IntArrayDialog dialog = new IntArrayDialog(cellEditorWindow.getShell(), new int[] { point.x, point.y },
				new String[] { "x:", "y:" });
		if (dialog.open() == Window.OK) {
			int[] result = dialog.getResult();
			point.x = result[0];
			point.y = result[1];
		}
		return point;
	}
}
