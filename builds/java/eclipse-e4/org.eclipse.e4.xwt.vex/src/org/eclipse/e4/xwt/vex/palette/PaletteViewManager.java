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
package org.eclipse.e4.xwt.vex.palette;

import org.eclipse.e4.xwt.vex.palette.part.ToolPaletteViewerProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.ui.IEditorPart;

/**
 * @author bqian
 * 
 */
public class PaletteViewManager {
	private PaletteViewerProvider provider;
	private PaletteRoot paletteRoot;
	private IEditorPart editorPart;
	private DefaultEditDomain editDomain;

	/**
	 * @param editorPart
	 */
	public PaletteViewManager(IEditorPart editorPart) {
		this.editorPart = editorPart;
	}

	public PaletteViewerProvider getPaletteViewerProvider() {
		if (provider == null)
			provider = createPaletteViewerProvider();
		return provider;
	}

	private PaletteViewerProvider createPaletteViewerProvider() {
		return new ToolPaletteViewerProvider(getEditDomain(), editorPart);
	}

	private EditDomain getEditDomain() {
		if (editDomain == null) {
			editDomain = new DefaultEditDomain(editorPart);
			editDomain.setPaletteRoot(getPaletteRoot());
		}
		return editDomain;
	}

	public PaletteRoot getPaletteRoot() {
		if (paletteRoot == null)
			paletteRoot = PaletteRootFactory.createPalette(editorPart);
		return paletteRoot;
	}

	public PaletteRoot getPaletteRootByResourceManager(PaletteResourceManager resourceManager) {
		paletteRoot = PaletteRootFactory.createPaletteByResourceManager(resourceManager);
		return paletteRoot;
	}

}
