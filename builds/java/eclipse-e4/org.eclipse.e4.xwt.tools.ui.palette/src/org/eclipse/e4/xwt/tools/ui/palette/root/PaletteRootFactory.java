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
package org.eclipse.e4.xwt.tools.ui.palette.root;

import java.util.Collection;

import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.page.resources.IPaletteContentProvider;
import org.eclipse.e4.xwt.tools.ui.palette.page.resources.IPaletteLabelProvider;
import org.eclipse.e4.xwt.tools.ui.palette.page.resources.IPaletteResourceProvider;
import org.eclipse.e4.xwt.tools.ui.palette.request.EntryCreationFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PaletteRootFactory {

	private Collection<IPaletteResourceProvider> resourceProviders;
	private Class<? extends Tool> creationToolClass;
	private Class<? extends Tool> selectionToolClass;

	public PaletteRootFactory(Collection<IPaletteResourceProvider> resourceProviders,
			Class<? extends Tool> creationToolClass, Class<? extends Tool> selectionToolClass) {
		this.resourceProviders = resourceProviders;
		this.creationToolClass = creationToolClass;
		this.selectionToolClass = selectionToolClass;
	}

	public PaletteRoot createPaletteRoot() {
		PaletteRoot palette = new PaletteRoot();
		if (resourceProviders == null) {
			return palette;
		}
		{// create selection group.
			PaletteGroup paletteGroup = new PaletteGroup("Selection Group");

			SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
			paletteGroup.add(selectionToolEntry);
			if (selectionToolClass != null) {
				selectionToolEntry.setToolClass(selectionToolClass);
			}

			MarqueeToolEntry marqueeToolEntry = new MarqueeToolEntry();
			marqueeToolEntry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
			paletteGroup.add(marqueeToolEntry);

			palette.add(paletteGroup);
			palette.setDefaultEntry(selectionToolEntry);
		}

		for (IPaletteResourceProvider resourceProvider : resourceProviders) {
			Resource resource = resourceProvider.getPaletteResource();
			if (resource != null) {
				PaletteContainer rootDrawer = createRootDrawer(resource, resourceProvider);
				palette.addAll(rootDrawer.getChildren());
			}
		}
		return palette;
	}

	private PaletteContainer createRootDrawer(Resource resource, IPaletteResourceProvider provider) {
		PaletteDrawer rootDrawer = createPaletteDrawer("Root");
		IPaletteContentProvider contentProvider = provider.getContentProvider();
		IPaletteLabelProvider labelProvider = provider.getLabelProvider();
		createPaletteStructure(rootDrawer, contentProvider, labelProvider, resource);
		return rootDrawer;
	}

	private void createPaletteStructure(PaletteDrawer parent,
			IPaletteContentProvider contentProvider, IPaletteLabelProvider labelProvider,
			Object source) {
		if (parent == null || contentProvider == null || labelProvider == null || source == null) {
			return;
		}
		Object[] children = contentProvider.getChildren(source);
		String name = labelProvider.getName(source);
		String description = labelProvider.getToolTip(source);
		ImageDescriptor iconSmall = labelProvider.getSmallIcon(source);
		ImageDescriptor iconLarge = labelProvider.getLargeIcon(source);

		if (name != null && source instanceof Entry) {
			if (children.length == 0) {
				EntryCreationFactory creationFactory = new EntryCreationFactory((Entry) source);
				CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(name,
						description, source, creationFactory, iconSmall, iconLarge);
				if (creationToolClass != null) {
					component.setToolClass(creationToolClass);
				}
				parent.add(component);
			} else {
				PaletteDrawer groupDrawer = createPaletteDrawer(name);
				groupDrawer.setSmallIcon(iconSmall);
				groupDrawer.setLargeIcon(iconLarge);
				groupDrawer.setDescription(description);
				parent.add(groupDrawer);
				groupDrawer.setParent(parent);
				parent = groupDrawer;
			}
		}
		// Create single one firstly.
		for (Object child : children) {
			Object[] childNodes = contentProvider.getChildren(child);
			if (childNodes == null || childNodes.length == 0) {
				createPaletteStructure(parent, contentProvider, labelProvider, child);
			}
		}
		for (Object child : children) {
			Object[] childNodes = contentProvider.getChildren(child);
			if (childNodes != null && childNodes.length != 0) {
				createPaletteStructure(parent, contentProvider, labelProvider, child);
			}
		}
	}

	/**
	 * Create a default Drawer.
	 */
	private PaletteDrawer createPaletteDrawer(String name) {
		PaletteDrawer componentsDrawer = new PaletteDrawerEx(name);
		componentsDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		return componentsDrawer;
	}
}
