/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.workbench.swt.modeling;

import javax.inject.Inject;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.workbench.swt.factories.IRendererFactory;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class MenuService implements EMenuService {
	@Inject
	private MPart myPart;

	public MPopupMenu registerContextMenu(Object parent, String menuId) {
		if (!(parent instanceof Control)) {
			return null;
		}
		Control parentControl = (Control) parent;
		for (MMenu mmenu : myPart.getMenus()) {
			if (menuId.equals(mmenu.getElementId())
					&& mmenu instanceof MPopupMenu) {
				Menu menu = registerMenu(parentControl, (MPopupMenu) mmenu,
						myPart);
				if (menu != null) {
					parentControl.setMenu(menu);
					return (MPopupMenu) mmenu;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	public static Menu registerMenu(final Control parentControl,
			final MPopupMenu mmenu, final MPart part) {
		if (mmenu.getWidget() != null) {
			return (Menu) mmenu.getWidget();
		}
		// we need to delegate to the renderer so that it "processes" the
		// MenuManager correctly
		IRendererFactory rendererFactory = part.getContext().get(
				IRendererFactory.class);
		AbstractPartRenderer renderer = rendererFactory.getRenderer(mmenu,
				parentControl);
		IEclipseContext popupContext = part.getContext().createChild(
				"popup:" + mmenu.getElementId());
		mmenu.setContext(popupContext);
		Object widget = renderer.createWidget(mmenu, parentControl);
		if (!(widget instanceof Menu)) {
			return null;
		}
		renderer.bindWidget(mmenu, widget);
		renderer.hookControllerLogic(mmenu);

		// Process its internal structure through the renderer that created
		// it
		Object castObject = mmenu;
		renderer.processContents((MElementContainer<MUIElement>) castObject);

		// Allow a final chance to set up
		renderer.postProcess(mmenu);

		// Now that we have a widget let the parent (if any) know
		if (mmenu.getParent() instanceof MUIElement) {
			MElementContainer<MUIElement> parentElement = mmenu.getParent();
			AbstractPartRenderer parentRenderer = rendererFactory.getRenderer(
					parentElement, null);
			if (parentRenderer != null)
				parentRenderer.childRendered(parentElement, mmenu);
		}

		return (Menu) widget;
	}
}
