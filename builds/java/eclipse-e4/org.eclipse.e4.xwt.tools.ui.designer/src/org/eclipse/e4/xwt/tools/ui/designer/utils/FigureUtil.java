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
package org.eclipse.e4.xwt.tools.ui.designer.utils;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FigureUtil {

	public static Point translateToRelative(EditPart parent, Point point) {
		if (!(parent instanceof GraphicalEditPart)) {
			return point.getCopy();
		}
		IFigure parentFigure = ((GraphicalEditPart) parent).getFigure();
		parentFigure.translateToRelative(point);
		Rectangle bounds = parentFigure.getBounds();
		int x = point.x - bounds.x;
		int y = point.y - bounds.y;
		return new Point(x, y);
	}

	public static Rectangle translateToRelative(EditPart parent,
			Rectangle rectangle) {
		Point location = rectangle.getLocation().getCopy();
		Point p = translateToRelative(parent, location);
		return new Rectangle(p, rectangle.getSize());
	}
}
