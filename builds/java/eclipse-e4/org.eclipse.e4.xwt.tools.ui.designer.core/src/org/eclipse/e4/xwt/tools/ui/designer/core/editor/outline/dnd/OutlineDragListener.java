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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class OutlineDragListener implements DragSourceListener {

	private TreeViewer treeViewer;

	public OutlineDragListener(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	private ISelection getSelection() {
		if (treeViewer == null) {
			return null;
		}

		ISelection selection = treeViewer.getSelection();
		if (selection == null || selection.isEmpty()) {
			return null;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		ArrayList<Object> collector = new ArrayList<Object>();
		for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			if (element instanceof EditPart) {
				collector.add(((EditPart) element).getModel());
			}
			else {
				collector.add(element);				
			}
		}
		return new StructuredSelection(collector.toArray());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		OutlineNodeTransfer.getTransfer().setSelection(null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		event.data = getSelection();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		ISelection selection = getSelection();
		if (selection == null) {
			event.doit = false;
		}
		OutlineNodeTransfer.getTransfer().setSelection(selection);
	}

}
