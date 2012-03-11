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
package org.eclipse.e4.xwt.ui.utils;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class ImageManager {

	public static final IPath ICONS_PATH = new Path("/icons/full"); //$NON-NLS-1$

	private static final String NAME_PREFIX = XWTUIPlugin.getDefault().getBundle().getSymbolicName() + ".";
	private static final int NAME_PREFIX_LENGTH = NAME_PREFIX.length();

	private static final String T_OBJ = "obj16"; //$NON-NLS-1$
	private static final String T_OVR = "ovr16"; //$NON-NLS-1$
	private static final String T_WIZBAN = "wizban"; //$NON-NLS-1$
	private static final String T_ELCL = "elcl16"; //$NON-NLS-1$
	private static final String T_DLCL = "dlcl16"; //$NON-NLS-1$
	private static final String T_ETOOL = "etool16"; //$NON-NLS-1$
	private static final String T_EVIEW = "eview16"; //$NON-NLS-1$

	/*
	 * Keys for images available from the Java-UI plug-in image registry.
	 */
	public static final String IMG_ELEMENT = NAME_PREFIX + "Element.png"; //$NON-NLS-1$
	public static final String IMG_EVENT = NAME_PREFIX + "Event.png"; //$NON-NLS-1$
	public static final String IMG_RESOURCES = NAME_PREFIX + "Resources.gif"; //$NON-NLS-1$
	public static final String IMG_TRIGGER = NAME_PREFIX + "Trigger.gif"; //$NON-NLS-1$
	public static final String IMG_PREVIEW = NAME_PREFIX + "Preview.png"; //$NON-NLS-1$
	public static final String IMG_TABLE = NAME_PREFIX + "Table.gif"; //$NON-NLS-1$

	/*
	 * Set of predefined Image Descriptors.
	 */
	public static final ImageDescriptor OBJ_ELEMENT = createManagedFromKey(T_OBJ, IMG_ELEMENT); //$NON-NLS-1$
	public static final ImageDescriptor OBJ_EVENT = createManagedFromKey(T_OBJ, IMG_EVENT); //$NON-NLS-1$
	public static final ImageDescriptor OBJ_RESOURCES = createManagedFromKey(T_OBJ, IMG_RESOURCES); //$NON-NLS-1$
	public static final ImageDescriptor OBJ_TRIGGER = createManagedFromKey(T_OBJ, IMG_TRIGGER); //$NON-NLS-1$
	public static final ImageDescriptor OBJ_PREVIEW = createManagedFromKey(T_OBJ, IMG_PREVIEW); //$NON-NLS-1$
	public static final ImageDescriptor OBJ_TABLE = createManagedFromKey(T_OBJ, IMG_TABLE); //$NON-NLS-1$

	private static ImageDescriptor createManagedFromKey(String prefix, String key) {
		return createManaged(prefix, key.substring(NAME_PREFIX_LENGTH), key);
	}

	private static ImageDescriptor createManaged(String prefix, String name, String key) {
		ImageDescriptor result = create(prefix, name, true);
		JFaceResources.getImageRegistry().put(key, result);
		return result;
	}

	/*
	 * Creates an image descriptor for the given path in a bundle. The path can contain variables like $NL$. If no image could be found, <code>useMissingImageDescriptor</code> decides if either the 'missing image descriptor' is returned or <code>null</code>. Added for 3.1.1.
	 */
	public static ImageDescriptor createImageDescriptor(Bundle bundle, IPath path, boolean useMissingImageDescriptor) {
		URL url = FileLocator.find(bundle, path, null);
		if (url != null) {
			return ImageDescriptor.createFromURL(url);
		}
		if (useMissingImageDescriptor) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
		return null;
	}

	/*
	 * Creates an image descriptor for the given prefix and name in the JDT UI bundle. The path can contain variables like $NL$. If no image could be found, <code>useMissingImageDescriptor</code> decides if either the 'missing image descriptor' is returned or <code>null</code>. or <code>null</code>.
	 */
	private static ImageDescriptor create(String prefix, String name, boolean useMissingImageDescriptor) {
		IPath path = ICONS_PATH.append(prefix).append(name);
		return createImageDescriptor(XWTUIPlugin.getDefault().getBundle(), path, useMissingImageDescriptor);
	}

	/*
	 * Creates an image descriptor for the given prefix and name in the JDT UI bundle. The path can contain variables like $NL$. If no image could be found, the 'missing image descriptor' is returned.
	 */
	private static ImageDescriptor createUnManaged(String prefix, String name) {
		return create(prefix, name, true);
	}

	/**
	 * Returns the image managed under the given key in this registry.
	 * 
	 * @param key
	 *            the image's key
	 * @return the image managed under the given key
	 */
	public static Image get(String key) {
		return JFaceResources.getImageRegistry().get(key);
	}

	public static Image getImage(ImageDescriptor imageDescriptor) {
		return (Image) JFaceResources.getResources().get(imageDescriptor);
	}
}
