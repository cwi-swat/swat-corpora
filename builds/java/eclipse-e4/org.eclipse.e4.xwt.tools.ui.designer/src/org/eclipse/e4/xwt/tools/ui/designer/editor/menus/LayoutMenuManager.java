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
package org.eclipse.e4.xwt.tools.ui.designer.editor.menus;

import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.ChangeLayoutAction;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.MenuManager;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class LayoutMenuManager extends MenuManager {

	private EditPart editPart;

	public LayoutMenuManager(EditPart editPart, String text) {
		super(text);
		this.editPart = editPart;
		if (LayoutsHelper.canSetLayout(editPart)) {
			initMenus();
		}
	}

	private void initMenus() {
		for (LayoutType layout : LayoutsHelper.layoutsList) {
			add(new ChangeLayoutAction(editPart, layout));
		}
	}
}
