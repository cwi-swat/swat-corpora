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
package org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.Draw2dTools;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTTools;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FormDataFigure extends Figure {
	private FormLayoutData layoutData;

	public FormDataFigure(FormLayoutData layoutData) {
		this.layoutData = layoutData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
	 */
	public void paint(Graphics graphics) {
		if (getLayoutData() == null || getLayoutData().data == null || getLayoutData().bounds == null) {
			return;
		}
		Rectangle r = getLayoutData().bounds.getCopy();
		IFigure parent = getParent();
		if (parent != null) {
			parent.translateToRelative(r);
		}
		graphics.setLineWidth(1);
		graphics.setForegroundColor(ColorConstants.blue);
		FormData formData = getLayoutData().data;
		FormAttachment left = formData.left, top = formData.top, right = formData.right, bottom = formData.bottom;
		if (left != null) {
			if (SWT.LEFT == left.alignment) {
				graphics.setLineStyle(SWT.LINE_DOT);
				Point p1 = r.getLocation().getTranslated(0, 15);
				Rectangle rc = getBounds(left.control);
				int dis = r.bottom() - rc.bottom() - r.height;
				Point p2 = p1.getTranslated(0, -dis - 30);
				graphics.drawLine(p1, p2);
			} else {
				graphics.setLineStyle(SWT.LINE_DASH);
				Point p1 = r.getLocation().getTranslated(0, r.height / 2);
				Point p2 = p1.getTranslated(-left.offset, 0);
				graphics.drawLine(p1, p2);
				if (left.control != null) {
					Rectangle rc = getBounds(left.control);
					if (r.bottom() > rc.bottom()) {
						int offset = r.bottom() - rc.bottom();
						Point p3 = p2.getTranslated(0, -offset / 2 - 5);
						Point p4 = p2.getTranslated(0, 15);
						graphics.drawLine(p3, p4);
					} else if (r.y < rc.y) {
						int offset = rc.y - r.y;
						Point p3 = p2.getTranslated(0, offset / 2 + 5);
						Point p4 = p2.getTranslated(0, -15);
						graphics.drawLine(p3, p4);
					}
				}
				drawArc(p2, PositionConstants.WEST, graphics);
			}
		}
		if (right != null) {
			if (SWT.RIGHT == right.alignment) {
				graphics.setLineStyle(SWT.LINE_DOT);
				Point p1 = r.getLocation().getTranslated(r.width, 15);
				Rectangle rc = getBounds(right.control);
				int dis = r.bottom() - rc.bottom() - r.height;
				Point p2 = p1.getTranslated(0, -dis - 30);
				graphics.drawLine(p1, p2);
			} else {
				graphics.setLineStyle(SWT.LINE_DASH);
				Point p1 = r.getLocation().getTranslated(r.width, r.height / 2);
				Point p2 = p1.getTranslated(-right.offset, 0);
				graphics.drawLine(p1, p2);
				drawArc(p2, PositionConstants.EAST, graphics);
			}
		}
		if (top != null) {
			if (SWT.TOP == top.alignment) {
				graphics.setLineStyle(SWT.LINE_DOT);
				Point p1 = r.getLocation().getTranslated(15, 0);
				Rectangle rc = getBounds(top.control);
				int dis = r.right() - rc.right() - r.width;
				Point p2 = p1.getTranslated(-dis - 30, 0);
				graphics.drawLine(p1, p2);
			} else {
				graphics.setLineStyle(SWT.LINE_DASH);
				Point p1 = r.getLocation().getTranslated(r.width / 2, 0);
				Point p2 = p1.getTranslated(0, -top.offset);
				graphics.drawLine(p1, p2);
				if (top.control != null) {
					Rectangle rc = getBounds(top.control);
					if (r.right() > rc.right()) {
						int offset = r.right() - rc.right();
						Point p3 = p2.getTranslated(-offset / 2 - 5, 0);
						Point p4 = p2.getTranslated(15, 0);
						graphics.drawLine(p3, p4);
					} else if (r.x < rc.x) {
						int offset = rc.x - r.x;
						Point p3 = p2.getTranslated(offset / 2 + 5, 0);
						Point p4 = p2.getTranslated(-15, 0);
						graphics.drawLine(p3, p4);
					}
				}
				drawArc(p2, PositionConstants.NORTH, graphics);
			}
		}
		if (bottom != null) {
			if (SWT.BOTTOM == bottom.alignment) {
				graphics.setLineStyle(SWT.LINE_DOT);
				Point p1 = r.getLocation().getTranslated(15, r.height);
				Rectangle rc = getBounds(bottom.control);
				int dis = r.right() - rc.right() - r.width;
				Point p2 = p1.getTranslated(-dis - 30, 0);
				graphics.drawLine(p1, p2);
			} else {
				graphics.setLineStyle(SWT.LINE_DASH);
				Point p1 = r.getLocation().getTranslated(r.width / 2, r.height);
				Point p2 = p1.getTranslated(0, -bottom.offset);
				graphics.drawLine(p1, p2);
				drawArc(p2, PositionConstants.SOUTH, graphics);
			}
		}
	}

	private void drawArc(Point point, int position, Graphics g) {
		try {
			g.setAntialias(SWT.ON);
		} catch (Exception e) {
		}
		g.setBackgroundColor(ColorConstants.blue);
		Point p = point.getTranslated(-3, -3);
		Dimension s = new Dimension(6, 6);
		int offset = 90;
		if (position == PositionConstants.WEST) {
			offset = offset * 3;
		} else if (position == PositionConstants.NORTH) {
			offset = offset * 2;
		} else if (position == PositionConstants.SOUTH) {
			offset = offset * 4;
		}
		g.fillArc(new Rectangle(p, s), offset, 180);
		try {
			g.setAntialias(SWT.OFF);
		} catch (Exception e) {
		}
	}

	private Rectangle getBounds(Control control) {
		Rectangle r = Draw2dTools.toDraw2d(SWTTools.getBounds(control));
		return r;
	}

	public void setLayoutData(FormLayoutData layoutData) {
		this.layoutData = layoutData;
		repaint();
	}

	public FormLayoutData getLayoutData() {
		return layoutData;
	}

}
