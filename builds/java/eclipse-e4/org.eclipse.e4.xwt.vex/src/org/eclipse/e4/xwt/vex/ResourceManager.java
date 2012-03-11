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
package org.eclipse.e4.xwt.vex;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class ResourceManager {
	private static String ICONS_PATH = "$nl$/icons/full/"; //$NON-NLS-1$

	// Use IPath and toOSString to build the names to ensure they have the
	// slashes correct
	private final static String OBJ = ICONS_PATH + "obj16/"; //$NON-NLS-1$

	static final String PREFIX_OBJ = Activator.PLUGIN_ID + ".obj16."; //$NON-NLS-1$

	public static final String IMG_OBJ_TOOL_ADD = PREFIX_OBJ + "add_tool.gif"; //$NON-NLS-1$
	public static final String IMG_OBJ_TOOL_DELETE = PREFIX_OBJ + "delete.gif"; //$NON-NLS-1$
	public static final String IMG_OBJ_TOOL_CUSTOM = PREFIX_OBJ + "custom_palette.gif"; //$NON-NLS-1$
	public static final String IMG_OBJ_TOOL_EFACE = PREFIX_OBJ + "eFace16.png"; //$NON-NLS-1$

	public static final String IMG_OBJ_ECLIPSE_ICON = PREFIX_OBJ + "eclipse_icon.png"; //$NON-NLS-1$

	public static final String IMG_ELEMENT = PREFIX_OBJ + "Element.png"; //$NON-NLS-1$
	public static final String IMG_TABLE = PREFIX_OBJ + "Table.gif"; //$NON-NLS-1$
	public static final String IMG_BUSY = PREFIX_OBJ + "Busy.gif"; //$NON-NLS-1$

	public static final String URL_PATH_BUSY = OBJ + "Busy.gif";

	private static ResourceManager manager = new ResourceManager();

	/**
	 * The image registry containing {@link Image images}.
	 */
	private ImageRegistry fgImageRegistry;

	/**
	 * Declare all images
	 */
	private void declareImages() {
		// Ant Editor images
		declareRegistryImage(IMG_OBJ_TOOL_ADD, OBJ + "add_tool.gif"); //$NON-NLS-1$
		declareRegistryImage(IMG_OBJ_TOOL_DELETE, OBJ + "delete.gif"); //$NON-NLS-1$
		declareRegistryImage(IMG_OBJ_TOOL_CUSTOM, OBJ + "custom_palette.gif"); //$NON-NLS-1$
		declareRegistryImage(IMG_OBJ_TOOL_EFACE, OBJ + "eFace16.png"); //$NON-NLS-1$
		declareRegistryImage(IMG_OBJ_ECLIPSE_ICON, OBJ + "eclipse_icon.png"); //$NON-NLS-1$
		declareRegistryImage(IMG_ELEMENT, OBJ + "Element.png"); //$NON-NLS-1$
		declareRegistryImage(IMG_TABLE, OBJ + "Table.gif"); //$NON-NLS-1$
		declareRegistryImage(IMG_BUSY, OBJ + "Busy.gif"); //$NON-NLS-1$
	}

	/**
	 * Declare an Image in the registry table.
	 * 
	 * @param key
	 *            The key to use when registering the image
	 * @param path
	 *            The path where the image can be found. This path is relative to where this plugin class is found (i.e. typically the packages directory)
	 */
	private final void declareRegistryImage(String key, String path) {
		ImageDescriptor desc = ImageDescriptor.getMissingImageDescriptor();
		URL url = getImageURL(path);
		if (url != null) {
			desc = ImageDescriptor.createFromURL(url);
		}
		getImageRegistry().put(key, desc);
	}

	/**
	 * Declare an Image in the registry table.
	 * 
	 * @param key
	 *            The key to use when registering the image
	 * @param path
	 *            The path where the image can be found. This path is relative to where this plugin class is found (i.e. typically the packages directory)
	 */
	public static final URL getImageURL(String path) {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		if (bundle != null) {
			return FileLocator.find(bundle, new Path(path), null);
		}
		return null;
	}

	public ImageRegistry getImageRegistry() {
		if (fgImageRegistry == null) {
			fgImageRegistry = Activator.getDefault().getImageRegistry();
			declareImages();
		}
		return fgImageRegistry;
	}

	public static Image getImage(String key) {
		return manager.getImageRegistry().get(key);
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		ImageRegistry imageRegistry = manager.getImageRegistry();
		return imageRegistry.getDescriptor(path);
	}
}
