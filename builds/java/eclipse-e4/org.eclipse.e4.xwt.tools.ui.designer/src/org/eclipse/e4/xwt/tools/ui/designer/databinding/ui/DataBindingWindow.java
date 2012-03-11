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

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DataBindingWindow extends Window {

	private static final int INIT_HEIGHT = 500;
	private static final int INIT_WIDTH = 600;

	private EditPart root;

	public DataBindingWindow(Shell parentShell, EditPart root) {
		super(parentShell);
		this.root = root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Create Data Bindings");
		newShell.setSize(INIT_WIDTH, INIT_HEIGHT);
		Rectangle bounds = newShell.getDisplay().getBounds();
		newShell.setLocation((bounds.width - INIT_WIDTH) / 2, (bounds.height - INIT_HEIGHT) / 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		CommandStack commandStack = EditDomain.getEditDomain(root).getCommandStack();
		DatabindingGroup databindingGroup = new DatabindingGroup(commandStack, new BindingContext());
		Composite control = databindingGroup.createGroup(parent, SWT.NONE);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		databindingGroup.setInput(root);
		return control;
	}

}
