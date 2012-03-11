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
package org.eclipse.e4.xwt.tools.ui.designer.core.problems;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ConfigurableProblemHandler implements ProblemHandler {

	private static final String MARKS_ID = "org.eclipse.e4.xwt.tools.ui.designer.markers";

	private Designer designer;
	private boolean hasProblems;
	private List<IProblemChecker> checkers;

	public ConfigurableProblemHandler(Designer designer) {
		this.designer = designer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.problems.ProblemHandler#handle(java.util.List)
	 */
	public void handle(List<Problem> problems) {
		if (!isValidate()) {
			return;
		}
		// 1. delete old ones.
		clear();

		hasProblems = problems != null && !problems.isEmpty();

		// 2. create new ones.
		if (!hasProblems) {
			return;
		}
		try {
			IFile file = designer.getFile();
			for (Problem problem : problems) {
				makeMaker(file, problem);
			}
			file.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
		}
	}

	private void makeMaker(IFile file, Problem problem) {
		try {
			IMarker marker = file.createMarker(MARKS_ID);
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

	public void clear() {
		IFile file = designer.getFile();
		try {
			file.deleteMarkers(MARKS_ID, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.problems.ProblemHandler#isValidate()
	 */
	public boolean isValidate() {
		return designer != null && designer.getFile() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.problems.ProblemHandler#handle()
	 */
	public void handle() {
		if (checkers == null) {
			checkers = ProblemCheckerRegistry.INSTANCE.getCheckers(designer.getSite().getPluginId());
		}
		if (checkers != null) {
			List<Problem> problems = new ArrayList<Problem>();
			for (IProblemChecker checker : checkers) {
				if (!checker.isAdapterFor(designer)) {
					continue;
				}
				List<Problem> result = checker.doCheck();
				if (result != null && !result.isEmpty()) {
					problems.addAll(result);
				}
			}
			handle(problems);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.problems.ProblemHandler#hasProblems()
	 */
	public boolean hasProblems() {
		handle();
		return hasProblems;
	}
}
