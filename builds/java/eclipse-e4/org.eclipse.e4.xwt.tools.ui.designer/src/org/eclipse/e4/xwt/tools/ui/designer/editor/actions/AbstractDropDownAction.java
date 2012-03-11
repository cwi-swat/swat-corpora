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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class AbstractDropDownAction extends SelectionAction implements IMenuCreator {

	protected MenuManager menuManager;

	public AbstractDropDownAction(IWorkbenchPart part) {
		super(part, AS_DROP_DOWN_MENU);
		setMenuCreator(this);
	}

	protected MenuManager getMenuManager() {
		if (menuManager == null) {
			menuManager = createMenuManager();
		}
		return menuManager;
	}

	protected abstract MenuManager createMenuManager();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		MenuManager mm = getMenuManager();
		if (mm != null) {
			return mm.createContextMenu(parent);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		MenuManager mm = getMenuManager();
		if (mm != null) {
			Menu menu = new Menu(parent);
			IContributionItem[] items = mm.getItems();
			for (int i = 0; i < items.length; i++) {
				IContributionItem item = items[i];
				IContributionItem newItem = item;
				if (item instanceof ActionContributionItem) {
					newItem = new ActionContributionItem(((ActionContributionItem) item).getAction());
				}
				newItem.fill(menu, -1);
			}
			return menu;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.SelectionAction#dispose()
	 */
	public void dispose() {
		super.dispose();
		if (menuManager != null) {
			menuManager.dispose();
			menuManager = null;
		}
	}
}
