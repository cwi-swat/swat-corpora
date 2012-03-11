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
package org.eclipse.e4.xwt.ui.editor.treeviewer;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLTableTreeViewer;

public class XWTTableTreeViewer extends XMLTableTreeViewer {

	public XWTTableTreeViewer(Composite parent) {
		super(parent);
	}

	class NodeActionMenuListener implements IMenuListener {
		public void menuAboutToShow(IMenuManager menuManager) {
			// used to disable NodeSelection listening while running
			// NodeAction
			XWTNodeActionManager nodeActionManager = new XWTNodeActionManager(((IDOMDocument) getInput()).getModel(), XWTTableTreeViewer.this) {
			};
			nodeActionManager.fillContextMenu(menuManager, getSelection());
		}
	}

	/**
	 * This creates a context menu for the viewer and adds a listener as well registering the menu for extension.
	 */
	protected void createContextMenu() {
		MenuManager contextMenu = new MenuManager("#PopUp"); //$NON-NLS-1$
		contextMenu.add(new Separator("additions")); //$NON-NLS-1$
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(new NodeActionMenuListener());
		Menu menu = contextMenu.createContextMenu(getControl());
		getControl().setMenu(menu);
	}
}
