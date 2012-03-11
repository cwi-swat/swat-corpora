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
package org.eclipse.e4.xwt.tools.ui.designer.policies.feedback;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class FillFeedback extends RectangleFigure {
	private boolean opaque;
	private static Color fillColor = new Color(null, 31, 31, 31);
	private static Color lineColor = new Color(null, 255, 0, 255);

	public FillFeedback(Rectangle r) {
		this(r, 1, SWT.LINE_SOLID, fillColor, lineColor, true);
	}

	public FillFeedback(Rectangle r, int lineWidth, int lineStyle, Color background, Color foreground, boolean opaque) {
		setBounds(r);
		setLineWidth(lineWidth);
		setLineStyle(lineStyle);
		if (foreground != null)
			setForegroundColor(foreground);
		if (background != null)
			setBackgroundColor(background);
		setOpaque(opaque);
		setFillXOR(true);
		this.opaque = opaque;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color bg) {
		Color backgroundColor = getBackgroundColor();
		if ((backgroundColor == null && bg != null) || (backgroundColor != null && backgroundColor != bg)) {
			opaque = true;
		}
		super.setBackgroundColor(bg);
	}

	public void fillShape(Graphics g) {
		if (opaque) {
			Rectangle r = getBounds().getCopy().resize(-1, -1);
			g.fillRectangle(r);
		}
	}

	public void outlineShape(Graphics g) {
		Rectangle r = getBounds().getCopy().resize(-1, -1);
		g.drawRectangle(r);

	}
}
