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

import java.util.List;

import org.eclipse.e4.xwt.vex.EditorMessages;
import org.eclipse.e4.xwt.vex.VEXEditor;
import org.eclipse.e4.xwt.vex.palette.customize.CustomizeComponentFactory;
import org.eclipse.e4.xwt.vex.palette.customize.model.CustomizeComponent;
import org.eclipse.e4.xwt.vex.palette.part.ToolPaletteDrawer;
import org.eclipse.e4.xwt.vex.toolpalette.ContextType;
import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPaletteFactory;
import org.eclipse.e4.xwt.vex.util.ImageHelper;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;

/**
 * Utility class that can create a GEF Palette.
 * 
 * @see #createPalette()
 */
public class PaletteRootFactory {

	/**
	 * PaletteResourceManager can provider resourcePath and iconsPath.
	 */
	private PaletteResourceManager trManager;

	public PaletteRootFactory(PaletteResourceManager trManager) {
		this.trManager = trManager;

	}

	/**
	 *Create a new PaletteRoot.
	 */
	public PaletteRoot createPaletteRoot() {
		PaletteRoot palette = new PaletteRoot();
		Resource resource = trManager.getResource();
		if (resource != null) {
			/*
			 * ToolPalette toolPalette = (ToolPalette) resource.getContents().get(0); EList<Entry> entries = toolPalette.getEntries(); // add dynamic entry. Entry dynamicEntry = ToolPaletteFactory.eINSTANCE.createEntry(); dynamicEntry.setName(EditorMessages.PaletteRootFactory_Dynamic); dynamicEntry.setContext(ContextType.XML_TAG); entries.add(dynamicEntry); dynamicEntry.getEntries().add(ToolPaletteFactory.eINSTANCE.createEntry());
			 * 
			 * // add custom entry. Entry customEntry = ToolPaletteFactory.eINSTANCE.createEntry(); customEntry.setName(EditorMessages.PaletteRootFactory_Customize); customEntry.setContext(ContextType.XML_TAG); entries.add(customEntry);
			 * 
			 * List<CustomizeComponent> result = CustomizeComponentFactory.loadCustomizeComponents(); if (result.isEmpty()) { customEntry.getEntries().add(ToolPaletteFactory.eINSTANCE.createEntry()); } for (CustomizeComponent customizeComponent : result) { Entry subEntry = ToolPaletteFactory.eINSTANCE.createEntry(); subEntry.setName(customizeComponent.getName()); subEntry.setScope(customizeComponent.getScope()); subEntry.setIcon(customizeComponent.getIcon()); subEntry.setLargeIcon(customizeComponent.getLargeIcon()); subEntry.setToolTip(customizeComponent.getTooptip()); subEntry.setContent(customizeComponent.getContent()); subEntry.setContext(ContextType.XML_TAG); // add sub entry customEntry.getEntries().add(subEntry); }
			 */

			PaletteContainer createShapesDrawer = createShapesDrawer(resource);
			palette.addAll(createShapesDrawer.getChildren());
		}

		return palette;
	}

	/**
	 * Create the "Shapes" drawer.
	 * 
	 * @param resource
	 */
	private PaletteContainer createShapesDrawer(Resource resource) {
		PaletteDrawer componentsDrawer = createPaletteDrawer(EditorMessages.PaletteRootFactory_Shapes);
		if (trManager != null) {
			EntryContentProvider contentProvider = new EntryContentProvider();
			EntryLabelProvider labelProvider = new EntryLabelProvider();
			createToolPaletteStructure(componentsDrawer, contentProvider, labelProvider, resource);
		}
		
		//  Bug 274057 - Modification - Start
		CustomWidgetDrawer.addCustomWidgets(componentsDrawer);
		//  Bug 274057 - Modification - End
		return componentsDrawer;
	}

	/**
	 *Create a default Drawer.
	 */
	private PaletteDrawer createPaletteDrawer(String name) {
		PaletteDrawer componentsDrawer = new ToolPaletteDrawer(name);
		componentsDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
		return componentsDrawer;
	}

	/**
	 * Create Tools.
	 * 
	 * @param parentDrawer
	 * @param contentProvider
	 * @param labelProvider
	 * @param resource
	 */
	private void createToolPaletteStructure(PaletteDrawer parentDrawer, EntryContentProvider contentProvider, EntryLabelProvider labelProvider, Object resource) {

		Object[] children = contentProvider.getElements(resource);
		String name = labelProvider.getText(resource);
		if (name == null) {
			return;
		}
		String description = labelProvider.getDescription(resource);
		ImageDescriptor image = getImageDescriptor(resource);
		if (image == null) {
			image = labelProvider.getImageDescriptor(resource);
		}
		ImageDescriptor largeImage = getLargeImageDescriptor(resource);
		if (largeImage == null) {
			largeImage = labelProvider.getLargeImageDescriptor(resource);
		}
		if (children.length == 0) {
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(name, description, resource, new SimpleFactory(resource.getClass()), image, largeImage);
			component.setDescription(description);
			parentDrawer.add(component);
		} else {
			if (resource instanceof Entry) {
				PaletteDrawer componentsDrawer = createPaletteDrawer(name);
				componentsDrawer.setSmallIcon(image);
				componentsDrawer.setLargeIcon(largeImage);
				componentsDrawer.setDescription(description);
				parentDrawer.add(componentsDrawer);
				componentsDrawer.setParent(parentDrawer);
				// if (EditorMessages.PaletteRootFactory_Dynamic.equals(name) || EditorMessages.PaletteRootFactory_Customize.equals(name)) {
				// componentsDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
				// }

				for (Object child : children) {
					createToolPaletteStructure(componentsDrawer, contentProvider, labelProvider, child);
				}

			} else {
				for (Object child : children) {
					createToolPaletteStructure(parentDrawer, contentProvider, labelProvider, child);
				}
			}
		}
	}

