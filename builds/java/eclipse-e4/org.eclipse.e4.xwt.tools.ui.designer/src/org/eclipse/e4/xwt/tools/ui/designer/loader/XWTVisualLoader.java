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
package org.eclipse.e4.xwt.tools.ui.designer.loader;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.xwt.XWTLoader;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.editor.model.XWTModelBuilder;
import org.eclipse.e4.xwt.tools.ui.designer.loader.metadata.HeightProperty;
import org.eclipse.e4.xwt.tools.ui.designer.loader.metadata.WidthProperty;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.e4.xwt.utils.PathHelper;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class XWTVisualLoader extends XWTLoader {

	private static IProperty widthProperty;
	private static IProperty heightProperty;
	private IFile file;

	public XWTVisualLoader(IFile file) {
		if (file != null && file.exists()) {
			IJavaProject project = JavaCore.create(file.getProject());
			if (project != null && project.exists()) {
				ProjectContext context = ProjectContext.getContext(project);
				setLoadingContext(context);
			}
			this.file = file;
		}
	}

	public URL getResourcePath() {
		if (file != null && file.exists()) {
			try {
				return file.getLocation().toFile().toURI().toURL();
			} catch (MalformedURLException e) {
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.XWTLoader#loadWithOptions(java.io.InputStream, java.net.URL, java.util.Map)
	 */
	public synchronized Object loadWithOptions(InputStream stream, URL base, Map<String, Object> options)
			throws Exception {
		// FIXME:
		return super.loadWithOptions(stream, base, options);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.XWTLoader#loadWithOptions(java.net.URL, java.util.Map)
	 */
	public synchronized Control loadWithOptions(URL url, Map<String, Object> options) throws Exception {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		String fileStr = url.getFile();
		if (fileStr.indexOf(PathHelper.WHITE_SPACE_ASCII) != -1) {
			fileStr = fileStr.replace(PathHelper.WHITE_SPACE_ASCII, " ");
		}
		IFile file = root.getFileForLocation(new Path(fileStr));
		if (file != null) {
			try {
				// the url given an binary file of project, we need find the source file of it and the load and open.
				IProject project = file.getProject();
				String fullPath = file.getFullPath().toString();
				IJavaProject javaProject = JavaCore.create(project);
				String outputPath = javaProject.getOutputLocation().toString();
				if (fullPath != null && outputPath != null && fullPath.startsWith(outputPath)) {
					String fileSourcePath = fullPath.substring(outputPath.length());
					IPackageFragmentRoot[] allPackageFragmentRoots = javaProject.getAllPackageFragmentRoots();
					for (IPackageFragmentRoot pRoot : allPackageFragmentRoots) {
						if (pRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
							IFolder resource = (IFolder) pRoot.getResource();
							IFile sourceFile = resource.getFile(new Path(fileSourcePath));
							if (sourceFile != null && sourceFile.exists()) {
								file = sourceFile;
								break;
							}
						}
					}
				}
			} catch (Exception e) {
			}
		}
		if (file != null) {
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart activeEditor = activePage.getActiveEditor();
			try {
				XWTDesigner designer = (XWTDesigner) activePage.openEditor(new FileEditorInput(file),
						XWTDesigner.EDITOR_ID, false);
				XamlDocument xamlDocument = (XamlDocument) designer.getDocumentRoot();
				XWTModelBuilder builder = null;
				if (xamlDocument == null) {
					builder = new XWTModelBuilder();
					builder.doLoad(designer, null);
					xamlDocument = builder.getDiagram();
				}
				Control control = (Control) new XWTProxy(file).load(xamlDocument.getRootElement(), options);
				if (builder != null) {
					builder.dispose();
				}
				return control;
			} finally {
				activePage.activate(activeEditor);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.XWTLoader#registerMetaclass(java.lang.Class)
	 */
	public synchronized IMetaclass registerMetaclass(Class<?> type) {
		IMetaclass metaclass = super.registerMetaclass(type);
		if (metaclass != null && type != null && Control.class.isAssignableFrom(type)) {
			IProperty[] properties = metaclass.getProperties();
			if (properties != null) {
				if (widthProperty == null) {
					widthProperty = new WidthProperty();
				}
				if (heightProperty == null) {
					heightProperty = new HeightProperty();
				}
				List<IProperty> propList = Arrays.asList(properties);
				if (!propList.contains(widthProperty)) {
					metaclass.addProperty(widthProperty);
				}
				if (!propList.contains(heightProperty)) {
					metaclass.addProperty(heightProperty);
				}
			}
		}
		return metaclass;
	}
}
