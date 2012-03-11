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
package org.eclipse.e4.xwt.ui.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.ui.ExceptionHandle;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.e4.xwt.ui.views.XWTView;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class XWTViewOpenAction implements IObjectActionDelegate {

	protected IFile file;

	public XWTViewOpenAction() {

	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void run(IAction action) {
		if (file != null) {
			XWTUIPlugin.checkStartup();
			try {
				XWTView view = (XWTView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(XWTView.ID);
				view.setContent(null, file);
			} catch (Exception e) {
				ExceptionHandle.handle(e, "");
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			if (element instanceof IFile) {
				file = (IFile) element;
				break;
			}
		}
	}

}
