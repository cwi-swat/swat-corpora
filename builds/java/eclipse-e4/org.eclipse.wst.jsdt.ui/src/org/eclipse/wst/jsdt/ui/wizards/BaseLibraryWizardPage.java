/*******************************************************************************
 * Copyright (c) 2007, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;

/**
 * NOT API
 */
public class BaseLibraryWizardPage extends NewElementWizardPage implements IJsGlobalScopeContainerPage, IJsGlobalScopeContainerPageExtension, IJsGlobalScopeContainerPageExtension2 {
	private static final String HTML5 = "html5"; //$NON-NLS-1$
	private static final String STANDARD_BROWSER = "/StandardBrowser/"; //$NON-NLS-1$

	private static IPath newDefaultPath() {
		return newV401Path();
	}

	private static Path newV401Path() {
		return new Path(org.eclipse.wst.jsdt.launching.JavaRuntime.BASE_BROWSER_LIB);
	}

	private static Path newV5Path() {
		return new Path(org.eclipse.wst.jsdt.launching.JavaRuntime.BASE_BROWSER_LIB + STANDARD_BROWSER + HTML5);
	}

	private IPath fPath = Path.ROOT;

	private ComboDialogField fVersionField;

	public BaseLibraryWizardPage() {
		super("BasicLibraryWizardPage"); //$NON-NLS-1$
		setTitle(NewWizardMessages.BaseLibraryWizardPage_title);
		setImageDescriptor(JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		DialogField field = new DialogField();

		field.setLabelText(NewWizardMessages.BaseLibraryWizardPage_DefaultBrowserLibraryAdded);
		LayoutUtil.doDefaultLayout(composite, new DialogField[]{field}, false, SWT.DEFAULT, SWT.DEFAULT);

		fVersionField = new ComboDialogField(SWT.READ_ONLY);
		fVersionField.setLabelText(NewWizardMessages.BaseLibraryWizardPage_DOM_objects_version);
		fVersionField.setItems(new String[]{NewWizardMessages.BaseLibraryWizardPage_HTML401, NewWizardMessages.BaseLibraryWizardPage_HTML5}); 
		if (fPath.segmentCount() > 2 && fPath.lastSegment().equals(HTML5)) {
			fVersionField.selectItem(1);
		}
		else {
			fVersionField.selectItem(0);
		}

		LayoutUtil.doDefaultLayout(composite, new DialogField[]{field, fVersionField}, false, SWT.DEFAULT, SWT.DEFAULT);

		Dialog.applyDialogFont(composite);
		setControl(composite);
		setDescription(NewWizardMessages.BaseLibraryWizardPage_WebBrowserSupport);
	}

	public boolean finish() {
		if (fVersionField == null) {
			fPath = newDefaultPath();
		}
		else {
			switch (fVersionField.getSelectionIndex()) {
				case 0 :
					fPath = newV401Path();
					break;
				case 1 :
					fPath = newV5Path();
					break;
				default :
					fPath = newDefaultPath();
					break;
			}
		}
		return true;
	}

	public IIncludePathEntry[] getNewContainers() {
		if (fPath == null || fPath.isRoot())
			fPath = newDefaultPath();

		IIncludePathEntry library = JavaScriptCore.newContainerEntry(fPath);
		return new IIncludePathEntry[]{library};
	}

	public IIncludePathEntry getSelection() {
		return JavaScriptCore.newContainerEntry(fPath);
	}

	public void initialize(IJavaScriptProject project, IIncludePathEntry[] currentEntries) {
		fPath = newDefaultPath();

		for (int i = 0; i < currentEntries.length; i++) {
			IPath path = currentEntries[i].getPath();
			if (!path.isEmpty() && org.eclipse.wst.jsdt.launching.JavaRuntime.BASE_BROWSER_LIB.equals(path.segment(0))) {
				fPath = path;
			}
		}
	}

	public void setSelection(IIncludePathEntry containerEntry) {
		if (containerEntry != null) {
			fPath = containerEntry.getPath();
		}
	}
}
