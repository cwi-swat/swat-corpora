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
package org.eclipse.e4.enterprise.installer;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.metaconfig.Configuration;
import org.eclipse.e4.core.metaconfig.ConfigurationException;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;


public class Activator extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.e4.enterprise.installer";
	
	private static final String CONFIG_TRACKER_CLASSNAME = Configuration.class.getName();
	// The shared instance
	private static Activator plugin;
	private ServiceTracker configTracker;
	private BundleContext context;
	
	public Activator() {
	}
	
	public static void logInfo(String message) {
		Activator.getDefault().getLog().log(new Status(Status.INFO, Activator.getDefault().PLUGIN_ID, message));
	}

	/**
	 * @return the current configuration file's Properties object. May be null
	 *         if the configuration service is not loaded or is not included in
	 *         the application.
	 */
	public Configuration getConfiguration() {
		Configuration configuration = (Configuration) configTracker.getService();
		if (null == configuration)
		{
			throw new ConfigurationException("Null config service: no service registered for :[" + CONFIG_TRACKER_CLASSNAME + "]");				
		}
		return configuration;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.context = context;
		configTracker = new ServiceTracker(context, CONFIG_TRACKER_CLASSNAME, null);
		configTracker.open();
		plugin = this;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
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
