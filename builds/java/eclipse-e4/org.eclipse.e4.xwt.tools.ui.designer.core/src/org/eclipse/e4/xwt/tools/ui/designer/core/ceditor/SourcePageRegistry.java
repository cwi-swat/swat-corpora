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
package org.eclipse.e4.xwt.tools.ui.designer.core.ceditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class SourcePageRegistry {

	private static String EXTENSION_ID = "org.eclipse.e4.xwt.tools.ui.designer.core.sourcePages";
	private static String TARGET_ID = "targetId";
	private static String PAGE = "Page";
	private static String PAGE_CLASS = "class";
	private static final ISourcePage[] EMPTY = new ISourcePage[0];

	public static ISourcePage[] getSourcePages(String editorId) {
		if (editorId == null) {
			return EMPTY;
		}
		return loadFromExtensions(editorId);
	}

	private static ISourcePage[] loadFromExtensions(String editorId) {
		IConfigurationElement[] configurationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						EXTENSION_ID);
		List<ISourcePage> pages = new ArrayList<ISourcePage>();
		for (IConfigurationElement element : configurationElements) {
			String attribute = element.getAttribute(TARGET_ID);
			if (!editorId.equals(attribute)) {
				continue;
			}
			IConfigurationElement[] children = element.getChildren(PAGE);
			for (IConfigurationElement child : children) {
				try {
					ISourcePage sourcePage = (ISourcePage) child
							.createExecutableExtension(PAGE_CLASS);
					pages.add(sourcePage);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return pages.toArray(new ISourcePage[pages.size()]);
	}
}
