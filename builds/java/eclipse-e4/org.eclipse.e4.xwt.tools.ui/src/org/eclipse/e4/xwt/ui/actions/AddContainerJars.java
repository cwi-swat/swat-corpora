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

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.xwt.ui.jdt.ProjectHelper;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class AddContainerJars implements IObjectActionDelegate {
	protected IWorkbenchPart part;

	protected IAction action;

	protected ISelection sel;

	/**
	 * Constructor for Action1.
	 */
	public AddContainerJars() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.part = targetPart;
		this.action = action;
	}

	/**
	 * @see IActionDelegate#run(IAction) It get back the project name , the create the Main Creation wizard dialog, and open it.
	 */
	public void run(IAction action) {
		IStructuredSelection structured = (IStructuredSelection) sel;
		Object object = structured.getFirstElement();

		IJavaProject javaProject = null;

		if (object instanceof IProject) // case of Navigator View
		{
			javaProject = JavaCore.create((IProject) object);
		} else if (object instanceof IJavaProject) {
			javaProject = ((IJavaProject) object);
		} else {
			return;
		}

		if (javaProject != null) {
			ProjectHelper.AddJars(javaProject);
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		sel = selection;
	}
}