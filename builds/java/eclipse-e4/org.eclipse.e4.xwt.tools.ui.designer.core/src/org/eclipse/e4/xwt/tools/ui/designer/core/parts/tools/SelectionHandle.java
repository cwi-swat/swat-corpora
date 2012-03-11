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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.tools.SelectEditPartTracker;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class SelectionHandle extends AbstractHandle {

	public SelectionHandle(GraphicalEditPart host) {
		super(host, new SelectionLocator(host.getFigure()));
		setBorder(new FocusBorder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		SelectEditPartTracker tracker = new SelectEditPartTracker(getOwner());
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#getOwnerFigure()
	 */
	public IFigure getOwnerFigure() {
		return super.getOwnerFigure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		if (!super.containsPoint(x, y))
			return false;
		return !Rectangle.SINGLETON.setBounds(getBounds()).shrink(2, 2).contains(x, y);
	}

}
