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

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Image manager.
 * 
 * @author yyang
 */
public class VEXSharedImages {
	public static final String[] IMAGE_SUFFIX = new String[] { ".png", ".gif" };

	public static final IPath ICONS_PATH = new Path("icons"); //$NON-NLS-1$
	private static ImageRegistry fgImageRegistry;

	/**
	 * Returns the image managed under the given key in this registry.
	 * 
	 * @param key
	 *            the image's key
	 * @return the image managed under the given key
	 */
	public static Image get(String key) {
		Image image = getImageRegistry().get(key);
		if (image == null) {
			image = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return image;
	}

	public static ImageDescriptor getImageDescriptor(String key) {
		ImageDescriptor des = getImageRegistry().getDescriptor(key);
		if (des == null) {
			des = ImageDescriptor.getMissingImageDescriptor();
		}
		return des;
	}

	private static ImageRegistry getImageRegistry() {
		if (fgImageRegistry == null) {
			fgImageRegistry = new ImageRegistry();
			loadImages();
		}
		return fgImageRegistry;
	}

	private static void loadImages() {
		Bundle bundle = Activator.getDefault().getBundle();
		String prefix = "platform:/plugin/" + bundle.getSymbolicName() + "/" + ICONS_PATH + "/";
		try {
			URL url = FileLocator.find(bundle, ICONS_PATH, null);
			URL iconUrl = FileLocator.toFileURL(url);
			String iconUrlString = iconUrl.toString();

			File iconFolder = new File(new Path(iconUrl.getPath()).toOSString());
			List<File> images = new LinkedList<File>();
			retreiveImages(iconFolder, images);
			for (File file : images) {
				URL fileURL = file.toURI().toURL();
				ImageDescriptor descriptor = ImageDescriptor.createFromURL(fileURL);
				String fileURLString = fileURL.toString();
				if (fileURLString.startsWith(iconUrlString)) {
					String suffix = fileURLString.substring(iconUrlString.length());
					String key = prefix + suffix;
					fgImageRegistry.put(key, descriptor);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void retreiveImages(File icon, List<File> images) {
		if (icon.isDirectory()) {
			for (File file : icon.listFiles()) {
				retreiveImages(file, images);
			}
		} else {
			String name = icon.getName();
			if (name.endsWith(IMAGE_SUFFIX[0]) || name.endsWith(IMAGE_SUFFIX[1])) {
				images.add(icon);
			}
		}
	}

	public static void initialize() {
		getImageRegistry();
	}
}
