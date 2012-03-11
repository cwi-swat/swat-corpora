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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.core.plugin.ISharedExtensionsModel;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.bundle.BundlePluginBase;
import org.eclipse.pde.internal.core.bundle.BundlePluginModelBase;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundleModel;
import org.eclipse.pde.internal.core.ibundle.IBundle;
import org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase;
import org.eclipse.pde.internal.core.plugin.AbstractPluginModelBase;
import org.eclipse.pde.internal.core.plugin.PluginBase;
import org.eclipse.pde.internal.core.plugin.WorkspaceExtensionsModel;
import org.eclipse.pde.internal.core.text.plugin.PluginBaseNode;
import org.eclipse.pde.internal.core.text.plugin.PluginDocumentNodeFactory;
import org.eclipse.pde.internal.core.util.VersionUtil;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class DependencyBuilder extends AbstractBuilder {
	private IPluginModelBase[] dependencies;
	private boolean[] optionals;

	public DependencyBuilder(IProject project, IPluginModelBase[] dependencies) {
		this(project, dependencies, null);
	}

	public DependencyBuilder(IProject project, IPluginModelBase[] dependencies, boolean[] optionals) {
		super(project);
		this.dependencies = dependencies;
		this.optionals = optionals;
		if (optionals == null) {
			this.optionals = new boolean[dependencies.length];
			for (int i = 0; i < this.optionals.length; i++) {
				this.optionals[i] = false;
			}
		}
	}

	public IStatus build() {
		if (dependencies == null || dependencies.length == 0) {
			return warning("No dependencies found");
		}
		return addDependencies(Arrays.asList(dependencies));
	}

	private boolean equals(IPluginModelBase source, IPluginModelBase target) {
		if (source == null) {
			return target == null;
		}
		if (target == null) {
			return false;
		}
		IPluginBase sourceBase = source.getPluginBase();
		String sourceId = sourceBase.getId();
		String sourceVersion = sourceBase.getVersion();
		if (sourceVersion != null) {
			sourceVersion = new Version(sourceVersion).toString();
		}

		IPluginBase targetBase = target.getPluginBase();
		String targetId = targetBase.getId();
		String targetVersion = targetBase.getVersion();
		if (targetVersion != null) {
			targetVersion = new Version(targetVersion).toString();
		}
		if (sourceVersion == null || targetVersion == null) {
			return sourceId.equals(targetId);
		}
		return sourceId.equals(targetId) && sourceVersion.equals(targetVersion);
	}

	private void filter(List<IPluginModelBase> pluginImports, IPluginImport pluginImport) {
		IPluginModelBase target = PluginRegistry.findModel(pluginImport.getId());
		for (Iterator iterator = pluginImports.iterator(); iterator.hasNext();) {
			IPluginModelBase source = (IPluginModelBase) iterator.next();
			if (equals(source, target)) {
				iterator.remove();
			}
		}
	}

	public IStatus addDependencies(List<IPluginModelBase> pluginImports) {
		try {
			refreshLocal();
			IBundlePluginModelBase model = (IBundlePluginModelBase) getModel();
			IPluginBase pluginBase = model.getPluginBase();
			IPluginImport[] existedImports = pluginBase.getImports();
			for (IPluginImport dep : existedImports) {
				filter(pluginImports, dep);
			}
			if (pluginImports.isEmpty()) {
				return warning("There's no dependency found.");
			}
			WorkspaceBundleModel bundleModel = (WorkspaceBundleModel) model.getBundleModel();
			synchronized (bundleModel) {
				bundleModel.load();
				boolean isBundleEditable = bundleModel.isEditable();
				if (!isBundleEditable){
					bundleModel.setEditable(true);
				}
				ISharedExtensionsModel extensionsModel = model.getExtensionsModel();
				if (extensionsModel != null && extensionsModel instanceof WorkspaceExtensionsModel){
					((WorkspaceExtensionsModel)extensionsModel).setEditable(true);
				}
				
				IPluginImport[] imports = new IPluginImport[pluginImports.size()];
				for (int i = 0; i < pluginImports.size(); i++) {
					IPluginModel candidate = (IPluginModel) pluginImports.get(i);
					String pluginId = candidate.getPlugin().getId();
					IPluginImport importNode = createImport(model.getPluginFactory(), pluginId);
					importNode.setOptional(optionals[i]);
					imports[i] = importNode;
				}
				addImports(pluginBase, imports);

				bundleModel.save();
				if (!isBundleEditable){
					bundleModel.setEditable(false);
				}
				if (extensionsModel != null && extensionsModel instanceof WorkspaceExtensionsModel){
					((WorkspaceExtensionsModel)extensionsModel).setEditable(false);
				}
			}
			buildClean();
		} catch (Exception e) {
			return error("Build dependency error: " + e.getMessage());
		}
		return Status.OK_STATUS;
	}

	private IPluginImport createImport(IPluginModelFactory factory, String id) {
		if (factory instanceof AbstractPluginModelBase) {
			return ((AbstractPluginModelBase) factory).createImport(id);
		} else if (factory instanceof BundlePluginModelBase) {
			return ((BundlePluginModelBase) factory).createImport(id);
		} else if (factory instanceof PluginDocumentNodeFactory) {
			return ((PluginDocumentNodeFactory) factory).createImport(id);
		}
		return null;
	}

	private void addImports(IPluginBase base, IPluginImport[] imports) throws Exception {
		if (base instanceof BundlePluginBase) {
			((BundlePluginBase) base).add(imports);
		} else if (base instanceof PluginBase) {
			((PluginBase) base).add(imports);
		} else if (base instanceof PluginBaseNode) {
			((PluginBaseNode) base).add(imports);
		}
	}

	public IStatus unbuild() {
		if (dependencies == null || dependencies.length == 0) {
			return warning("No dependency found");
		}
		try {
			refreshLocal();
			IBundlePluginModelBase model = (IBundlePluginModelBase) getModel();
			IPluginBase pluginBase = model.getPluginBase();
			List<IPluginImport> removes = new ArrayList<IPluginImport>();
			IPluginImport[] existings = pluginBase.getImports();
			for (IPluginImport pluginImport : existings) {
				IPluginModelBase importModel = PluginRegistry.findModel(pluginImport.getId());
				for (IPluginModelBase dep : dependencies) {
					if (equals(importModel, dep)) {
						removes.add(pluginImport);
					}
				}
			}
			if (removes.isEmpty()) {
				return warning("No dependency found for remove");
			}
			IBundle bundle = model.getBundleModel().getBundle();
			WorkspaceBundleModel bundleModel = (WorkspaceBundleModel) model.getBundleModel();
			synchronized (bundleModel) {
				bundleModel.load();
				bundleModel.setEditable(true);
				removeImports(pluginBase, removes.toArray(new IPluginImport[0]));
				removeDependencies(bundle, removes.toArray(new IPluginImport[0]));
				bundleModel.save();
				bundleModel.setEditable(false);
			}
			buildClean();
		} catch (Exception e) {
			return error("Build dependency error: " + e.getMessage());
		}
		return Status.OK_STATUS;
	}

	private void removeImports(IPluginBase base, IPluginImport[] imports) throws CoreException {
		if (base instanceof BundlePluginBase)
			((BundlePluginBase) base).remove(imports);
		else if (base instanceof PluginBase)
			((PluginBase) base).remove(imports);
		else if (base instanceof PluginBaseNode)
			((PluginBaseNode) base).remove(imports);
	}

	/**
	 * Fixed a bug of removing library from IPluginBase.remove(pluginImports), the bundle does not update at all.
	 */
	private void removeDependencies(IBundle bundle, IPluginImport[] imports) {
		String oldValue = bundle.getHeader(Constants.REQUIRE_BUNDLE);
		if (oldValue == null || oldValue.equals("")) {
			return;
		}
		List<String> values = new ArrayList<String>();
		StringTokenizer stk = new StringTokenizer(oldValue, ",");
		while (stk.hasMoreTokens()) {
			values.add(stk.nextToken().trim());
		}
		for (IPluginImport dep : imports) {
			String name = dep.getId();
			for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
				String requiredDep = iterator.next();
				if (requiredDep.startsWith(name)) {
					iterator.remove();
				}
			}
		}
		String newValue = null;
		for (int i = 0; i < values.size(); i++) {
			if (newValue == null) {
				newValue = values.get(i);
			} else {
				newValue += "," + values.get(i);
			}
		}
		bundle.setHeader(Constants.REQUIRE_BUNDLE, newValue);
	}
}
