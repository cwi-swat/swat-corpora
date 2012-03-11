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
package org.eclipse.e4.xwt.tools.ui.palette.tools;

import java.util.Collection;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.palette.page.CustomPalettePage;
import org.eclipse.e4.xwt.tools.ui.palette.page.CustomPaletteViewerProvider;
import org.eclipse.e4.xwt.tools.ui.palette.page.resources.ExtensionRegistry;
import org.eclipse.e4.xwt.tools.ui.palette.page.resources.IPaletteResourceProvider;
import org.eclipse.e4.xwt.tools.ui.palette.root.PaletteRootFactory;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.ui.IEditorPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PaletteTools {

	public static CustomPalettePage createPalettePage(IEditorPart editorPart,
			IPaletteResourceProvider resourceProvider, Class<? extends Tool> creationToolClass,
			Class<? extends Tool> selectionToolClass) {
		EditDomain editDomain = (EditDomain) editorPart.getAdapter(EditDomain.class);
		if (editDomain == null) {
			editDomain = new DefaultEditDomain(editorPart);
		}
		List<IPaletteResourceProvider> resourceProviders = ExtensionRegistry
				.allResourceExtensions(editorPart);
		if (resourceProvider != null) {
			if (!resourceProviders.isEmpty()) {
				resourceProviders.add(0, resourceProvider);
			} else {
				resourceProviders.add(resourceProvider);
			}
		}

		PaletteRoot paletteRoot = createPaletteRoot(resourceProviders, creationToolClass,
				selectionToolClass);
		if (paletteRoot != null) {
			editDomain.setPaletteRoot(paletteRoot);
		}
		CustomPaletteViewerProvider provider = new CustomPaletteViewerProvider(editDomain);
		return new CustomPalettePage(provider);
	}

	private static PaletteRoot createPaletteRoot(
			Collection<IPaletteResourceProvider> resourceProviders,
			Class<? extends Tool> createToolClass, Class<? extends Tool> selectionToolClass) {
		PaletteRootFactory factory = new PaletteRootFactory(resourceProviders, createToolClass,
				selectionToolClass);
		return factory.createPaletteRoot();
	}
}
