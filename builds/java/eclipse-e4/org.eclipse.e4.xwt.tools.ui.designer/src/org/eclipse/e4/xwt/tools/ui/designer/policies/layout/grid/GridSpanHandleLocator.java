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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.GridLayoutEditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.RelativeHandleLocator;

public class GridSpanHandleLocator extends RelativeHandleLocator {
	GridLayoutEditPolicy layoutEditPolicy;
	GraphicalEditPart editpart;
	Rectangle fullCellBounds = new Rectangle();

	public GridSpanHandleLocator(GraphicalEditPart editpart, int location, GridLayoutEditPolicy layoutEditPolicy) {
		super(editpart.getFigure(), location);
		this.layoutEditPolicy = layoutEditPolicy;
		this.editpart = editpart;
	}

	/*
	 * Need to override in order to provide a bounding box that includes spanning horizontally and vertically.
	 * 
	 * @see org.eclipse.draw2d.RelativeLocator#getReferenceBox()
	 */
	protected Rectangle getReferenceBox() {
		return fullCellBounds;
	}

	/*
	 * Need to reset it's bounds since we've relocated.
	 * 
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	public void relocate(final IFigure target) {
		// Need to spawn this off in an async runnable to give the gridlayout edit policy
		// time to update it's grid and cell positions. Then when we get the cell bounds
		// for the handle, it will be correct.
		editpart.getViewer().getControl().getDisplay().asyncExec(new Runnable() {
			public void run() {
				doRelocate(target);
			}
		});
	}

	private void doRelocate(IFigure target) {
		fullCellBounds = layoutEditPolicy.getFullCellBounds(editpart).expand(-1, -1);
		super.relocate(target);
	}

}
