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
package org.eclipse.e4.xwt.ui;

import org.eclipse.e4.xwt.ui.views.XWTView;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class XWTPerspectiveFactory implements IPerspectiveFactory {

	public static final String XWT_PERSPECTIVE_ID = "org.eclipse.e4.xwt.ui.perspective";
	private IPageLayout factory;

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		factory.setFixed(false);
		String editorArea = factory.getEditorArea();
		// Left Top
		IFolderLayout leftTop = factory.createFolder("leftTop", IPageLayout.LEFT, (float) 0.25, editorArea); //$NON-NLS-1$
		leftTop.addPlaceholder(IPageLayout.ID_RES_NAV);
		leftTop.addView(JavaUI.ID_PACKAGES);

		// Left Top
		IFolderLayout leftButtom = factory.createFolder("leftBottom", IPageLayout.BOTTOM, (float) 0.50, "leftTop"); //$NON-NLS-1$
		leftButtom.addView(IPageLayout.ID_OUTLINE);

		// Right
		IFolderLayout right = factory.createFolder("right", IPageLayout.RIGHT, (float) 0.75, editorArea);
		right.addView("org.eclipse.gef.ui.palette_view");

		// Bottom
		IFolderLayout bottom = factory.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75, editorArea); //$NON-NLS-1$
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(XWTView.ID);
	}

	private void addActionSets() {
		factory.addActionSet(JavaUI.ID_ACTION_SET);
		factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
	}

	private void addNewWizardShortcuts() {

		// XWT Creation Wizards.
		factory.addNewWizardShortcut("org.eclipse.e4.xwt.ui.wizards.newUIFile");
		factory.addNewWizardShortcut("org.eclipse.e4.xwt.ui.wizards.NewPresentationWizard");
		factory.addNewWizardShortcut("org.eclipse.e4.xwt.ui.wizards.NewUIElementWizard");
		factory.addNewWizardShortcut("org.eclipse.e4.xwt.ui.wizards.NewE4StaticPartWizard");
		factory.addNewWizardShortcut("org.eclipse.e4.xwt.ui.wizards.NewE4DynamicPartWizard");

		// Default JDT Wizards.
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"); //$NON-NLS-1$
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); //$NON-NLS-1$
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"); //$NON-NLS-1$
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard"); //$NON-NLS-1$
		// Eclipse Resource Wizards.
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //$NON-NLS-1$
		factory.addPerspectiveShortcut(JavaUI.ID_PERSPECTIVE);
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut(JavaUI.ID_PACKAGES);
		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}
}
