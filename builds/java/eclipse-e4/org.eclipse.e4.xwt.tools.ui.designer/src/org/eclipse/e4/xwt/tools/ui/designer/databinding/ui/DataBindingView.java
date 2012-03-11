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
package org.eclipse.e4.xwt.tools.ui.designer.databinding.ui;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DataBindingView extends ViewPart {

	private Composite partControl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		EditPart root = getRoot();
		CommandStack commandStack = null;
		if (root != null) {
			commandStack = EditDomain.getEditDomain(root).getCommandStack();
		}
		DatabindingGroup databindingGroup = new DatabindingGroup(commandStack, new BindingContext());
		partControl = databindingGroup.createGroup(parent, SWT.V_SCROLL);
		databindingGroup.setInput(root);
	}

	private EditPart getRoot() {
		try {
			Designer designer = (Designer) getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
			Object root = designer.getVisualsRender().getRoot();
			if (root instanceof Widget) {
				root = XWTProxy.getModel((Widget) root);
			}
			return designer.getEditPart(root);
		} catch (Exception e) {
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		partControl.setFocus();
	}

}
