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
package org.eclipse.e4.xwt.tools.ui.designer.core.util.swt;

import java.lang.reflect.Field;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Get the widths and heights of the columns and rows so that the IDE can show these in its GEF feedback This class works on Eclipse 3.0 because it has access to fields in the GridLayout In 3.1 these were removed so this won't work, however the IDE GridLayoutPolicyHelper instead uses the newer peer named GridLayoutHelper
 */

public class GridLayoutHelper_30 {

	private Composite fComposite;
	public int[] widths;
	public int[] heights;
	private int[] expandableColumns;
	private int[] expandableRows;
	private Field pixelColumnWidthsFieldProxy;
	private Field pixelRowHeightsFieldProxy;
	private Field expandableColumnsFieldProxy;
	private Field expandableRowsFieldProxy;
	private GridLayout gridLayout;

	public void setComposite(Composite aComposite) {
		fComposite = aComposite;
		aComposite.layout();
		computeValues();
	}

	private void computeValues() {
		gridLayout = (GridLayout) fComposite.getLayout();
		fComposite.layout();
		if (fComposite.getChildren().length == 0) {
			// If there aren't any children, there is no grid currently.
			widths = heights = new int[0];
			return;
		}
		try {
			if (pixelColumnWidthsFieldProxy == null) {
				pixelColumnWidthsFieldProxy = gridLayout.getClass().getDeclaredField("pixelColumnWidths"); //$NON-NLS-1$
				pixelColumnWidthsFieldProxy.setAccessible(true);
			}
			if (pixelRowHeightsFieldProxy == null) {
				pixelRowHeightsFieldProxy = gridLayout.getClass().getDeclaredField("pixelRowHeights"); //$NON-NLS-1$
				pixelRowHeightsFieldProxy.setAccessible(true);
			}
			if (expandableColumnsFieldProxy == null) {
				expandableColumnsFieldProxy = gridLayout.getClass().getDeclaredField("expandableColumns"); //$NON-NLS-1$
				expandableColumnsFieldProxy.setAccessible(true);
			}
			if (expandableRowsFieldProxy == null) {
				expandableRowsFieldProxy = gridLayout.getClass().getDeclaredField("expandableRows"); //$NON-NLS-1$
				expandableRowsFieldProxy.setAccessible(true);
			}
			widths = (int[]) pixelColumnWidthsFieldProxy.get(gridLayout);
			heights = (int[]) pixelRowHeightsFieldProxy.get(gridLayout);
			expandableColumns = (int[]) expandableColumnsFieldProxy.get(gridLayout);
			expandableRows = (int[]) expandableRowsFieldProxy.get(gridLayout);
			if (expandableColumns.length > 0 || expandableRows.length > 0)
				adjustGridDimensions(gridLayout);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

	}

	private void adjustGridDimensions(GridLayout gridLayout) {

		// The expandableColumns holds an array of which controls want to grab excess width
		// The expandableRows holds an array of which controls want to grab excess height
		// We need to adjust the widths and heights arrays for this
		// 1 - Get the available space that the controls who want to grow into available space can occupy
		// 2 - Divide this space among the controls that want it
		// 3 - adjust the widths and heights array

		// 1) Work out the available total width and height
		int availableHorizontal = fComposite.getClientArea().width;
		int availableVerical = fComposite.getClientArea().height;

		// 2) Divide the height and width by the columns that want it
		// 3) increment each control that is expandable
		if (expandableColumns.length > 0) {
			// Reduce the width by the margins
			availableHorizontal = availableHorizontal - (gridLayout.marginWidth * 2);
			// Reduce the width by the spacing
			if (widths.length >= 2) {
				availableHorizontal = availableHorizontal - (widths.length - 1) * gridLayout.horizontalSpacing;
			}
			// Subtract the currently used space
			for (int i = 0; i < widths.length; i++) {
				availableHorizontal = availableHorizontal - widths[i];
			}
			// We have the remaining space to use. Divide by the number of columns that want to be expanded
			int widthAdjustment = availableHorizontal / expandableColumns.length;
			// Now add this to each column that wants to be expanded
			for (int i = 0; i < expandableColumns.length; i++) {
				widths[expandableColumns[i]] = widthAdjustment + widths[expandableColumns[i]];
			}
		}

		if (expandableRows.length > 0) {
			// Reduce the width by the margins
			availableVerical = availableVerical - (gridLayout.marginHeight * 2);
			// Reduce the height by the spacing
			if (heights.length >= 2) {
				availableVerical = availableVerical - (heights.length - 1) * gridLayout.verticalSpacing;
			}
			// Subtract the currently used space
			for (int i = 0; i < heights.length; i++) {
				availableVerical = availableVerical - heights[i];
			}
			// We have the remaining space to use. Divide by the number of rows that want to be expanded
			int heightAdjustment = availableVerical / expandableRows.length;
			// Now add this to each row that wants to be expanded
			for (int i = 0; i < expandableRows.length; i++) {
				heights[expandableRows[i]] = heightAdjustment + heights[expandableRows[i]];
			}
		}
	}
}