	/**
	 * Create ImageDescriptor of given object. Load images from extension registry.
	 */
	private ImageDescriptor getImageDescriptor(Object source) {
		if (source instanceof Entry) {
			return ImageHelper.getImageDescriptor(trManager, ((Entry) source).getIcon());
		}
		return null;
	}

	/**
	 *Create Large ImageDescriptor of given object. Load images from extension registry.
	 */
	private ImageDescriptor getLargeImageDescriptor(Object source) {
		if (source instanceof Entry) {
			return ImageHelper.getImageDescriptor(trManager, ((Entry) source).getLargeIcon());
		}
		return null;
	}

	/**
	 * Creates the PaletteRoot and adds all palette elements. Use this factory method to create a new palette for your graphical editor.
	 * 
	 * @param editorPart
	 * 
	 * @return a new PaletteRoot
	 */
	public static PaletteRoot createPalette(IEditorPart editorPart) {
		PaletteRoot palette = new PaletteRoot();
		if (!(editorPart instanceof VEXEditor)) {
			return palette;
		}
		PaletteResourceManager trManager = (PaletteResourceManager) editorPart.getAdapter(PaletteResourceManager.class);
		PaletteRootFactory paletteFactory = new PaletteRootFactory(trManager);
		return paletteFactory.createPaletteRoot();
	}

	public static PaletteRoot createPaletteByResourceManager(PaletteResourceManager resourceManager) {
		PaletteRootFactory paletteFactory = new PaletteRootFactory(resourceManager);
		return paletteFactory.createPaletteRoot();
	}

	public static PaletteRoot createDynamicPalette(IEditorPart editorPart) {
		PaletteRoot palette = new PaletteRoot();
		if (!(editorPart instanceof VEXEditor)) {
			return palette;
		}
		PaletteResourceManager trManager = (PaletteResourceManager) editorPart.getAdapter(PaletteResourceManager.class);
		PaletteRootFactory paletteFactory = new PaletteRootFactory(trManager);
		return paletteFactory.createDynamicPaletteRoot();
	}

	public PaletteRoot createDynamicPaletteRoot() {
		PaletteRoot palette = new PaletteRoot();
		PaletteGroup dynamicGroup = new PaletteGroup(EditorMessages.VEXEditor_Dynamic);
		palette.add(dynamicGroup);
		return palette;
	}

	public static PaletteRoot createCustomizePalette(IEditorPart editorPart) {
		PaletteRoot palette = new PaletteRoot();
		if (!(editorPart instanceof VEXEditor)) {
			return palette;
		}
		PaletteResourceManager trManager = (PaletteResourceManager) editorPart.getAdapter(PaletteResourceManager.class);
		PaletteRootFactory paletteFactory = new PaletteRootFactory(trManager);
		return paletteFactory.createCustomizePaletteRoot();
	}

	public PaletteRoot createCustomizePaletteRoot() {
		PaletteRoot palette = new PaletteRoot();

		PaletteGroup customizeGroup = new PaletteGroup("Customize");
		Resource resource = trManager.getCustomizeResource();
		if (resource != null) {
			List<CustomizeComponent> result = CustomizeComponentFactory.loadCustomizeComponents();
			for (CustomizeComponent customizeComponent : result) {
				Entry customizeEntry = ToolPaletteFactory.eINSTANCE.createEntry();
				customizeEntry.setName(customizeComponent.getName());
				customizeEntry.setScope(customizeComponent.getScope());
				String iconString = customizeComponent.getIcon();
				if (iconString != null && iconString.length() > 0) {
					customizeEntry.setIcon(iconString);
				}
				iconString = customizeComponent.getLargeIcon();
				if (iconString != null && iconString.length() > 0) {
					customizeEntry.setLargeIcon(iconString);
				}
				customizeEntry.setToolTip(customizeComponent.getTooptip());
				customizeEntry.setContent(customizeComponent.getContent());
				customizeEntry.setContext(ContextType.XML_TAG);
				CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(customizeEntry.getName(), customizeEntry.getToolTip(), customizeEntry, new SimpleFactory(resource.getClass()), ImageHelper.getImageDescriptor(trManager, customizeEntry.getIcon()), ImageHelper.getImageDescriptor(trManager, customizeEntry.getLargeIcon()));

				customizeGroup.add(component);
			}
		}
		palette.add(customizeGroup);
		return palette;
	}

}