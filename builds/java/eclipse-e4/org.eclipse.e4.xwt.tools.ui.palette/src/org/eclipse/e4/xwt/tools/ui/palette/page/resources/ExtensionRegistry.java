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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ExtensionRegistry {
	public static final String EXTENSION_ID = "org.eclipse.e4.xwt.tools.ui.palette.resourceProvider";

	public static final String PROVIDER = "provider";
	public static final String RESOURCE = "resource";

	public static final String EDITOR_ID = "editorId";
	public static final String CLASS = "class";
	public static final String URI = "uri";

	private static List<IPaletteResourceProvider> registry = new ArrayList<IPaletteResourceProvider>();

	public static List<IPaletteResourceProvider> allResourceExtensions(IEditorPart editorPart) {
		String id = editorPart.getSite().getId();

		List<IPaletteResourceProvider> providers = new ArrayList<IPaletteResourceProvider>();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(EXTENSION_ID);
		for (IConfigurationElement element : configurationElements) {
			String editorId = element.getAttribute(EDITOR_ID);
			if (id == null || id.equals(editorId)) {
				if (PROVIDER.equals(element.getName())) {
					try {
						Object executable = element.createExecutableExtension(CLASS);
						if (executable instanceof IPaletteResourceProvider) {
							providers.add((IPaletteResourceProvider) executable);
						}
					} catch (CoreException e) {
					}
				} else if (RESOURCE.equals(element.getName())) {
					String uri = element.getAttribute(URI);

					String npId = element.getNamespaceIdentifier();

					ResourceContentProvider provider = new ResourceContentProvider(uri, npId);
					providers.add(provider);
				}
			}
		}
		providers.addAll(registry);
		return providers;
	}

	public static void registerPaletteResourceProvider(IPaletteResourceProvider provider) {
		registry.add(provider);
	}
}
