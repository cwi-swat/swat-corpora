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
package org.eclipse.e4.xwt.tools.ui.designer.parts.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.parts.MenuItemEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.StyleHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

public class MenuItemFigure extends RectangleFigure {

	private MenuItemEditPart host;
	private Label nameLabel = null;

	public MenuItemFigure(MenuItemEditPart host) {
		this.host = host;
		setFill(false);
		setLayoutManager(new XYLayout());
		setForegroundColor(ColorConstants.menuBackground);
		nameLabel = new Label();
		nameLabel.setTextPlacement(PositionConstants.WEST);
		nameLabel.setTextAlignment(PositionConstants.CENTER);
		nameLabel.setLabelAlignment(PositionConstants.LEFT);
		add(nameLabel);
	}

	public MenuItemEditPart getHost() {
		return host;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int hint, int hint2) {
		if (host != null) {
			return host.getVisualInfo().getBounds().getSize();
		}
		return super.getPreferredSize(hint, hint2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		Rectangle copy = rect.getCopy().shrink(20, 0).resize(-4, 0);
		nameLabel.setBounds(copy);
	}

	private boolean displayText() {
		Widget widget = host.getWidget();
		if (widget != null && !widget.isDisposed() && widget instanceof MenuItem) {
			return !StyleHelper.isOnMenubar((MenuItem) widget);
		}
		return false;
	}

	private boolean isSeparator() {
		Widget widget = host.getWidget();
		return StyleHelper.checkStyle(widget, SWT.SEPARATOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	public void paintFigure(Graphics graphics) {
		Rectangle r = getBounds().getCopy();
		Widget widget = host.getWidget();
		if (isSeparator()) {
			graphics.setForegroundColor(ColorConstants.gray);
			graphics.drawLine(r.x, r.y, r.x + r.width, r.y);
		} else {
			if (displayText()) {
				if (widget != null && !widget.isDisposed() && widget instanceof MenuItem) {
					nameLabel.setText(((MenuItem) widget).getText());
				}
				nameLabel.setForegroundColor(ColorConstants.black);
			}
			super.paintFigure(graphics);
		}

		if (displayText() && StyleHelper.checkStyle(widget, SWT.CASCADE)) {
			Color fc = graphics.getForegroundColor();
			Color bc = graphics.getBackgroundColor();

			graphics.setBackgroundColor(ColorConstants.black);
			graphics.setForegroundColor(ColorConstants.black);
			Rectangle b = nameLabel.getBounds();
			int x = b.x + b.width + 4;
			int y = b.y;
			drawRightArrow(graphics, x, y);

			graphics.setForegroundColor(fc);
			graphics.setBackgroundColor(bc);
		}
	}

	protected void drawRightArrow(Graphics gc, int x, int y) {
		x += 2;
		y += 4;
		gc.drawLine(x + 2, y, x + 5, y + 3);
		gc.drawLine(x + 5, y + 4, x + 5, y + 4);
		gc.drawLine(x + 4, y + 3, x + 4, y + 5);
		gc.drawLine(x + 3, y + 1, x + 3, y + 6);
		gc.drawLine(x + 2, y + 1, x + 2, y + 7);
	}
}
