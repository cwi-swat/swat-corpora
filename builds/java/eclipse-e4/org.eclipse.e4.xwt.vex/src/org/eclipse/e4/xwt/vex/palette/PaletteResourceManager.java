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

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.xwt.vex.Activator;
import org.eclipse.e4.xwt.vex.EditorMessages;
import org.eclipse.e4.xwt.vex.VEXEditor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.IEditorInput;

/**
 * This class is used to provided palette resource and iconsPath for PaletteView. The <code>reourcePath</code> and <code>iconsPath</code> can be registied through "org.eclipse.e4.xwt.vex.palettePath" extension-point.
 * 
 * @author yyang
 */
public class PaletteResourceManager {
	private static final String DEFAULT_PATH = EditorMessages.PaletteResourceManager_toolkit;
	private static final String DEFAULT_ICON_PATH = EditorMessages.PaletteResourceManager_icons;

	public static final String PALETTE_CONTENT_PROVIDER_ID = Activator.PLUGIN_ID + ".paletteContentProvider";
	public static final String GENARATOR = "generator";
	public static final String CLASS = "class";

	private URI resourcePath;
	private URI iconsPath;
	private Resource resource;
	private Resource dynamicResource;
	private Resource customizeResource;
	private VEXEditor host = null;

	/**
	 * Constructor a new {@link PaletteResourceManager} with given editor,
	 * 
	 * see getAdaptor() method at VEXEditor.
	 */
	public PaletteResourceManager(VEXEditor editor) {
		assert host != null;
		host = editor;
	}

	public Resource getResource() {
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(PALETTE_CONTENT_PROVIDER_ID);
		for (IConfigurationElement configurationElement : configurationElements) {
			if (GENARATOR.equals(configurationElement.getName())) {
				// get PaletteContentProvider here
				try {
					IPaletteContentProvider paletteContentProvider = (IPaletteContentProvider) configurationElement.createExecutableExtension(CLASS);
					if (paletteContentProvider != null) {
						ResourceSet rs = new ResourceSetImpl();
						String location = Activator.getDefault().getStateLocation().toPortableString(); // .getBundle().getLocation();
						URI uri = URI.createFileURI(location + "/tools/toolkit.toolpalette");
						Resource newResource = rs.createResource(uri);
						newResource.load(paletteContentProvider.getPaletteInputStream(), null);
						return newResource;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (resource == null) {
			URI modelFilePath = getResourceFile();
			if (modelFilePath != null) {
				ResourceSet resourceSet = new ResourceSetImpl();
				resource = resourceSet.getResource(modelFilePath, true);
			}
		}
		return resource;
	}

	/**
	 * get dynamic palette resource
	 * 
	 * @return
	 */
	public Resource getDynamicResource() {
		if (dynamicResource == null) {
			dynamicResource = new ResourceImpl();
		}
		return dynamicResource;
	}

	/**
	 * get customize palette resource
	 * 
	 * @return
	 */
	public Resource getCustomizeResource() {
		if (customizeResource == null) {
			customizeResource = new ResourceImpl();
		}
		return customizeResource;
	}

	private IProject getCurrentProject() {
		IEditorInput editorInput = host.getEditorInput();
		IFile file = (IFile) editorInput.getAdapter(IFile.class);
		IProject project = file.getProject();
		if (!project.exists()) {
			return null;
		}
		return project;
	}

	public URI getIconsPath() {
		if (iconsPath == null) {
			iconsPath = computeIconsPath();
		}
		return iconsPath;
	}

	private URI computeIconsPath() {
		IProject currentProject = getCurrentProject();
		if (currentProject != null) {
			try {
				currentProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			IFile file = currentProject.getFile(DEFAULT_ICON_PATH);
			if (file.exists()) {
				return URI.createFileURI(file.getLocation().toString());
			}
		}
		// Loading iconsPath from Extension Registry.
		String pluginId = host.getEditorSite().getPluginId();
		String path = PalettePathRegistry.getIconsPath(pluginId);
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return URI.createPlatformPluginURI(pluginId + path, false);
	}

	public URI getResourceFile() {
		if (resourcePath == null) {
			resourcePath = computeResourcePath();
		}
		return resourcePath;
	}

	private URI computeResourcePath() {
		IProject currentProject = getCurrentProject();
		if (currentProject != null) {
			try {
				currentProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			IFile file = currentProject.getFile(DEFAULT_PATH);
			if (file.exists()) {
				return URI.createFileURI(file.getLocation().toString());
			}
		}
		// Loading path from Extension Registry.
		String pluginId = host.getEditorSite().getPluginId();
		String path = PalettePathRegistry.getPath(pluginId);
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return URI.createPlatformPluginURI(pluginId + path, false);
	}

	public void saveToolDefinition() {
		IProject currentProject = getCurrentProject();
		if (currentProject != null) {
			String currentProjectPath = currentProject.getName();
			String filePath = currentProjectPath + "/" + DEFAULT_PATH;
			try {
				resource.setURI(URI.createPlatformResourceURI(filePath, false));
				resource.save(null);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
