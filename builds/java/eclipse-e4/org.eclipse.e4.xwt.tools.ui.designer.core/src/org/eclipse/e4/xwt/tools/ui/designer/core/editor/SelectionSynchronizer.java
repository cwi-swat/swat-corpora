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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class SelectionSynchronizer implements ISelectionChangedListener,
		ISelectionSynchronizer {

	private List<ISelectionProvider> providers = new ArrayList<ISelectionProvider>();
	private boolean isDispatching = false;
	private int disabled = 0;
	private ISelectionProvider pendingSelection;

	public void addProvider(ISelectionProvider provider) {
		provider.addSelectionChangedListener(this);
		providers.add(provider);
	}

	/**
	 * Maps the given editpart from one viewer to an editpart in another viewer.
	 * It returns <code>null</code> if there is no corresponding part. This
	 * method can be overridden to provide custom mapping.
	 * 
	 * @param provider
	 *            the viewer being mapped to
	 * @param part
	 *            a part from another viewer
	 * @return <code>null</code> or a corresponding editpart
	 */
	protected EditPart convert(ISelectionProvider provider, EditPart part) {
		if (provider instanceof EditPartViewer) {
			return (EditPart) ((EditPartViewer) provider).getEditPartRegistry()
					.get(part.getModel());
		}
		return part;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.core.editor.ISelectionSynchronizer
	 * #removeViewer(org.eclipse.jface.viewers.ISelectionProvider)
	 */
	public void removeProvider(ISelectionProvider viewer) {
		viewer.removeSelectionChangedListener(this);
		providers.remove(viewer);
		if (pendingSelection == viewer)
			pendingSelection = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.core.editor.ISelectionSynchronizer
	 * #selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		if (isDispatching)
			return;
		ISelectionProvider source = event.getSelectionProvider();
		if (disabled > 0) {
			pendingSelection = source;
		} else {
			ISelection selection = event.getSelection();
			syncSelection(source, selection);
		}
	}

	private void syncSelection(ISelectionProvider source, ISelection selection) {
		isDispatching = true;
		for (int i = 0; i < providers.size(); i++) {
			if (providers.get(i) != source) {
				ISelectionProvider viewer = providers.get(i);
				setViewerSelection(source, viewer, selection);
			}
		}
		isDispatching = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.core.editor.ISelectionSynchronizer
	 * #setEnabled(boolean)
	 */
	public void setEnabled(boolean value) {
		if (!value)
			disabled++;
		else if (--disabled == 0 && pendingSelection != null) {
			syncSelection(pendingSelection, pendingSelection.getSelection());
			pendingSelection = null;
		}
	}

	protected void setViewerSelection(ISelectionProvider source,
			ISelectionProvider viewer, ISelection selection) {
		ArrayList<EditPart> result = new ArrayList<EditPart>();
		Iterator<?> iter = ((IStructuredSelection) selection).iterator();
		while (iter.hasNext()) {
			EditPart part = convert(viewer, (EditPart) iter.next());
			if (part != null)
				result.add(part);
		}
		viewer.setSelection(new StructuredSelection(result));
		if (result.size() > 0) {
			if (viewer instanceof EditPartViewer) {
				((EditPartViewer) viewer).reveal((EditPart) result.get(result
						.size() - 1));
			}
		}
	}
}
