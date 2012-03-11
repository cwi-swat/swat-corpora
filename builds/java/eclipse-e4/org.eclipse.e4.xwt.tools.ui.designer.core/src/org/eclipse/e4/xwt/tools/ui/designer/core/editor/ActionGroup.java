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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ActionGroup {

	public static final int SELECTION_GRP = 0;
	public static final int PROPERTY_GRP = 1;
	public static final int STACK_GRP = 2;

	private IEditorPart editor;
	private ActionRegistry actionRegistry;
	private List<String> selectionActions = new ArrayList<String>();
	private List<String> stackActions = new ArrayList<String>();
	private List<String> propertyActions = new ArrayList<String>();

	public ActionGroup(IEditorPart editor) {
		this.editor = editor;
	}

	/**
	 * @return the actionRegistry
	 */
	public ActionRegistry getActionRegistry() {
		if (actionRegistry == null) {
			actionRegistry = (ActionRegistry) editor.getAdapter(ActionRegistry.class);
		}
		return actionRegistry;
	}

	public void createActions() {
		IAction action;

		action = new UndoAction(editor);
		action.setText("&Undo \tCTRL+Z");
		getActionRegistry().registerAction(action);
		getStackActions().add(action.getId());

		action = new RedoAction(editor);
		action.setText("&Redo \tCTRL+Y");
		getActionRegistry().registerAction(action);
		getStackActions().add(action.getId());

		action = new SelectAllAction(editor);
		getActionRegistry().registerAction(action);

		DeleteAction deleteAction = new DeleteAction((IWorkbenchPart) editor);
		deleteAction.setAccelerator(Integer.valueOf(SWT.DEL));
		getActionRegistry().registerAction(deleteAction);
		getSelectionActions().add(deleteAction.getId());
	}

	/**
	 * Returns the list of <em>IDs</em> of Actions that are dependant on changes in the workbench's {@link ISelectionService}. The associated Actions can be found in the action registry. Such actions should implement the {@link UpdateAction} interface so that they can be updated in response to selection changes.
	 * 
	 * @see #updateActions(List)
	 * @return the list of selection-dependant action IDs
	 */
	protected List<String> getSelectionActions() {
		return selectionActions;
	}

	/**
	 * Returns the list of {@link IAction IActions} dependant on property changes in the Editor. These actions should implement the {@link UpdateAction} interface so that they can be updated in response to property changes. An example is the "Save" action.
	 * 
	 * @return the list of property-dependant actions
	 */
	protected List<String> getPropertyActions() {
		return propertyActions;
	}

	/**
	 * Returns the list of <em>IDs</em> of Actions that are dependant on the CommmandStack's state. The associated Actions can be found in the action registry. These actions should implement the {@link UpdateAction} interface so that they can be updated in response to command stack changes. An example is the "undo" action.
	 * 
	 * @return the list of stack-dependant action IDs
	 */
	protected List<String> getStackActions() {
		return stackActions;
	}

	public void updateActions(int gourp) {
		switch (gourp) {
		case SELECTION_GRP:
			updateActions(selectionActions);
			break;
		case PROPERTY_GRP:
			updateActions(propertyActions);
			break;
		case STACK_GRP:
			updateActions(stackActions);
			break;
		}
	}

	/**
	 * A convenience method for updating a set of actions defined by the given List of action IDs. The actions are found by looking up the ID in the {@link #getActionRegistry() action registry}. If the corresponding action is an {@link UpdateAction}, it will have its <code>update()</code> method called.
	 * 
	 * @param actionIds
	 *            the list of IDs to update
	 */
	protected void updateActions(List<String> actionIds) {
		ActionRegistry registry = getActionRegistry();
		for (String actionId : actionIds) {
			IAction action = registry.getAction(actionId);
			if (action instanceof UpdateAction) {
				((UpdateAction) action).update();
				if (action instanceof RedoAction) {
					RedoAction redoAction = (RedoAction) action;
					String text = redoAction.getText();
					if (!text.endsWith("\tCtrl+Y")) {
						if (text.endsWith("Ctrl+Y")) {
							text = text.substring(0, text.length() - 6);
						}
						redoAction.setText(text + "\tCtrl+Y");					
					}
				}
				else if (action instanceof UndoAction) {
					UndoAction undoAction = (UndoAction) action;
					String text = undoAction.getText();
					if (!text.endsWith("\tCtrl+Z")) {
						if (text.endsWith("Ctrl+Z")) {
							text = text.substring(0, text.length() - 6);
						}
						undoAction.setText(text + "\tCtrl+Z");					
					}
				}
			}
		}
	}

	public Action getAction(String actionId) {
		return null;
	}
}
