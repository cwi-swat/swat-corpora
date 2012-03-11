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
package org.eclipse.e4.xwt.pde;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * The activator class controls the plug-in life cycle
 */
public class PDEPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.e4.xwt.pde";

	// The shared instance
	private static PDEPlugin plugin;

	private static BundleContext context;

	/**
	 * The constructor
	 */
	public PDEPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// To register all metaclass using EP for XWT
		ExtensionService.initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
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
	public static PDEPlugin getDefault() {
		return plugin;
	}

	public static BundleContext getContext() {
		return context;
	}

	public static void checkStartup() {
		Bundle bundle = Platform.getBundle("org.eclipse.e4.xwt");
		if (bundle.getState() != Bundle.ACTIVE) {
			try {
				bundle.start(Bundle.START_TRANSIENT);
			} catch (BundleException e) {
				e.printStackTrace();
			}
		}
	}

	static public Shell getShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench
				.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
			return null;
		return activeWorkbenchWindow.getShell();
	}

	public static void log(int severity, int code, String message,
			Throwable exception) {
		getDefault().getLog().log(
				new Status(severity,
						getDefault().getBundle().getSymbolicName(), code,
						message, exception));
	}

	public static void log(int severity, int code, String message) {
		getDefault().getLog().log(
				new Status(severity,
						getDefault().getBundle().getSymbolicName(), code,
						message, null));
	}

	public static void log(int severity, String message) {
		getDefault().getLog().log(
				new Status(severity,
						getDefault().getBundle().getSymbolicName(), -1,
						message, null));
	}

	public static void log(Throwable exception) {
		getDefault().getLog().log(
				new Status(IStatus.ERROR, getDefault().getBundle()
						.getSymbolicName(), -1, "", exception));
	}
}
