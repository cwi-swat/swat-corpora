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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.GridLayoutEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutPolicyHelper.GridComponent;
import org.eclipse.swt.layout.GridLayout;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class GridLayoutGridFigure extends Figure {
	// number of pixels either side of a row/column that will trigger showing the row/column insertion figure.
	public static final int ROW_COLUMN_SENSITIVITY = 5;
	private GridLayoutPolicyHelper helper;
	private int marginWidth, marginHeight, verticalSpacing, horizontalSpacing;
	// Note: all of these that have "model" in the name positions are relative in the model coordinate system (which may be Right-to-Left and so backwards
	// from the GEF grid figure).
	int[] columnModelPositions, rowModelPositions;
	Point[] rowStartModelPositions, rowEndModelPositions;
	Point[] columnStartModelPositions, columnEndModelPositions;
	int[][] columnModelSegments, rowModelSegments;

	/**
	 * @param copy
	 */
	public GridLayoutGridFigure(GridLayoutPolicyHelper helper) {
		this.helper = helper;
		setBounds(helper.getClientArea());
		if (helper != null) {
			Rectangle spacing = helper.getLayoutSpacing();
			marginWidth = spacing.x;
			marginHeight = spacing.y;
			verticalSpacing = spacing.height;
			horizontalSpacing = spacing.width;
		} else {
			GridLayout example = new GridLayout();
			marginWidth = example.marginWidth;
			marginHeight = example.marginHeight;
			horizontalSpacing = example.horizontalSpacing;
			verticalSpacing = example.verticalSpacing;
		}
		if (helper != null) {
			int[][] layoutDimensions = helper.getLayoutDimensions();
			GridComponent[][] cellContents = helper.getLayoutTable();
			if (layoutDimensions != null) {
				int[] columnWidths = layoutDimensions[0];
				int[] rowHeights = layoutDimensions[1];
				columnModelSegments = calculateColumnDividers(columnWidths, rowHeights, cellContents);
				rowModelSegments = calculateRowDividers(columnWidths, rowHeights, cellContents);
			}
		}
	}

	protected int[][] calculateColumnDividers(int[] columnWidths, int[] rowHeights, GridComponent[][] cellContents) {
		if (rowHeights == null || columnWidths == null /* || helper.getClientArea() == null */)
			return null;

		int spacingLeft = (int) Math.ceil((double) horizontalSpacing / 2);
		int spacingRight = (int) Math.floor((double) horizontalSpacing / 2);
		int spacingTop = (int) Math.ceil((double) verticalSpacing / 2);
		int spacingBottom = (int) Math.floor((double) verticalSpacing / 2);

		int containerHeight = 0;
		columnModelPositions = new int[columnWidths.length + 1];
		columnStartModelPositions = new Point[columnModelPositions.length];
		columnEndModelPositions = new Point[columnModelPositions.length];
		int[][] columnSegments = new int[columnModelPositions.length][];

		for (int i = 0; i < rowHeights.length; i++) {
			containerHeight += rowHeights[i];
		}
		// add height for the spacing
		containerHeight += marginHeight * 2;
		containerHeight += verticalSpacing * (rowHeights.length - 1);

		Rectangle clientArea = /* helper.getClientArea() */getBounds().getCopy();
		int xPos = clientArea.x;
		int yMin = clientArea.y;
		int yMax = clientArea.y + containerHeight;
		int xMax = xPos + clientArea.width - 1; // This is the far right side, less one so that it draws within the box (otherwise it would be outside the box).

		// draw the first divider
		columnModelPositions[0] = xPos;
		columnStartModelPositions[0] = new Point(xPos, yMin);
		columnEndModelPositions[0] = new Point(xPos, yMax);
		columnSegments[0] = new int[] { yMin, yMax }; // The left border will always be one segemnt.

		// move up by the initial margin width
		xPos += marginWidth;

		int[] colSegs = new int[2 + rowHeights.length * 2]; // Each entry is a y position. They are in 2-tuples (start,stop) for a segment. For a col, it is max of one segment(or 2 points) + 2 for start of next.
		// draw the dividers in between and at the end
		for (int i = 1; i < columnModelPositions.length; i++) {
			xPos += columnWidths[i - 1];

			// Place the position in the middle of the the horizontal spacing gap
			if (i < columnWidths.length) {
				xPos += spacingLeft;
			} else {
				// or after the end margin, if this is the last line
				xPos += marginWidth;
			}

			xPos = Math.min(xPos, xMax);
			Point startPoint = new Point(xPos, yMin);
			columnModelPositions[i] = xPos;
			columnStartModelPositions[i] = startPoint;
			columnEndModelPositions[i] = new Point(xPos, yMax);

			// Now calculate the column segments.
			if (i < columnWidths.length && i < cellContents.length) {
				GridComponent[] leftColumn = cellContents[i - 1];
				GridComponent[] rightColumn = cellContents[i];
				int yPos = yMin;
				int lastRow = leftColumn.length - 1;
				int colSegsNdx = 0; // This will always point to the start index of the next segment.
				colSegs[colSegsNdx] = yMin; // Will always start at yMin.
				boolean prevSpan = true; // Previous was a span. (We treat first one as previous span so as not to close off an empty segment)
				// Walk each row, and compare left and right column to see if spanned.
				for (int j = 0; j < leftColumn.length; j++) {
					int trueRowHeight = 0;
					if (j < rowHeights.length)// if there is not this condition,it may cause an exception;
						trueRowHeight = rowHeights[j];
					if (j == 0)
						trueRowHeight += marginHeight;
					else
						trueRowHeight += spacingTop;
					if (j != lastRow)
						trueRowHeight += spacingBottom;
					else
						trueRowHeight += marginHeight;
					GridComponent leftObject, rightObject;
					if ((leftObject = leftColumn[j]) != helper.EMPTY_GRID && (rightObject = rightColumn[j]) != helper.EMPTY_GRID && leftObject == rightObject) {
						// We are spanning, so skip it and move start of segment to next cell.
						if (!prevSpan) {
							// Need to close off previous one (if not first)
							colSegs[++colSegsNdx] = yPos;
							colSegsNdx++; // Move to start of next.
						}
						yPos += trueRowHeight;
						colSegs[colSegsNdx] = yPos + 1; // Start of next seg.
						prevSpan = true;
					} else {
						// We are not spanning, continue line through it.
						prevSpan = false;
						yPos += trueRowHeight;
					}
				}
				if (colSegs[colSegsNdx] < yPos) {
					// We have to something to draw for last segment.
					colSegs[++colSegsNdx] = yMax;
				} else
					colSegsNdx--; // We had a segment start that is the same as the last stop, so get rid of it.
				columnSegments[i] = new int[++colSegsNdx];
				System.arraycopy(colSegs, 0, columnSegments[i], 0, colSegsNdx);
			} else {
				columnSegments[i] = new int[] { yMin, yMax }; // The right border will always be one segemnt.
			}
			// add the remainder of the spacing
			xPos += spacingRight;
		}

		return columnSegments;
	}

	protected int[][] calculateRowDividers(int[] columnWidths, int[] rowHeights, GridComponent[][] cellContents) {
		if (columnWidths == null || rowHeights == null /* || helper.getClientArea() == null */)
			return null;

		int spacingLeft = (int) Math.ceil((double) horizontalSpacing / 2);
		int spacingRight = (int) Math.floor((double) horizontalSpacing / 2);
		int spacingTop = (int) Math.ceil((double) verticalSpacing / 2);
		int spacingBottom = (int) Math.floor((double) verticalSpacing / 2);

		int containerWidth = 0;
		rowModelPositions = new int[rowHeights.length + 1];
		rowStartModelPositions = new Point[rowModelPositions.length];
		rowEndModelPositions = new Point[rowModelPositions.length];
		int[][] rowSegments = new int[rowModelPositions.length][];

		for (int i = 0; i < columnWidths.length; i++) {
			containerWidth += columnWidths[i];
		}
		// add width for the spacing
		containerWidth += marginWidth * 2;
		containerWidth += horizontalSpacing * (columnWidths.length - 1);

		Rectangle clientArea = /* helper.getClientArea() */getBounds().getCopy();
		int yPos = clientArea.y;
		int xMin = clientArea.x;
		int xMax = clientArea.x + containerWidth;
		int yMax = yPos + clientArea.height - 1; // This is the bottom side, less one so that it draws within the box (otherwise it would be outside the box).

		// draw the first divider
		rowModelPositions[0] = yPos;
		rowStartModelPositions[0] = new Point(xMin, yPos);
		rowEndModelPositions[0] = new Point(xMax, yPos);
		rowSegments[0] = new int[] { xMin, xMax }; // The top border will always be one segemnt.

		// move up the initial margin height
		yPos += marginHeight;

		int[] rowSegs = new int[2 + columnWidths.length * 2]; // Each entry is an x position. They are in 2-tuples (start,stop) for a segment. For a row, it is max of one segment(or 2 points) + 2 for start of next.
		// draw the dividers in between and at the end
		for (int i = 1; i < rowModelPositions.length; i++) {
			yPos += rowHeights[i - 1];

			// Place the position in the middle of the the vertical spacing gap
			if (i < rowHeights.length) {
				yPos += spacingTop;
			} else {
				// or after the end margin, if this is the last line
				yPos += marginHeight;
			}

			yPos = Math.min(yPos, yMax);
			Point startPoint = new Point(xMin, yPos);
			Point endPoint = new Point(xMax, yPos);
			rowModelPositions[i] = yPos;
			rowStartModelPositions[i] = startPoint;
			rowEndModelPositions[i] = endPoint;

			// Now calculate the row segments.
			if (i < rowHeights.length) {
				int upperRow = i - 1;
				int lowerRow = i;
				int xPos = xMin;
				int length = Math.min(cellContents.length, columnWidths.length);
				int lastCol = length - 1;
				int rowSegsNdx = 0; // This will always point to the start index of the next segment.
				rowSegs[rowSegsNdx] = xMin; // Will always start at xMin.
				boolean prevSpan = true; // Previous was a span. (We treat first one as previous span so as not to close off an empty segment)
				// Walk each column comparing upper row and lower to see if spanned.
				for (int j = 0; j < length; j++) {
					int trueColWidth = columnWidths[j];
					if (j == 0)
						trueColWidth += marginWidth;
					else
						trueColWidth += spacingLeft;
					if (j != lastCol)
						trueColWidth += spacingRight;
					else
						trueColWidth += marginWidth;
					GridComponent upperObject, lowerObject;
					if ((upperObject = cellContents[j][upperRow]) != helper.EMPTY_GRID && (lowerObject = cellContents[j][lowerRow]) != helper.EMPTY_GRID && upperObject == lowerObject) {
						// We are spanning, so skip it and move start of segment to next cell.
						if (!prevSpan) {
							// Need to close off previous one (if not first)
							rowSegs[++rowSegsNdx] = xPos;
							rowSegsNdx++; // Move to start of next.
						}
						xPos += trueColWidth;
						rowSegs[rowSegsNdx] = xPos + 1; // Start of next seg.
						prevSpan = true;
					} else {
						// We are not spanning, continue line through it.
						xPos += trueColWidth;
						prevSpan = false;
					}
				}
				if (rowSegs[rowSegsNdx] < xPos) {
					// We have to something to draw for last segment.
					rowSegs[++rowSegsNdx] = xMax;
				} else
					rowSegsNdx--; // We had a segment start that is the same as the last stop, so get rid of it.
				rowSegments[i] = new int[++rowSegsNdx];
				System.arraycopy(rowSegs, 0, rowSegments[i], 0, rowSegsNdx);
			} else {
				rowSegments[i] = new int[] { xMin, xMax }; // The bottom border will always be one segemnt.
			}

			// add the remainder of the spacing
			yPos += spacingBottom;
		}

		return rowSegments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		g.setForegroundColor(ColorConstants.gray);
		g.setLineStyle(Graphics.LINE_DOT);
		drawRowDividers(g);
		drawColumnDividers(g);
	}

	protected void drawColumnDividers(Graphics g) {
		if (columnModelSegments == null || columnStartModelPositions == null)
			return;
		Point fromPoint = new Point();
		Point toPoint = new Point();
		for (int i = 0; i < columnModelSegments.length; i++) {
			int[] colSegs = columnModelSegments[i];
			int xPos = columnStartModelPositions[i].x;
			int j = -1;
			while (++j < colSegs.length) {
				// Map from model to figure coordinates. First map to relative to figure, and then maps to absolute (through the figure bounds upper-left).
				// mapModelToFigure(fromPoint.setLocation(xPos, colSegs[j]));
				// mapModelToFigure(toPoint.setLocation(xPos, colSegs[++j]));
				fromPoint.setLocation(xPos, colSegs[j]);
				toPoint.setLocation(xPos, colSegs[++j]);
				g.drawLine(fromPoint, toPoint);
			}
		}
	}

	/**
	 * Draw the row dividers based on the GridBagLayout's origin and the row widths.
	 */
	protected void drawRowDividers(Graphics g) {
		if (rowModelSegments == null || rowStartModelPositions == null)
			return;
		Point fromPoint = new Point();
		Point toPoint = new Point();
		for (int i = 0; i < rowModelSegments.length; i++) {
			int[] rowSegs = rowModelSegments[i];
			int yPos = rowStartModelPositions[i].y;
			int j = -1;
			while (++j < rowSegs.length) {
				// Map from model to figure coordinates. First map to relative to figure, and then maps to absolute (through the figure bounds upper-left).
				// mapModelToFigure(fromPoint.setLocation(rowSegs[j], yPos));
				// mapModelToFigure(toPoint.setLocation(rowSegs[++j], yPos));
				fromPoint.setLocation(rowSegs[j], yPos);
				toPoint.setLocation(rowSegs[++j], yPos);
				g.drawLine(fromPoint, toPoint);
			}
		}
	}

	/**
	 * Get the cell location (i.e. the grid x/y) that the point (in model coordinates) is within.
	 * 
	 * @param p
	 *            point to look for what cell it is in. It is in model coordinates.
	 * @return the cell grid x/y as a point. '-1' for a grid means the incoming dimension was to the left/above the left-most/top-most col/row.
	 * 
	 * @since 1.2.0
	 */
	public Point getCellLocation(Point p) {
		return getCellLocation(p.x, p.y);
	}

	/**
	 * Get the cell bounds for the cell that the position is within.
	 * 
	 * @param pos
	 *            position in col,row.
	 * @return cell bounds in model coor of the cell that the pos is in. If outside model, it will have a default size. This point may be modified.
	 * 
	 * @since 1.2.0
	 */
	public Rectangle getCellBounds(Point pos) {
		if (rowModelPositions == null || columnModelPositions == null)
			return new Rectangle(0, 0, GridLayoutEditPolicy.DEFAULT_CELL_WIDTH, GridLayoutEditPolicy.DEFAULT_CELL_HEIGHT);

		int cellxpos, cellypos, cellwidth, cellheight;
		if (pos.x < 0)
			cellxpos = columnModelPositions[0] - GridLayoutEditPolicy.DEFAULT_CELL_WIDTH;
		else
			cellxpos = columnModelPositions[pos.x];
		if (pos.x < columnModelPositions.length - 1)
			cellwidth = columnModelPositions[pos.x + 1] - cellxpos;
		else
			cellwidth = GridLayoutEditPolicy.DEFAULT_CELL_WIDTH;

		if (pos.y < 0)
			cellypos = rowModelPositions[0] - GridLayoutEditPolicy.DEFAULT_CELL_HEIGHT;
		else
			cellypos = rowModelPositions[pos.y];
		if (pos.y < rowModelPositions.length - 1)
			cellheight = rowModelPositions[pos.y + 1] - cellypos;
		else
			cellheight = GridLayoutEditPolicy.DEFAULT_CELL_HEIGHT;

		return new Rectangle(cellxpos, cellypos, cellwidth, cellheight);
	}

	/**
	 * Get the rectangle for the column left hand side (in model terms) for the column sent in.
	 * 
	 * @param col
	 *            the column to get the rect for.
	 * @return rect (0 width) for the left hand side of the column. This rect can be modified by caller. It will be in model coors.
	 * 
	 * @since 1.2.0
	 */
	public Rectangle getColumnRectangle(int col) {
		if (columnStartModelPositions != null) {
			return new Rectangle(columnStartModelPositions[col], columnEndModelPositions[col]).resize(-1, -1); // 1.
		}
		return new Rectangle();
	}

	/**
	 * Get the rectangle for the row top hand side for the row sent in.
	 * 
	 * @param row
	 *            the row to get the row rect for.
	 * @return rect (0 width) for the top side of the row. It will be in model coors. It can be modified.
	 * 
	 * @since 1.2.0
	 */
	public Rectangle getRowRectangle(int row) {
		if (rowStartModelPositions != null) {
			return new Rectangle(rowStartModelPositions[row], rowEndModelPositions[row]).resize(-1, -1);
		}
		return new Rectangle();
	}

	/**
	 * Get the cell location (i.e. the grid x/y) that the point (in model coordinates) is within.
	 * 
	 * @param x
	 *            x to look for what cell it is in. It is in model coordinates.
	 * @param y
	 *            y to look for what cell it is in. It is in model coordinates.
	 * @return the cell grid x/y as a point. '-1' for a grid means the incoming dimension was to the left/above the left-most/top-most col/row.
	 * 
	 * 
	 * @since 1.2.0
	 */
	public Point getCellLocation(int x, int y) {
		return getCellLocation(x, y, false, false);
	}

	/**
	 * Get the cell location (i.e. the grid x/y) that the point (in model coordinates) is within.
	 * 
	 * @param x
	 *            x to look for what cell it is in. It is in model coordinates.
	 * @param y
	 *            y to look for what cell it is in. It is in model coordinates.
	 * @param includeEmptyColumns
	 * @param includeEmptyRows
	 * @return the cell grid x/y as a point. '-1' for a grid means the incoming dimension was to the left/above the left-most/top-most col/row.
	 * 
	 * @since 1.2.0
	 */
	public Point getCellLocation(int x, int y, boolean includeEmptyColumns, boolean includeEmptyRows) {
		if (rowModelPositions == null || columnModelPositions == null)
			return new Point(0, 0);

		int gridx = -1, gridy = -1;
		boolean foundx = false, foundy = false;
		for (int i = 0; i < columnModelPositions.length - 1; i++) {
			int xpos = columnModelPositions[i];
			if (x >= xpos && x < columnModelPositions[i + 1]) {
				gridx = i;
				if (includeEmptyColumns) {
					/*
					 * Since column positions can be equal if there are columns that don't contain components, iterate back throught the columns positions to get the first one with this position.
					 */
					int j;
					for (j = i; j >= 0 && columnModelPositions[i] == columnModelPositions[j]; j--)
						// ;
						gridx = j + 1;
				}
				foundx = true;
				break;
			}
		}
		for (int i = 0; i < rowModelPositions.length - 1; i++) {
			int ypos = rowModelPositions[i];
			if (y >= ypos && y < rowModelPositions[i + 1]) {
				gridy = i;
				if (includeEmptyRows) {
					/*
					 * Since row positions can be equal if there are rows that don't contain components, iterate back throught the rows to get the first one with this position.
					 */
					int j;
					for (j = i; j >= 0 && rowModelPositions[i] == rowModelPositions[j]; j--)
						gridy = j + 1;
				}
				foundy = true;
				break;
			}
		}

		if (!foundx && x >= columnModelPositions[columnModelPositions.length - 1])
			gridx = columnModelPositions.length - 1;
		if (!foundy && y >= rowModelPositions[rowModelPositions.length - 1])
			gridy = rowModelPositions.length - 1;

		return new Point(gridx, gridy);
	}

	/**
	 * Get the grid figure rect for the specified cell dimensions. The cells dimensions are packed into a Rectangle according to the following rules:
	 * 
	 * rect.x = column position rect.y = row position rect.width = horizontal span rect.height = vertical span
	 * 
	 * @param cellsBounds
	 *            The cell area to calculate
	 * @return the rect in model coor. for the grid representing these cells.
	 * 
	 * @since 1.0.0
	 */
	public Rectangle getGridBroundsForCellBounds(Rectangle cellsBounds) {
		Rectangle r = new Rectangle();
		if (rowModelPositions != null && columnModelPositions != null && cellsBounds.y <= rowModelPositions.length - 1 && cellsBounds.x <= columnModelPositions.length - 1) {
			r.x = columnModelPositions[cellsBounds.x];
			r.y = rowModelPositions[cellsBounds.y];

			if (cellsBounds.x + cellsBounds.width > columnModelPositions.length - 1) {
				r.width = columnModelPositions[columnModelPositions.length - 1];
			} else {
				r.width = columnModelPositions[cellsBounds.x + cellsBounds.width];
			}
			r.width -= r.x;

			if (cellsBounds.y + cellsBounds.height > rowModelPositions.length - 1) {
				r.height = rowModelPositions[rowModelPositions.length - 1];
			} else {
				r.height = rowModelPositions[cellsBounds.y + cellsBounds.height];
			}
			r.height -= r.y;
		}
		return r;

	}

	/**
	 * Get the grid layout request type from the given position in model coor.
	 * 
	 * @param pos
	 *            position in model coor.
	 * @param helper
	 *            helper to use.
	 * @return gridlayout request for the given position.
	 * 
	 * @since 1.2.0
	 */
	public GridLayoutRequest getGridLayoutRequest(Point pos, GridLayoutPolicyHelper helper) {
		Point cell = getCellLocation(pos);// get the control position(column,row)
		GridLayoutRequest req = new GridLayoutRequest();
		req.column = cell.x;
		req.row = cell.y;

		if (req.column == -1 || req.row == -1)
			req.type = GridLayoutRequest.NO_ADD; // We are above or to the left, we can't add.
		else if (columnModelPositions == null || req.column >= columnModelPositions.length - 1)
			if (rowModelPositions == null || req.row >= rowModelPositions.length - 1)
				req.type = GridLayoutRequest.ADD_ROW_COL;
			else
				req.type = GridLayoutRequest.ADD_COLUMN;
		else if (req.row >= rowEndModelPositions.length - 1)
			req.type = GridLayoutRequest.ADD_ROW;
		else {
			int colSensitive;
			int nextCol = req.column + 1;
			int colWidth = columnModelPositions[nextCol] - columnModelPositions[req.column];
			if (colWidth <= 2 * ROW_COLUMN_SENSITIVITY) {
				// We are inside a cell that is too small (the row/col sensitivities would override and we couldn't drop in it)
				// Reduce col sensitivity to a minimum. These reductions will mean that the col sensitivities from
				// the right side and from the left side will not overlap. If they overlapped it would be difficult
				// to figure which side should get the insert.
				if (colWidth <= 5)
					colSensitive = 0; // No sensitivity. Must be right on.
				else
					colSensitive = 1; // Decrease to 1.
			} else
				colSensitive = ROW_COLUMN_SENSITIVITY;
			if (pos.x - columnModelPositions[req.column] <= colSensitive) {
				if (helper.isEmptyAtCell(cell) || helper.isFillerLabelAtCell(cell))
					req.type = GridLayoutRequest.INSERT_COLUMN;
				else {
					// We are over a control. If we are over a spanned section of the control, then it must be
					// changed to insert column in row back at the left side of the control.
					Rectangle childDim = helper.getChildDimensions(cell);
					// If empty (childDim == null) then insert col.
					if (childDim != null && childDim.x < cell.x) {
						req.type = GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW;
						req.column = childDim.x; // Move back to start of child.
					} else
						req.type = GridLayoutRequest.INSERT_COLUMN; // It's ok, we are at the left side of the child.
				}
			} else if (columnModelPositions[nextCol] - pos.x <= colSensitive) {
				cell.x = req.column = nextCol; // Nearer to next column.
				if (req.column < columnModelPositions.length - 1) {
					if (helper.isEmptyAtCell(cell) || helper.isFillerLabelAtCell(cell))
						req.type = GridLayoutRequest.INSERT_COLUMN;
					else {
						// We are over a control. If we are over a spanned section of the control, then it must be
						// changed to insert column in row back at the left side of the control.
						Rectangle childDim = helper.getChildDimensions(cell);
						// If empty (childDim == null) then insert column.
						if (childDim != null && childDim.x < cell.x) {
							req.type = GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW;
							req.column = childDim.x; // Move back to start of child.
						} else
							req.type = GridLayoutRequest.INSERT_COLUMN; // It's ok, we are at the left side of the child.
					}
				} else
					req.type = GridLayoutRequest.ADD_COLUMN;
			} else {
				int rowSensitive;
				int nextRow = req.row + 1;
				int rowHeight = rowModelPositions[nextRow] - rowModelPositions[req.row];
				if (rowHeight <= 2 * ROW_COLUMN_SENSITIVITY) {
					// We are inside a cell that is too small (the row/col sensitivities would override and we couldn't drop in it)
					// Reduce row sensitivity to a minimum. These reductions will mean that the row sensitivities from
					// the top side and from the bottom side will not overlap. If they overlapped it would be difficult
					// to figure which side should get the insert.
					if (rowHeight <= 5)
						rowSensitive = 0; // No sensitivity. Must be right on.
					else
						rowSensitive = 2; // Decrease to 2.
				} else
					rowSensitive = ROW_COLUMN_SENSITIVITY;
				;
				if (pos.y - rowModelPositions[req.row] <= rowSensitive) {
					if (helper.isEmptyAtCell(cell) || helper.isFillerLabelAtCell(cell))
						req.type = GridLayoutRequest.INSERT_ROW;
					else {
						// We are over a control. If we are over a spanned section of the control, then it must be
						// changed to no add. Can't insert row over a vertically spanned control.
						Rectangle childDim = helper.getChildDimensions(cell);
						// If empty (childDim == null) then insert row.
						if (childDim != null && childDim.y < cell.y) {
							req.type = GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW;
							req.column = childDim.x; // Move back to start of child.
						} else
							req.type = GridLayoutRequest.INSERT_ROW; // It's ok, we are at the top side of the child.
					}
				} else if (rowModelPositions[nextRow] - pos.y <= rowSensitive) {
					cell.y = req.row = nextRow; // Nearer to next row.
					req.type = req.row < rowModelPositions.length - 1 ? GridLayoutRequest.INSERT_ROW : GridLayoutRequest.ADD_ROW; // Add if next
					// row is
					// actually
					// outside.
					if (req.row < rowModelPositions.length - 1) {
						// We are over a control. If we are over a spanned section of the control, then it must be
						// changed to no add. Can't insert row over a vertically spanned control.
						Rectangle childDim = helper.getChildDimensions(cell);
						// If empty (childDim == null) then insert row.
						if (childDim != null && childDim.y < cell.y) {
							req.type = GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW;
							req.column = childDim.x; // Move back to start of child.
						} else
							req.type = GridLayoutRequest.INSERT_ROW; // It's ok, we are at the top side of the child.
					} else
						req.type = GridLayoutRequest.ADD_ROW;

				} else {
					// In the cell, see if replace filler, insert column in row, or replace empty.
					if (helper.isEmptyAtCell(cell))
						req.type = GridLayoutRequest.ADD_TO_EMPTY_CELL;
					else if (helper.isFillerLabelAtCell(cell))
						req.type = GridLayoutRequest.REPLACE_FILLER;
					else {
						Rectangle childDim = helper.getChildDimensions(cell);
						req.type = GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW;
						if (childDim != null && childDim.x < cell.x)
							req.column = childDim.x; // Move back to start of child.
					}
				}
			}
		}

		return req;
	}
}
