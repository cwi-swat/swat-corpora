/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui.builders;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.e4.emf.ecore.javascript.ScriptClassLoader;
import org.eclipse.emf.common.util.URI;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.optimizer.ClassCompiler;

public class JavascriptBuilder extends IncrementalProjectBuilder {

	public final static String BUILDER_NAME = "JavascriptBuilder";
	
	private List<IProject> dependentProjects = new ArrayList<IProject>();

	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		dependentProjects.clear();
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			fullBuild(monitor);
		} else if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD || kind == IncrementalProjectBuilder.AUTO_BUILD) {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return (IProject[])dependentProjects.toArray(new IProject[dependentProjects.size()]);
	}

	protected void clean(IProgressMonitor monitor) {
		try {
			super.clean(monitor);
			getProject().accept(cleanVisitor);
		} catch (CoreException e) {
		}
	}

	private void fullBuild(IProgressMonitor monitor) {
		try {
			getProject().accept(buildVisitor);
		} catch (CoreException e) {
		}
	}
	
	private IResourceVisitor buildVisitor = new IResourceVisitor() {
		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IContainer) {
				return true;
			} else if (resource instanceof IFile && changeShouldTriggerBuild((IFile)resource)) {
				build((IFile)resource);
			}
			return false;
		}
	};

	private IResourceVisitor cleanVisitor = new IResourceVisitor() {
		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IContainer) {
				return true;
			} else if (resource instanceof IFile) {
				clean((IFile)resource);
			}
			return false;
		}

	};
	
	protected boolean changeShouldTriggerBuild(IFile file) {
		return JavascriptSupport.JAVASCRIPT_EXTENSION.equals(file.getFileExtension());
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		delta.accept(buildDeltaVisitor);
	}
	
	private IResourceDeltaVisitor buildDeltaVisitor = new IResourceDeltaVisitor() {
		public boolean visit(IResourceDelta delta) throws CoreException {
			int kind = delta.getKind();
			if (kind == IResourceDelta.CHANGED || kind == IResourceDelta.ADDED) {
				return buildVisitor.visit(delta.getResource());
			}
			return false;
		}
	};

	protected void startupOnInitialize() {
		super.startupOnInitialize();
	}

	//

	private CompilerEnvirons compilerEnvironment = new CompilerEnvirons();
	private ClassCompiler compiler = new ClassCompiler(compilerEnvironment);

	private void clean(IFile file) throws CoreException {
		if (changeShouldTriggerBuild(file)) {
			file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		} else if ("class".equals(file.getFileExtension())) {
			String name = file.getName();
			if (ScriptClassLoader.hasUriClassNameMarker(name.substring(0, name.lastIndexOf('.')))) {
				try {
					file.delete(true, null);
				} catch (CoreException e1) {
				}
			}
		}
	}
	
//	private IFile getClassFile(IFile jsFile) {
//		String classFileName = ScriptClassLoader.getUriClassName(URI.createPlatformResourceURI(jsFile.getFullPath().toString(), true));
//		int pos = classFileName.lastIndexOf('.');
//		if (pos >= 0) {
//			classFileName = classFileName.substring(pos + 1);
//		}
//		return jsFile.getParent().getFile(new Path(classFileName + ".class"));
//	}
	
	protected void build(IFile jsFile) {
		String fileName = jsFile.getName();
		StringBuilder stringBuilder = new StringBuilder();
		Object[] compiled = null;
		try {
			jsFile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		} catch (CoreException e1) {
		}
		String fullClassName = ScriptClassLoader.getUriClassName(URI.createPlatformResourceURI(jsFile.getFullPath().toString(), true));
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(jsFile.getContents()));
			String line = null;
			while (reader.ready() && (line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
			compiled = compiler.compileToClassFiles(stringBuilder.toString(), fileName, 1, fullClassName);
		} catch (Exception e) {
			createMarker(jsFile, null, e.getMessage(), IMarker.SEVERITY_ERROR);
			return;
		}
		int pos = fullClassName.lastIndexOf('.');
		String packageName = (pos > 0 ? fullClassName.substring(0, pos + 1) : "");
        for (int i = 0; i < compiled.length; i += 2) {
            String className = (String)compiled[i];
            String classFileName = (className.startsWith(packageName) ? className.substring(packageName.length()) : className);
            IFile classFile = jsFile.getParent().getFile(new Path(classFileName + ".class"));
            byte[] bytes = (byte[])compiled[i + 1];
			try {
	            if (! classFile.exists()) {
	            	classFile.create(new ByteArrayInputStream(bytes), true, null);
	            } else {
	            	classFile.setContents(new ByteArrayInputStream(bytes), true, true, null);
	            }
			} catch (CoreException e) {
				createMarker(jsFile, null, e.getMessage(), IMarker.SEVERITY_ERROR);
			}
        }
	}

	//

	public static IMarker createMarker(IResource resource, String problemType, String message, int severity) {
		IMarker marker = null;
		try {
			marker = resource.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			int priority = IMarker.PRIORITY_NORMAL;
			if (severity == IMarker.SEVERITY_INFO) {
				priority = IMarker.PRIORITY_LOW;
			} else if (severity == IMarker.SEVERITY_ERROR) {
				priority = IMarker.PRIORITY_HIGH;
			}
			marker.setAttribute(IMarker.PRIORITY, priority);
		} catch (CoreException e) {
		}
		return marker;
	}
}
