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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class CrossFeedback extends Figure {
	private static final int WIDTH = 50;
	private static final int HEIGHT = 50;
	private Point center;
	private Label tooltip;

	public CrossFeedback(Point center) {
		tooltip = new Label();
		tooltip.setForegroundColor(ColorConstants.blue);
		setCenter(center);
		setLayoutManager(new XYLayout());
		add(tooltip);
	}

	public void setCenter(Point center) {
		this.center = center;
		Point l = center.getTranslated(-(WIDTH / 2), -(HEIGHT / 2));
		setBounds(new Rectangle(l.x, l.y, WIDTH, HEIGHT));
		tooltip.setBounds(new Rectangle(l.x, l.y, 50, 25));
		repaint();
	}

	public Point getCenter() {
		return center;
	}

	public void setTooltipText(String text) {
		if (text == null) {
			text = "";
		}
		tooltip.setText(text);
	}

	protected void paintFigure(Graphics graphics) {
		graphics.setLineStyle(SWT.LINE_DOT);
		graphics.setForegroundColor(ColorConstants.blue);
		int x = center.x;
		int y = center.y;
		Point p1 = new Point(x - (WIDTH / 2), y);
		Point p2 = new Point(x + (WIDTH / 2), center.y);
		graphics.drawLine(p1, p2);
		Point p3 = new Point(x, y - (HEIGHT / 2));
		Point p4 = new Point(x, y + (HEIGHT / 2));
		graphics.drawLine(p3, p4);
	}
}
