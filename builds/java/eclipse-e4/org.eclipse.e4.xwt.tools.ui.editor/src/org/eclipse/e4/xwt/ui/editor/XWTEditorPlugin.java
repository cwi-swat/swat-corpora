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
package org.eclipse.e4.xwt.ui.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.pde.PDEPlugin;
import org.eclipse.e4.xwt.vex.palette.customize.CustomWidgetManager;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class XWTEditorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.e4.xwt.tools.ui.editor";

	// The shared instance
	private static XWTEditorPlugin plugin;
	/**
	 * The template context type registry for the xml editor.
	 */
	private ContextTypeRegistry fContextTypeRegistry;
	/**
	 * The template store for the xml editor.
	 * 
	 */
	private TemplateStore fTemplateStore;

	/**
	 * The constructor
	 */
	public XWTEditorPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		PDEPlugin.getDefault();
		
		// Bug 274057 - Start
		for (Class<?> cls: CustomWidgetManager.getInstance().getWidgetClassList())
		{
			XWT.registerMetaclass(cls);
		}
		// Bug 274057 - end
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
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
	public static XWTEditorPlugin getDefault() {
		return plugin;
	}

	public static void log(Throwable e) {
		getDefault().getLog().log(new Status(IStatus.ERROR, getDefault().getBundle().getSymbolicName(), -1, "", e));
	}
}
