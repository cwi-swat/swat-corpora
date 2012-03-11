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

public class CopyElementAction extends SelectionAction {
	private XWTDesigner editorPart;
	private EditPart editpart;

	public CopyElementAction(IWorkbenchPart part) {
		super(part);
		this.editorPart = (XWTDesigner) part;
		setText(WorkbenchMessages.Workbench_copy);
		setToolTipText(WorkbenchMessages.Workbench_copyToolTip);
		setId(ActionFactory.COPY.getId());
		setAccelerator(SWT.MOD1 | 'c');
		ISharedImages sharedImages = part.getSite().getWorkbenchWindow().getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
	}

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

	public void run() {
		List<?> selectedEditParts = this.editorPart.getGraphicalViewer().getSelectedEditParts();
		List<XamlElement> selectResult = new ArrayList<XamlElement>();
		if (selectedEditParts == null || selectedEditParts.isEmpty()) {
			// Diagram directly...
		} else {
			for (int i = 0; i < selectedEditParts.size(); i++) {
				editpart = (EditPart) selectedEditParts.get(i);
				Object model = this.editpart.getModel();
				Object parentModel = this.editpart.getParent().getModel();
				if (model instanceof XamlElement && parentModel instanceof XamlElement) {
					XamlElement copymodel = (XamlElement) EcoreUtil.copy((XamlElement) model);
					selectResult.add(copymodel);

				}
			}

		}
		if (selectResult != null && selectResult.size() != 0)
			Clipboard.getDefault().setContents(selectResult);
		super.run();
	}
}
