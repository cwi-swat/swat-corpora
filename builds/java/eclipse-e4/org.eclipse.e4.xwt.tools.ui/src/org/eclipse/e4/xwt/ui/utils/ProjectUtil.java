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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginLibrary;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

/**
 * 
 * @author yyang <yves.yang@soyatec.com>
 */
public class ProjectUtil {

	static String[] WORKBENCH_BUNDLES = new String[]{
			"org.eclipse.e4.ui.services", "org.eclipse.e4.ui.workbench",
			"org.eclipse.e4.core.di",
			"org.eclipse.e4.core.di.extensions", "org.eclipse.e4.core.contexts",
			"org.eclipse.e4.core.services", "org.eclipse.e4.ui.workbench.swt",
			"org.eclipse.e4.ui.css.core", "org.w3c.css.sac",
			"org.eclipse.e4.core.commands", "org.eclipse.e4.ui.bindings",
			"org.eclipse.e4.xwt.css", "org.eclipse.e4.xwt.ui.workbench",
			"javax.inject"};

	static String[] XWT_CORE_BUNDLES = new String[]{"org.eclipse.e4.xwt",
			"org.eclipse.jface.databinding", "org.eclipse.swt",
			"org.eclipse.jface", "org.eclipse.core.databinding", "com.ibm.icu"};

	static String[] XWT_DATABINING_BUNDLES = new String[]{
			"org.eclipse.core.databinding.beans",
			"org.eclipse.core.databinding.property"};

	static String[] XWT_EMF_BUNDLES = new String[]{"org.eclipse.e4.xwt.emf",
			"org.eclipse.emf.databinding",
			"org.eclipse.core.databinding.property"};

	public static void updateXWTCoreDependencies(IProject project) {
		addDependencies(project, XWT_CORE_BUNDLES);
	}

	public static void updateXWTDataBindingDependencies(IProject project) {
		addDependencies(project, XWT_CORE_BUNDLES, XWT_DATABINING_BUNDLES);
	}

	public static void updateXWTWorkbenchDependencies(IProject project) {
		addDependencies(project, XWT_CORE_BUNDLES, XWT_DATABINING_BUNDLES,
				WORKBENCH_BUNDLES);
	}
	public static void updateXWTEMFDependencies(IProject project) {
		addDependencies(project, XWT_CORE_BUNDLES, XWT_EMF_BUNDLES);
	}
	public static IStatus addDependencies(IProject project,
			String[]... pluginIds) {
		IPluginModelBase[] dependencies = getDependencies(project, true,
				pluginIds);
		return new DependencyBuilder(project, dependencies).build();
	}

	public static IStatus addDependencies(IProject project,
			IPluginModelBase[] pluginImports) {
		return new DependencyBuilder(project, pluginImports).build();
	}

	public static IStatus removeDependencies(IProject project,
			String[]... pluginIds) {
		IPluginModelBase[] dependencies = getDependencies(project, false,
				pluginIds);
		return new DependencyBuilder(project, dependencies).unbuild();
	}

	public static IStatus removeDependencies(IProject project,
			IPluginModelBase[] pluginImports) {
		return new DependencyBuilder(project, pluginImports).unbuild();
	}

	public static IStatus addLibraries(IProject project, String[] jarPaths) {
		String[] libraries = getLibraries(project, jarPaths, true);
		return new LibraryBuilder(project, libraries).build();
	}

	public static IStatus removeLibraries(IProject project, String[] jarPaths) {
		String[] libraries = getLibraries(project, jarPaths, false);
		return new LibraryBuilder(project, libraries).unbuild();
	}

	public static IPluginModelBase[] getDependencies(IProject project,
			boolean ignoreExists, String[]... pluginIds) {
		if (pluginIds == null || pluginIds.length == 0) {
			return new IPluginModelBase[0];
		}

		List<String> existingImports = new ArrayList<String>();
		if (ignoreExists) {
			IPluginBase pluginBase = getPluginBase(project);
			if (pluginBase != null) {
				IPluginImport[] imports = pluginBase.getImports();
				for (IPluginImport pluginImport : imports) {
					existingImports.add(pluginImport.getId());
				}
			}
		}
		List<IPluginModelBase> models = new ArrayList<IPluginModelBase>();
		for (String[] ids : pluginIds) {
			for (String pluginId : ids) {
				if (ignoreExists && existingImports.contains(pluginId)) {
					continue;
				}
				IPluginModelBase model = PluginRegistry.findModel(pluginId);
				if (model == null) {
					continue;
				}
				models.add(model);
			}
		}
		return models.toArray(new IPluginModelBase[models.size()]);
	}

	public static IPluginBase getPluginBase(IProject project) {
		if (project == null || !project.exists()) {
			return null;
		}
		IPluginModelBase model = PluginRegistry.findModel(project);
		if (model != null) {
			return model.getPluginBase(true);
		}
		return null;
	}

	public static String[] getLibraries(IProject project, String[] jarPaths,
			boolean ignoreExists) {
		if (jarPaths == null || jarPaths.length == 0) {
			return new String[0];
		}
		List<String> existingLibraries = new ArrayList<String>();
		if (ignoreExists) {
			IPluginBase pluginBase = getPluginBase(project);
			if (pluginBase != null) {
				IPluginLibrary[] libraries = pluginBase.getLibraries();
				for (IPluginLibrary pluginLibrary : libraries) {
					existingLibraries.add(pluginLibrary.getName());
				}
			}
		}
		List<String> libraries = new ArrayList<String>();
		for (String path : jarPaths) {
			if (ignoreExists && existingLibraries.contains(path)) {
				continue;
			}
			libraries.add(path);
		}
		return libraries.toArray(new String[libraries.size()]);
	}
}
