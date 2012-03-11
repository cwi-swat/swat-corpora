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

import org.eclipse.e4.xwt.ui.utils.ImageManager;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors. Responsible for the redirection of global actions to the active editor. Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class XWTEditorContributor extends MultiPageEditorActionBarContributor {
	private IEditorPart activeEditorPart;
	private Action previewAction;
	private Action javaAction;

	/**
	 * Creates a multi-page contributor.
	 */
	public XWTEditorContributor() {
		super();
		createActions();
	}

	/**
	 * Returns the action registed with the given text editor.
	 * 
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(ITextEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}

	/*
	 * (non-JavaDoc) Method declared in AbstractMultiPageEditorActionBarContributor.
	 */

	public void setActivePage(IEditorPart part) {
		if (activeEditorPart == part)
			return;

		activeEditorPart = part;

		IActionBars actionBars = getActionBars();
		if (actionBars != null) {

			ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part : null;

			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), getAction(editor, ITextEditorActionConstants.DELETE));
			actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getAction(editor, ITextEditorActionConstants.UNDO));
			actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), getAction(editor, ITextEditorActionConstants.REDO));
			actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), getAction(editor, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), getAction(editor, ITextEditorActionConstants.COPY));
			actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), getAction(editor, ITextEditorActionConstants.PASTE));
			actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), getAction(editor, ITextEditorActionConstants.FIND));
			actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(), getAction(editor, IDEActionFactory.BOOKMARK.getId()));
			actionBars.updateActionBars();
		}
		previewAction.setEnabled(part != null);
		javaAction.setEnabled(part != null);
	}

	private void createActions() {
		previewAction = new Action() {
			public void run() {
				if (activeEditorPart instanceof StructuredTextEditor) {
					StructuredTextEditor structuredTextEditor = (StructuredTextEditor) activeEditorPart;
					IEditorPart editorPart = structuredTextEditor.getEditorPart();
					if (editorPart instanceof XWTEditor) {
						XWTEditor editor = (XWTEditor) editorPart;
						editor.handlePreview();
					}
				}
				// MessageDialog.openInformation(null, "Editors Plug-in",
				// "Sample Action Executed");
			}
		};
		previewAction.setText("Preview");
		previewAction.setToolTipText("Preview");
		previewAction.setImageDescriptor(ImageManager.OBJ_PREVIEW);

		javaAction = new Action() {
			public void run() {
				if (activeEditorPart instanceof StructuredTextEditor) {
					StructuredTextEditor structuredTextEditor = (StructuredTextEditor) activeEditorPart;
					IEditorPart editorPart = structuredTextEditor.getEditorPart();
					if (editorPart instanceof XWTEditor) {
						XWTEditor editor = (XWTEditor) editorPart;
						editor.generateCLRCodeAction();
					}
				}
			}
		};
		javaAction.setText("Java");
		javaAction.setToolTipText("Preview");
		javaAction.setImageDescriptor(JavaPluginImages.DESC_OBJS_CLASS);
	}

	public void contributeToMenu(IMenuManager manager) {
		IMenuManager menu = new MenuManager("XWT");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(previewAction);
		menu.add(javaAction);
	}

	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
	}
}
