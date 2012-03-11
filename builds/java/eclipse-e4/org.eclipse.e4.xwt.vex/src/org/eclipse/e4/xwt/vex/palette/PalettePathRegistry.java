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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.Platform;

/**
 * A class of loading resourcePath and iconsPath from EXTENSION-POINT.
 * 
 * @author jliu
 */
public class PalettePathRegistry {

	public static final String EXTENSION_ID = "org.eclipse.e4.xwt.vex.palettePath";

	public static final String DEFAULT_PATH = "/tools/toolkit.toolpalette";
	public static final String DEFAULT_ICONS_PATH = "/icons";

	private static final String PATH = "path";
	private static final String ICONS_PATH = "iconsPath";

	private PalettePathRegistry() {
	}

	public static String getPath(String editorId) {
		if (editorId == null) {
			return DEFAULT_PATH;
		}
		IConfigurationElement[] configElem = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
		for (IConfigurationElement ce : configElem) {
			IContributor contributor = ce.getContributor();
			if (editorId.equals(contributor.getName())) {
				String attribute = ce.getAttribute(PATH);
				if (attribute != null) {
					return attribute;
				}
			}
		}
		return DEFAULT_PATH;
	}

	public static String getIconsPath(String editorId) {
		IConfigurationElement[] configElem = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
		for (IConfigurationElement ce : configElem) {
			IContributor contributor = ce.getContributor();
			if (editorId.equals(contributor.getName())) {
				String attribute = ce.getAttribute(ICONS_PATH);
				if (attribute != null) {
					return attribute;
				}
			}
		}
		return DEFAULT_ICONS_PATH;
	}
}
