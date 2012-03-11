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
package org.eclipse.e4.xwt.tools.ui.designer.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.parts.MenuItemEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.figures.Expandable;
import org.eclipse.e4.xwt.tools.ui.designer.utils.StyleHelper;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.widgets.MenuItem;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class MenuItemExpandableEditPolicy extends ExpandableEditPolicy {
	private ArrayList<Expandable> expandedFigures;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editpolicies.ExpandableEditPolicy#expand()
	 */
	public void expand() {
		// Retrieve expandables from editpart directly.
		List children = getHost().getChildren();
		if (children == null || children.isEmpty()) {
			return;
		}
		if (expandedFigures == null || expandedFigures.isEmpty()) {
			expandedFigures = new ArrayList<Expandable>();
			for (Object object : children) {
				if (!(object instanceof GraphicalEditPart)) {
					continue;
				}
				IFigure figure = ((GraphicalEditPart) object).getFigure();
				if (figure instanceof Expandable) {
					expandedFigures.add((Expandable) figure);
				}
			}
		}

		expandAll();
	}

	/**
	 * @param hostFigure
	 */
	private void expandAll() {
		IFigure hostFigure = getHostFigure();
		for (Expandable f : expandedFigures) {
			IFigure layer = getLayer();
			if (f.getParent() != layer) {
				layer.add(f);
			}
			Rectangle r = hostFigure.getBounds().getCopy();
			hostFigure.translateToAbsolute(r);
			if (r.isEmpty()) {
				continue;
			}
			MenuItem menuItem = (MenuItem) ((MenuItemEditPart) getHost()).getWidget();
			if (StyleHelper.isOnMenubar(menuItem)) {
				f.setLocation(new Point(r.x + 1, r.y + r.height));
			} else {
				f.setLocation(new Point(r.x + r.width, r.y));
			}
			f.expand();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editpolicies.ExpandableEditPolicy#collapse()
	 */
	public void collapse() {
		if (expandedFigures != null && !expandedFigures.isEmpty()) {
			for (Expandable figure : expandedFigures) {
				figure.collapse();
			}
		}
	}
}
