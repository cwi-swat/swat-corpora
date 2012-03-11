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

import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.Palette;
import org.eclipse.e4.xwt.tools.ui.palette.contribution.PaletteContribution;
import org.eclipse.e4.xwt.tools.ui.palette.page.resources.IPaletteResourceProvider;
import org.eclipse.e4.xwt.tools.ui.palette.root.PaletteRootFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.ui.IEditorPart;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class ContributePalettePage extends CustomPalettePage {

	public ContributePalettePage(IEditorPart editorPart, EditDomain editDomain) {
		super(new CustomPaletteViewerProvider(editDomain));
		createPaletteRoot(editorPart, editDomain);
	}

	private void createPaletteRoot(IEditorPart editorPart, EditDomain editDomain) {
		if (editorPart == null) {
			return;
		}
		if (editDomain == null) {
			editDomain = (EditDomain) editorPart.getAdapter(EditDomain.class);
		}
		if (editDomain == null) {
			editDomain = new DefaultEditDomain(editorPart);
		}

		String editorId = editorPart.getSite().getId();
		PaletteContribution contribution = PaletteContribution.getContribution(editorId);
		List<IPaletteResourceProvider> resourceProviders = contribution.getResourceProviders();

		boolean hasInitialiers = contribution.hasInitialiers();
		if (hasInitialiers) {
			for (IPaletteResourceProvider rp : resourceProviders) {
				Resource res = rp.getPaletteResource();
				if (res == null) {
					continue;
				}
				EList<EObject> contents = res.getContents();
				for (EObject eObject : contents) {
					if (!(eObject instanceof Palette)) {
						continue;
					}
					Palette palette = (Palette) eObject;
					for (Entry entry : palette.getEntries()) {
						applyInitializer(contribution, entry);
					}
				}
			}
		}

		Class<? extends Tool> selectionTool = contribution.getSelectionTool();
		Class<? extends Tool> creationTool = contribution.getCreationTool();
		PaletteRootFactory factory = new PaletteRootFactory(resourceProviders, creationTool,
				selectionTool);
		PaletteRoot paletteRoot = factory.createPaletteRoot();
		editDomain.setPaletteRoot(paletteRoot);

	}

	private void applyInitializer(PaletteContribution contribution, Entry entry) {
		contribution.applyInitializer(entry);
		for (Entry child : entry.getEntries()) {
			applyInitializer(contribution, child);
		}
	}
}
