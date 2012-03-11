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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ApplyAttributeSettingCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.GridLayoutCommandsFactory;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.GridLayoutHelper;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.GridLayoutHelper_30;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.VisualInfo;
import org.eclipse.e4.xwt.tools.ui.designer.loader.ResourceVisitor;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ShellEditPart;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.tools.AnnotationTools;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class GridLayoutPolicyHelper {

	public static final String FILLER_DATA = "Filter_DATA";
	private static final String NO_MODS = "NOMODS"; //$NON-NLS-1$
	private static final int NOT_MODIFIED_SPAN = -1;
	private static final int SET_TO_DEFAULT_SPAN = -2;

	public final GridComponent EMPTY_GRID = new GridComponent(null);
	private XamlElement gridLayout;
	private CompositeEditPart host;
	protected GridComponent[][] glayoutTable;
	protected int numColumns = -1;
	protected int originalNumColumns;
	protected int defaultHorizontalSpan, defaultVerticalSpan;
	protected GridComponent first, last;
	private List<XamlNode> deletedComponents = new ArrayList<XamlNode>();
	private List<XamlNode> orphanedComponents;
	private GridLayoutCommandsFactory factory;

	public GridLayoutPolicyHelper() {
		initializeDefaults();
	}

	public void setHost(CompositeEditPart host) {
		this.host = host;
		this.gridLayout = ((CompositeEditPart) host).getLayoutModel();
		factory = new GridLayoutCommandsFactory(host);
	}

	protected void initializeDefaults() {
		GridData gd = new GridData();
		defaultHorizontalSpan = gd.horizontalSpan;
		defaultVerticalSpan = gd.verticalSpan;
	}

	public Rectangle getLayoutSpacing() {
		Rectangle result = new Rectangle();
		GridLayout layout = (GridLayout) XWTProxy.createValue(gridLayout);
		if (layout == null) {
			layout = new GridLayout();
		}
		result.x = layout.marginWidth;
		result.y = layout.marginHeight;
		result.width = layout.horizontalSpacing;
		result.height = layout.verticalSpacing;
		return result;
	}

	/**
	 * @return
	 */
	public int[][] getLayoutDimensions() {
		Composite composite = getComposite();
		if (composite == null) {
			return null;
		}
		int[][] result = new int[2][];
		int[] widths = null;
		int[] heights = null;
		int version = SWT.getVersion();
		if (version > 3100) {
			GridLayoutHelper helper = new GridLayoutHelper();
			helper.setComposite(composite);
			widths = helper.widths;
			heights = helper.heights;
		} else {
			GridLayoutHelper_30 helper = new GridLayoutHelper_30();
			helper.setComposite(composite);
			widths = helper.widths;
			heights = helper.heights;
		}

		if (widths == null) {
			result[0] = new int[0];
		} else {
			result[0] = widths;
		}
		if (heights == null) {
			result[1] = new int[0];
		} else {
			result[1] = heights;
		}
		return result;
	}

	/**
	 * @return
	 */
	private Composite getComposite() {
		if (host == null) {
			return null;
		}
		VisualInfo visualComp = (VisualInfo) host.getVisualInfo();
		if (visualComp == null || visualComp.getVisualObject() == null) {
			return null;
		}
		return (Composite) visualComp.getVisualObject();
	}

	/**
	 * @return
	 */
	public GridComponent[][] getLayoutTable() {
		if (glayoutTable == null) {
			int[][] dimensions = getLayoutDimensions();
			if (dimensions == null) {
				return null;
			}
			Composite composite = getComposite();
			if (composite == null) {
				return null;
			}
			GridLayout layout = getGridLayout();
			if (layout == null) {
				return null;
			}
			glayoutTable = new GridComponent[dimensions[0].length][dimensions[1].length];
			// Original num columns is the actual setting in the layout.
			originalNumColumns = layout.numColumns;
			numColumns = dimensions[0].length;

			// If empty container, don't continue.
			if (glayoutTable.length < 1 || glayoutTable[0].length < 1) {
				return glayoutTable;
			}

			int row = 0;
			int col = 0;
			int horizontalSpan;
			int verticalSpan;

			int childNum = 0;

			Control[] children = composite.getChildren();
			for (Control child : children) {
				XamlNode model = (XamlNode) child.getData(ResourceVisitor.ELEMENT_KEY);
				GridComponent gcomp = new GridComponent(model);
				gcomp.filler = AnnotationTools.isAnnotated(model, FILLER_DATA);
				addGridComponent(gcomp);
				Object layoutData = child.getLayoutData();
				if (layoutData == null || !(layoutData instanceof GridData)) {
					horizontalSpan = defaultHorizontalSpan;
					verticalSpan = defaultVerticalSpan;
				} else {
					GridData gd = (GridData) layoutData;
					horizontalSpan = gd.horizontalSpan;
					verticalSpan = gd.verticalSpan;
				}
				Rectangle r = new Rectangle();

				// Find the next un-occupied cell
				while (row < glayoutTable[0].length && glayoutTable[col][row] != null) {
					col += 1;
					if (col >= numColumns) {
						row += 1;
						col = 0;
					}
				}
				// if there's not enough columns left for the horizontal span, go to
				// next row
				if (col != 0 && (col + horizontalSpan - 1) >= numColumns) {
					row += 1;
					col = 0;
				}

				// Add the child to the table in all spanned cells. Handle users coding mistakes by not spanning past the end of the table.
				int maxColSpan = Math.min(col + horizontalSpan, glayoutTable.length);
				int maxRowSpan = Math.min(row + verticalSpan, glayoutTable[col].length);
				for (int coli = col; coli < maxColSpan; coli++) {
					for (int rowj = row; rowj < maxRowSpan; rowj++) {
						glayoutTable[coli][rowj] = gcomp;
					}
				}

				r.x = col;
				r.y = row;
				r.width = maxColSpan - col; // True horizontal span
				r.height = maxRowSpan - row; // True vertical span
				gcomp.gridDimension = r;
				if (r.width != horizontalSpan)
					gcomp.setSpanWidth(r.width);
				if (r.height != verticalSpan)
					gcomp.setSpanHeight(r.height);
				childNum++;

				// Add the spanned columns to the column position
				col += r.width - 1;
			}
		}
		// Now change all null entries to be EMPTY.
		for (int i = 0; i < glayoutTable.length; i++) {
			for (int j = 0; j < glayoutTable[i].length; j++) {
				if (glayoutTable[i][j] == null) {
					glayoutTable[i][j] = EMPTY_GRID;
				}
			}
		}
		return glayoutTable;
	}

	public Rectangle getClientArea() {
		if (host instanceof ShellEditPart){
			return host.getVisualInfo().getClientArea();
		}
		return host.getFigure().getBounds();
	}

	protected void insertGridGomponentAtBeginning(GridComponent gc) {
		gc.next = first;
		gc.prev = null;
		if (first == null)
			last = gc;
		else
			first.prev = gc;
		first = gc;
	}

	protected void insertGridComponentBefore(GridComponent gc, GridComponent before) {
		if (before != null) {
			if (before == first) {
				first = gc;
			} else
				before.prev.next = gc;
			gc.next = before;
			gc.prev = before.prev;
			before.prev = gc;
		} else
			addGridComponent(gc);
	}

	private void addGridComponent(GridComponent gc) {
		if (last == null)
			insertGridGomponentAtBeginning(gc);
		else {
			last.next = gc;
			gc.prev = last;
			gc.next = null;
			last = gc;
		}
	}

	private GridLayout getGridLayout() {
		Composite composite = getComposite();
		if (composite != null && !composite.isDisposed()) {
			return (GridLayout) composite.getLayout();
		}
		return null;
	}

	/**
	 * @return
	 */
	public CompositeEditPart getEditPart() {
		return host;
	}

	public void refresh() {
		glayoutTable = null;
		first = last = null;
		numColumns = originalNumColumns = -1;
	}

	protected XamlNode getLayoutData(Object parent) {
		if (parent == null || !(parent instanceof XamlNode)) {
			return null;
		}
		XamlNode model = (XamlNode) parent;
		XamlNode layoutData = null;
		XamlAttribute attr = model.getAttribute("layoutData", IConstants.XWT_NAMESPACE);
		if (attr != null && !attr.getChildNodes().isEmpty()) {
			layoutData = attr.getChildNodes().get(0);
		}
		return layoutData;
	}

	/**
	 * @param cell
	 * @return
	 */
	public boolean isEmptyAtCell(Point cell) {
		getLayoutTable();
		// Check to make sure the cell position is within the grid
		if (cell.x >= 0 && cell.x < glayoutTable.length && cell.y >= 0 && cell.y < glayoutTable[0].length)
			return glayoutTable[cell.x][cell.y] == EMPTY_GRID;
		return false;
	}

	/**
	 * @param cell
	 * @return
	 */
	public boolean isFillerLabelAtCell(Point cell) {
		// Check to make sure the cell position is within the grid
		if (cell.x >= 0 && cell.x < glayoutTable.length && cell.y >= 0 && cell.y < glayoutTable[0].length)
			return glayoutTable[cell.x][cell.y].isFillerLabel();
		return false;
	}

	/**
	 * @param cell
	 * @return
	 */
	public Rectangle getChildDimensions(Point cell) {
		getLayoutTable();
		if (cell.x < 0 || cell.x >= glayoutTable.length || cell.y < 0 || cell.y >= glayoutTable[0].length)
			return null;
		GridComponent gc = glayoutTable[cell.x][cell.y];
		if (gc != EMPTY_GRID)
			return gc.gridDimension;
		else
			return null;
	}

	/**
	 * Get the child dimensions for the child.
	 * 
	 * @param child
	 *            child to find dimensions of.
	 * @return rectangle of dimensions or <code>null</code> if child not a component. This rectangle must not be modified.
	 * @since 1.2.0
	 */
	public Rectangle getChildDimensions(EObject child) {
		getLayoutTable();
		GridComponent gc = getComponent(child);
		if (gc != null)
			return gc.gridDimension;
		else
			return null;
	}

	/**
	 * 
	 */
	public void startRequest() {
		getLayoutTable();
		deletedComponents.clear();
		if (orphanedComponents != null) {
			orphanedComponents.clear();
		}
	}

	private GridComponent getComponentIfMove(EObject childEObject, Object requestType) {
		if (RequestConstants.REQ_CREATE.equals(requestType) || RequestConstants.REQ_ADD.equals(requestType))
			return null;
		return getComponent(childEObject);
	}

	/**
	 * Get the component for the given child.
	 * 
	 * @param childEObject
	 * @return the component or <code>null</code> if not in the layout.
	 * @since 1.2.0
	 */
	protected GridComponent getComponent(EObject childEObject) {
		if (childEObject == null)
			return null;
		GridComponent gc = first;
		while (gc != null) {
			if (gc.model == childEObject)
				return gc;
			gc = gc.next;
		}
		return null;
	}

	protected void deleteComponent(GridComponent gcomp) {
		if (gcomp.requestType != null && !gcomp.requestType.equals(RequestConstants.REQ_ADD)
				&& !gcomp.requestType.equals(RequestConstants.REQ_CREATE)) {
			// If it was create or add, then it wasn't here to begin with so no need to add to deleted list.
			addToDeleted(gcomp.model);
		}
	}

	protected void addToDeleted(XamlNode child) {
		if (deletedComponents == null)
			deletedComponents = new ArrayList<XamlNode>();
		deletedComponents.add(child);
	}

	protected void addToOrphaned(XamlNode child) {
		if (orphanedComponents == null)
			orphanedComponents = new ArrayList<XamlNode>();
		orphanedComponents.add(child);
	}

	/**
	 * Used by callers to delete the child from the layout. It will clean up and remove now empty columns and rows that the child used to cover. It will also call the appropriate delete from the container policy.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param child
	 * @param delete
	 * @since 1.2.0
	 */
	public void deleteChild(XamlNode child) {
		GridComponent gc = getComponent(child);
		if (gc != null)
			removeChild(gc, true, false); // Don't force removal of fillers. That is because if it would be replaced by a filler the user would get confused and think nothing happened. Instead they will get a not sign.
		else
			addToDeleted(child); // May be some other non-grid child. Let it get deleted.

	}

	/**
	 * Used by callers to orphan the child from the layout. It will clean up and remove now empty columns and rows that the child used to cover. It will also call the appropriate orphan from the container policy.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param child
	 * @param delete
	 * @since 1.2.0
	 */
	public void orphanChild(XamlNode child) {
		GridComponent gc = getComponent(child);
		if (gc != null)
			removeChild(gc, false, true); // Need to force removal because it is going away to a new parent.
		addToOrphaned(child);
	}

	/**
	 * Same as {@link #orphanChild(EObject)} except it does it for each child in the list.
	 * 
	 * @param children
	 * @since 1.2.0
	 */
	public void orphanChildren(List<XamlNode> children) {
		for (Iterator<XamlNode> iter = children.iterator(); iter.hasNext();) {
			XamlNode child = iter.next();
			orphanChild(child);
		}
	}

	/**
	 * Remove the gridcomponent from the table and clean up rows/cols if needed. Also set it as a child to delete from the container if the delete flag is true.
	 * 
	 * @param oldChild
	 * @param delete
	 * @param forceRemove
	 *            force the remove. If the oldChild was a filler and this is <code>false</code> it doesn't actually remove it. That is because if it was removed, it would just put a filler back in its place, and then see if row/col should be deleted. If using <code>true</code> then it will force a removal of the filler. Even though a filler will go back in its place, this may still be necessary
	 *            because the actual eobject has trully been orphaned or moved. And in that case we need to remove it.
	 * @since 1.2.0
	 */
	protected void removeChild(GridComponent oldChild, boolean delete, boolean forceRemove) {
		// First replace the child squares with Empties, then fill in with fillers.
		// Remove the child from the linked list and delete it if requested
		// And finally clear out now exposed empty rows/cols.

		int toCol = oldChild.gridDimension.x + oldChild.gridDimension.width - 1;
		int toRow = oldChild.gridDimension.y + oldChild.gridDimension.height - 1;
		if (!oldChild.isFillerLabel() || forceRemove) {
			removeGridComponent(oldChild);
			if (delete)
				deleteComponent(oldChild);

			for (int col = oldChild.gridDimension.x; col <= toCol; col++) {
				for (int row = oldChild.gridDimension.y; row <= toRow; row++) {
					glayoutTable[col][row] = EMPTY_GRID;
				}
			}
			for (int col = oldChild.gridDimension.x; col <= toCol; col++) {
				for (int row = oldChild.gridDimension.y; row <= toRow; row++) {
					replaceEmptyCell(createFillerComponent(), col, row);
				}
			}
		}

		// Now remove empty rows/cols.
		for (int remRow = toRow; remRow >= oldChild.gridDimension.y; remRow--)
			removeRowIfEmpty(remRow);
		for (int remCol = toCol; remCol >= oldChild.gridDimension.x; remCol--)
			removeColIfEmpty(remCol);
	}

	/**
	 * Remove this row if contains nothing but empties, or fillers.
	 * 
	 * @param row
	 * @return <code>true</code> if the row was removed.
	 * @since 1.2.0
	 */
	protected boolean removeRowIfEmpty(int row) {
		for (int col = 0; col < glayoutTable.length; col++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else if (gc.isFillerLabel())
				continue;
			else
				return false; // We have one that starts on this row
		}

		// We have an empty row. Now go through and remove all of the fillers, decrease by one any vertical spans, and then just
		// move up the entire layout one row.
		for (int col = 0; col < glayoutTable.length; col++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else {
				// It must be a filler. already verified that above.
				removeGridComponent(gc);
				deleteComponent(gc);
			}
		}

		for (int col = 0; col < glayoutTable.length; col++) {
			GridComponent[] oldCol = glayoutTable[col];
			GridComponent[] newCol = glayoutTable[col] = new GridComponent[oldCol.length - 1];
			System.arraycopy(oldCol, 0, newCol, 0, row);
			System.arraycopy(oldCol, row + 1, newCol, row, newCol.length - row);
			// And finally! update the grid dimensions of all of the moved controls.
			for (int rrow = row; rrow < newCol.length; rrow++) {
				GridComponent gc = newCol[rrow];
				if (gc != EMPTY_GRID) {
					if (gc.gridDimension.x == col && gc.gridDimension.y == rrow + 1) {
						gc.gridDimension.y = rrow;
					}
					// Skip over control to get to next filled row.
					rrow = (gc.gridDimension.y + gc.gridDimension.height - 1);
				}
			}
		}

		return true;
	}

	/**
	 * Remove this col if contains nothing but empties, ir fillers.
	 * 
	 * @param col
	 * @return <code>true</code> if the col was removed.
	 * @since 1.2.0
	 */
	protected boolean removeColIfEmpty(int col) {
		for (int row = 0; row < glayoutTable[col].length; row++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else if (gc.isFillerLabel())
				continue;
			else
				return false; // We have one that starts on this col
		}

		// We have an empty col. Now go through and remove all of the fillers, decrease by one any horizontal spans, and then just
		// move up the entire layout one col.
		for (int row = 0; row < glayoutTable[col].length; row++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else {
				// Must be a filler. Already verified that above.
				removeGridComponent(gc);
				deleteComponent(gc);
			}
		}

		GridComponent[][] oldLayout = glayoutTable;
		glayoutTable = new GridComponent[glayoutTable.length - 1][];
		System.arraycopy(oldLayout, 0, glayoutTable, 0, col);
		System.arraycopy(oldLayout, col + 1, glayoutTable, col, glayoutTable.length - col);
		numColumns = glayoutTable.length;
		// And finally! update the grid dimensions of all of the moved controls.
		for (int rcol = col; rcol < glayoutTable.length; rcol++) {
			// Need to rcol in the next for loop because we modify rcol within the for loop.
			for (int rrow = 0; rcol < glayoutTable.length && rrow < glayoutTable[rcol].length; rrow++) {
				GridComponent gc = glayoutTable[rcol][rrow];
				if (gc != EMPTY_GRID) {
					if (gc.gridDimension.x == rcol + 1 && gc.gridDimension.y == rrow) {
						gc.gridDimension.x = rcol;
					}
					// Skip over control to get to next filled col.
					rcol = (gc.gridDimension.x + gc.gridDimension.width - 1);
				}
			}
		}

		return true;
	}

	/**
	 * Replace the empty cell. The child must be a new grid component. It cannot be one that already exists. It could be modified during the execution.
	 * 
	 * @param child
	 * @param cellCol
	 * @param cellRow
	 * @since 1.2.0
	 */
	protected void replaceEmptyCell(GridComponent child, int cellCol, int cellRow) {
		if (glayoutTable[cellCol][cellRow] != EMPTY_GRID)
			return; // Invalid request.

		GridComponent movedComponent = getComponentIfMove(child.model, child.requestType);

		// Find the next occupied cell to be used as the before object.
		GridComponent before = findNextValidGC(cellCol, cellRow);

		// Need to find all empties between the object BEFORE the beforeObject (i.e. the object we are going AFTER)
		// and our position. They need to be replaced with filler. Otherwise ours will not be placed correctly.
		GridComponent componentBeforeUs = before != null ? before.prev : last;
		Rectangle beforeUsDim = componentBeforeUs != null ? componentBeforeUs.gridDimension : new Rectangle();
		int startCol = beforeUsDim.x;
		for (int row = beforeUsDim.y; row <= cellRow; row++) {
			int endCol = row != cellRow ? glayoutTable.length : cellCol; // Stop at our cell only on our row, else do entire row.
			for (int col = startCol; col < endCol; col++) {
				if (glayoutTable[col][row] == EMPTY_GRID) {
					insertComponent(createFillerComponent(), before, col, row, 1, 1);
				}
			}
			startCol = 0; // After the first row we want to start in first col.
		}

		if (movedComponent == null)
			insertComponent(child, before, cellCol, cellRow, 1, 1);
		else {
			child.setMovedComponent(movedComponent);
			insertComponent(child, before, cellCol, cellRow, 1, 1);
			removeChild(movedComponent, false, true); // Need to force removal because if it was a filler it is now in a new place.
		}
	}

	/**
	 * Insert the component in the ordered list before the given component, and fill in the layout table with the new component. It is assumed that the slots taken by the new component are empty (or the current nameMap have been handled and can be replaced by this new component).
	 * 
	 * @param gc
	 * @param beforeComponent
	 *            component to be ordered before, <code>null</code> for add to end.
	 * @param x
	 * @param y
	 * @param spanX
	 * @param spanY
	 * @since 1.2.0
	 */
	protected void insertComponent(GridComponent gc, GridComponent beforeComponent, int x, int y, int spanX, int spanY) {
		gc.gridDimension = new Rectangle(x, y, spanX, spanY);
		insertGridComponentBefore(gc, beforeComponent);
		// Now fill in the slots
		int stopCol = x + spanX;
		int stopRow = y + spanY;
		for (int col = x; col < stopCol; col++) {
			for (int row = y; row < stopRow; row++)
				glayoutTable[col][row] = gc;
		}
	}

	private GridComponent findNextValidGC(int columnStart, int rowStart) {
		if (glayoutTable.length == 0 || glayoutTable[0].length == 0)
			return null;

		// Find the next occupied cell to be used as the before object.
		int col = columnStart, row = rowStart;
		for (int i = row; i < glayoutTable[0].length; i++) {
			for (int j = col; j < glayoutTable.length; j++) {
				GridComponent child = glayoutTable[j][i];
				if (child != EMPTY_GRID) {
					// If the row is going through a control that is spanning vertically more than one
					// row, skip it. This is checked by comparing this control's starting y (row) value
					// with where we are in the table lookup. If it doesn't span vertically, then its gridY will be the same as the row.
					if (child.gridDimension.y == i) {
						return child;
					}
				}
			}
			col = 0; // Reset so that we now start from beginning of all subsequent rows.
		}
		return null;

	}

	/**
	 * 
	 */
	private GridComponent createFillerComponent() {
		XamlElement filler = XamlFactory.eINSTANCE.createElement("Label", IConstants.XWT_NAMESPACE);
		// XamlElement comp = (XamlElement) host.getModel();
		// comp.getChildNodes().add(filler);
		GridComponent gc = new GridComponent(filler, true);
		AnnotationTools.addAnnotation(filler, FILLER_DATA, FILLER_DATA);
		return gc;
	}

	/**
	 * @param child
	 * @param requestType
	 * @param cell
	 */
	public void replaceFiller(XamlNode child, Object requestType, Point cell) {
		if (!isFillerLabelAtCell(cell))
			return; // Invalid request.
		GridComponent movedComponent = getComponentIfMove(child, requestType);
		GridComponent compAtCell = glayoutTable[cell.x][cell.y];
		deleteComponent(compAtCell); // Delete the filler.
		if (movedComponent == null)
			compAtCell.setComponent(child, requestType); // Just set in new stuff for same index position.
		else {
			// This is a move, so we will instead set into this filler component the moved child. This will be a copy and then
			// the old child will be removed.
			compAtCell.setComponent(child, requestType); // First make it new move component
			compAtCell.setMovedComponent(movedComponent); // Then copy what is needed from old.
			removeChild(movedComponent, false, true); // Need to force removal because if it is a filler, it is now in a new place.
		}
	}

	private void removeGridComponent(GridComponent gc) {
		if (first == gc)
			first = gc.next;
		else
			gc.prev.next = gc.next;
		if (last == gc)
			last = gc.prev;
		else
			gc.next.prev = gc.prev;
	}

	/**
	 * @param cell
	 */
	public boolean insertColWithinRow(Point cell) {
		if (cell.x < 0 || cell.x >= glayoutTable.length || cell.y < 0 || cell.y >= glayoutTable[0].length)
			return false;

		// Create a column at the end if this row doesn't end with a filler or empty. None of the children of any row other than the one with the column being
		// inserted will enter this column. Then the row starting with the insert col will be shifted left one column.
		// If any one spans vertically then we will not allow the move. This is a restriction for now because
		// it becomes very difficult to figure out what needs to move because the vertical span would push
		// other components that would not normally be involved to also be shifted. It could cause shifting
		// both above and below the new row.

		// First test to see if a vertical span is involved. Don't do anything if it is.
		for (int col = cell.x; col < glayoutTable.length; col++) {
			GridComponent gc = glayoutTable[col][cell.y];
			if (gc != EMPTY_GRID && gc.gridDimension.height > 1)
				return false; // We have a vertical span, can't handle these at this time.
		}

		GridComponent lastCol = glayoutTable[numColumns - 1][cell.y];
		if (lastCol != EMPTY_GRID && !last.isFillerLabel())
			createNewCol(numColumns);

		// Now start the needed shifting. Remove the new filler component just added in the new column and move over by one each until
		// we reach the insert column.
		GridComponent newFiller = glayoutTable[numColumns - 1][cell.y];
		if (newFiller != EMPTY_GRID) {
			removeGridComponent(newFiller);
			deleteComponent(newFiller);
		}
		for (int col = glayoutTable.length - 2, toCol = col + 1; col >= cell.x; col--, toCol--) {
			glayoutTable[toCol][cell.y] = glayoutTable[col][cell.y];
		}
		// Now put an empty in the col spot and replace with the new child.
		glayoutTable[cell.x][cell.y] = EMPTY_GRID;
		replaceEmptyCell(createFillerComponent(), cell.x, cell.y);
		return true;
	}

	/**
	 * @param newObject
	 * @param requestType
	 * @param cell
	 */
	public void replaceFillerOrEmpty(XamlNode newObject, Object requestType, Point cell) {
		if (isFillerLabelAtCell(cell))
			replaceFiller(newObject, requestType, cell);
		else if (isEmptyAtCell(cell))
			replaceEmptyCell(newObject, requestType, cell);
	}

	/**
	 * @param x
	 */
	public void createNewCol(int newCol) {
		if (newCol < 0 || newCol > glayoutTable.length)
			return;
		if (glayoutTable.length == 0) {
			glayoutTable = new GridComponent[numColumns = 1][1];
			glayoutTable[0][0] = EMPTY_GRID;
			replaceEmptyCell(createFillerComponent(), 0, 0);
			return;
		}

		GridComponent[][] newLayoutTable = new GridComponent[numColumns = glayoutTable.length + 1][glayoutTable[0].length];

		// We need to create the new table. First copy each column up to but including new col as is since those won't change.
		for (int col = 0; col < newCol; col++) {
			System.arraycopy(glayoutTable[col], 0, newLayoutTable[col], 0, glayoutTable[col].length);
		}

		// First fill new col in with Empty so we don't get errors later accessing null slots.
		Arrays.fill(newLayoutTable[newCol], EMPTY_GRID);

		GridComponent[][] oldLayoutTable = glayoutTable;
		glayoutTable = newLayoutTable; // Now have a valid table up to the prev col. We make it the table so that findNext, etc. will work.
		// Now comes the hard part, we need to fill in the new col, but we have to be careful because of horizontal spans.
		// First move all from old col to the last col over one col and updating their dimensions to their new col.
		// This is needed so that when we create the new row after this we have valid "next valid objects".
		// Note: The reason we are coming from the right is because that way we can tell when we hit the
		// top-left of a a component. If we went from left to right we would keep incrementing the x coor (because
		// we hit it, we increment it, and then on the next col we hit it again and would increment it again).
		for (int col = oldLayoutTable.length - 1, toCol = col + 1; col >= newCol; col--, toCol--) {
			for (int row = 0; row < oldLayoutTable[0].length; row++) {
				GridComponent cellEntry = oldLayoutTable[col][row];
				if (cellEntry == EMPTY_GRID)
					glayoutTable[toCol][row] = EMPTY_GRID;
				else {
					if (cellEntry.gridDimension.x == col && cellEntry.gridDimension.y == row) {
						cellEntry.gridDimension.x = toCol; // It is being moved over one.
					}
					int spanHeight = cellEntry.gridDimension.height;
					// This will increment row for us to so we pick in the for loop with the next row after the span.
					row--;
					while (spanHeight-- > 0) {
						glayoutTable[toCol][++row] = cellEntry;
					}
				}
			}
		}
		int lastRow = oldLayoutTable[0].length - 1;
		if (newCol != oldLayoutTable.length) {
			// Now go through old col, and find any that are horizontal spanned but start before that col, increase their's by one because they now span new col.
			// For those that don't, put in a filler instead. (Note: we could try to get smart and figure out if they should
			// be empties or not, but that gets real complicated. Leave for a later exercise. :-) )
			for (int row = 0; row <= lastRow; row++) {
				GridComponent gc = oldLayoutTable[newCol][row];
				if (gc != EMPTY_GRID && gc.gridDimension.x < newCol) {
					gc.setSpanWidth(gc.gridDimension.width + 1);
					int spanHeight = gc.gridDimension.height;
					// This will increment col for us to so we pick in the for loop with the next col after the span.
					row--;
					while (spanHeight-- > 0) {
						glayoutTable[newCol][++row] = gc;
					}
				} else
					replaceEmptyCell(createFillerComponent(), newCol, row);
			}
		} else {
			// Last col. Just replace with fillers
			for (int row = 0; row < glayoutTable[0].length; row++) {
				replaceEmptyCell(createFillerComponent(), newCol, row);
			}
		}
	}

	/**
	 * @param newRow
	 */
	public void createNewRow(int newRow) {
		if (newRow < 0 || (glayoutTable.length == 0 && newRow > 0) || newRow > glayoutTable[0].length)
			return;

		if (glayoutTable.length == 0) {
			glayoutTable = new GridComponent[numColumns = 1][1];
			glayoutTable[0][0] = EMPTY_GRID;
			replaceEmptyCell(createFillerComponent(), 0, 0);
			return;
		}

		GridComponent[][] newLayoutTable = new GridComponent[glayoutTable.length][glayoutTable[0].length + 1];
		if (newRow > 0) {
			// We need to create the new table. First for each column, copy up to but not including the new row as is since those won't change.
			for (int col = 0; col < glayoutTable.length; col++) {
				System.arraycopy(glayoutTable[col], 0, newLayoutTable[col], 0, newRow);
			}
		}

		// First fill new row in with Empty so we don't get errors later accessing null slots.
		for (int col = 0; col < glayoutTable.length; col++) {
			newLayoutTable[col][newRow] = EMPTY_GRID;
		}

		GridComponent[][] oldLayoutTable = glayoutTable;
		glayoutTable = newLayoutTable; // Now have a valid table up to the prev row. We make it the table so that findNext, etc. will work.
		if (newRow == oldLayoutTable[0].length) {
			// It is the last row, use replace empty on first col to fill it correctly.
			replaceEmptyCell(createFillerComponent(), 0, newRow);
		} else {
			// Now comes the hard part, we need to fill in the new row, but we have to be careful because of vertical spans.
			// First move all from old row to the last row down one row and updating their dimensions to their new row.
			// This is needed so that when we create the new row after this we have valid "next valid objects".
			// Note: The reason we are coming from the bottom up is because that way we can tell when we hit the
			// top-left of a a component. If we went from top down we would keep incrementing the y coor (because
			// we hit it, we increment it, and then on the next row we hit it again and would increment it again).
			for (int row = oldLayoutTable[0].length - 1, toRow = row + 1; row >= newRow; row--, toRow--) {
				for (int col = 0; col < oldLayoutTable.length; col++) {
					GridComponent cellEntry = oldLayoutTable[col][row];
					if (cellEntry == EMPTY_GRID)
						glayoutTable[col][toRow] = EMPTY_GRID;
					else {
						if (cellEntry.gridDimension.y == row && cellEntry.gridDimension.x == col) {
							cellEntry.gridDimension.y = toRow; // It is being moved down one.
						}
						int spanx = cellEntry.gridDimension.width;
						// This will increment col for us to so we pick in the for loop with the next col after the span.
						col--;
						while (spanx-- > 0) {
							glayoutTable[++col][toRow] = cellEntry;
						}
					}
				}
			}
			// Now go through old row, and find any that are vertical spanned but start before that row, increase their's by one because they now span new row.
			// For those that don't, put in a filler instead. (Note: we could try to get smart and figure out if they should
			// be empties or not, but that gets real complicated. Leave for a later exercise. :-) )
			for (int col = 0; col < oldLayoutTable.length; col++) {
				GridComponent gc = oldLayoutTable[col][newRow];
				if (gc != EMPTY_GRID && gc.gridDimension.y < newRow) {
					gc.setSpanHeight(gc.gridDimension.height + 1);
					int spanWidth = gc.gridDimension.width;
					// This will increment col for us to so we pick in the for loop with the next col after the span.
					col--;
					while (spanWidth-- > 0) {
						glayoutTable[++col][newRow] = gc;
					}
				} else
					replaceEmptyCell(createFillerComponent(), col, newRow);
			}
		}
	}

	/**
	 * @param newObject
	 * @param requestType
	 * @param cell
	 */
	public void replaceEmptyCell(XamlNode newObject, Object requestType, Point cell) {
		if (glayoutTable[cell.x][cell.y] != EMPTY_GRID)
			return; // Invalid request.
		replaceEmptyCell(new GridComponent(newObject, requestType), cell.x, cell.y);

	}

	/**
	 * @return
	 */
	public int getNumColumns() {
		if (numColumns == -1) {
			getLayoutTable();
		}
		return numColumns;
	}

	/**
	 * @return
	 */
	public int getNumRows() {
		getLayoutTable();
		return glayoutTable.length != 0 ? glayoutTable[0].length : 0;
	}

	private void handleSpanAtEnd(GridComponent gc, CompoundCommand cb) {
		if (gc.modSpanWidth != NOT_MODIFIED_SPAN || gc.modSpanHeight != NOT_MODIFIED_SPAN) {
			XamlNode gridData = gc.useGriddata == null ? getLayoutData(gc.model) : gc.useGriddata;
			XamlAttribute attribute = gridData == null ? null : gridData.getAttribute("horizontalSpan",
					IConstants.XWT_NAMESPACE);
			switch (gc.modSpanWidth) {
			case SET_TO_DEFAULT_SPAN: {
				if (gridData != null && attribute != null) {
					int defaultHSpan = new GridData().horizontalSpan;
					if (!String.valueOf(defaultHSpan).equals(attribute.getValue())) {
						cb.add(ApplyAttributeSettingCommand.createCommand(gridData, "horizontalSpan",
								IConstants.XWT_NAMESPACE, String.valueOf(defaultHSpan)));
					}
				}
				break;
			}
			case NOT_MODIFIED_SPAN:
				// Do nothing, not modified.
				break;
			default:
				if (gridData != null && gc.modSpanWidth > 0) {
					int defaultHSpan = new GridData().horizontalSpan;
					if ((attribute == null && defaultHSpan != gc.modSpanWidth)
							|| (attribute != null && (!String.valueOf(gc.modSpanWidth).equals(attribute.getValue())))) {
						cb.add(ApplyAttributeSettingCommand.createCommand(gridData, "horizontalSpan",
								IConstants.XWT_NAMESPACE, String.valueOf(gc.modSpanWidth)));
					}
				}
				break;
			}
			switch (gc.modSpanHeight) {
			case SET_TO_DEFAULT_SPAN:
				if (gridData != null && attribute != null) {
					int defaultVSpan = new GridData().verticalSpan;
					if (!String.valueOf(defaultVSpan).equals(attribute.getValue())) {
						cb.add(ApplyAttributeSettingCommand.createCommand(gridData, "verticalSpan",
								IConstants.XWT_NAMESPACE, String.valueOf(defaultVSpan)));
					}
				}
				break;
			case NOT_MODIFIED_SPAN:
				// Do nothing, not modified.
				break;
			default:
				if (gridData != null && gc.modSpanHeight > 0) {
					int defaultHSpan = new GridData().verticalSpan;
					if ((attribute == null && defaultHSpan != gc.modSpanHeight)
							|| (attribute != null && (!String.valueOf(gc.modSpanHeight).equals(attribute.getValue())))) {
						cb.add(ApplyAttributeSettingCommand.createCommand(gridData, "verticalSpan",
								IConstants.XWT_NAMESPACE, String.valueOf(gc.modSpanHeight)));
					}
				}
				break;
			}
		}
	}

	/**
	 * Span the child.
	 * 
	 * @param child
	 * @param newSpan
	 * @param spanDirection
	 * @param griddata
	 *            use <code>null</code> if should use griddata from the child or create one if it doesn't have one. Supply an explicit griddata here in the case of building up from an implicit and we can't fluff one up because there is one being created before this, but not yet applied.
	 * @since 1.2.0
	 */
	public void spanChild(EObject child, Point newSpan, int spanDirection, XamlNode griddata) {
		GridComponent gc = getComponent(child);
		if (gc == null)
			return;
		gc.setGriddata(griddata);

		if (spanDirection == PositionConstants.EAST || spanDirection == PositionConstants.WEST) {
			int newgridDataWidth = newSpan.x;
			if (newgridDataWidth != gc.gridDimension.width) {
				if (newgridDataWidth > gc.gridDimension.width) {
					// Increase the horizontalSpan
					// but first see if we can expand into empty cells without increasing the number of columns
					int numColsIncrement = spanHorizontalIntoEmptyColumns(gc, gc.gridDimension.y, gc.gridDimension.x
							+ gc.gridDimension.width, gc.gridDimension.height, newgridDataWidth
							- gc.gridDimension.width);
					if (numColsIncrement > 0) {
						// We now need to insert at this point this number of columns so that we can span into them.
						int insertColAt = gc.gridDimension.x + gc.gridDimension.width; // Insert just after current end of control.
						while (numColsIncrement-- > 0)
							createNewCol(insertColAt);
						spanHorizontalIntoEmptyColumns(gc, gc.gridDimension.y, gc.gridDimension.x
								+ gc.gridDimension.width, gc.gridDimension.height, newgridDataWidth
								- gc.gridDimension.width); // Now span into these new ones.
					}
				} else {
					// Shrink by one column at a time from the right. Fill with filler and then see if column can go away.
					if (gc.gridDimension.x == 0 && gc.gridDimension.y > 0) {
						// However, there is a problem need to worry about. If this control is the first in the row and the previous
						// row ends in at least the number of the new smaller span empty cells, then we need to fill in those empty
						// with fillers. Otherwise the control will flow back onto the previous row.
						//
						// If we have too many empties, need to fill with filler. We'll do a little optimization here. We will actually
						// leave one less empty than the new span width.
						// If we simply instead fill the last empty in the row, then all of the row will be filled with fillers
						// and this would create extra unneeded fillers.
						//
						// For example say we need to have no more than two empties in the prev row (i.e. span down to two for
						// the control in the next row), and we had the following layout:
						//
						// C C C E E E E
						//
						// The most efficient is to change to:
						//
						// C C C F F F E
						boolean allEmpties = true;
						int prevRow = gc.gridDimension.y - 1;
						for (int numCols = newgridDataWidth, colToTest = glayoutTable.length - 1; numCols > 0; numCols--, colToTest--) {
							if (glayoutTable[colToTest][prevRow] != EMPTY_GRID) {
								allEmpties = false;
								break;
							}
						}
						if (allEmpties) {
							// So replace the first one in the span of empties of the size we needed so that there will no longer
							// be enough room for the next control.
							replaceEmptyCell(createFillerComponent(), glayoutTable.length - newgridDataWidth, prevRow);
						}
					}
					int colToSpanOutOf = gc.gridDimension.x + gc.gridDimension.width - 1;
					while (gc.gridDimension.width > newgridDataWidth) {
						gc.setSpanWidth(gc.gridDimension.width - 1);
						int nextRow = gc.gridDimension.y + gc.gridDimension.height;
						Arrays.fill(glayoutTable[colToSpanOutOf], gc.gridDimension.y, nextRow, EMPTY_GRID);
						for (int row = gc.gridDimension.y; row < nextRow; row++) {
							replaceEmptyCell(createFillerComponent(), colToSpanOutOf, row);
						}
						removeColIfEmpty(colToSpanOutOf);
						colToSpanOutOf--;
					}
				}
			}
		} else if (spanDirection == PositionConstants.SOUTH) {
			int newgridDataHeight = newSpan.y;
			if (newgridDataHeight != gc.gridDimension.height) {
				if (newgridDataHeight > gc.gridDimension.height) {
					// Increase the horizontalSpan
					// but first see if we can expand into empty cells without increasing the number of columns
					int numRowsIncrement = spanVerticalIntoEmptyRows(gc, gc.gridDimension.y + gc.gridDimension.height,
							gc.gridDimension.x, gc.gridDimension.width, newgridDataHeight - gc.gridDimension.height);
					if (numRowsIncrement > 0) {
						// We now need to insert at this point this number of columns so that we can span into them.
						int insertRowAt = gc.gridDimension.y + gc.gridDimension.height; // Insert just after current end of control.
						while (numRowsIncrement-- > 0)
							createNewRow(insertRowAt);
						spanVerticalIntoEmptyRows(gc, gc.gridDimension.y + gc.gridDimension.height, gc.gridDimension.x,
								gc.gridDimension.width, newgridDataHeight - gc.gridDimension.height); // Now span into these new ones.
					}
				} else {
					// Shrink by one row at a time from the bottom. Fill with filler and then see if row can go away.
					int rowToSpanOutOf = gc.gridDimension.y + gc.gridDimension.height - 1;
					while (gc.gridDimension.height > newgridDataHeight) {
						gc.setSpanHeight(gc.gridDimension.height - 1);
						int nextCol = gc.gridDimension.x + gc.gridDimension.width;
						for (int col = gc.gridDimension.x; col < nextCol; col++) {
							glayoutTable[col][rowToSpanOutOf] = EMPTY_GRID;
						}
						for (int col = gc.gridDimension.x; col < nextCol; col++) {
							replaceEmptyCell(createFillerComponent(), col, rowToSpanOutOf);
						}
						removeRowIfEmpty(rowToSpanOutOf);
						rowToSpanOutOf--;
					}
				}
			}
		}
	}

	/*
	 * For spanning horizontally, walk through the row starting atColumn and delete empty or filler labels so we can expand into the empty columns. If no empty cells, numColIncrement is returned so the number of columns can be incremented on the overall grid.
	 */
	private int spanHorizontalIntoEmptyColumns(GridComponent spanGC, int atRow, int atColumn, int childHeight,
			int numColsIncrement) {
		// Span as far as we can with empty vertical columns, move the spanGC into the new columns as we go.
		if (atColumn < glayoutTable.length && atRow < glayoutTable[0].length) {
			for (int col = atColumn; col < glayoutTable.length && numColsIncrement != 0; col++) {
				int toRow = atRow + childHeight - 1;
				if (isHorizontalSpaceAvailable(col, atRow, toRow)) {
					for (int row = atRow; row <= toRow; row++) {
						GridComponent gc = glayoutTable[col][row];
						if (gc.isFillerLabel()) {
							removeGridComponent(gc);
							deleteComponent(gc);
						}
						glayoutTable[col][row] = spanGC;
					}
					spanGC.setSpanWidth(spanGC.gridDimension.width + 1);
					numColsIncrement--;
				} else
					break; // We hit our first non-empty column. Can't span any further.
			}
		}
		return numColsIncrement;
	}

	/*
	 * For spanning vertically, walk through the col starting atRow and delete empty or filler labels so we can expand into the empty rows. If no empty cells, numRowIncrement is returned so the number of rows can be incremented on the overall grid.
	 */
	private int spanVerticalIntoEmptyRows(GridComponent spanGC, int atRow, int atColumn, int childWidth,
			int numRowsIncrement) {
		// Span as far as we can with empty horizontal rows, move the spanGC into the new rows as we go.
		if (atColumn < glayoutTable.length && atRow < glayoutTable[0].length) {
			for (int row = atRow; row < glayoutTable[0].length && numRowsIncrement != 0; row++) {
				int toCol = atColumn + childWidth - 1;
				if (isVerticalSpaceAvailable(row, atColumn, toCol)) {
					for (int col = atColumn; col <= toCol; col++) {
						GridComponent gc = glayoutTable[col][row];
						if (gc.isFillerLabel()) {
							removeGridComponent(gc);
							deleteComponent(gc);
						}
						glayoutTable[col][row] = spanGC;
					}
					spanGC.setSpanHeight(spanGC.gridDimension.height + 1);
					numRowsIncrement--;
				} else
					break; // We hit our first non-empty row. Can't span any further.
			}
		}
		return numRowsIncrement;
	}

	/*
	 * Return true if the cells atRow from columnStart to columnEnd have either an EMPTY object or is a filler label.
	 */
	private boolean isVerticalSpaceAvailable(int atRow, int columnStart, int columnEnd) {
		if (glayoutTable.length == 0 || glayoutTable[0].length == 0 || columnStart >= glayoutTable.length
				|| columnEnd >= glayoutTable.length || atRow >= glayoutTable[0].length)
			return false;
		for (int col = columnStart; col <= columnEnd; col++) {
			if (glayoutTable[col][atRow] != EMPTY_GRID && !glayoutTable[col][atRow].isFillerLabel()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Return true if the cells atCol from rowStart to rowEnd have either an EMPTY object or is a filler label.
	 */
	private boolean isHorizontalSpaceAvailable(int atCol, int rowStart, int rowEnd) {
		if (glayoutTable.length == 0 || glayoutTable[0].length == 0 || rowStart >= glayoutTable[0].length
				|| rowEnd >= glayoutTable[0].length || atCol >= glayoutTable.length)
			return false;
		for (int row = rowStart; row <= rowEnd; row++) {
			if (glayoutTable[atCol][row] != EMPTY_GRID && !glayoutTable[atCol][row].isFillerLabel()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	public Command stopRequest() {

		CompoundCommand cb = new CompoundCommand();

		if (numColumns != originalNumColumns) {
			createNumColumnsCommand(numColumns, cb);
		}

		// There is actually one optimization we can do and that is remove all trailing fillers. They don't
		// add anything. They won't create extra columns and they could create empty rows at the end, but we don't
		// want those anyway.

		GridComponent end = last;
		while (last != null && (last == EMPTY_GRID || last.isFillerLabel())) {
			GridComponent next = last.prev;
			if (last != EMPTY_GRID) {
				removeGridComponent(end);
				deleteComponent(end);
				glayoutTable[end.gridDimension.x][end.gridDimension.y] = EMPTY_GRID;
			}
			end = next;
		}

		Object beforeComp = null;
		Object currentModState = null;
		List<GridComponent> currentComponentGCs = new ArrayList<GridComponent>();
		Object prevComp = beforeComp;
		// We build up from the end instead of from the beginning because we need to have the prevComp in place before we can put something
		// in front of it. If we went from the beginning, something may of been moved to a later spot and it would not be in the correct
		// order in the real list.
		for (GridComponent gc = last; gc != null; gc = gc.prev) {
			if (gc.requestType != NO_MODS) {
				if (!gc.requestType.equals(currentModState)) {
					// We are switching to a new type, send out the old group.
					if (!currentComponentGCs.isEmpty()) {
						getCommandForAddCreateMoveChildren(currentModState, currentComponentGCs, beforeComp, cb);
						currentComponentGCs.clear();
					}
					beforeComp = prevComp; // This new guy will now go before the latest prev component.
					currentModState = gc.requestType;
				}
				currentComponentGCs.add(0, gc); // Since we build up backwards, we insert from the front so that it results in forward.
			} else {
				// Switch to no change, so put what we have.
				if (!currentComponentGCs.isEmpty()) {
					getCommandForAddCreateMoveChildren(currentModState, currentComponentGCs, beforeComp, cb);
					currentComponentGCs.clear();
					currentModState = null;
				}
			}

			handleSpanAtEnd(gc, cb); // Handle if the span had changed.
			prevComp = gc.model;
		}

		// Do last group.
		if (!currentComponentGCs.isEmpty()) {
			getCommandForAddCreateMoveChildren(currentModState, currentComponentGCs, beforeComp, cb);
		}

		if (deletedComponents != null && !deletedComponents.isEmpty()) {
			cb.add(factory.getDeleteDependentCommand(deletedComponents));
		}
		if (orphanedComponents != null && !orphanedComponents.isEmpty()) {
			cb.add(factory.getOrphanChildrenCommand(orphanedComponents));
		}

		refresh();
		return cb;
	}

	private void createNumColumnsCommand(int numCols, CompoundCommand cb) {
		XamlNode parent = (XamlNode) host.getLayoutModel();
		XamlAttribute attribute = parent.getAttribute("numColumns", IConstants.XWT_NAMESPACE);
		int defaultNum = new GridLayout().numColumns;
		if ((attribute == null && numCols != defaultNum)
				|| (attribute != null && !String.valueOf(numCols).equals(attribute.getValue()))) {
			Command cmd = ApplyAttributeSettingCommand.createCommand(parent, "numColumns", IConstants.XWT_NAMESPACE,
					Integer.toString(numCols));
			cb.add(cmd);
		}
	}

	private void getCommandForAddCreateMoveChildren(Object requestType, List<GridComponent> childrenGC,
			Object beforeObject, CompoundCommand cb) {
		List<XamlNode> children = new ArrayList<XamlNode>(childrenGC.size());
		List<XamlNode> constraints = new ArrayList<XamlNode>(childrenGC.size());
		for (int i = 0; i < childrenGC.size(); i++) {
			GridComponent gc = (GridComponent) childrenGC.get(i);
			children.add(gc.model);
			constraints.add(gc.useGriddata);
		}
		// // of it will mess up the policy commands created..
		// // Create the appropriate set of constraints to apply with.
		if (RequestConstants.REQ_CREATE.equals(requestType)) {
			cb.add(factory.getCreateCommand(constraints, children, beforeObject));
		} else if (RequestConstants.REQ_ADD.equals(requestType)) {
			cb.add(factory.getAddCommand(constraints, children, beforeObject));
		} else /* if (RequestConstants.REQ_MOVE_CHILDREN.equals(requestType)) */{
			cb.add(factory.getMoveChildrenCommand(children, (XamlNode) beforeObject));
		}
	}

	public static class GridComponent {

		private XamlNode model;
		private boolean filler;
		Object requestType = NO_MODS;
		int modSpanWidth = NOT_MODIFIED_SPAN;
		int modSpanHeight = NOT_MODIFIED_SPAN;
		Rectangle gridDimension;
		GridComponent prev, next;
		XamlNode useGriddata;

		public GridComponent(XamlNode model) {
			this.model = model;
		}

		public GridComponent(XamlNode model, boolean filler) {
			this(model, RequestConstants.REQ_CREATE);
			this.filler = filler;
		}

		public GridComponent(XamlNode model, Object requestType) {
			this(model);
			this.requestType = requestType;
			setupUseGriddata(model, requestType);
		}

		/**
		 * @return the filter
		 */
		public boolean isFillerLabel() {
			return filler;
		}

		public void setSpanWidth(int spanWidth) {
			gridDimension.width = modSpanWidth = spanWidth;
			if (spanWidth == 1)
				modSpanWidth = SET_TO_DEFAULT_SPAN;
		}

		public void setSpanHeight(int spanHeight) {
			gridDimension.height = modSpanHeight = spanHeight;
			if (spanHeight == 1)
				modSpanHeight = SET_TO_DEFAULT_SPAN;
		}

		/**
		 * Set into this component the moved component passed in. This is used for moved components. It is assumed that this GridComponent is the new GridComponent for the moved component sent in on the request. This "this" cannot be an existing component.
		 * 
		 * @param gc
		 * @since 1.2.0
		 */
		void setMovedComponent(GridComponent gc) {
			// If the moved component was not just added/created, (i.e. it was here before we started), then set to move state
			// so that it will be picked up as needing to be moved in the stopRequest. Else leave as add/create/move so that
			// it will be processed correctly for that appropriate state. We should not change an add/create to a move. It won't
			// work correctly in the container policy.
			requestType = NO_MODS.equals(gc.requestType) ? RequestConstants.REQ_MOVE : gc.requestType;
			modSpanWidth = modSpanHeight = SET_TO_DEFAULT_SPAN; // Also cancel the span for the moved child.
			filler = gc.filler;
			useGriddata = gc.useGriddata;
		}

		public void setComponent(XamlNode model, Object requestType) {
			this.model = model;
			this.requestType = requestType;
			modSpanWidth = modSpanHeight = NOT_MODIFIED_SPAN;
			filler = false;
			setupUseGriddata(model, requestType);
		}

		private void setupUseGriddata(XamlNode model, Object requestType) {
			useGriddata = null;
			if (RequestConstants.REQ_CREATE.equals(requestType) || RequestConstants.REQ_ADD.equals(requestType)) {
				XamlAttribute attr = model.getAttribute("layoutData", IConstants.XWT_NAMESPACE);
				if (attr != null && !attr.getChildNodes().isEmpty()) {
					useGriddata = attr.getChildNodes().get(0);
					modSpanHeight = modSpanWidth = SET_TO_DEFAULT_SPAN; // Need to cancel out any current settings
				}
			}
		}

		public void setGriddata(XamlNode griddata) {
			if (useGriddata == null)
				useGriddata = griddata;
		}

		public XamlNode getModel() {
			return model;
		}

		public int getSpanWidth() {
			return gridDimension.width;
		}

		public int getSpanHeight() {
			return gridDimension.height;
		}
	}

	/**
	 * @return
	 */
	public boolean isRightToLeft() {
		return getEditPart().isRightToLeft();
	}
}
