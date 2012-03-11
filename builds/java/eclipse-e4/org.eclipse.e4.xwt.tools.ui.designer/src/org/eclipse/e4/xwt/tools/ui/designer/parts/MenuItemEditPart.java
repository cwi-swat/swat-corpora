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
package org.eclipse.e4.xwt.tools.ui.designer.parts;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ContentPaneFigure;
import org.eclipse.e4.xwt.tools.ui.designer.loader.ResourceVisitor;
import org.eclipse.e4.xwt.tools.ui.designer.parts.figures.MenuItemFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.MenuItemExpandableEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.PolicyConstants;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MenuItemEditPart extends WidgetEditPart {

	public MenuItemEditPart(MenuItem item, XamlNode model) {
		super(item, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.editor.editparts.GraphicalNodeEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		ContentPaneFigure figure = new ContentPaneFigure();
		figure.setContentPane(new MenuItemFigure(this));
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editparts.WidgetEditPart#collectExternalModels()
	 */
	protected void collectExternalModels(List<Object> collector) {
		MenuItem menuItem = (MenuItem) getWidget();
		if (menuItem != null) {
			Menu menu = menuItem.getMenu();
			if (menu != null) {
				Object data = menu.getData(ResourceVisitor.ELEMENT_KEY);
				if (data != null && data instanceof XamlElement) {
					collector.add(data);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#removeChildVisual(org.eclipse.gef.EditPart)
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		if (child.getParent() != null) {
			super.removeChildVisual(childEditPart);
		}
		// TODO: handle expandable items.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.editor.editparts.GraphicalNodeEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(PolicyConstants.EXPANDABLE_EDITPOLICY, new MenuItemExpandableEditPolicy());
	}
}
