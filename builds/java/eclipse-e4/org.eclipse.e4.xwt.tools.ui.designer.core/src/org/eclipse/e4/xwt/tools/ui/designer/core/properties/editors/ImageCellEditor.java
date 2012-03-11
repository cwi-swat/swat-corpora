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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ImageCellEditor extends AbstractCellEditor {

	public ImageCellEditor(Composite parent) {
		super(parent);
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		return null;
	}
}
