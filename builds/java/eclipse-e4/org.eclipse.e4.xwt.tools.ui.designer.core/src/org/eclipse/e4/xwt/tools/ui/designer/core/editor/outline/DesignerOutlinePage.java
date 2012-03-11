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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DesignerOutlinePage extends ContentOutlinePage {

	private EditDomain editDomain;
	private EditPartFactory editPartFactory;

	public DesignerOutlinePage(EditDomain editDomain,
			EditPartFactory editPartFactory) {
		super(new TreeViewer());
		this.editDomain = editDomain;
		this.editPartFactory = editPartFactory;
		configureTreeViewer();
	}

	public TreeViewer getTreeViewer() {
		return (TreeViewer) getViewer();
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	protected void configureTreeViewer() {
		TreeViewer treeViewer = getTreeViewer();
		editDomain.addViewer(treeViewer);
		treeViewer.setEditPartFactory(editPartFactory);
		treeViewer
				.addDropTargetListener(new AbstractTransferDropTargetListener(
						treeViewer, LocalSelectionTransfer.getTransfer()) {
					protected void updateTargetRequest() {

					}

					public void dragOver(DropTargetEvent event) {
						super.dragOver(event);
						System.out
								.println("DesignerOutlinePage.configureTreeViewer().new AbstractTransferDropTargetListener() {...}.dragOver()");
					}

					protected void handleHover() {
						super.handleHover();
						System.out
								.println("DesignerOutlinePage.configureTreeViewer().new AbstractTransferDropTargetListener() {...}.handleHover()");
					}
				});
	}

	public void setContents(final Object contents) {
		TreeViewer treeViewer = getTreeViewer();
		if (treeViewer == null) {
			return;
		}
		RootEditPart rootEditPart = treeViewer.getRootEditPart();
		EditPart rootEp = new AbstractTreeEditPart() {
			protected List getModelChildren() {
				ArrayList children = new ArrayList();
				children.add(contents);
				return children;
			}
		};
		rootEditPart.setContents(rootEp);
	}

	public void refresh(Object model) {
		if (model == null) {
			return;
		}
		TreeViewer treeViewer = getTreeViewer();
		if (treeViewer == null) {
			return;
		}
		if (model instanceof EditPart) {
			model = ((EditPart) model).getModel();
		}
		EditPart editPart = (EditPart) treeViewer.getEditPartRegistry().get(
				model);
		if (editPart == null) {
			editPart = treeViewer.getContents();
		}
		refresh(editPart);
	}

	private void refresh(EditPart editPart) {
		editPart.refresh();
		List<?> children = editPart.getChildren();
		for (Object object : children) {
			refresh((EditPart) object);
		}
	}

}
