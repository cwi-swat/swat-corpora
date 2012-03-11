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
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginLibrary;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundleModel;
import org.eclipse.pde.internal.core.ibundle.IBundle;
import org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase;
import org.osgi.framework.Constants;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class LibraryBuilder extends AbstractBuilder {

	private String[] libraries;

	public LibraryBuilder(IProject project, String[] libraries) {
		super(project);
		this.libraries = libraries;
	}

	public IStatus build() {
		if (libraries == null || libraries.length == 0) {
			return Status.CANCEL_STATUS;
		}
		try {
			refreshLocal();
			IBundlePluginModelBase model = (IBundlePluginModelBase) getModel();
			IPluginBase pluginBase = model.getPluginBase();
			WorkspaceBundleModel bundleModel = (WorkspaceBundleModel) model.getBundleModel();
			synchronized (bundleModel) {
				bundleModel.load();
				boolean isNonEdit = bundleModel.isEditable();
				if (!isNonEdit) {
					bundleModel.setEditable(true);
				}
				for (String libPath : libraries) {
					IPluginLibrary library = model.getPluginFactory().createLibrary();
					library.setName(libPath.toString());
					library.setExported(true);
					pluginBase.add(library);
				}
				checkSourceRootEntry();
				updateBuildProperties(new String[libraries.length], libraries, false);
				updateJavaClasspathLibs(new String[libraries.length], libraries);

				bundleModel.save();
				if (!isNonEdit) {
					bundleModel.setEditable(false);
				}
			}
			buildClean();
		} catch (CoreException e) {
			error(e.getMessage());
		}
		return Status.OK_STATUS;
	}

	private void checkSourceRootEntry() {
		IBundlePluginModelBase pluginModel = (IBundlePluginModelBase) getModel();
		IPluginLibrary[] libraries = pluginModel.getPluginBase().getLibraries();
		for (int i = 0; i < libraries.length; i++)
			if (libraries[i].getName().equals(".")) //$NON-NLS-1$
				return;
		IBuildModel model = pluginModel.getBuildModel();
		if (model == null)
			return;

		IBuildEntry[] entires = model.getBuild().getBuildEntries();
		for (int i = 0; i < entires.length; i++) {
			if (entires[i].getName().equals(IBuildPropertiesConstants.PROPERTY_SOURCE_PREFIX + '.')) {
				IPluginLibrary library = pluginModel.getPluginFactory().createLibrary();
				try {
					library.setName("."); //$NON-NLS-1$
					pluginModel.getPluginBase().add(library);
				} catch (CoreException e) {
				}
			}
		}
	}

	private IBuildModel getBuildModel() {
		IBundlePluginModelBase pluginModel = (IBundlePluginModelBase) getModel();
		if (pluginModel != null) {
			return pluginModel.getBuildModel();
		}
		return null;
	}

	private void updateBuildProperties(final String[] oldPaths, final String[] newPaths, boolean modifySourceEntry) {
		IBuildModel bmodel = getBuildModel();
		if (bmodel == null)
			return;

		IBuild build = bmodel.getBuild();

		IBuildEntry entry = build.getEntry(IBuildPropertiesConstants.PROPERTY_BIN_INCLUDES);
		if (entry == null)
			entry = bmodel.getFactory().createEntry(IBuildPropertiesConstants.PROPERTY_BIN_INCLUDES);

		try {
			// adding new entries
			if (oldPaths[0] == null) {
				for (int i = 0; i < newPaths.length; i++)
					if (newPaths[i] != null) {
						entry.addToken(newPaths[i]);
						if (modifySourceEntry)
							configureSourceBuildEntry(bmodel, null, newPaths[i]);
					}
				// removing entries
			} else if (newPaths[0] == null) {
				for (int i = 0; i < oldPaths.length; i++)
					if (oldPaths[i] != null) {
						entry.removeToken(oldPaths[i]);
						if (modifySourceEntry)
							configureSourceBuildEntry(bmodel, oldPaths[i], null);
					}
				if (entry.getTokens().length == 0)
					build.remove(entry);
				// rename entries
			} else {
				for (int i = 0; i < oldPaths.length; i++)
					if (newPaths[i] != null && oldPaths[i] != null) {
						entry.renameToken(oldPaths[i], newPaths[i]);
						if (modifySourceEntry)
							configureSourceBuildEntry(bmodel, oldPaths[i], newPaths[i]);
					}
			}
		} catch (CoreException e) {
		}
	}

	private void configureSourceBuildEntry(IBuildModel bmodel, String oldPath, String newPath) throws CoreException {
		IBuild build = bmodel.getBuild();
		IBuildEntry entry = build.getEntry(IBuildPropertiesConstants.PROPERTY_SOURCE_PREFIX + (oldPath != null ? oldPath : newPath));
		try {
			if (newPath != null) {
				if (entry == null) {
					IJavaProject jproject = JavaCore.create(project);
					ArrayList tokens = new ArrayList();
					IClasspathEntry[] entries = jproject.getRawClasspath();
					for (int i = 0; i < entries.length; i++)
						if (entries[i].getEntryKind() == IClasspathEntry.CPE_SOURCE)
							tokens.add(entries[i].getPath().removeFirstSegments(1).addTrailingSeparator().toString());
					if (tokens.size() == 0)
						return;

					entry = bmodel.getFactory().createEntry(IBuildPropertiesConstants.PROPERTY_SOURCE_PREFIX + newPath);
					for (int i = 0; i < tokens.size(); i++)
						entry.addToken((String) tokens.get(i));
					build.add(entry);
				} else
					entry.setName(IBuildPropertiesConstants.PROPERTY_SOURCE_PREFIX + newPath);
			} else if (entry != null && newPath == null)
				build.remove(entry);
		} catch (JavaModelException e) {
		}
	}

	private void updateJavaClasspathLibs(String[] oldPaths, String[] newPaths) {
		IJavaProject jproject = JavaCore.create(project);
		try {
			IClasspathEntry[] entries = jproject.getRawClasspath();
			ArrayList toBeAdded = new ArrayList();
			int index = -1;
			entryLoop: for (int i = 0; i < entries.length; i++) {
				if (entries[i].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					if (index == -1)
						index = i;
					// do not add the old paths (handling deletion/renaming)
					IPath path = entries[i].getPath().removeFirstSegments(1).removeTrailingSeparator();
					for (int j = 0; j < oldPaths.length; j++)
						if (oldPaths[j] != null && path.equals(new Path(oldPaths[j]).removeTrailingSeparator()))
							continue entryLoop;
				} else if (entries[i].getEntryKind() == IClasspathEntry.CPE_CONTAINER)
					if (index == -1)
						index = i;
				toBeAdded.add(entries[i]);
			}
			if (index == -1)
				index = entries.length;

			// add paths
			for (int i = 0; i < newPaths.length; i++) {
				if (newPaths[i] == null)
					continue;
				IClasspathEntry entry = JavaCore.newLibraryEntry(project.getFullPath().append(newPaths[i]), null, null, true);
				if (!toBeAdded.contains(entry))
					toBeAdded.add(index++, entry);
			}

			if (toBeAdded.size() == entries.length)
				return;

			IClasspathEntry[] updated = (IClasspathEntry[]) toBeAdded.toArray(new IClasspathEntry[toBeAdded.size()]);
			jproject.setRawClasspath(updated, null);
		} catch (JavaModelException e) {
		}
	}

	public IStatus unbuild() {
		if (libraries == null || libraries.length == 0) {
			return warning("No library found.");
		}
		IPluginModelBase pluginModel = getModel();
		IBundlePluginModelBase model = (IBundlePluginModelBase) pluginModel;
		IPluginBase pluginBase = model.getPluginBase();
		IPluginLibrary[] exists = pluginBase.getLibraries();
		List<IPluginLibrary> libForRemove = new ArrayList<IPluginLibrary>();
		for (IPluginLibrary pluginLibrary : exists) {
			for (String libName : libraries) {
				if (pluginLibrary.getName().equals(libName)) {
					libForRemove.add(pluginLibrary);
				}
			}
		}
		if (libForRemove.isEmpty()) {
			return warning("No library found.");
		}

		try {
			refreshLocal();
			WorkspaceBundleModel bundleModel = (WorkspaceBundleModel) model.getBundleModel();
			synchronized (bundleModel) {
				bundleModel.load();
				boolean isNonEdit = bundleModel.isEditable();
				if (!isNonEdit) {
					bundleModel.setEditable(true);
				}
				String[] remove = new String[libForRemove.size()];
				int i = 0;
				IBundle bundle = bundleModel.getBundle();
				for (IPluginLibrary pluginLibrary : libForRemove) {
					pluginBase.remove(pluginLibrary);
					String name = pluginLibrary.getName();
					remove[i++] = name;
				}
				removeLibraries(bundle, remove);
				bundleModel.save();
				if (!isNonEdit) {
					bundleModel.setEditable(false);
				}
				updateBuildProperties(remove, new String[remove.length], true);
				updateJavaClasspathLibs(remove, new String[remove.length]);
			}
			buildClean();
		} catch (CoreException e) {
			error(e.getMessage());
		}
		return Status.OK_STATUS;
	}

	/**
	 * Fixed a bug of removing library from IPluginBase.remove(pluginLibrary), the bundle does not update at all.
	 */
	private void removeLibraries(IBundle bundle, String[] remove) {
		String oldValue = bundle.getHeader(Constants.BUNDLE_CLASSPATH);
		if (oldValue == null || oldValue.equals("")) {
			return;
		}
		List<String> values = new ArrayList<String>();
		StringTokenizer stk = new StringTokenizer(oldValue, ",");
		while (stk.hasMoreTokens()) {
			values.add(stk.nextToken().trim());
		}
		for (String r : remove) {
			values.remove(r);
		}
		String newValue = null;
		for (int i = 0; i < values.size(); i++) {
			if (newValue == null) {
				newValue = values.get(i);
			} else {
				newValue += "," + values.get(i);
			}
		}
		bundle.setHeader(Constants.BUNDLE_CLASSPATH, newValue);
	}

}
