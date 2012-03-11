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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.ui.ExceptionHandle;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.e4.xwt.ui.utils.ProjectUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class NewUIFileWizard extends Wizard implements INewWizard {

	private NewUIFileWizardPage fileWizardPage;

	private IStructuredSelection selection;

	public NewUIFileWizard() {
		setNeedsProgressMonitor(true);
		setWindowTitle("New Widget File Wizard.");
	}

	public void addPages() {
		fileWizardPage = new NewUIFileWizardPage(selection);
		addPage(fileWizardPage);
	}

	public boolean performFinish() {
		final String containerName = fileWizardPage.getContainerName();
		final String fileName = fileWizardPage.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					IFile file = doFinish(containerName, fileName, monitor);
					tryToOpenPerspective();
					ProjectUtil.updateXWTCoreDependencies(file.getProject());
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	protected void tryToOpenPerspective() {
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				XWTUIPlugin.getDefault().openXWTPerspective();
			}
		});
	}

	/**
	 * The worker method. It will find the container, create the file if missing or just replace its nameMap, and open the editor on the newly created file.
	 */

	private IFile doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream(file);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
		return file;
	}

	/**
	 * We will initialize file nameMap with a sample text.
	 */

	private InputStream openContentStream(IFile file) {
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(arrayOutputStream);

		printStream.println("<Composite");
		printStream.println("\t xmlns=\"" + IConstants.XWT_NAMESPACE + "\"");
		printStream.println("\t xmlns:x=\"" + IConstants.XWT_X_NAMESPACE + "\">");
		printStream.println("</Composite>");

		try {
			byte[] content = arrayOutputStream.toByteArray();
			printStream.close();
			arrayOutputStream.close();
			return new ByteArrayInputStream(content);
		} catch (Exception e) {
			XWTUIPlugin.log(e);
			ExceptionHandle.handle(e, "save failed in the file: " + fileWizardPage.getFileName());
		}
		return new ByteArrayInputStream(new byte[] {});
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, XWTUIPlugin.PLUGIN_ID, IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}
