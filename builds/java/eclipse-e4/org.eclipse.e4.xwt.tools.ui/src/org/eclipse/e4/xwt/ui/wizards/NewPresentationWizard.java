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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.e4.xwt.ui.utils.ProjectUtil;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

public class NewPresentationWizard extends NewElementWizard {

	private NewPresentationWizardPage fPage;

	public NewPresentationWizard() {
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New Data Presentation in XWT");
	}

	public NewPresentationWizard(IType contextType) {
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New Data Presentation in XWT");
	}
	
	public void addPages() {
		Object element = getSelection().getFirstElement();
		if (!(element instanceof ICompilationUnit) && !(element instanceof IType)) {
			getShell().setAlpha(0);
			MessageDialog.openError(getShell(), "Error", "Please select a Java class.");
			getShell().getDisplay().asyncExec(new Runnable() {				
				public void run() {
					getShell().close();
				}
			});
			return;
		}
		fPage = new NewPresentationWizardPage();
		fPage.init(getSelection());
		addPage(fPage);
	}
	

	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
		boolean res = super.performFinish();
		if (res) {
			tryToOpenResource();
			ProjectUtil.updateXWTDataBindingDependencies(getCreatedElement().getResource().getProject());
		}
		XWTUIPlugin.getDefault().openXWTPerspective();
		return res;
	}

	private void tryToOpenResource() {
		IResource resource = fPage.getModifiedResource();
		if (resource != null) {
			selectAndReveal(resource);
		}
		IResource guiResource = fPage.getGuiResource();
		if (guiResource != null) {
			selectAndReveal(guiResource);
			openResource((IFile) guiResource);
		} else {
			openResource((IFile) resource);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#getCreatedElement()
	 */
	public IJavaElement getCreatedElement() {
		return fPage.getCreatedType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#canRunForked()
	 */
	protected boolean canRunForked() {
		return !fPage.isEnclosingTypeSelected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		fPage.createType(monitor); // use the full progress monitor
	}
}
