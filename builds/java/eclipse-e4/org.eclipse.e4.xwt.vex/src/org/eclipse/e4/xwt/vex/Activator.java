/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *     Anaik Trihoreau <anaik@anyware-tech.com> - Bug 274057
 *******************************************************************************/
package org.eclipse.e4.xwt.vex;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.e4.xwt.vex";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	// Bug 274057 - Modification - Start
	/**
	 * Get an image from the local ImageRegistry. If the given Image's path is not already
	 * registered, do it.
	 * 
	 * @param imagePath
	 *        String, path and key identifying the image in the ImageRegistry
	 * 
	 * @return Image or null if nothing corresponds to the given key
	 */
	public static Image getImage(String imagePath)
	{
		return getImage(PLUGIN_ID, imagePath);
	}

	/**
	 * Get an image at the given plug-in relative path from the local ImageRegistry. If the given
	 * Image's path is not already registered, do it.
	 * 
	 * @param pluginId
	 *        the plug-in identifier
	 * @param imagePath
	 *        String, path and key identifying the image in the ImageRegistry
	 * 
	 * @return Image or null if nothing corresponds to the given key
	 */
	public static Image getImage(String pluginId, String imagePath)
	{
		ImageRegistry imageRegistry = getDefault().getImageRegistry();
		String key = pluginId + "_" + imagePath;
		Image result = imageRegistry.get(key);

		if (result == null && imagePath != null)
		{
			ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId,
				imagePath);
			imageRegistry.put(key, descriptor);
			result = imageRegistry.get(key);
		}

		return result;
	}

	/**
	 * Get an image descriptor from the local ImageRegistry. If the given Image's path is not
	 * already registered, do it.
	 * 
	 * @param imagePath
	 *        String, path and key identifying the image in the ImageRegistry
	 * 
	 * @return ImageDescriptor or null if nothing corresponds to the given key
	 */
	public static ImageDescriptor getImageDescriptor(String imagePath)
	{
		return getImageDescriptor(PLUGIN_ID, imagePath);
	}

	/**
	 * Get an image descriptor at the given plug-in relative path from the local ImageRegistry. If
	 * the given Image's path is not already registered, do it.
	 * 
	 * @param pluginId
	 *        the plug-in identifier
	 * @param imagePath
	 *        String, path and key identifying the image in the ImageRegistry
	 * 
	 * @return ImageDescriptor or null if nothing corresponds to the given key
	 */
	public static ImageDescriptor getImageDescriptor(String pluginId, String imagePath)
	{
		ImageRegistry imageRegistry = getDefault().getImageRegistry();
		String key = pluginId + "_" + imagePath;
		ImageDescriptor result = imageRegistry.getDescriptor(key);

		if (result == null && imagePath != null)
		{
			result = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, imagePath);
			imageRegistry.put(key, result);
		}

		return result;
	}
	// Bug 274057 - Modification - End
}
