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
package org.eclipse.e4.xwt.tools.ui.palette.page.editparts;

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.xwt.tools.ui.palette.page.editparts.figures.PaletteDrawerFigure;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.palette.PaletteDrawer;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
@SuppressWarnings("restriction")
public class PaletteDrawerEditPart extends DrawerEditPart {

	/**
	 * @param drawer
	 */
	public PaletteDrawerEditPart(PaletteDrawer drawer) {
		super(drawer);
	}

	public IFigure createFigure() {
		if (getParent() instanceof PaletteDrawerEditPart) {
			PaletteDrawerFigure fig = new PaletteDrawerFigure(getViewer().getControl());
			fig.setExpanded(getDrawer().isInitiallyOpen());
			fig.setPinned(getDrawer().isInitiallyPinned());
			fig.getCollapseToggle().addFocusListener(new FocusListener.Stub() {
				public void focusGained(FocusEvent fe) {
					getViewer().select(PaletteDrawerEditPart.this);
				}
			});
			return fig;
		}
		return super.createFigure();
	}
}
