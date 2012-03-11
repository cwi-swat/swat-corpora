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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class DesignerMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	public DesignerMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;
	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ContextMenuProvider#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager menu) {
		menu.add(new Separator(DesignerActionConstants.UNDO));
		menu.add(new Separator(DesignerActionConstants.DELETE));
		menu.add(new Separator(DesignerActionConstants.PRINT));
		menu.add(new Separator(DesignerActionConstants.COPY));
		menu.add(new Separator(DesignerActionConstants.EDIT));
		menu.add(new Separator(DesignerActionConstants.ACTIONS));
		menu.add(new Separator(DesignerActionConstants.ADDITIONS));
		super.menuAboutToShow(menu);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menu) {
		ActionRegistry actionRegistry = getActionRegistry();
		IAction action = actionRegistry.getAction(ActionFactory.REDO.getId());
		if (action != null) {
			menu.appendToGroup(DesignerActionConstants.UNDO, action);
		}
		action = actionRegistry.getAction(ActionFactory.UNDO.getId());
		if (action != null) {
			menu.appendToGroup(DesignerActionConstants.UNDO, action);
		}
		action = actionRegistry.getAction(ActionFactory.DELETE.getId());
		if (action != null) {
			menu.appendToGroup(DesignerActionConstants.DELETE, action);
		}
		action = actionRegistry.getAction(ActionFactory.COPY.getId());
		if (action != null) {
			menu.appendToGroup(DesignerActionConstants.COPY, action);
		}
		action = actionRegistry.getAction(ActionFactory.PASTE.getId());
		if (action != null) {
			menu.appendToGroup(DesignerActionConstants.COPY, action);
		}
		action = actionRegistry.getAction(ActionFactory.CUT.getId());
		if (action != null) {
			menu.appendToGroup(DesignerActionConstants.COPY, action);
		}
		action = actionRegistry.getAction(ActionFactory.SELECT_ALL.getId());
		if (action != null) {
			menu.appendToGroup(DesignerActionConstants.EDIT, action);
		}
	}

}
