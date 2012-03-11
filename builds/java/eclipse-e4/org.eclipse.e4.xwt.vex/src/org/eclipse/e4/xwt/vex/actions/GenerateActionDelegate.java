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
package org.eclipse.e4.xwt.vex.actions;

import org.eclipse.e4.xwt.vex.VEXEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class GenerateActionDelegate implements IEditorActionDelegate {
	protected VEXEditor editor;

	public void run(IAction action) {
		if (editor != null) {
			editor.generateCLRCodeAction();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(editor != null);
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof StructuredTextEditor) {
			StructuredTextEditor structuredTextEditor = (StructuredTextEditor) targetEditor;
			IEditorPart editorPart = structuredTextEditor.getEditorPart();
			if (editorPart instanceof VEXEditor) {
				editor = (VEXEditor) editorPart;
			}
		}
	}
}
