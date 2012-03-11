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
package org.eclipse.e4.emf.ecore.javascript;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.emf.javascript";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private Map<URI, URI> genmodelUriMap = null;

	public void addGenmodelUris(Map<URI, URI> uriMap) {
		if (genmodelUriMap == null) {
			initGenmodelUriMap();
		}
		uriMap.putAll(genmodelUriMap);
	}

	private void initGenmodelUriMap() {
		genmodelUriMap = new HashMap<URI, URI>();

		IExtensionPoint ep = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.emf.ecore.generated_package");
		IExtension[] extensions = ep.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			for (IConfigurationElement ces: extensions[i].getConfigurationElements()) {
				String name = ces.getName();
				if ("package".equals(name)) {
					
//		            uri="http://www.eclipse.org/e4/tm/widgets.ecore" 
//		            class="org.eclipse.e4.tm.widgets.impl.WidgetsPackageImpl"
//		            genModel="model/tm.genmodel"/>

					URI uri =  URI.createURI(ces.getAttribute("uri"));
					String genModel = ces.getAttribute("genModel");
					URI pluginUri = URI.createPlatformPluginURI("/" + extensions[i].getNamespaceIdentifier() + "/" + genModel, true);

					URI parentFolderUri = JavascriptSupport.createParentFolderUri(uri);
					URI pluginParentFolderUri = JavascriptSupport.createParentFolderUri(pluginUri);
					addUriMapping(genmodelUriMap, parentFolderUri, pluginParentFolderUri);
				}
			}
		}
	}

	private void addUriMapping(Map<URI, URI> uriMap, URI source, URI target) {
		URI existingTarget = uriMap.get(source);
		if (existingTarget == null) {
			uriMap.put(source, target);
		} else if (! target.equals(existingTarget)) {
			getLog().log(new Status(Status.WARNING, PLUGIN_ID, "Tried to map " + source + " to " + target + ", but it already maps to " + existingTarget));
		}
	}
}
