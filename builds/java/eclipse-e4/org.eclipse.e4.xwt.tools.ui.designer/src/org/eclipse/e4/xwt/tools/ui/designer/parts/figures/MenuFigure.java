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

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.parts.MenuEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.widgets.Display;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class MenuFigure extends Figure implements Expandable {

	private static final int HEIGHT_HINT = 4;
	private static final int WIDTH_HINT = 40;
	private MenuEditPart host;
	private Label initLabel = new Label(" (New Menu) ");

	public MenuFigure(MenuEditPart host) {
		this.host = host;
		initLabel.setFont(Display.getCurrent().getSystemFont());
		setBackgroundColor(ColorConstants.white);
		setForegroundColor(ColorConstants.gray);
		MenuLayout manager = new MenuLayout();
		manager.setStretchMinorAxis(true);
		manager.setHorizontalSpacing(4);
		manager.setVerticalSpacing(2);
		manager.setMinorAlignment(MenuLayout.ALIGN_CENTER);
		setLayoutManager(manager);

		add(initLabel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#add(org.eclipse.draw2d.IFigure, java.lang.Object, int)
	 */
	public void add(IFigure figure, Object constraint, int index) {
		if (figure != null && figure != initLabel && initLabel.getParent() == this) {
			remove(initLabel);
		}
		super.add(figure, constraint, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		Rectangle r = getBounds().getCopy();
		int x = r.x;
		int y = r.y;
		int w = r.width - 1;
		int h = r.height - 1;
		graphics.fillRectangle(r);
		graphics.drawRectangle(new Rectangle(x, y, w, h));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editparts.figures.Expandable#collapse()
	 */
	public void collapse() {
		setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editparts.figures.Expandable#expand()
	 */
	public void expand() {
		List<IFigure> children = getChildren();
		int width = 0;
		int height = 0;
		for (IFigure figure : children) {
			Dimension size = figure.getPreferredSize();
			width = Math.max(width, size.width);
			height += size.height;
			figure.setBackgroundColor(ColorConstants.white);
			figure.setForegroundColor(ColorConstants.white);
		}
		if (width > 0 && height > 0) {
			setSize(new Dimension(width + WIDTH_HINT, height + HEIGHT_HINT));
			layout();
			setVisible(true);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editparts.figures.Expandable#getHost()
	 */
	public GraphicalEditPart getHost() {
		return host;
	}

	private static class MenuLayout extends ToolbarLayout {
		private int horizontalSpacing = 0;
		private int verticalSpacing = 0;

		public MenuLayout() {
			setVertical(true);
		}

		public void layout(IFigure parent) {
			super.layout(parent);
			List<IFigure> children = parent.getChildren();
			for (IFigure child : children) {
				Rectangle r = child.getBounds().getCopy();
				int height = r.height - getVerticalSpacing() * 2;
				if (height < 1) {
					height = 1;// for separator.
				}
				child.setBounds(new Rectangle(r.x + getHorizontalSpacing(), r.y + getVerticalSpacing(), r.width - getHorizontalSpacing() * 2, height));
			}
		}

		/**
		 * @param horizontalSpacing
		 *            the horizontalSpacing to set
		 */
		public void setHorizontalSpacing(int horizontalSpacing) {
			this.horizontalSpacing = horizontalSpacing;
		}

		/**
		 * @return the horizontalSpacing
		 */
		public int getHorizontalSpacing() {
			return horizontalSpacing;
		}

		/**
		 * @param verticalSpacing
		 *            the verticalSpacing to set
		 */
		public void setVerticalSpacing(int verticalSpacing) {
			this.verticalSpacing = verticalSpacing;
		}

		/**
		 * @return the verticalSpacing
		 */
		public int getVerticalSpacing() {
			return verticalSpacing;
		}
	}

}
