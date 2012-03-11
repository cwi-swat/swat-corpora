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
package org.eclipse.e4.xwt.ui.jdt;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.framework.adaptor.BundleData;
import org.eclipse.osgi.framework.internal.core.BundleFragment;
import org.eclipse.osgi.framework.internal.core.BundleHost;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.internal.core.ICoreConstants;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundleFragmentModel;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModelBase;
import org.osgi.framework.Bundle;

public class ProjectHelper {
	static String[] bundleNames = new String[] { "org.eclipse.swt." + Platform.getWS() + "." + Platform.getOS() + "." + Platform.getOSArch(), "org.eclipse.swt." + Platform.getWS() + "." + Platform.getOS(), "org.eclipse.jface" };

	/**
	 * Collect all jars
	 * 
	 * @return
	 */
	public static ClasspathContainer createContainer() {
		ArrayList<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

		for (String bundleName : bundleNames) {
			Bundle bundle = Platform.getBundle(bundleName);
			BundleData bundleData = null;
			// TODO maybe there is a good solution.
			if (bundle instanceof BundleHost) {
				bundleData = ((BundleHost) bundle).getBundleData();
			} else if (bundle instanceof BundleFragment) {
				bundleData = ((BundleFragment) bundle).getBundleData();
			} else {
				continue;
			}
			try {
				Field fileNameFile = bundleData.getClass().getDeclaredField("fileName");
				fileNameFile.setAccessible(true);
				String fileName = (String) fileNameFile.get(bundleData);
				IPath path = new Path(fileName);
				if (path.toFile().exists()) {
					if (IContainerConstants.JAR_FILE_EXTENSION.equalsIgnoreCase(path.getFileExtension())) {
						entries.add(JavaCore.newLibraryEntry(path, null, null));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		IClasspathEntry[] paths = new IClasspathEntry[entries.size()];
		entries.toArray(paths);

		return new ClasspathContainer(paths);
	}

	public static void checkDependenceJars(IProject project) {
		checkDependenceJars(JavaCore.create(project));
	}

	/**
	 * Check if it is a plugin. If it is, check if com.soyatec.eface.ui is used.
	 * 
	 * @param javaProject
	 */
	public static void checkDependenceJars(IJavaProject javaProject) {
		if (javaProject == null) {
			return;
		}
		IProject project = javaProject.getProject();
		IFile file = project.getFile(ICoreConstants.BUNDLE_FILENAME_DESCRIPTOR);

		if (file != null && file.exists()) {
			WorkspaceBundlePluginModelBase fModel = null;
			IFile fragmentFile = project.getFile(ICoreConstants.FRAGMENT_FILENAME_DESCRIPTOR);
			IFile pluginFile = project.getFile(ICoreConstants.PLUGIN_FILENAME_DESCRIPTOR);
			if (fragmentFile != null && fragmentFile.exists()) {
				fModel = new WorkspaceBundleFragmentModel(file, fragmentFile);
			} else {
				fModel = new WorkspaceBundlePluginModel(file, pluginFile);
			}
			IPluginBase pluginBase = fModel.getPluginBase();
			IPluginImport[] imports = pluginBase.getImports();
			for (IPluginImport pluginImport : imports) {
				// if (eFaceRCPPlugin.PLUGIN_ID.equals(pluginImport.getName())) {
				return;
				// }
			}

			try {
				IPluginReference[] dependencies = getDependencies();
				for (IPluginReference pluginReference : dependencies) {
					IPluginImport iimport = fModel.getPluginFactory().createImport();
					iimport.setId(pluginReference.getId());
					iimport.setVersion(pluginReference.getVersion());
					iimport.setMatch(pluginReference.getMatch());
					pluginBase.add(iimport);
				}
				fModel.save();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else {
			AddJars(javaProject);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.ui.templates.AbstractTemplateSection#getDependencies(java.lang.String)
	 */
	public static IPluginReference[] getDependencies() {
		return new IPluginReference[0];
	}

	protected static Bundle findProjectBundle(IProject project) {
		try {
			URL location = project.getLocationURI().toURL();

			IBundleGroupProvider[] groupProviders = Platform.getBundleGroupProviders();
			for (IBundleGroupProvider bundleGroupProvider : groupProviders) {
				for (IBundleGroup bundleGroup : bundleGroupProvider.getBundleGroups()) {
					for (Bundle bundle : bundleGroup.getBundles()) {
						URL bundlePath = bundle.getEntry("/");
						URL url = FileLocator.resolve(bundlePath);
						if (location.equals(url)) {
							return bundle;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected static boolean containsJar(IJavaProject javaProject, IClasspathEntry entry, String jarName) {
		switch (entry.getEntryKind()) {
		case IClasspathEntry.CPE_VARIABLE:
			IClasspathEntry resolvedEntry = JavaCore.getJavaCore().getResolvedClasspathEntry(entry);
			IPath resolvedPath = resolvedEntry.getPath();
			String string = resolvedPath.toString();
			if (string.indexOf(jarName) != -1) {
				return true;
			}
			break;
		case IClasspathEntry.CPE_CONTAINER:
			try {
				IPath path = entry.getPath();
				IClasspathContainer classpathContainer = JavaCore.getJavaCore().getClasspathContainer(path, javaProject);
				if (classpathContainer != null) {
					classpathContainer.getClasspathEntries();
					IClasspathEntry[] oldclasspath = classpathContainer.getClasspathEntries();
					for (int i = 0; i < oldclasspath.length; i++) {
						if (containsJar(javaProject, oldclasspath[i], jarName)) {
							return true;
						}
					}
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case IClasspathEntry.CPE_SOURCE:
		case IClasspathEntry.CPE_LIBRARY:
			IPath path = entry.getPath();
			String value = path.toString();
			if (value.indexOf(jarName) != -1) {
				return true;
			}
		}

		return false;
	}

	public static void AddJars(IJavaProject javaProject) {
		try {
			IClasspathEntry[] oldclasspath = javaProject.getRawClasspath();
			for (int i = 0; i < oldclasspath.length; i++) {
				if ((oldclasspath[i]).getPath().toString().equals(IContainerConstants.LIB_CONTAINER)) {
					return;
				}
			}

			ClasspathContainer container = ProjectHelper.createContainer();

			IClasspathEntry newContainerEntry = JavaCore.newContainerEntry(container.getPath());
			JavaCore.setClasspathContainer(newContainerEntry.getPath(), new IJavaProject[] { javaProject }, new IClasspathContainer[] { container }, null);

			IClasspathEntry[] newclasspath = new IClasspathEntry[oldclasspath.length + 1];
			for (int i = 0; i < oldclasspath.length; i++) {
				newclasspath[i] = oldclasspath[i];
			}
			newclasspath[newclasspath.length - 1] = newContainerEntry;
			javaProject.setRawClasspath(newclasspath, null);
		} catch (JavaModelException javaModelException) {
			javaModelException.printStackTrace();
		}
	}

	public static void RemoveJars(IJavaProject javaProject) {
		try {
			IClasspathEntry[] oldclasspath = javaProject.getRawClasspath();
			for (int i = 0; i < oldclasspath.length; i++) {
				IPath path = oldclasspath[i].getPath();
				if (path.toString().equals(IContainerConstants.LIB_CONTAINER)) {
					oldclasspath[i] = null;
					break;
				}
				if (i == oldclasspath.length - 1) {
					return;
				}
			}

			IClasspathEntry[] newclasspath = new IClasspathEntry[oldclasspath.length - 1];
			for (int i = 0, m = 0; i < oldclasspath.length; i++) {
				if (oldclasspath[i] != null) {
					newclasspath[m++] = oldclasspath[i];
				}
			}
			javaProject.setRawClasspath(newclasspath, null);
		} catch (JavaModelException javaModelException) {
			javaModelException.printStackTrace();
		}
	}
}
