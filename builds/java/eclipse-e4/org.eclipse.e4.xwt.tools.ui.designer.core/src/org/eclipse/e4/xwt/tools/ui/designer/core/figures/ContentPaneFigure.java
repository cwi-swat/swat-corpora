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
package org.eclipse.e4.xwt.tools.ui.designer.core.figures;

import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ContentPaneFigure extends Figure {
	private IFigure contentPane;

	private static final ContentPaneLayout DEFAULT_LAYOUT = new ContentPaneLayout();

	public ContentPaneFigure() {
		setLayoutManager(DEFAULT_LAYOUT);
	}

	public void setContentPane(IFigure contentPane) {
		this.contentPane = contentPane;
		if (contentPane.getParent() != this) {
			add(contentPane, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		IFigure contentPane = getContentPane();
		if (contentPane != null) {
			contentPane.setBounds(getClientArea());
		}

	}

	public IFigure getContentPane() {
		return contentPane;
	}

	public static class ContentPaneLayout extends AbstractHintLayout {

		protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
			ContentPaneFigure cf = (ContentPaneFigure) container;
			return cf.getContentPane() != null ? cf.getContentPane().getPreferredSize(wHint, hHint) : new Dimension();
		}

		public void layout(IFigure container) {
			ContentPaneFigure cf = (ContentPaneFigure) container;
			Rectangle r = cf.getClientArea();
			IFigure contentPane = cf.getContentPane();
			if (contentPane != null) {
				contentPane.setBounds(r);
			}
		}

	}
}
