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
package org.eclipse.e4.xwt.tools.ui.designer.editor.source;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.core.ceditor.ISourcePage;
import org.eclipse.e4.xwt.tools.ui.designer.model.XamlDocumentProvider;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTJavaEditor extends CompilationUnitEditor implements ISourcePage {

	private IType type;

	public IType getType() {
		return type;
	}

	public String getPageName() {
		return "Java";
	}

	public boolean isEnabledFor(IEditorPart editorPart) {
		IEditorInput editorInput = editorPart.getEditorInput();
		if (editorInput == null || !(editorInput instanceof FileEditorInput)) {
			return false;
		}
		IFile file = ((IFileEditorInput) editorInput).getFile();
		if (file == null || !file.exists()
				|| !"xwt".equals(file.getFileExtension())) {
			return false;
		}
		String clr = loadCLR(file);
		if (clr == null) {
			return false;
		}
		IProject project = file.getProject();
		IJavaProject create = JavaCore.create(project);
		try {
			type = create.findType(clr);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return type != null;
	}

	private String loadCLR(IFile file) {
		XamlDocument document = XamlDocumentProvider.getDocument(file);
		if (document == null || document.getRootElement() == null) {
			return null;
		}
		XamlElement root = document.getRootElement();
		XamlAttribute attribute = root.getAttribute("Class",
				IConstants.XWT_X_NAMESPACE);
		return attribute != null ? attribute.getValue() : null;
	}

	public void configureSourcePage(IEditorPart editorPart) {
		if (type == null) {
			return;
		}
		setInput(new FileEditorInput((IFile) type.getResource()));
	}
}
