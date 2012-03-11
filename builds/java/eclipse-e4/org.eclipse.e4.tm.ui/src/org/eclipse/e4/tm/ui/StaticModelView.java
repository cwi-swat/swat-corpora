/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

public class StaticModelView extends AbstractModelView {

	private ModelContext modelContext = null;

	protected ModelContext getModelContext() {
		if (modelContext == null) {
			modelContext = StaticModelView.getModel(getSite().getId());
		}
		return modelContext;
	}

	private static ModelContext getModel(String id) {
		IExtensionPoint ep = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.ui", "views");
		IExtension[] extensions = ep.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] ces = extensions[i].getConfigurationElements();
			for (int j = 0; j < ces.length; j++) {
				final IConfigurationElement element = ces[j];
				if ("view".equals(element.getName()) && id.equals(element.getAttribute("id"))) {
					final String uri = element.getAttribute("model");
					if (uri != null) {
						return new UriModelContext(null) {
							public EObject getModel() {
								setUri(URI.createURI(uri.indexOf(':') > 0 ? uri : Platform.getBundle(element.getNamespaceIdentifier()).getEntry(uri).toString()));
								return super.getModel();
							}
						};
					}
				}
			}
		}
		return null;
	}
}
