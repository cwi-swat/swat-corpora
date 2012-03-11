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
package org.eclipse.e4.xwt.tools.ui.designer.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTJavaEditor extends CompilationUnitEditor {
	private IJavaProject javaProject;
	private String hostClassName;
	private IType type;
	private IEditorInput javaEditorInput;

	public XWTJavaEditor(IJavaProject javaProject, String hostClassName) {
		this.javaProject = javaProject;
		this.hostClassName = hostClassName;
		configureJavaEditor();
	}

	/**
	 * setup editorinput.
	 */
	private void configureJavaEditor() {
		try {
			type = javaProject.findType(hostClassName);
			if (type == null || !type.exists()) {
				return;
			}
			IFile javaFile = (IFile) type.getResource();
			this.javaEditorInput = new FileEditorInput(javaFile);
		} catch (JavaModelException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#getEditorInput()
	 */
	public IEditorInput getEditorInput() {
		IEditorInput editorInput = super.getEditorInput();
		if (editorInput == null) {
			return javaEditorInput;
		}
		return editorInput;
	}

	public IType getType() {
		return type;
	}
}
