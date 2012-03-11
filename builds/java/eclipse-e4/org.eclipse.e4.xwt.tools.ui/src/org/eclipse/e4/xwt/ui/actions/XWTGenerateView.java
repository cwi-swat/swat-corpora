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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class XWTGenerateView implements IObjectActionDelegate {

	protected Collection<IType> types = new ArrayList<IType>();

	public XWTGenerateView() {
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	public void run(IAction action) {
		if (types != null && !types.isEmpty()) {
			// ToolsUIPlugin.checkStartup();

			for (IType type : types) {
				generateXMLFor(type);
			}
		}
	}

	protected void generateXMLFor(IType type) {
	}

	public void selectionChanged(IAction action, ISelection selection) {
		types.clear();
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			if (element instanceof IType) {
				types.add((IType) element);
			} else if (element instanceof ICompilationUnit) {
				ICompilationUnit unit = (ICompilationUnit) element;
				try {
					IType[] children = unit.getAllTypes();
					for (int i = 0; i < children.length; i++) {
						types.add(children[i]);
					}
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
