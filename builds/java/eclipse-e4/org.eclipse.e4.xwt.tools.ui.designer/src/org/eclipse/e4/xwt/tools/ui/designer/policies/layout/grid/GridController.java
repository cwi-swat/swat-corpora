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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class GridController {
	public static final String GRID_KEY = "org.soyatec.xaml.ve.xwt.editpolicies.gridlayout.gridkey"; //$NON-NLS-1$

	private boolean showGrid = false;
	private int gridWidth = 15;
	private int gridHeight = 15;
	private int gridMargin = 0;

	private List<IGridListener> listeners = new ArrayList<IGridListener>();
	protected Map<EditPart, GridController> registeredEPs = new HashMap<EditPart, GridController>(2);

	public void addGridListener(IGridListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeGridListener(IGridListener listener) {
		listeners.remove(listener);
	}

	/**
	 * @param showGrid
	 *            the showGrid to set
	 */
	public void setGridShowing(boolean showGrid) {
		this.showGrid = showGrid;
		for (int i = 0; i < listeners.size(); i++) {
			((IGridListener) listeners.get(i)).gridVisibilityChanged(showGrid);
		}
	}

	/**
	 * @return the showGrid
	 */
	public boolean isGridShowing() {
		return showGrid;
	}

	/**
	 * @param gridWidth
	 *            the gridWidth to set
	 */
	public void setGridWidth(int gridWidth) {
		int oldGridWidth = this.gridWidth;
		this.gridWidth = gridWidth;
		for (int i = 0; i < listeners.size(); i++)
			((IGridListener) listeners.get(i)).gridWidthChanged(gridWidth, oldGridWidth);

	}

	/**
	 * @return the gridWidth
	 */
	public int getGridWidth() {
		return gridWidth;
	}

	/**
	 * @param gridHeight
	 *            the gridHeight to set
	 */
	public void setGridHeight(int gridHeight) {
		int oldGridHeight = this.gridHeight;
		this.gridHeight = gridHeight;
		for (int i = 0; i < listeners.size(); i++)
			((IGridListener) listeners.get(i)).gridHeightChanged(gridHeight, oldGridHeight);

	}

	/**
	 * @return the gridHeight
	 */
	public int getGridHeight() {
		return gridHeight;
	}

	/**
	 * @param gridMargin
	 *            the gridMargin to set
	 */
	public void setGridMargin(int gridMargin) {
		int oldGridMargin = this.gridMargin;
		this.gridMargin = gridMargin;
		for (int i = 0; i < listeners.size(); i++)
			((IGridListener) listeners.get(i)).gridMarginChanged(gridMargin, oldGridMargin);

	}

	/**
	 * @return the gridMargin
	 */
	public int getGridMargin() {
		return gridMargin;
	}

	/**
	 * Static helper method to return the grid controller for a specific editpart.
	 */
	public static GridController getGridController(EditPart editPart) {
		EditPartViewer viewer = editPart.getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		// This is the master grid controller for this viewer
		GridController gc = (GridController) dom.getViewerData(viewer, GRID_KEY);
		if (gc != null) {
			return (GridController) gc.registeredEPs.get(editPart);
		}
		return null;
	}

	/**
	 * Register the Editpart as having a grid on it.
	 */
	public static void registerEditPart(EditPart ep, GridController newgc) {
		EditPartViewer viewer = ep.getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		// This is the master grid controller for this viewer
		GridController gc = (GridController) dom.getViewerData(viewer, GRID_KEY);
		if (gc != null)
			gc.registeredEPs.put(ep, newgc);
	}

	/**
	 * Unregister the EditPart.
	 */
	public static void unregisterEditPart(EditPart ep) {
		EditPartViewer viewer = ep.getRoot().getViewer();
		EditDomain dom = (EditDomain) viewer.getEditDomain();
		// This is the master grid controller for this viewer
		GridController gc = (GridController) dom.getViewerData(viewer, GRID_KEY);
		if (gc != null && gc.registeredEPs.get(ep) != null)
			gc.registeredEPs.remove(ep);
	}
}
