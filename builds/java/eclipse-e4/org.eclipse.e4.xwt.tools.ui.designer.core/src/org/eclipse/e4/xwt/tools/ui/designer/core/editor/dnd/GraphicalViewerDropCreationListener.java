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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd;

import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class GraphicalViewerDropCreationListener extends
		AbstractTransferDropTargetListener {

	public GraphicalViewerDropCreationListener(EditPartViewer viewer) {
		super(viewer, LocalSelectionTransfer.getTransfer());
		setEnablementDeterminedByCommand(true);
	}

	protected void updateTargetRequest() {
		Request req = getTargetRequest();
		if (req instanceof CreateRequest) {
			((CreateRequest) req).setLocation(getDropLocation());
		}
	}

	/**
	 * The purpose of a template is to be copied. Therefore, the drop operation
	 * can't be anything but <code>DND.DROP_COPY</code>.
	 * 
	 * @see AbstractTransferDropTargetListener#handleDragOperationChanged()
	 */
	protected void handleDragOperationChanged() {
		getCurrentEvent().detail = DND.DROP_COPY;
		super.handleDragOperationChanged();
	}

	protected void handleDragOver() {
		getCurrentEvent().detail = DND.DROP_COPY;
		getCurrentEvent().feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
		super.handleDragOver();
	}

	protected Request createTargetRequest() {
		CreationFactory creationFactory = getCreationFactory();
		if (creationFactory != null) {
			CreateRequest createReq = new CreateRequest();
			createReq.setFactory(creationFactory);
			return createReq;
		}
		return super.createTargetRequest();
	}

	protected CreationFactory getCreationFactory() {
		ISelection selection = LocalSelectionTransfer.getTransfer()
				.getSelection();
		if (selection instanceof IStructuredSelection) {
			return createCreationFactory(((IStructuredSelection) selection)
					.getFirstElement());
		}
		return createCreationFactory(selection);
	}

	protected CreationFactory createCreationFactory(Object selection) {
		return null;
	}

	protected Class<?> getClassType(ICompilationUnit unit) {
		try {
			IType type = unit.findPrimaryType();
			return ProjectContext.getContext(type.getJavaProject()).loadClass(
					type.getFullyQualifiedName());
		} catch (Exception e) {
		}
		return null;
	}
}
