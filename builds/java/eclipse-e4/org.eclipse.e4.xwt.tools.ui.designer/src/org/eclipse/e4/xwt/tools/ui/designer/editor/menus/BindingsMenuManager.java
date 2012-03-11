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

import org.eclipse.e4.xwt.tools.ui.designer.databinding.Observable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.ObservableUtil;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Property;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.CreateBindingAction;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BindingsMenuManager extends MenuManager {

	private EditPart editPart;

	public BindingsMenuManager(EditPart editPart) {
		super("Bindings");
		this.editPart = editPart;
		if (editPart != null) {
			createMenus(this);
		}
	}

	private void createMenus(MenuManager parent) {
		if (!(editPart instanceof WidgetEditPart)) {
			return;
		}
		Widget observe = ((WidgetEditPart) editPart).getWidget();
		if (observe == null || observe.isDisposed()) {
			return;
		}
		Observable observable = ObservableUtil.getObservable(observe);
		Property[] properties = observable.getProperties();
		for (Property property : properties) {
			Image image = ImageShop.getImageForType(property.getType());
			parent.add(new CreateBindingAction(editPart, observe, property.getName(), image));
		}
	}
}
