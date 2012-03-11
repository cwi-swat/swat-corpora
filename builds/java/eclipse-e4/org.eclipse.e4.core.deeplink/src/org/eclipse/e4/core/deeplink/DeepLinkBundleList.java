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
package org.eclipse.e4.core.deeplink;

import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * This class is responsible for starting the appropriate bundles required by 
 * DeepLinking's HttpServlets.
 */
public class DeepLinkBundleList {

	private static final String EQUINOX_HTTP_REGISTRY = "org.eclipse.equinox.http.registry";
	private static final String EQUINOX_HTTP_SERVLET = "org.eclipse.equinox.http.servlet";
	private static final String EQUINOX_HTTP_JETTY = "org.eclipse.equinox.http.jetty";
	private static final String DEEPLINK_HANDLER = "org.eclipse.e4.core.deeplink.handler";
	private BundleContext context;

	String[] loadTheseBundles = new String[] {
			DEEPLINK_HANDLER,
			EQUINOX_HTTP_JETTY,
			EQUINOX_HTTP_SERVLET,
			EQUINOX_HTTP_REGISTRY
	};
	
	public DeepLinkBundleList(BundleContext context) {
		this.context = context;
	}
	
	public void startupBundlesForHttpServlets() throws BundleException {		
		int startedBundles = 0;
		boolean deepLinkPresent = false;
		
		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			for (String bundleName : loadTheseBundles) {
				if (bundleName.equals(bundle.getSymbolicName())) {
					bundle.stop();
				}
			}
		}
		for (Bundle bundle : bundles) {
			for (String bundleName : loadTheseBundles) {
				if (bundleName.equals(bundle.getSymbolicName())) {
					logStarting(bundle);
					bundle.start();
					
					if (bundleName.contains("deeplink")) {
						deepLinkPresent = true;
					}
					++startedBundles;
				}
			}
		}
		if (deepLinkPresent && startedBundles > 0 && startedBundles < loadTheseBundles.length) {
			String bundleNames = "";
			for (String bundleName : loadTheseBundles) {
				bundleNames += bundleName + " ";
			}
			throw new BundleException("Deep Linking present: Expected to load: [" + bundleNames + "]");
		}
	}

	private void logStarting(Bundle bundle) {
		Activator activator = Activator.getDefault();
		if (activator != null) {
			activator.getLog().log(
				new Status(Status.INFO, Activator.PLUGIN_ID,
						"Starting: " + bundle.getSymbolicName()));
		}
	}

}
