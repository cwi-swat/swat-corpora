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
package org.eclipse.e4.xwt.tools.ui.palette.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ImageTools {

	public static ImageDescriptor getImageDescriptor(String imagePath) {
		if (imagePath == null) {
			return null;
		}
		if (imagePath.startsWith("platform:/plugin/")) {
			imagePath = imagePath.substring("platform:/plugin/".length());
			int index = imagePath.indexOf("/");
			if (index != -1) {
				String pluginId = imagePath.substring(0, index);
				imagePath = imagePath.substring(index + 1);
				return AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, imagePath);
			}
		}
		return null;
	}
}
