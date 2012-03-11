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
package org.eclipse.e4.xwt.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * The activator class controls the plug-in life cycle
 */
public class XWTUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.e4.xwt.tools.ui";

	// The shared instance
	private static XWTUIPlugin plugin;

	/**
	 * The constructor
	 */
	public XWTUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		ProjectContext.start();
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		ProjectContext.stop();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static XWTUIPlugin getDefault() {
		return plugin;
	}

	public static Shell getShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
			return null;
		return activeWorkbenchWindow.getShell();
	}

	public static void log(int severity, int code, String message, Throwable exception) {
		getDefault().getLog().log(new Status(severity, getDefault().getBundle().getSymbolicName(), code, message, exception));
	}

	public static void log(int severity, int code, String message) {
		getDefault().getLog().log(new Status(severity, getDefault().getBundle().getSymbolicName(), code, message, null));
	}

	public static void log(int severity, String message) {
		getDefault().getLog().log(new Status(severity, getDefault().getBundle().getSymbolicName(), -1, message, null));
	}

	public static void log(Throwable exception) {
		getDefault().getLog().log(new Status(IStatus.ERROR, getDefault().getBundle().getSymbolicName(), -1, "", exception));
	}

	public static ImageDescriptor getBundledImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(getDefault().getBundle().getSymbolicName(), path);
	}

	public Image getBundledImage(String path) {
		Image image = getImageRegistry().get(path);
		if (image == null) {
			getImageRegistry().put(path, getBundledImageDescriptor(path));
			image = getImageRegistry().get(path);
		}
		return image;
	}

	public static void checkStartup() {
		Bundle bundle = getDefault().getBundle();
		if (bundle.getState() != Bundle.ACTIVE) {
			try {
				bundle.start();
			} catch (BundleException e) {
				e.printStackTrace();
			}
		}
	}

	public void openXWTPerspective() {
		IWorkbench workbench = getWorkbench();
		int count = workbench.getWorkbenchWindowCount();
		if (count == 0) {
			return;
		}
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null) {
			window = workbench.getWorkbenchWindows()[0];
		}
		IPerspectiveDescriptor pers = workbench.getPerspectiveRegistry().findPerspectiveWithId(XWTPerspectiveFactory.XWT_PERSPECTIVE_ID);
		if (pers == null) {
			return;
		}
		try {
			workbench.showPerspective(XWTPerspectiveFactory.XWT_PERSPECTIVE_ID, window);
		} catch (WorkbenchException e) {
			System.out.println("Can't Open XWT Perspective");
		}
	}
}
