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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline.dnd;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class OutlineDropListener extends ViewerDropAdapter {

	private OutlineDropManager dropManager;
	private Command dropCommand;

	public OutlineDropListener(TreeViewer viewer, OutlineDropManager dropManager) {
		super(viewer);
		this.dropManager = dropManager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
	 */
	public boolean performDrop(Object data) {
		return dropManager.execute(dropCommand);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#determineTarget(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	protected Object determineTarget(DropTargetEvent event) {
		Object target = super.determineTarget(event);
		if (target instanceof EditPart) {
			target = ((EditPart) target).getModel();
		}
		return target;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object, int, org.eclipse.swt.dnd.TransferData)
	 */
	public boolean validateDrop(Object target, int operation, TransferData transferType) {
		if (dropManager == null || target == null) {
			return false;
		}
		OutlineNodeTransfer transfer = OutlineNodeTransfer.getTransfer();
		IStructuredSelection selection = transfer.getSelection();
		if (selection == null) {
			return false;
		}
		int currentLocation = getCurrentLocation();
		switch (currentLocation) {
		case LOCATION_AFTER: {
			dropCommand = dropManager.getMoveAfter(selection, target, getCurrentOperation());
			break;
		}
		case LOCATION_BEFORE: {
			dropCommand = dropManager.getMoveBefore(selection, target, getCurrentOperation());
			break;
		}
		case LOCATION_ON: {
			dropCommand = dropManager.getMoveOn(selection, target, getCurrentOperation());
			break;
		}
		}
		return dropCommand != null && dropCommand.canExecute();
	}
}
