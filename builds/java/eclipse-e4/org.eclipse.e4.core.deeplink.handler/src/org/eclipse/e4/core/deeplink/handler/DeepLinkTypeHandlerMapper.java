/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.core.deeplink.handler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.deeplink.api.AbstractDeepLinkTypeHandler;
import org.eclipse.e4.core.deeplink.api.Activator;


/**
 * A function object that computes a Map linking the handlerType identifiers
 * to instances of {@link AbstractDeepLinkTypeHandler} using the extension 
 * point registry.
 */
public class DeepLinkTypeHandlerMapper {
	
	Map<String, AbstractDeepLinkTypeHandler> handlerMap = new HashMap<String, AbstractDeepLinkTypeHandler>();
	
	public DeepLinkTypeHandlerMapper(IExtensionRegistry extensionRegistry) {
		if(extensionRegistry == null) {
			throw new IllegalArgumentException("IExtensionResgistry passed to DeepLinkTypeHandlerMapper was null");
		}
		
		IConfigurationElement[] elements = extensionRegistry.getConfigurationElementsFor(Activator.PLUGIN_ID,
						Activator.DEEP_LINK_EXT_PT_ID);
		
		for (IConfigurationElement element : elements) {
			String name = element.getAttribute("name");
			try {
				AbstractDeepLinkTypeHandler handler = (AbstractDeepLinkTypeHandler) element.createExecutableExtension("class");
				handlerMap.put(name, handler);
			} catch (CoreException e) {
				Activator.getDefault().getLog().log(
						new Status(Status.ERROR, Activator.PLUGIN_ID, "Incorrect deep link extension for: " + name, e));
				continue;
			}
		}
	}

	public Map<String, AbstractDeepLinkTypeHandler> getMap() {
		return handlerMap;
	}

}
