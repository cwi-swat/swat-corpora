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
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

/**
 * Since Eclipse 3.1 GridLayout took away the access to the column widths and
 * row heights needed to show grid lines and feedback for the GridLayout policy
 * helper, we need to calculate this ourselves using the same algorithms that
 * GridLayout.layout uses. The main work is done in the computeValues method
 * which does much of what the GridLayout.layout method except it doesn't layout
 * the controls... just gets the column widths and row heights.
 * 
 * Note: In the future, if SWT GridLayout changes it's layout method or GridData
 * changes it's protected fields, this class will have to be refactored to
 * handle those changes.
 * 
 * This class accesses and sets the following package protected GridData fields:
 * cacheWidth, cacheHeight
 * 
 * It also executes the following package protected method: computeSize(Control,
 * int, int, boolean)
 */

public class GridLayoutHelper {

	private Composite fComposite;
	public int[] widths;// save current composit's all control's width and
						// height
	public int[] heights;
	private GridLayout fGridLayout;
	private Field cacheWidthField; // GridData package protected field
	private Field cacheHeightField; // GridData package protected field
	private Method computeSizeMethod; // GridData package protected method

	public void setComposite(Composite aComposite) {
		fComposite = aComposite;
		if (aComposite.isDisposed()) {
			return;
		}
		fGridLayout = (GridLayout) aComposite.getLayout();
		if (fGridLayout != null) {
			computeValues();
		}
	}

