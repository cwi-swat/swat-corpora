/*******************************************************************************
 * Copyright (c) 2010 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.springframework.internal;

import java.util.Properties;

import org.eclipse.e4.xwt.IXWTInitializer;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.springframework.DefaultBeanNameProvider;
import org.eclipse.e4.xwt.springframework.SpringCLRFactory;
import org.eclipse.e4.xwt.springframework.internal.utils.StringUtils;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.context.ApplicationContext;

/**
 * OSGi {@link BundleActivator} used to store the {@link BundleContext} used by
 * the {@link ApplicationContextTracker}.
 * 
 */
public class Activator implements BundleActivator, IXWTInitializer {

	/**
	 * Lazy bean property name configuration
	 */
	private static final String LAZY_BEAN_PROPERTY_NAME = "org.eclipse.e4.xwt.springclrfactory.lazy_bean";

	/**
	 * Lazy property name configuration
	 */
	private static final String LAZY_PROPERTY_NAME = "org.eclipse.e4.xwt.springclrfactory.lazy";

	/**
	 * Timeout property name configuration.
	 */
	private static final String TIMEOUT_PROPERTY_NAME = "org.eclipse.e4.xwt.springclrfactory.timeout";

	/**
	 * (Default) bundle property name configuration.
	 */
	private static final String BUNDLE_PROPERTY_NAME = "org.eclipse.e4.xwt.springclrfactory.bundle";

	/**
	 * Default value for timeout.
	 */
	public static int DEFAULT_TIMEOUT = 5000;

	/**
	 * The singleton Activator.
	 */
	private static Activator activator;

	/**
	 * OSGi {@link ServiceTracker} to retrieve the Eclipse OSGi service
	 * {@link DebugOptions}.
	 */
	private ServiceTracker debugTracker = null;

	/**
	 * The Bundle context.
	 */
	private BundleContext bundleContext;

	/**
	 * The timeout used to retrieve Spring {@link ApplicationContext}. See
	 * {@link ApplicationContextTracker}.
	 */
	private int timeout = DEFAULT_TIMEOUT;

	/**
	 * The default bundle which contains Spring XML files which declare CLR bean
	 * declaration.
	 */
	private String defaultBundle = null;

	/**
	 * True if {@link XWT#setCLRFactory(org.eclipse.e4.xwt.ICLRFactory)} must be
	 * filled with {@link SpringCLRFactory#INSTANCE} and false otherwise.
	 */
	private boolean lazy = false;

	public boolean isLazy() {
		return lazy;
	}

	private boolean lazyBean;

	private boolean initialized;

	public Activator() {
		Activator.activator = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		this.bundleContext = bundleContext;
		initialized = false;
		try {
			if (DebugHelper.DEBUG) {
				DebugHelper.log("BEGIN Activator#start");
			}

			XWT.addInitializer(this);
			// Load lazy, timeout, bundle properties.
			loadProperties();

			if (lazyBean) {
				SpringCLRFactory.INSTANCE
						.setBeanNameProvider(DefaultBeanNameProvider.INSTANCE);
				if (DebugHelper.DEBUG) {
					DebugHelper
							.log("Use DefaultBeanNameProvider.INSTANCE to retrieve bean parameter with XWT file name. XWT file can use syntax x:ClassFactory=\"org.eclipse.e4.xwt.springframework.SpringCLRFactory.INSTANCE bundle=<myBundle>\"",
									1);
				}
			}

			XWT.checkInitialization();
		} finally {
			if (!lazy) {
				initialized = true;
			}
			if (DebugHelper.DEBUG) {
				DebugHelper.log("END Activator#start");
			}
		}
	}

	public void initialize(IXWTLoader loader) {
		if (lazy) {
			setDefaultCLRFactory();
		}
		initialized = true;
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Set default XWT ICLRFactory with SpringCLRFactory.
	 */
	private void setDefaultCLRFactory() {
		XWT.setCLRFactory(SpringCLRFactory.INSTANCE);
		if (DebugHelper.DEBUG) {
			DebugHelper
					.log("Use SpringCLRFactory.INSTANCE as default XWT x:ClassFactory. XWT file can use syntax x:ClassFactory=\"+ bean=<myBean> bundle=<myBundle>\"",
							1);
		}
	}

	/**
	 * Load timeout, bundle properties.
	 */
	private void loadProperties() {
		Properties properties = SettingsHelper
				.load(getClass().getClassLoader());

		// Timeout
		this.timeout = SettingsHelper.getValue(TIMEOUT_PROPERTY_NAME,
				properties, DEFAULT_TIMEOUT);

		// Default bundle
		this.defaultBundle = SettingsHelper.getValue(BUNDLE_PROPERTY_NAME,
				properties, null);

		// lazy
		this.lazy = SettingsHelper.getValue(LAZY_PROPERTY_NAME, properties,
				false);

		// lazy bean
		this.lazyBean = SettingsHelper.getValue(LAZY_BEAN_PROPERTY_NAME,
				properties, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		try {
			if (DebugHelper.DEBUG) {
				DebugHelper.log("BEGIN Activator#stop");
			}
			if (debugTracker != null) {
				debugTracker.close();
				debugTracker = null;
			}
			bundleContext = null;
		} finally {
			if (DebugHelper.DEBUG) {
				DebugHelper.log("END Activator#stop");
			}
		}

	}

	/**
	 * Returns the timeout used to retrieve Spring {@link ApplicationContext}.
	 * See {@link ApplicationContextTracker}.
	 * 
	 * @return
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Returns the default bundle which contains Spring XML files which declare
	 * CLR bean declaration.
	 * 
	 * @return
	 */
	public String getDefaultBundle() {
		return defaultBundle;
	}

	/**
	 * Returns the Bundle context.
	 * 
	 * @return
	 */
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * Returns the singleton {@link Activator}.
	 * 
	 * @return
	 */
	public static Activator getDefault() {
		return Activator.activator;
	}

	/**
	 * Return the debug value of the option if founded otherwise return
	 * defaultValue.
	 * 
	 * @param option
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBooleanDebugOption(String option,
			boolean defaultValue) {
		Activator activator = getDefault();
		if (activator == null)
			// No OSGi context
			return defaultValue;
		BundleContext myBundleContext = activator.bundleContext;
		if (myBundleContext == null)
			return defaultValue;

		// Search the DebugOptions OSGi service
		if (getDefault().debugTracker == null) {
			getDefault().debugTracker = new ServiceTracker(
					getDefault().bundleContext, DebugOptions.class.getName(),
					null);
			getDefault().debugTracker.open();
		}

		DebugOptions options = (DebugOptions) getDefault().debugTracker
				.getService();
		if (options != null) {
			// get the value of teh option by using OSGi service DebugOptions
			String value = options.getOption(option);
			if (value != null)
				return value.equalsIgnoreCase(StringUtils.TRUE); //$NON-NLS-1$
		}
		return defaultValue;
	}
}
