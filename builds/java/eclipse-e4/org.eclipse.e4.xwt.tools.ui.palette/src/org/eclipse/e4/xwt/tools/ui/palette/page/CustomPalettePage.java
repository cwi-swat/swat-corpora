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

import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class CustomPalettePage extends PaletteViewerPage {
	private Composite parent;

	public CustomPalettePage(CustomPaletteViewerProvider pvProvider) {
		super(pvProvider);
	}

	/**
	 * @return the PaletteViewer created and displayed by this page
	 */
	public PaletteViewer getPaletteViewer() {
		return viewer;
	}

	/**
	 * @see org.eclipse.gef.ui.views.palette.PaletteViewerPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		this.parent = parent;
		super.createControl(parent);
	}

	public void reCreate() {
		if (parent != null && !parent.isDisposed()) {
			viewer = provider.createPaletteViewer(parent);
		}
	}

	public LocalSelectionTransfer getPaletteTransfer() {
		return ((CustomPaletteViewerProvider) provider).getPaletteTransfer();
	}
}
