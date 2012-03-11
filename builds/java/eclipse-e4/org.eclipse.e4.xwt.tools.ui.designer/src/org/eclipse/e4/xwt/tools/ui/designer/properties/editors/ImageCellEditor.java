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

import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.dialogs.ImageDialog;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ImageCellEditor extends AbstractCellEditor {

	public ImageCellEditor(Composite parent) {
		super(parent);
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		XWTDesigner designer = XWTDesignerPlugin.getDefault().getActiveDesigner();
		if (designer != null) {
			ImageDialog dialog = new ImageDialog(cellEditorWindow.getShell(), designer.getFile());
			if (Window.OK == dialog.open()) {
				return dialog.getImagePath();
			}
		}
		return null;
	}
}
