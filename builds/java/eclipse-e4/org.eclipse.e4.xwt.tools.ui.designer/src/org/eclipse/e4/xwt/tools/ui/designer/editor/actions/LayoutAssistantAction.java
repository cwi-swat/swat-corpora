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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.dialogs.LayoutAssistantWindow;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class LayoutAssistantAction extends SelectionAction {

	public static final String ID = "org.eclipse.e4.xwt.tools.ui.designer.editor.actions.LayoutAssistantAction";

	private LayoutAssistantWindow window;

	public LayoutAssistantAction(IWorkbenchPart part) {
		super(part);
		setText("Layout Assistant...");
		setImageDescriptor(ImageShop.getImageDescriptor(ImageShop.IMG_LAYOUT_ASSIST));
		setId(ID);
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		EditPart editPart = getEditPart();
		if (editPart == null) {
			return;
		}
		if (window == null || window.isClosed()) {
			window = new LayoutAssistantWindow(getWorkbenchPart().getSite().getShell());
		}
		window.setEditPart(editPart);
		if (!window.isOpened()) {
			window.open();
		} else {
			window.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.SelectionAction#dispose()
	 */
	public void dispose() {
		if (window != null) {
			window.close();
		}
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.SelectionAction#handleSelectionChanged()
	 */
	protected void handleSelectionChanged() {
		super.handleSelectionChanged();
		EditPart editPart = getEditPart();
		if (window != null && window.isOpened() && editPart != null) {
			window.setEditPart(editPart);
		}
	}

	public EditPart getEditPart() {
		List selectedObjects = getSelectedObjects();
		if (selectedObjects.size() == 1 && selectedObjects.get(0) instanceof EditPart) {
			return (EditPart) selectedObjects.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		EditPart editPart = getEditPart();
		if (editPart == null) {
			return false;
		}
		Object model = editPart.getModel();
		if (model instanceof EObject) {
			EObject object = (EObject)model;
			if (object.eContainer() == null) {
				return false;
			}
		}
		return true;
	}
}
