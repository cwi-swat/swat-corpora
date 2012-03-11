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
package org.eclipse.e4.xwt.tools.ui.palette.page;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DragSourceAdapter extends AbstractTransferDragSourceListener {

	public DragSourceAdapter(EditPartViewer viewer, LocalSelectionTransfer transfer) {
		super(viewer, transfer);
	}

	/**
	 * @see AbstractTransferDragSourceListener#dragFinished(DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		((LocalSelectionTransfer) getTransfer()).setSelection(null);
	}

	/**
	 * Get the <i>template</i> from the selected {@link PaletteTemplateEntry}
	 * and sets it as the event data to be dropped.
	 * 
	 * @param event
	 *            the DragSourceEvent
	 */
	public void dragSetData(DragSourceEvent event) {
		event.data = getTemplate();
	}

	/**
	 * Cancels the drag if the selected item does not represent a
	 * PaletteTemplateEntry.
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		Object template = getTemplate();
		if (template == null) {
			event.doit = false;
			return;
		}
		IStructuredSelection selection = new StructuredSelection(template);
		((LocalSelectionTransfer) getTransfer()).setSelection(selection);
	}

	/**
	 * A helper method that returns <code>null</code> or the <i>template</i>
	 * Object from the currently selected EditPart.
	 * 
	 * @return the template
	 */
	protected Object getTemplate() {
		List selection = getViewer().getSelectedEditParts();
		if (selection.size() == 1) {
			EditPart editpart = (EditPart) getViewer().getSelectedEditParts().get(0);
			Object model = editpart.getModel();
			if (model instanceof PaletteTemplateEntry)
				return ((PaletteTemplateEntry) model).getTemplate();
			if (model instanceof CombinedTemplateCreationEntry)
				return ((CombinedTemplateCreationEntry) model).getTemplate();
		}
		return null;
	}
}
