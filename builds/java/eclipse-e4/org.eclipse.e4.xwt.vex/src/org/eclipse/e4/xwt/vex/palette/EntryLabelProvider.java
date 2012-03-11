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

import org.eclipse.e4.xwt.vex.VEXSharedImages;
import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.e4.xwt.vex.toolpalette.util.ToolPaletteHelper;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author yyang
 * 
 */
public class EntryLabelProvider extends LabelProvider {

	public Image getImage(Object object) {
		if (object instanceof Entry) {
			Entry element = (Entry) object;
			String iconPath = element.getIcon();
			if (iconPath != null && iconPath.trim().length() > 0) {
				return VEXSharedImages.get(iconPath);
			}
		}
		return null;
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		if (object instanceof Entry) {
			Entry element = (Entry) object;
			String iconPath = element.getIcon();
			if (iconPath != null && iconPath.trim().length() > 0) {
				return VEXSharedImages.getImageDescriptor(iconPath);
			}
		}
		return null;
	}

	public ImageDescriptor getLargeImageDescriptor(Object object) {
		if (object instanceof Entry) {
			Entry element = (Entry) object;
			String iconPath = element.getLargeIcon();
			if (iconPath != null && iconPath.trim().length() > 0) {
				return VEXSharedImages.getImageDescriptor(iconPath);
			}
		}
		return null;
	}

	public String getText(Object object) {
		if (object instanceof Entry) {
			Entry element = (Entry) object;
			return ToolPaletteHelper.getSimpleName(element);
		}
		return object.toString();
	}

	public String getDescription(Object object) {
		if (object instanceof Entry) {
			Entry element = (Entry) object;
			String description = element.getToolTip();

			if (description != null) {
				return description;
			}
			return ToolPaletteHelper.getSimpleName(element);
		}
		return object.toString();
	}

}
