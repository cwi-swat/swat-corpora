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

import org.eclipse.e4.xwt.tools.ui.palette.page.editparts.EditPartFactory;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class CustomPaletteViewerProvider extends PaletteViewerProvider {

	private LocalSelectionTransfer paletteTransfer;

	public CustomPaletteViewerProvider(EditDomain graphicalViewerDomain) {
		super(graphicalViewerDomain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.palette.PaletteViewerProvider#createPaletteViewer(
	 * org.eclipse.swt.widgets.Composite)
	 */
	public PaletteViewer createPaletteViewer(Composite parent) {
		PaletteViewer pViewer = new PaletteViewer();
		pViewer.createControl(parent);
		configurePaletteViewer(pViewer);
		hookPaletteViewer(pViewer);
		return pViewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.palette.PaletteViewerProvider#configurePaletteViewer
	 * (org.eclipse.gef.ui.palette.PaletteViewer)
	 */
	protected void configurePaletteViewer(PaletteViewer viewer) {
		super.configurePaletteViewer(viewer);
		EditDomain editDomain = getEditDomain();
		if (editDomain != null) {
			editDomain.setPaletteViewer(viewer);
			viewer.setEditDomain(editDomain);
		}
		viewer.setKeyHandler(new PaletteViewerKeyHandler(viewer));
		viewer.setEditPartFactory(new EditPartFactory());
		viewer.addDragSourceListener(new DragSourceAdapter(viewer, getPaletteTransfer()));
	}

	/**
	 * @return the paletteTransfer
	 */
	public LocalSelectionTransfer getPaletteTransfer() {
		if (paletteTransfer == null) {
			paletteTransfer = LocalSelectionTransfer.getTransfer();
		}
		return paletteTransfer;
	}
}
