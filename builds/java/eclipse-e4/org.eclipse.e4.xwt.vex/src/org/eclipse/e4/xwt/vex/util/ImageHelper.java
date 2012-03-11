/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.vex.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.e4.xwt.vex.palette.PaletteResourceManager;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author BOB
 * 
 */
public class ImageHelper {

	/**
	 * Create ImageDescriptor of given path. Load images from extension registry.
	 */
	public static ImageDescriptor getImageDescriptor(PaletteResourceManager tResourceManager, String iconPath) {
		if (tResourceManager == null || iconPath == null) {
			return null;
		}
		if (iconPath.startsWith("/")) {
			iconPath = iconPath.substring(1);
		}
		URI registryPath = tResourceManager.getIconsPath();
		String newIconPath = registryPath.toString() + iconPath;
		URL url;
		try {
			File file = new File(iconPath);
			if (file.exists()) {
				// try customize component first
				url = file.toURL();
			} else {
				// if don't work, try system component then
				url = new URL(newIconPath);
			}
			ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
			if (imageDescriptor.getImageData() != null) {
				return imageDescriptor;
			} else {
				return null;
			}

		} catch (MalformedURLException e) {
			// if both don't work, return null
			return null;
		}
	}

}
