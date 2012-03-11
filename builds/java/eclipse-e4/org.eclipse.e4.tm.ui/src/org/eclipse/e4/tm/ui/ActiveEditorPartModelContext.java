/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui;

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class ActiveEditorPartModelContext extends EditorPartModelContext implements IPartListener {

	public ActiveEditorPartModelContext(IWorkbenchPart part) {
		super(part);
		getOwner().getSite().getWorkbenchWindow().getPartService().addPartListener(this);
	}

	public void dispose() {
		getOwner().getSite().getWorkbenchWindow().getPartService().removePartListener(this);
		super.dispose();
	}

	private void updateWorkbenchPart(IWorkbenchPart part) {
		if (part == getOwner()) {
			try {
				part = getOwner().getSite().getWorkbenchWindow().getActivePage().getActivePart();
			} catch (NullPointerException e) {
			}
		}
		if (part instanceof IEditingDomainProvider) {
			setEditingDomainProvider((IEditingDomainProvider)part);
		}
	}

	public void partActivated(IWorkbenchPart part) {
		updateWorkbenchPart(part);
	}
	public void partOpened(IWorkbenchPart part) {
		updateWorkbenchPart(part);
	}
	public void partBroughtToTop(IWorkbenchPart part) {
		updateWorkbenchPart(part);
	}
	public void partClosed(IWorkbenchPart part) {
	}
	public void partDeactivated(IWorkbenchPart part) {
	}
}
