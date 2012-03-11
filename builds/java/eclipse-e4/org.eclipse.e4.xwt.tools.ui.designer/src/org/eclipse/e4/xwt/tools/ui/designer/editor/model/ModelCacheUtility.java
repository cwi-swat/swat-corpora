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
package org.eclipse.e4.xwt.tools.ui.designer.editor.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.xwt.tools.ui.designer.core.DesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.osgi.framework.Bundle;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ModelCacheUtility {
	protected static final IPath VISUAL_EDITOR_MODEL_CACHE_ROOT = DesignerPlugin.VE_CACHE_ROOT_NAME
			.append("emfmodel"); //$NON-NLS-1$ 
	private static final Map XML_CACHE_SAVE_OPTIONS;
	static {
		// Normally focus on speed not readability
		XML_CACHE_SAVE_OPTIONS = new HashMap(4);
		XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_PROCESS_DANGLING_HREF,
				XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
		XML_CACHE_SAVE_OPTIONS.put(XMLResource.OPTION_SAVE_TYPE_INFORMATION,
				Boolean.TRUE);
	}

	protected static IPath getEMFModelCacheDestination(IProject p) {
		DesignerPlugin plugin = DesignerPlugin.getDefault();
		Bundle bundle = plugin.getBundle();
		String symbolicName = bundle.getSymbolicName();
		IPath workingLocation = p.getWorkingLocation(symbolicName);
		return workingLocation.append(VISUAL_EDITOR_MODEL_CACHE_ROOT);
	}

	public static IPath getCacheDirectory(IFile f) {
		// addCacheResourceListener(); // Make sure we are listening.

		IProject p = f.getProject();
		IPath projectCachePath = getEMFModelCacheDestination(p);
		File dir = projectCachePath.toFile();
		if (!dir.exists())
			if (dir.mkdirs())
				return projectCachePath;
			else
				return null;
		else
			return projectCachePath;
	}

	protected static IPath getCachedPath(IFile f) {
		IPath savedPath = getCacheDirectory(f).append(
				f.getProjectRelativePath());
		return savedPath.removeFileExtension().addFileExtension("xmi"); //$NON-NLS-1$
	}

	public static URI getCacheURI(IFile f) {
		return URI.createFileURI(getCachedPath(f).toString());
	}

	public static XamlDocument doLoadFromCache(IFile input,
			IProgressMonitor monitor) {
		if (monitor == null)
			monitor = new NullProgressMonitor();
		monitor.subTask("Loading from cache: " + input.getName());
		if (isValidCache(input)) {
			monitor.worked(1);
			try {
				URI uri = getCacheURI(input);
				ResourceSet rs = new ResourceSetImpl();
				Resource resource = rs.getResource(uri, true);
				return (XamlDocument) resource.getContents().get(0);
			} catch (Exception e) {
				removeCache(input);
			} finally {
				monitor.worked(1);
			}
		}
		return null;
	}

	public static void doSaveCache(XamlDocument document, IFile input,
			IProgressMonitor monitor) {
		if (monitor == null)
			monitor = new NullProgressMonitor();
		monitor.beginTask("Save model to cache", 4);
		monitor.worked(1);
		if (document != null) {
			try {
				monitor.worked(1);
				Resource resource = document.eResource();
				URI cacheURI = getCacheURI(input);
				if (resource == null) {
					ResourceSet rs = new ResourceSetImpl();
					resource = rs.createResource(cacheURI);
					resource.getContents().add(document);
				}
				if (resource.getURI().equals(cacheURI)) {
					monitor.worked(1);
					resource.save(XML_CACHE_SAVE_OPTIONS);
				} else {
					File f = getCachedPath(input).toFile();
					monitor.worked(1);
					monitor.subTask("Save model to cache" + f.getName());
					FileOutputStream os = new FileOutputStream(f);
					resource.save(os, XML_CACHE_SAVE_OPTIONS);
					os.close();
				}
			} catch (RuntimeException e) {
			} catch (IOException e) {
				removeCache(input);
			}
		}
		monitor.done();
	}

	public static void removeCache(IFile file) {
		try {
			if (file != null) {
				getCachedPath(file).toFile().delete();
			}
		} catch (Exception e) {
		}
	}

	public static boolean isValidCache(IFile f) {
		File dest = getCachedPath(f).toFile();
		if (/* dest.exists() && */dest.canRead()) {
			if (dest.lastModified() > f.getLocalTimeStamp())
				return true;
		}
		return false;
	}
}
