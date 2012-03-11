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

package org.eclipse.e4.xwt.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.e4.xwt.vex.VEXFileChecker;
import org.eclipse.e4.xwt.vex.problems.IProblemChecker;
import org.eclipse.e4.xwt.vex.problems.Problem;
import org.eclipse.e4.xwt.vex.problems.ProblemCheckerRegistry;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * @author jliu
 * 
 */
public class XWTFileChecker implements VEXFileChecker {

	public static final String EXTENSION_ID = "org.eclipse.e4.xwt.tools.ui.editor.problemchecker";
	private final String MARKER_ID = XWTEditorPlugin.PLUGIN_ID + ".XAMLEditorMaker";
	private StructuredTextEditor fTextEditor = null;

	public XWTFileChecker(StructuredTextEditor fTextEditor) {
		this.fTextEditor = fTextEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.vex.VEXFileChecker#doCheck(java.lang.String)
	 */
	public void doCheck(String javaName) {
		ProjectContext.stop();
		deleteMarkers();

		List<IProblemChecker> allCheckers = ProblemCheckerRegistry.INSTANCE.getCheckers(EXTENSION_ID);
		if (allCheckers.isEmpty()) {
			return;
		}

		List<Problem> problems = new ArrayList<Problem>();
		for (IProblemChecker checker : allCheckers) {
			if (!checker.canChecked(fTextEditor, javaName)) {
				continue;
			}
			List<Problem> subs = checker.checkProblems(fTextEditor, javaName);
			if (subs != null) {
				problems.addAll(subs);
			}
		}
		if (problems.isEmpty()) {
			return;
		}
		IFile file = (IFile) fTextEditor.getEditorInput().getAdapter(IFile.class);
		for (Problem problem : problems) {
			makeMaker(file, problem);
		}
		ProjectContext.start();
	}

	private void makeMaker(IFile file, Problem problem) {
		try {
			IMarker marker = file.createMarker(MARKER_ID);
			marker.setAttribute(IMarker.TRANSIENT, true);
			marker.setAttribute(IMarker.MESSAGE, problem.getMessage());
			marker.setAttribute(IMarker.CHAR_START, problem.start);
			marker.setAttribute(IMarker.CHAR_END, problem.end);
			int type = problem.getType();
			switch (type) {
			case Problem.ERROR:
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				break;
			case Problem.INFO:
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
				break;
			case Problem.WARNING:
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				break;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, problem.line);
		} catch (CoreException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.vex.VEXFileChecker#deleteMarkers()
	 */
	public void deleteMarkers() {
		IFile file = (IFile) fTextEditor.getEditorInput().getAdapter(IFile.class);
		try {
			file.deleteMarkers(null, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
		}
	}

}
