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

import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.DesignerActionConstants;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.DesignerMenuProvider;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.ChangeTextAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.LayoutAssistantAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.OpenBindingDialogAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.OpenExternalizeStringsAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.PreviewAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.StyleAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.SurroundWithAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.menus.EventMenuManager;
import org.eclipse.e4.xwt.tools.ui.designer.editor.menus.LayoutMenuManager;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class XWTDesignerMenuProvider extends DesignerMenuProvider {

	private static final String BINDINGS = "Bindings";

	private static final String EXTERNALIZE = "Externalize"; // add by xrchen
																// 2009/9/22

	private XWTDesigner designer;

	public XWTDesignerMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry, XWTDesigner designer) {
		super(viewer, actionRegistry);
		this.designer = designer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ContextMenuProvider#menuAboutToShow(org.eclipse.jface
	 * .action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager menu) {
		menu.add(new Separator(DesignerActionConstants.UNDO));
		menu.add(new Separator(DesignerActionConstants.DELETE));
		menu.add(new Separator(DesignerActionConstants.PRINT));
		menu.add(new Separator(DesignerActionConstants.COPY));
		menu.add(new Separator(DesignerActionConstants.EDIT));
		menu.add(new Separator(BINDINGS));
		menu.add(new Separator(EXTERNALIZE)); // add by xrchen 2009/9/22
		menu.add(new Separator(DesignerActionConstants.ADDITIONS));
		super.menuAboutToShow(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.xaml.ve.editor.EditorMenuProvider#buildContextMenu(org.eclipse
	 * .jface.action.IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menu) {
		super.buildContextMenu(menu);
		ActionRegistry actionRegistry = getActionRegistry();
		// menu.appendToGroup(BINDINGS,
		// actionRegistry.getAction(BindingLayerAction.ID));
		List<?> selectedEditParts = getViewer().getSelectedEditParts();
		if (selectedEditParts == null || selectedEditParts.isEmpty()) {
			// Diagram directly...
		} else {
			if (selectedEditParts.size() == 1) {
				menu.appendToGroup(DesignerActionConstants.PRINT,
						actionRegistry.getAction(PreviewAction.ACTION_ID));

				EditPart editPart = (EditPart) selectedEditParts.get(0);
				if (designer.getEventHandler() != null) {
					EventMenuManager eventPopMenu = new EventMenuManager(
							editPart, designer);
					menu.add(eventPopMenu);
					menu.add(new Separator());
				}
				LayoutMenuManager layoutPopMenu = new LayoutMenuManager(
						editPart, "Set Layout");
				menu.appendToGroup(DesignerActionConstants.EDIT, actionRegistry
						.getAction(ChangeTextAction.ID));
				menu.add(layoutPopMenu);

				menu.add(actionRegistry.getAction(LayoutAssistantAction.ID));

				// menu.appendToGroup(BINDINGS, new
				// BindingsMenuManager(editPart));
				menu.appendToGroup(BINDINGS, actionRegistry
						.getAction(OpenBindingDialogAction.ID));

				if (editPart instanceof WidgetEditPart) {
					menu.appendToGroup(DesignerActionConstants.EDIT,
							new StyleAction((WidgetEditPart) editPart));
				}
				// Single selection...
			} else {
				// Multi-Selection.
			}
			menu.appendToGroup(DesignerActionConstants.ADDITIONS,
					actionRegistry.getAction(SurroundWithAction.ID));
		}
		// add by xrchen 2009/9/22
		menu.appendToGroup(EXTERNALIZE, actionRegistry
				.getAction(OpenExternalizeStringsAction.ID));
	}
}
