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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.root.BindingLayer;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.root.DesignerRootEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BindingLayerAction extends Action {

	public static final String ID = BindingLayerAction.class.getName();
	private Designer designer;

	public BindingLayerAction(Designer designer) {
		super("Binding Layer", SWT.CHECK);
		this.designer = designer;
		setId(ID);
		setChecked(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		DesignerRootEditPart rootEp = (DesignerRootEditPart) designer.getAdapter(EditPart.class);
		Boolean layerVisible = rootEp.getLayerVisible(BindingLayer.ID);
		if (layerVisible == null) {
			return;
		}
		rootEp.setLayerVisible(BindingLayer.ID, !layerVisible.booleanValue());
		setChecked(!layerVisible.booleanValue());
	}
}