	private void runComputeSize(GridData aGridData, Object[] args) {
		if (computeSizeMethod == null) {
			try {
				computeSizeMethod = GridData.class
						.getDeclaredMethod(
								"computeSize", new Class[] { Control.class, int.class, int.class, boolean.class }); //$NON-NLS-1$
				computeSizeMethod.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			if (computeSizeMethod != null)
				computeSizeMethod.invoke(aGridData, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getCacheWidth(GridData aGridData) {
		if (cacheWidthField == null) {
			try {
				cacheWidthField = GridData.class.getDeclaredField("cacheWidth"); //$NON-NLS-1$
				cacheWidthField.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Integer cacheWidth = (Integer) cacheWidthField.get(aGridData);
			return cacheWidth.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int getCacheHeight(GridData aGridData) {
		if (cacheHeightField == null) {
			try {
				cacheHeightField = GridData.class
						.getDeclaredField("cacheHeight"); //$NON-NLS-1$
				cacheHeightField.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Integer cacheWidth = (Integer) cacheHeightField.get(aGridData);
			return cacheWidth.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void putCacheWidth(GridData aGridData, int value) {
		if (cacheWidthField == null) {
			try {
				cacheWidthField = GridData.class.getDeclaredField("cacheWidth"); //$NON-NLS-1$
				cacheWidthField.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			cacheWidthField.setInt(aGridData, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void putCacheHeight(GridData aGridData, int value) {
		if (cacheHeightField == null) {
			try {
				cacheHeightField = GridData.class
						.getDeclaredField("cacheHeight"); //$NON-NLS-1$
				cacheHeightField.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			cacheHeightField.setInt(aGridData, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method is basically a copy of the code used in GridLayout.layout
	 * (Composite composite, boolean move, int x, int y, int width, int height,
	 * boolean flushCache)
	 */
	private void computeValues() {

		Rectangle clientArea = fComposite.getClientArea();
		int width = clientArea.width;
		int height = clientArea.height;

		if (fGridLayout.numColumns < 1) {
			return;
		}

		int count = 0;
		Control[] children = fComposite.getChildren();
		for (int i = 0; i < children.length; i++) {
			Control control = children[i];
			GridData data = (GridData) control.getLayoutData();
			if (data == null || !data.exclude) {
				children[count++] = children[i];
			}
		}

		for (int i = 0; i < count; i++) {
			Control child = children[i];
			GridData data = (GridData) child.getLayoutData();
			if (data == null)
				child.setLayoutData(data = new GridData());
			runComputeSize(data, new Object[] { child,
					Integer.valueOf(data.widthHint),
					Integer.valueOf(data.heightHint), Boolean.FALSE });
			if (data.grabExcessHorizontalSpace && data.minimumWidth > 0) {
				if (getCacheWidth(data) < data.minimumWidth) {
					int trim = 0;
					// TEMPORARY CODE
					if (child instanceof Scrollable) {
						Rectangle rect = ((Scrollable) child).computeTrim(0, 0,
								0, 0);
						trim = rect.width;
					} else {
						trim = child.getBorderWidth() * 2;
					}
					putCacheWidth(data, SWT.DEFAULT);
					putCacheHeight(data, SWT.DEFAULT);
					runComputeSize(data, new Object[] { child,
							Integer.valueOf(Math.max(0, data.minimumWidth
									- trim)), Integer.valueOf(data.heightHint),
							Boolean.FALSE });
				}
			}
			if (data.grabExcessVerticalSpace && data.minimumHeight > 0) {
				putCacheHeight(data, Math.max(getCacheHeight(data),
						data.minimumHeight));
			}
		}

		/* Build the grid */
		int row = 0, column = 0, rowCount = 0, columnCount = fGridLayout.numColumns;
		Control[][] grid = new Control[4][columnCount];
		for (int i = 0; i < count; i++) {
			Control child = children[i];
			GridData data = (GridData) child.getLayoutData();
			if (data == null)
				data = new GridData();
			int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
			int vSpan = Math.max(1, data.verticalSpan);
			while (true) {
				int lastRow = row + vSpan;
				if (lastRow >= grid.length) {
					Control[][] newGrid = new Control[lastRow + 4][columnCount];
					System.arraycopy(grid, 0, newGrid, 0, grid.length);
					grid = newGrid;
				}
				if (grid[row] == null) {
					grid[row] = new Control[columnCount];
				}
				while (column < columnCount && grid[row][column] != null) {
					column++;
				}
				int endCount = column + hSpan;
				if (endCount <= columnCount) {
					int index = column;
					while (index < endCount && grid[row][index] == null) {
						index++;
					}
					if (index == endCount)
						break;
					column = index;
				}
				if (column + hSpan >= columnCount) {
					column = 0;
					row++;
				}
			}
			for (int j = 0; j < vSpan; j++) {
				if (grid[row + j] == null) {
					grid[row + j] = new Control[columnCount];
				}
				for (int k = 0; k < hSpan; k++) {
					grid[row + j][column + k] = child;
				}
			}
			rowCount = Math.max(rowCount, row + vSpan);
			column += hSpan;
		}

		/* Column widths */
		if (rowCount == 0) {
			// There are no rows/columns if there are no rows.
			widths = new int[0];
			heights = new int[0];
			return;
		}
		int availableWidth = width
				- fGridLayout.horizontalSpacing
				* (columnCount - 1)
				- (fGridLayout.marginLeft + fGridLayout.marginWidth * 2 + fGridLayout.marginRight);
		int expandCount = 0;
		widths = new int[columnCount];
		int[] minWidths = new int[columnCount];
		boolean[] expandColumn = new boolean[columnCount];
		for (int j = 0; j < columnCount; j++) {
			for (int i = 0; i < rowCount; i++) {
				GridData data = getData(grid, i, j, rowCount, columnCount, true);
				if (data != null) {
					int hSpan = Math.max(1, Math.min(data.horizontalSpan,
							columnCount));
					if (hSpan == 1) {
						int w = getCacheWidth(data) + data.horizontalIndent;
						widths[j] = Math.max(widths[j], w);
						if (data.grabExcessHorizontalSpace) {
							if (!expandColumn[j])
								expandCount++;
							expandColumn[j] = true;
						}
						if (!data.grabExcessHorizontalSpace
								|| data.minimumWidth != 0) {
							w = !data.grabExcessHorizontalSpace
									|| data.minimumWidth == SWT.DEFAULT ? getCacheWidth(data)
									: data.minimumWidth;
							w += data.horizontalIndent;
							minWidths[j] = Math.max(minWidths[j], w);
						}
					}
				}
			}
			for (int i = 0; i < rowCount; i++) {
				GridData data = getData(grid, i, j, rowCount, columnCount,
						false);
				if (data != null) {
					int hSpan = Math.max(1, Math.min(data.horizontalSpan,
							columnCount));
					if (hSpan > 1) {
						int spanWidth = 0, spanMinWidth = 0, spanExpandCount = 0;
						for (int k = 0; k < hSpan; k++) {
							spanWidth += widths[j - k];
							spanMinWidth += minWidths[j - k];
							if (expandColumn[j - k])
								spanExpandCount++;
						}
						if (data.grabExcessHorizontalSpace
								&& spanExpandCount == 0) {
							expandCount++;
							expandColumn[j] = true;
						}
						int w = getCacheWidth(data) + data.horizontalIndent
								- spanWidth - (hSpan - 1)
								* fGridLayout.horizontalSpacing;
						if (w > 0) {
							if (spanExpandCount == 0) {
								widths[j] += w;
							} else {
								int delta = w / spanExpandCount;
								int remainder = w % spanExpandCount, last = -1;
								for (int k = 0; k < hSpan; k++) {
									if (expandColumn[j - k]) {
										widths[last = j - k] += delta;
									}
								}
								if (last > -1)
									widths[last] += remainder;
							}
						}
						if (!data.grabExcessHorizontalSpace
								|| data.minimumWidth != 0) {
							w = !data.grabExcessHorizontalSpace
									|| data.minimumWidth == SWT.DEFAULT ? getCacheWidth(data)
									: data.minimumWidth;
							w += data.horizontalIndent - spanMinWidth
									- (hSpan - 1)
									* fGridLayout.horizontalSpacing;
							if (w > 0) {
								if (spanExpandCount == 0) {
									minWidths[j] += w;
								} else {
									int delta = w / spanExpandCount;
									int remainder = w % spanExpandCount, last = -1;
									for (int k = 0; k < hSpan; k++) {
										if (expandColumn[j - k]) {
											minWidths[last = j - k] += delta;
										}
									}
									if (last > -1)
										minWidths[last] += remainder;
								}
							}
						}
					}
				}
			}
		}
		if (fGridLayout.makeColumnsEqualWidth) {
			int minColumnWidth = 0;
			int columnWidth = 0;
			for (int i = 0; i < columnCount; i++) {
				minColumnWidth = Math.max(minColumnWidth, minWidths[i]);
				columnWidth = Math.max(columnWidth, widths[i]);
			}
			columnWidth = width == SWT.DEFAULT || expandCount == 0 ? columnWidth
					: Math.max(minColumnWidth, availableWidth / columnCount);
			for (int i = 0; i < columnCount; i++) {
				expandColumn[i] = expandCount > 0;
				widths[i] = columnWidth;
			}
		} else {
			if (width != SWT.DEFAULT && expandCount > 0) {
				int totalWidth = 0;
				for (int i = 0; i < columnCount; i++) {
					totalWidth += widths[i];
				}
				int c = expandCount;
				int delta = (availableWidth - totalWidth) / c;
				int remainder = (availableWidth - totalWidth) % c;
				int last = -1;
				while (totalWidth != availableWidth) {
					for (int j = 0; j < columnCount; j++) {
						if (expandColumn[j]) {
							if (widths[j] + delta > minWidths[j]) {
								widths[last = j] = widths[j] + delta;
							} else {
								widths[j] = minWidths[j];
								expandColumn[j] = false;
								c--;
							}
						}
					}
					if (last > -1)
						widths[last] += remainder;

					for (int j = 0; j < columnCount; j++) {
						for (int i = 0; i < rowCount; i++) {
							GridData data = getData(grid, i, j, rowCount,
									columnCount, false);
							if (data != null) {
								int hSpan = Math.max(1, Math.min(
										data.horizontalSpan, columnCount));
								if (hSpan > 1) {
									if (!data.grabExcessHorizontalSpace
											|| data.minimumWidth != 0) {
										int spanWidth = 0, spanExpandCount = 0;
										for (int k = 0; k < hSpan; k++) {
											spanWidth += widths[j - k];
											if (expandColumn[j - k])
												spanExpandCount++;
										}
										int w = !data.grabExcessHorizontalSpace
												|| data.minimumWidth == SWT.DEFAULT ? getCacheWidth(data)
												: data.minimumWidth;
										w += data.horizontalIndent - spanWidth
												- (hSpan - 1)
												* fGridLayout.horizontalSpacing;
										if (w > 0) {
											if (spanExpandCount == 0) {
												widths[j] += w;
											} else {
												int delta2 = w
														/ spanExpandCount;
												int remainder2 = w
														% spanExpandCount, last2 = -1;
												for (int k = 0; k < hSpan; k++) {
													if (expandColumn[j - k]) {
														widths[last2 = j - k] += delta2;
													}
												}
												if (last2 > -1)
													widths[last2] += remainder2;
											}
										}
									}
								}
							}
						}
					}
					if (c == 0)
						break;
					totalWidth = 0;
					for (int i = 0; i < columnCount; i++) {
						totalWidth += widths[i];
					}
					delta = (availableWidth - totalWidth) / c;
					remainder = (availableWidth - totalWidth) % c;
					last = -1;
				}
			}
		}
		/* Wrapping */
		GridData[] flush = null;
		int flushLength = 0;
		if (width != SWT.DEFAULT) {
			for (int j = 0; j < columnCount; j++) {
				for (int i = 0; i < rowCount; i++) {
					GridData data = getData(grid, i, j, rowCount, columnCount,
							false);
					if (data != null) {
						if (data.heightHint == SWT.DEFAULT) {
							Control child = grid[i][j];
							// TEMPORARY CODE
							int hSpan = Math.max(1, Math.min(
									data.horizontalSpan, columnCount));
							int currentWidth = 0;
							for (int k = 0; k < hSpan; k++) {
								currentWidth += widths[j - k];
							}
							currentWidth += (hSpan - 1)
									* fGridLayout.horizontalSpacing
									- data.horizontalIndent;
							if ((currentWidth != getCacheWidth(data) && data.horizontalAlignment == SWT.FILL)
									|| (getCacheWidth(data) > currentWidth)) {
								int trim = 0;
								if (child instanceof Scrollable) {
									Rectangle rect = ((Scrollable) child)
											.computeTrim(0, 0, 0, 0);
									trim = rect.width;
								} else {
									trim = child.getBorderWidth() * 2;
								}
								putCacheWidth(data, SWT.DEFAULT);
								putCacheHeight(data, SWT.DEFAULT);
								runComputeSize(data, new Object[] {
										child,
										Integer.valueOf(Math.max(0,
												currentWidth - trim)),
										Integer.valueOf(data.heightHint),
										Boolean.FALSE });
								if (data.grabExcessVerticalSpace
										&& data.minimumHeight > 0) {
									putCacheHeight(data, Math.max(
											getCacheHeight(data),
											data.minimumHeight));
								}
								if (flush == null)
									flush = new GridData[count];
								// now that we've changed the actual GridData
								// for this control we need
								// to store for later on when we'll reset it
								// back.
								flush[flushLength++] = data;
							}
						}
					}
				}
			}
		}

		/* Row heights */
		int availableHeight = height
				- fGridLayout.verticalSpacing
				* (rowCount - 1)
				- (fGridLayout.marginTop + fGridLayout.marginHeight * 2 + fGridLayout.marginBottom);
		expandCount = 0;
		heights = new int[rowCount];
		int[] minHeights = new int[rowCount];
		boolean[] expandRow = new boolean[rowCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				GridData data = getData(grid, i, j, rowCount, columnCount, true);
				if (data != null) {
					int vSpan = Math.max(1, Math.min(data.verticalSpan,
							rowCount));
					if (vSpan == 1) {
						int h = getCacheHeight(data) + data.verticalIndent;
						heights[i] = Math.max(heights[i], h);
						if (data.grabExcessVerticalSpace) {
							if (!expandRow[i])
								expandCount++;
							expandRow[i] = true;
						}
						if (!data.grabExcessVerticalSpace
								|| data.minimumHeight != 0) {
							h = !data.grabExcessVerticalSpace
									|| data.minimumHeight == SWT.DEFAULT ? getCacheHeight(data)
									: data.minimumHeight;
							h += data.verticalIndent;
							minHeights[i] = Math.max(minHeights[i], h);
						}
					}
				}
			}
			for (int j = 0; j < columnCount; j++) {
				GridData data = getData(grid, i, j, rowCount, columnCount,
						false);
				if (data != null) {
					int vSpan = Math.max(1, Math.min(data.verticalSpan,
							rowCount));
					if (vSpan > 1) {
						int spanHeight = 0, spanMinHeight = 0, spanExpandCount = 0;
						for (int k = 0; k < vSpan; k++) {
							spanHeight += heights[i - k];
							spanMinHeight += minHeights[i - k];
							if (expandRow[i - k])
								spanExpandCount++;
						}
						if (data.grabExcessVerticalSpace
								&& spanExpandCount == 0) {
							expandCount++;
							expandRow[i] = true;
						}
						int h = getCacheHeight(data) + data.verticalIndent
								- spanHeight - (vSpan - 1)
								* fGridLayout.verticalSpacing;
						if (h > 0) {
							if (spanExpandCount == 0) {
								heights[i] += h;
							} else {
								int delta = h / spanExpandCount;
								int remainder = h % spanExpandCount, last = -1;
								for (int k = 0; k < vSpan; k++) {
									if (expandRow[i - k]) {
										heights[last = i - k] += delta;
									}
								}
								if (last > -1)
									heights[last] += remainder;
							}
						}
						if (!data.grabExcessVerticalSpace
								|| data.minimumHeight != 0) {
							h = !data.grabExcessVerticalSpace
									|| data.minimumHeight == SWT.DEFAULT ? getCacheHeight(data)
									: data.minimumHeight;
							h += data.verticalIndent - spanMinHeight
									- (vSpan - 1) * fGridLayout.verticalSpacing;
							if (h > 0) {
								if (spanExpandCount == 0) {
									minHeights[i] += h;
								} else {
									int delta = h / spanExpandCount;
									int remainder = h % spanExpandCount, last = -1;
									for (int k = 0; k < vSpan; k++) {
										if (expandRow[i - k]) {
											minHeights[last = i - k] += delta;
										}
									}
									if (last > -1)
										minHeights[last] += remainder;
								}
							}
						}
					}
				}
			}
		}
		if (height != SWT.DEFAULT && expandCount > 0) {
			int totalHeight = 0;
			for (int i = 0; i < rowCount; i++) {
				totalHeight += heights[i];
			}
			int c = expandCount;
			int delta = (availableHeight - totalHeight) / c;
			int remainder = (availableHeight - totalHeight) % c;
			int last = -1;
			while (totalHeight != availableHeight) {
				for (int i = 0; i < rowCount; i++) {
					if (expandRow[i]) {
						if (heights[i] + delta > minHeights[i]) {
							heights[last = i] = heights[i] + delta;
						} else {
							heights[i] = minHeights[i];
							expandRow[i] = false;
							c--;
						}
					}
				}
				if (last > -1)
					heights[last] += remainder;

				for (int i = 0; i < rowCount; i++) {
					for (int j = 0; j < columnCount; j++) {
						GridData data = getData(grid, i, j, rowCount,
								columnCount, false);
						if (data != null) {
							int vSpan = Math.max(1, Math.min(data.verticalSpan,
									rowCount));
							if (vSpan > 1) {
								if (!data.grabExcessVerticalSpace
										|| data.minimumHeight != 0) {
									int spanHeight = 0, spanExpandCount = 0;
									for (int k = 0; k < vSpan; k++) {
										spanHeight += heights[i - k];
										if (expandRow[i - k])
											spanExpandCount++;
									}
									int h = !data.grabExcessVerticalSpace
											|| data.minimumHeight == SWT.DEFAULT ? getCacheHeight(data)
											: data.minimumHeight;
									h += data.verticalIndent - spanHeight
											- (vSpan - 1)
											* fGridLayout.verticalSpacing;
									if (h > 0) {
										if (spanExpandCount == 0) {
											heights[i] += h;
										} else {
											int delta2 = h / spanExpandCount;
											int remainder2 = h
													% spanExpandCount, last2 = -1;
											for (int k = 0; k < vSpan; k++) {
												if (expandRow[i - k]) {
													heights[last2 = i - k] += delta2;
												}
											}
											if (last2 > -1)
												heights[last2] += remainder2;
										}
									}
								}
							}
						}
					}
				}
				if (c == 0)
					break;
				totalHeight = 0;
				for (int i = 0; i < rowCount; i++) {
					totalHeight += heights[i];
				}
				delta = (availableHeight - totalHeight) / c;
				remainder = (availableHeight - totalHeight) % c;
				last = -1;
			}
		}
		// clean up cache - resets the GridData for those that were changed
		// during the wrapping process
		for (int i = 0; i < flushLength; i++) {
			putCacheHeight(flush[i], -1);
			putCacheWidth(flush[i], -1);
		}

	}

	protected GridData getData(Control[][] grid, int row, int column,
			int rowCount, int columnCount, boolean first) {
		Control control = grid[row][column];
		if (control != null) {
			GridData data = (GridData) control.getLayoutData();
			if (data == null)
				data = new GridData();
			int hSpan = Math.max(1, Math.min(data.horizontalSpan, columnCount));
			int vSpan = Math.max(1, data.verticalSpan);
			int i = first ? row + vSpan - 1 : row - vSpan + 1;
			int j = first ? column + hSpan - 1 : column - hSpan + 1;
			if (0 <= i && i < rowCount) {
				if (0 <= j && j < columnCount) {
					if (control == grid[i][j])
						return data;
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {

		Display d = new Display();
		Shell s = new Shell();
		s.setLayout(new GridLayout());

		new Label(s, SWT.NONE).setText("Label"); //$NON-NLS-1$
		new Button(s, SWT.NONE).setText("Button"); //$NON-NLS-1$

		s.open();

		GridLayoutHelper helper = new GridLayoutHelper();
		helper.setComposite(s);

		while (!s.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		d.dispose();
	}

}
