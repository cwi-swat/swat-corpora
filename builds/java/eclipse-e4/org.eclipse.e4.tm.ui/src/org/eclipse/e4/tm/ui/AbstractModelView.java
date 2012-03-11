/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public abstract class AbstractModelView extends ViewPart {

	protected ModelPartContent modelContent;
	protected Composite modelComposite;

	public void createPartControl(Composite parent) {
		modelComposite = parent;
		modelContent = new ModelPartContent(modelComposite);
		modelContent.setModelContext(getModelContext());
		createActions();
		createMenu();
		createToolbar();
	}

	protected abstract ModelContext getModelContext();

	public void dispose() {
		getModelContext().dispose();
		if (modelContent != null) {
			modelContent.dispose();
		}
		super.dispose();
	}

	public void setFocus() {
	}

	//

	private IAction refreshAction;

	private void createActions() {
		refreshAction = new Action("Refresh") {
			public void run() {
				modelContent.contextChanged(getModelContext());
			}
		};
	}

	private void createMenu() {
		createMenu(getViewSite().getActionBars().getMenuManager());
	}

	protected void createMenu(IMenuManager mgr) {
		mgr.add(refreshAction);
	}

	private void createToolbar() {
		createToolbar(getViewSite().getActionBars().getToolBarManager());
	}

	protected void createToolbar(IToolBarManager mgr) {
		mgr.add(refreshAction);
	}
}
