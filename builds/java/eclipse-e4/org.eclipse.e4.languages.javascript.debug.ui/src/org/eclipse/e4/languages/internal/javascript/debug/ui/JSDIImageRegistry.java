/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.internal.javascript.debug.ui;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Image registry for shared images used in JSDI
 * 
 * @since 1.0
 */
public final class JSDIImageRegistry {

	/** 
	 * The image registry containing <code>Image</code>s and <code>ImageDescriptor</code>s.
	 */
	private static ImageRegistry imageRegistry;
	private static ImageDescriptorRegistry descRegistry;
	
	private static String ICONS_PATH = "$nl$/icons/"; //$NON-NLS-1$
	
	/**
	 * Icon paths
	 */
	private final static String ELCL = ICONS_PATH + "elcl16/"; //enabled - size 16x16 //$NON-NLS-1$
	private final static String OVR = ICONS_PATH + "ovr16/"; //overlays
	private final static String DLCL = ICONS_PATH + "dlcl16/"; //disabled icons
	
	/**
	 * Declare all images
	 */
	private static void declareImages() {
		//ELCL
		declareRegistryImage(ISharedImages.IMG_SCRIPT, ELCL + "script.gif");
		declareRegistryImage(ISharedImages.IMG_LOCAL_VAR, ELCL + "localvariable.gif");
		declareRegistryImage(ISharedImages.IMG_CONNECT, ELCL + "connect.gif");
		declareRegistryImage(ISharedImages.IMG_BRKP, ELCL + "brkp_obj.gif");
		declareRegistryImage(ISharedImages.IMG_SCRIPTBRKP, ELCL + "scriptbrkp_obj.gif");
		
		//DLCL
		declareRegistryImage(ISharedImages.IMG_BRKP_DISABLED, DLCL + "brkpd_obj.gif");
		
		//overlays
		declareRegistryImage(ISharedImages.IMG_OVR_CONDITIONAL, OVR + "conditional_ovr.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_CONDITIONAL_DISABLED, OVR + "conditional_ovr_disabled.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_ENTRY, OVR + "entry_ovr.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_ENTRY_DISABLED, OVR + "entry_ovr_disabled.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_EXIT, OVR + "exit_ovr.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_EXIT_DISABLED, OVR + "exit_ovr_disabled.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_INSTALLED, OVR + "installed_ovr.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_INSTALLED_DISABLED, OVR + "installed_ovr_disabled.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_SCOPED, OVR + "scoped_ovr.gif");
		declareRegistryImage(ISharedImages.IMG_OVR_SCOPED_DISABLED, OVR + "scoped_ovr_disabled.gif");
	}
	
	/**
	 * Declare an Image in the registry table.
	 * @param key 	The key to use when registering the image
	 * @param path	The path where the image can be found. This path is relative to where
	 *				this plugin class is found (i.e. typically the packages directory)
	 */
	private final static void declareRegistryImage(String key, String path) {
		ImageDescriptor desc = ImageDescriptor.getMissingImageDescriptor();
		Bundle bundle = Platform.getBundle("org.eclipse.e4.languages.javascript.debug.ui");
		URL url = null;
		if (bundle != null){
			url = FileLocator.find(bundle, new Path(path), null);
			if(url != null) {
				desc = ImageDescriptor.createFromURL(url);
			}
		}
		imageRegistry.put(key, desc);
	}
	
	/**
	 * Returns the shared image for the given key or <code>null</code>.
	 * @param key
	 * @return the requested image or <code>null</code> if the image does not exist
	 */
	public static Image getSharedImage(String key) {
		if (imageRegistry == null) {
			initializeImageRegistry();
		}
		return imageRegistry.get(key);
	}
	
	/**
	 * Initializes the registry if it has not been already
	 * @return the initialized registry
	 */
	private synchronized static ImageRegistry initializeImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry(PlatformUI.getWorkbench().getDisplay());
			declareImages();
		}
		return imageRegistry;
	}
	
	/**
	 * Creates the image from the descriptor and caches it for proper disposal
	 * @param descriptor
	 * @return
	 */
	public static Image getImage(ImageDescriptor descriptor) {
		if(descRegistry == null) {
			descRegistry = new ImageDescriptorRegistry();
		}
		return descRegistry.get(descriptor);
	}
	
	/**
	 * Disposes the image registry
	 */
	public static void dispose() {
		if(imageRegistry != null) {
			imageRegistry.dispose();
		}
		if(descRegistry != null) {
			descRegistry.dispose();
		}
	}
	
	/**
	 * Constructor
	 */
	private JSDIImageRegistry() {
		// no direct instantiation
	}
}
