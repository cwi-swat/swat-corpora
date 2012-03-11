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
package org.eclipse.e4.core.deeplink.api;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.e4.core.deeplink.handler.HttpServiceTracker;
import org.eclipse.equinox.http.registry.HttpContextExtensionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.e4.core.deeplink.handler";
	public static final String DEEP_LINK_EXT_PT_ID = "deepLinkTypeHandler";

	// The shared instance
	private static Activator plugin;

	protected HttpServiceTracker httpServiceTracker;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin=this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

        // RAP [fappel]: ensure that the rap http context was loaded before
        //               the mapping of servlets from branding takes place
        String serviceName = HttpContextExtensionService.class.getName();
        ServiceTracker httpContextExtensionServiceTracker
          = new ServiceTracker( context, serviceName, null )
        {
          public Object addingService( final ServiceReference reference ) {
            Object result = super.addingService( reference );
            httpServiceTracker = new HttpServiceTracker(context);
            httpServiceTracker.open();
            return result;
          }
        };
        httpContextExtensionServiceTracker.open();
//		/*
//		 * DO NOT RUN ANY OTHER CODE AFTER THIS LINE.  If you do, then you are
//		 * likely to cause a deadlock in class loader code.  Please see Bug 86450
//		 * for more information.
//		 */
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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

}
