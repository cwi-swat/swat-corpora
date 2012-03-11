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
package org.eclipse.e4.xwt.ui.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

/**
 * @author jliu jin.liu@soyatec.com
 */
public abstract class AbstractBuilder {

	IProject project;

	public AbstractBuilder(IProject project) {
		this.project = project;
	}

	public IProject getProject() {
		return project;
	}

	public IPluginModelBase getModel() {
		return PluginRegistry.findModel(project);
	}

	public IPluginBase getPluginBase() {
		IPluginModelBase model = getModel();
		if (model != null) {
			return model.getPluginBase(true);
		}
		return null;
	}

	protected IStatus warning(String message) {
		return new Status(IStatus.WARNING, XWTUIPlugin.PLUGIN_ID, message);
	}

	protected IStatus info(String message) {
		return new Status(IStatus.INFO, XWTUIPlugin.PLUGIN_ID, message);
	}

	protected IStatus error(String message) {
		return new Status(IStatus.ERROR, XWTUIPlugin.PLUGIN_ID, message);
	}

	protected void refreshLocal() throws CoreException {
		project.refreshLocal(IProject.DEPTH_INFINITE, null);
	}

	protected void buildClean() throws CoreException {
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);
	}

	public abstract IStatus build();

	public abstract IStatus unbuild();
}
