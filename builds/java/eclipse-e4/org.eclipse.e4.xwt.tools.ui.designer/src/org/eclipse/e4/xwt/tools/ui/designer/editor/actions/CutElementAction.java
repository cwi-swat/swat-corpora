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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.commands.DeleteCommand;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.internal.WorkbenchMessages;

public class CutElementAction extends SelectionAction {
	private XWTDesigner editorPart;

	/**
	 * @param part
	 */
	public CutElementAction(IWorkbenchPart part) {
		super(part);
		this.editorPart = (XWTDesigner) part;
		this.setText(WorkbenchMessages.Workbench_cut);
		this.setToolTipText(WorkbenchMessages.Workbench_cutToolTip);
		this.setId(ActionFactory.CUT.getId());
		this.setAccelerator(SWT.MOD1 | 'x');
		ISharedImages sharedImages = part.getSite().getWorkbenchWindow().getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (editorPart == null) {
			return false;
		}
		if (editorPart.getGraphicalViewer() == null) {
			return false;
		}
		List<?> selectedEditParts = this.editorPart.getGraphicalViewer().getSelectedEditParts();
		boolean result = selectedEditParts != null && !selectedEditParts.isEmpty();
		if (result) {
			for (Iterator<?> iterator = selectedEditParts.iterator(); iterator
					.hasNext();) {
				EditPart editPart = (EditPart) iterator.next();
				Object object = editPart.getModel();
				if (object instanceof EObject) {
					EObject eObject = (EObject) object;
					if (eObject.eContainer() == null) {
						return false;
					}
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		List<?> selectedEditParts = this.editorPart.getGraphicalViewer().getSelectedEditParts();
		List<XamlElement> selectResult = new ArrayList<XamlElement>();
		for (Iterator<?> iterator = selectedEditParts.iterator(); iterator.hasNext();) {
			EditPart part = (EditPart) iterator.next();
			Object model = part.getModel();
			if (model instanceof XamlElement) {
				XamlElement copymodel = (XamlElement) EcoreUtil.copy((XamlElement) model);
				selectResult.add(copymodel);
			}
		}
		if (selectResult != null && selectResult.size() != 0)
			Clipboard.getDefault().setContents(selectResult);
		DeleteCommand dcmd = new DeleteCommand(selectedEditParts);
		editorPart.getEditDomain().getCommandStack().execute(dcmd);
	}
}
