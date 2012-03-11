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
package org.eclipse.e4.xwt.tools.ui.designer.core.parts.tools;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FocusBorder extends LineBorder {

	public FocusBorder() {
		setColor(ColorConstants.blue);
		setWidth(1);
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		if (figure instanceof SelectionHandle) {
			IFigure ownerFigure = ((SelectionHandle) figure).getOwnerFigure();
			Rectangle r = ownerFigure.getBounds().getCopy();
			tempRect.setBounds(r);
		} else {
			Rectangle paintRectangle = getPaintRectangle(figure, insets);
			tempRect.setBounds(paintRectangle);
		}
		if (getWidth() % 2 != 0) {
			tempRect.width--;
			tempRect.height--;
		}
		tempRect.shrink(getWidth() / 2, getWidth() / 2);
		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());
		graphics.drawRectangle(tempRect);
	}

	public static Border getBorder(Color color, int width) {
		FocusBorder focusBorder = new FocusBorder();
		focusBorder.setColor(color);
		focusBorder.setWidth(width);
		return focusBorder;
	}

	public static Border getDefaultBorder() {
		return new FocusBorder();
	}

}
