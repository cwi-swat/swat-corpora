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
package org.eclipse.e4.xwt.ui.wizards;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

public class NewE4DynamicPartWizardPage extends NewClassWizardPage {

	public NewE4DynamicPartWizardPage() {
		setTitle("New Wizard Creation");
		setDescription("This wizard creates a view part for e4 workbench.");
	}
	
	protected String getSuperClassName() {
		return "org.eclipse.e4.xwt.ui.workbench.views.XWTDynamicPart";
	}
	
	@Override
	protected void initTypePage(IJavaElement elem) {
		super.initTypePage(elem);
		setSuperClass(getSuperClassName(), false);
	}

	public int getModifiers() {
		return F_PUBLIC;
	}
	
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		// pick & choose the wanted UI components

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);

		createTypeNameControls(composite, nColumns);

		createSeparator(composite, nColumns);

		createCommentControls(composite, nColumns);
		enableCommentControl(true);

		createSeparator(composite, nColumns);

		setControl(composite);

		Dialog.applyDialogFont(composite);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
				IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
	}
}
