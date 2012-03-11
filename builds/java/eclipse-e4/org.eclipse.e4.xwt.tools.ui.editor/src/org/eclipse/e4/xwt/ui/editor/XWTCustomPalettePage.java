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
package org.eclipse.e4.xwt.ui.editor;

import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;

/**
 * @author bqian
 * 
 */
public class XWTCustomPalettePage extends PaletteViewerPage {
	/**
	 * Constructor
	 * 
	 * @param provider
	 *            the provider used to create a PaletteViewer
	 */
	public XWTCustomPalettePage(PaletteViewerProvider provider) {
		super(provider);
	}

	/**
	 * @return the PaletteViewer created and displayed by this page
	 */
	public PaletteViewer getPaletteViewer() {
		return viewer;
	}
}