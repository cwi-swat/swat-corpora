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

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ClassLoaderHelper {

	public static URL getResourceAsURL(IJavaProject javaProject, String name) {
		if (javaProject == null)
			return null;

		Set<IJavaProject> visited = new HashSet<IJavaProject>();
		URL url = findResourceURL(javaProject, visited, true, name);
		if (url == null) {
			IFile file = javaProject.getProject().getFile(name);
			if (file.exists()) {
				url = toURL(file.getLocation());
			}
		}
		return url;
	}

	public static byte[] getClassContent(IJavaProject javaProject, String className) {
		if (javaProject == null || !javaProject.exists())
			return null;
		String resourceName = className.replace('.', '/') + ".class";
		try {
			IPath outPath = javaProject.getProject().getLocation().removeLastSegments(1).append(javaProject.getOutputLocation());
			outPath = outPath.addTrailingSeparator();
			{
				URL url = toURL(outPath.append(resourceName));
				if (url != null) {
					InputStream inputStream = url.openStream();
					byte[] content = new byte[inputStream.available()];
					inputStream.read(content);
					return content;
				}
				for (IProject project : javaProject.getProject().getReferencedProjects()) {
					if (!project.isOpen()) {
						continue;
					}
					IJavaProject javaReferencedProject = JavaCore.create(project);
					if (javaReferencedProject.exists()) {
						byte[] content = getClassContent(javaReferencedProject, className);
						if (content != null) {
							return content;
						}
					}
				}
			}
			IType type = javaProject.findType(className);
			if (type != null && type.exists()) {
				if (type.isBinary()) {
					return type.getClassFile().getBytes();
				} else {
					IJavaProject typeProject = type.getJavaProject();
					if (!javaProject.equals(typeProject)) {
						return getClassContent(typeProject, className);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static URL[] getClasspathAsURLArray(IJavaProject javaProject) {
		if (javaProject == null)
			return null;
		Set<IJavaProject> visited = new HashSet<IJavaProject>();
		List<URL> urls = new ArrayList<URL>(20);
		collectClasspathURLs(javaProject, urls, visited, true);
		URL[] result = new URL[urls.size()];
		urls.toArray(result);
		return result;
	}

	private static URL findResourceURL(IJavaProject javaProject, Set<IJavaProject> visited, boolean isFirstProject, String name) {
		if (visited.contains(javaProject))
			return null;
		visited.add(javaProject);
		try {
			IPath outPath = javaProject.getProject().getLocation().removeLastSegments(1).append(javaProject.getOutputLocation());
			outPath = outPath.addTrailingSeparator();
			{
				URL url = toURL(outPath.append(name));
				if (url != null) {
					return url;
				}
			}
			for (IPackageFragmentRoot fragment : javaProject.getPackageFragmentRoots()) {
				if (fragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
					URL url = toURL(fragment.getResource().getLocation().append(name));
					if (url != null) {
						return url;
					}
				}
			}
			// urls.add(out);
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
			for (IClasspathEntry entry : entries) {
				switch (entry.getEntryKind()) {
				case IClasspathEntry.CPE_LIBRARY: {
					// TODO
					IClasspathEntry resolveEntry = JavaCore.getResolvedClasspathEntry(entry);
					File file = resolveEntry.getPath().toFile();
					IPath path = resolveEntry.getPath();
					if (!file.exists()) {
						String projectName = path.segment(0);
						IProject project = javaProject.getProject().getWorkspace().getRoot().getProject(projectName);
						path = project.getLocation().append(path.removeFirstSegments(1));
					}
					String spec = "jar:file:" + path.toString() + "!/" + name;
					try {
						URL url2 = new URL(spec);
						url2.getContent();
						return url2;
					} catch (Exception e) {
					}
				}
					break;
				case IClasspathEntry.CPE_CONTAINER:

					break;
				case IClasspathEntry.CPE_VARIABLE: {
					{
						// TODO
						URL url = toURL(outPath.append(name));
						if (url != null) {
							return url;
						}
					}
				}
					break;
				case IClasspathEntry.CPE_PROJECT: {
					if (isFirstProject || entry.isExported()) {
						URL url = findResourceURL(getJavaProject(entry), visited, false, name);
						if (url != null) {
							return url;
						}
					}
					break;
				}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static URL toURL(IPath outPath) {
		File file = outPath.toFile();
		if (file != null && file.exists()) {
			try {
				return file.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void collectClasspathURLs(IJavaProject javaProject, List<URL> urls, Set<IJavaProject> visited, boolean isFirstProject) {
		if (visited.contains(javaProject))
			return;
		visited.add(javaProject);
		try {
			IPath outPath = javaProject.getProject().getWorkspace().getRoot().getFullPath().append(javaProject.getOutputLocation());
			outPath = outPath.addTrailingSeparator();
			URL out = createFileURL(outPath);
			urls.add(out);
			IClasspathEntry[] entries = null;
			entries = javaProject.getResolvedClasspath(true);
			for (IClasspathEntry entry : entries) {
				switch (entry.getEntryKind()) {
				case IClasspathEntry.CPE_LIBRARY:
				case IClasspathEntry.CPE_CONTAINER:
				case IClasspathEntry.CPE_VARIABLE:
					collectClasspathEntryURL(entry, urls);
					break;
				case IClasspathEntry.CPE_PROJECT: {
					if (isFirstProject || entry.isExported())
						collectClasspathURLs(getJavaProject(entry), urls, visited, false);
					break;
				}
				}
			}
		} catch (JavaModelException e) {
			return;
		}
	}

	private static URL createFileURL(IPath path) {
		URL url = null;
		try {
			url = new URL("file://" + path.toOSString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	private static void collectClasspathEntryURL(IClasspathEntry entry, List<URL> urls) {
		URL url = createFileURL(entry.getPath());
		if (url != null)
			urls.add(url);
	}

	private static IJavaProject getJavaProject(IClasspathEntry entry) {
		IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().segment(0));
		if (proj != null)
			return JavaCore.create(proj);
		return null;
	}
}
