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
package org.eclipse.e4.xwt.tools.ui.palette.page.resources;

import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.tools.ImageTools;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * entry label provider class (tree structure)
 * 
 * 
 * @author jliu (jin.liu@soyatec.com)
 */
public class EntryLabelProvider implements IPaletteLabelProvider {

	public String getSimpleName(Entry entry) {
		String name = entry.getName();
		if (name == null) {
			return null;
		}
		int index = name.lastIndexOf('.');
		if (index != -1) {
			name = name.substring(index + 1);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.palette.page.resources.IPaletteLabelProvider#getContent
	 * (java.lang.Object)
	 */
	public String getContent(Object obj) {
		if (obj instanceof Entry) {
			return ((Entry) obj).getContent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.palette.page.resources.IPaletteLabelProvider#getLargeIcon
	 * (java.lang.Object)
	 */
	public ImageDescriptor getLargeIcon(Object obj) {
		if (obj instanceof Entry) {
			String largeIcon = ((Entry) obj).getLargeIcon();
			if (largeIcon != null && largeIcon.trim().length() > 0) {
				return ImageTools.getImageDescriptor(largeIcon);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.palette.page.resources.IPaletteLabelProvider#getName
	 * (java.lang.Object)
	 */
	public String getName(Object obj) {
		if (obj instanceof Entry) {
			return getSimpleName((Entry) obj);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.palette.page.resources.IPaletteLabelProvider#getSmallIcon
	 * (java.lang.Object)
	 */
	public ImageDescriptor getSmallIcon(Object obj) {
		if (obj instanceof Entry) {
			String icon = ((Entry) obj).getIcon();
			if (icon != null && icon.trim().length() > 0) {
				return ImageTools.getImageDescriptor(icon);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.palette.page.resources.IPaletteLabelProvider#getTooltip
	 * (java.lang.Object)
	 */
	public String getToolTip(Object obj) {
		if (obj instanceof Entry) {
			return ((Entry) obj).getToolTip();
		}
		return null;
	}
}
