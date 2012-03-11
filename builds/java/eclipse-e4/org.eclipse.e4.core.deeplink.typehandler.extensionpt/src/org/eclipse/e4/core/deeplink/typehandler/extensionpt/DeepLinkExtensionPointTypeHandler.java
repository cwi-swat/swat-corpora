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
package org.eclipse.e4.core.deeplink.typehandler.extensionpt;

import java.io.IOException;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.deeplink.api.AbstractDeepLinkTypeHandler;
import org.eclipse.e4.core.deeplink.api.ParameterProcessResults;
import org.eclipse.swt.widgets.Display;


/**
 * Implements a type handler for deeplinks of the following form:
 * <p>
 * deeplink://application/extensionpt/id/action?param1=value1&param2=value2
 * <p>
 * where "id" is an ID attribute defined in the extension point.  See the
 * plugin.xml and the extension point schema for details.
 */
public class DeepLinkExtensionPointTypeHandler extends AbstractDeepLinkTypeHandler {
	private static final String EXTENSION_POINT_INSTANCE_ID_ATTRIBUTE = "id";

	@Override
	public void processDeepLink() throws IOException{
		final ParameterProcessResults[] results = new ParameterProcessResults[] { new ParameterProcessResults() };
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				IConfigurationElement[] congfigElementsFromRegistry = getCongfigElementsFromRegistry(Activator.PLUGIN_ID, Activator.DEEP_LINK_EXT_PT_ID);
				findInstanceHandlerAndExecuteCallback(results, congfigElementsFromRegistry, getParameterMap(), EXTENSION_POINT_INSTANCE_ID_ATTRIBUTE);
			}
		});

		outputResponse(results[0]);
	}
}