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
package org.eclipse.e4.xwt.tools.ui.palette.page.editparts.figures;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PaletteDrawerFigure extends DrawerFigure {

	private static final int X_OFFSET = 17;

	/**
	 * @param control
	 */
	public PaletteDrawerFigure(Control control) {
		super(control);
	}

	public Rectangle getBounds() {
		return new Rectangle(bounds.x + X_OFFSET, bounds.y, bounds.width, bounds.height);
	}
}
