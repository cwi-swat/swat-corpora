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
package org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWTException;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class GridLayoutColumnFigure extends Figure {
	private Rectangle columnBounds;

	public GridLayoutColumnFigure(Rectangle columnBounds) {
		super();
		this.columnBounds = columnBounds;
		setBounds(columnBounds.getCopy().expand(6, 8));
	}

	public void paintFigure(Graphics g) {
		try {
			g.setAlpha(150);
		} catch (SWTException e) {
			// For OS platforms that don't support alpha
		}
		int[] polygonPoints = new int[] { bounds.x + 1, bounds.y + 1, // left upper corner
				columnBounds.x, columnBounds.y, columnBounds.x, columnBounds.y + columnBounds.height, bounds.x + 1, bounds.y + bounds.height - 1, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1, columnBounds.x + columnBounds.width, columnBounds.y + columnBounds.height, columnBounds.x + columnBounds.width, columnBounds.y, bounds.x + bounds.width - 1, bounds.y + 1, bounds.x + 1, bounds.y + 1 };
		g.setBackgroundColor(ColorConstants.red);
		g.fillPolygon(polygonPoints);
		g.setBackgroundColor(ColorConstants.black);
		g.drawPolygon(polygonPoints);
	}
}
