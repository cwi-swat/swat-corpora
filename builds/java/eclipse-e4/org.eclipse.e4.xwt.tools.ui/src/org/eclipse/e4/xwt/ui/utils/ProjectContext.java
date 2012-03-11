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

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;

public class ProjectContext extends LoadingContext {
	protected static WeakHashMap<String, ProjectContext> contexts = new WeakHashMap<String, ProjectContext>();

	protected static IResourceChangeListener changeListener = new IResourceChangeListener() {
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			if (!contexts.isEmpty() && delta != null) {
				processDelta(delta);
			}
		}

		protected void processDelta(IResourceDelta delta) {
			for (IResourceDelta resourceDelta : delta.getAffectedChildren()) {
				IProject project = resourceDelta.getResource().getProject();
				if (project != null && project.exists()) {
					String name = project.getName();
					ProjectContext projectContext = contexts.get(name);
					if (projectContext != null) {
						projectContext.processDelta(delta);
						continue;
					}
				}
				processDelta(resourceDelta);
			}
		}
	};

	protected IJavaProject javaProject;
	protected HashSet<String> classes = new HashSet<String>();

	class RuntimeLoader extends ClassLoader {

		public RuntimeLoader(ClassLoader parent) {
			super(parent);
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			Class<?> type = null;
			try {
				type = super.findClass(name);
			} catch (ClassNotFoundException e) {
				if (type == null) {
					type = redefined(name);
				}
				if (type == null) {
					throw e;
				} else {
					classes.add(name);
				}
			}
			return type;
		}

		public Class<?> redefined(String name) {
			try {
				byte[] content = ClassLoaderHelper.getClassContent(ProjectContext.this.javaProject, name);
				if (content != null) {
					return defineClass(name, content, 0, content.length);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected URL findResource(String name) {
			URL url = super.findResource(name);
			if (url == null) {
				url = ClassLoaderHelper.getResourceAsURL(ProjectContext.this.javaProject, name);

			}
			return url;
		}
	}

	private ProjectContext(IJavaProject javaProject) {
		this.javaProject = javaProject;
		resetLoader();
	}

	protected void resetLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		setClassLoader(new RuntimeLoader(classLoader));
		firePropertyChangeListener(new PropertyChangeEvent(this, "ClassLoader", null, getClassLoader()));
	}

	protected boolean processDelta(IResourceDelta delta) {
		IResourceDelta[] resourceDeltas = delta.getAffectedChildren();
		for (IResourceDelta resourceDelta : resourceDeltas) {
			switch (resourceDelta.getKind()) {
			case IResourceDelta.CHANGED:
			case IResourceDelta.MOVED_FROM:
			case IResourceDelta.MOVED_TO:
				IPath path = resourceDelta.getProjectRelativePath();
				if (!path.isEmpty() && "class".equals(path.getFileExtension())) {
					resetLoader();
					return true;
				}
				break;
			}
			for (IResourceDelta childDelta : resourceDelta.getAffectedChildren()) {
				IPath path = childDelta.getProjectRelativePath();
				if (!path.isEmpty() && "class".equals(path.getFileExtension())) {
					resetLoader();
					return true;
				}
				if (processDelta(childDelta)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ProjectContext getContext(IJavaProject javaProject) {
		String elementName = javaProject.getElementName();
		ProjectContext context = contexts.get(elementName);
		if (context == null) {
			context = new ProjectContext(javaProject);
			contexts.put(elementName, context);
		}
		return context;
	}

	static public void start() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(changeListener);
	}

	static public void stop() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(changeListener);
	}
}
