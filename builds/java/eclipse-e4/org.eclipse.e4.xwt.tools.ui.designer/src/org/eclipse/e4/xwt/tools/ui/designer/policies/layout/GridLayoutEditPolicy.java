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
package org.eclipse.e4.xwt.tools.ui.designer.policies.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.commands.GridLayoutCommandsFactory;
import org.eclipse.e4.xwt.tools.ui.designer.commands.NoOpCommand;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.CreateReqHelper;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.EntryHelper;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.MenuBarEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewNonResizeEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewResizableEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackHelper;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackManager;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridController;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutAddedCellFeedbackFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutColumnFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutFeedbackFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutGridFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutPolicyHelper;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutRequest;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutRowFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutSpanFeedbackFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridSpanHandle;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.IGridListener;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ForwardedRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.widgets.Display;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class GridLayoutEditPolicy extends LayoutEditPolicy implements
		IGridListener, ILayoutEditPolicy {

	public static final String REQ_GRIDLAYOUT_SPAN = "GridLayout span cells"; //$NON-NLS-1$

	public static final int DEFAULT_CELL_WIDTH = 40;
	public static final int DEFAULT_CELL_HEIGHT = 35;
	private static final int ADDEDCELLBORDER = 3; // How much to expand cell for
													// added cell to draw the
													// new border.
	private GridController gridController;
	private boolean fShowGrid = false;
	private GridLayoutGridFigure fGridLayoutGridFigure;
	private GridLayoutSpanFeedbackFigure fGridLayoutSpanFigure;
	private GridLayoutFeedbackFigure fGridLayoutCellFigure;
	private IFigure fRowColFigure = null;
	private EditPartListener editPartListener;
	private GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper();

	private FeedbackManager fbm = new FeedbackManager(this);

	protected FigureListener hostFigureListener = new FigureListener() {
		public void figureMoved(IFigure source) {
			refresh();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#activate()
	 */
	public void activate() {
		helper.setHost((CompositeEditPart) getHost());
		gridController = new GridController();
		GridController.registerEditPart(getHost(), gridController);
		gridController.addGridListener(this);
		getHostFigure().addFigureListener(hostFigureListener);
		if (getHost().getSelected() == EditPart.SELECTED
				|| getHost().getSelected() == EditPart.SELECTED_PRIMARY) {
			gridController.setGridShowing(true);
		}
		editPartListener = createEditPartListener();
		getHost().addEditPartListener(editPartListener);
		List children = getHost().getChildren();
		Iterator iterator = children.iterator();
		while (iterator.hasNext())
			((EditPart) iterator.next()).addEditPartListener(editPartListener);
		super.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(org
	 * .eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof MenuBarEditPart) {
			return new NewNonResizeEditPolicy(false);
		}
		// return new NonResizableSpannableEditPolicy(this);
		return new NewResizableEditPolicy(PositionConstants.SOUTH
				| PositionConstants.EAST, true);
	}

	private EditPartListener createEditPartListener() {
		return new EditPartListener.Stub() {
			public void childAdded(EditPart editpart, int index) {
				if (editPartListener != null)
					editpart.addEditPartListener(editPartListener);
				helper.refresh();
				refreshGridFigure();
			}

			public void removingChild(EditPart editpart, int index) {
				if (editPartListener != null)
					editpart.removeEditPartListener(editPartListener);
				helper.refresh();
				refreshGridFigure();
			}

			public void selectedStateChanged(EditPart editpart) {
				if ((editpart == null)
						|| (editpart == getHost())
						|| (isChildEditPart(editpart))
						&& (editpart.getSelected() == EditPart.SELECTED || editpart
								.getSelected() == EditPart.SELECTED_PRIMARY)) {
					if (gridController != null) {
						gridController.setGridShowing(true);
					} else {
						if (gridController != null)
							gridController.setGridShowing(false);
					}
				} else {
					// Hide the grid just in case we were show before and
					// changed the prefs
					if (gridController != null
							&& gridController.isGridShowing())
						gridController.setGridShowing(false);
				}
			}
		};
	}

	private GridLayoutRequest createGridLayoutRequest(Point position) {
		return getGridLayoutGridFigure().getGridLayoutRequest(position, helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#deactivate()
	 */
	public void deactivate() {

		GridController gridController = GridController
				.getGridController(getHost());
		eraseGridFigure();
		if (gridController != null) {
			gridController.removeGridListener(this);
			GridController.unregisterEditPart(getHost());
		}

		getHostFigure().removeFigureListener(hostFigureListener);
		if (editPartListener != null) {
			getHost().removeEditPartListener(editPartListener);
			List children = getHost().getChildren();
			Iterator iterator = children.iterator();
			while (iterator.hasNext())
				((EditPart) iterator.next())
						.removeEditPartListener(editPartListener);
			editPartListener = null;
		}
		super.deactivate();
	}

	/**
	 * erase grid figure.
	 */
	private void eraseGridFigure() {
		if (fGridLayoutGridFigure != null) {
			if (fGridLayoutGridFigure.getParent() != null)
				removeFeedback(fGridLayoutGridFigure);
			fGridLayoutGridFigure = null;
		}
		fShowGrid = false;
	}

	public void eraseTargetFeedback(Request request) {
		fbm.eraseFeedback(request);
		if (!fShowGrid)
			if (fGridLayoutGridFigure != null) {
				if (fGridLayoutGridFigure.getParent() != null)
					removeFeedback(fGridLayoutGridFigure);
				fGridLayoutGridFigure = null;
			}
		if (fGridLayoutSpanFigure != null) {
			removeFeedback(fGridLayoutSpanFigure);
			fGridLayoutSpanFigure = null;
		}
		if (fGridLayoutCellFigure != null) {
			removeFeedback(fGridLayoutCellFigure);
			fGridLayoutCellFigure = null;
		}
		if (fRowColFigure != null) {
			removeFeedback(fRowColFigure);
			fRowColFigure = null;
		}
		super.eraseTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getCommand(org.eclipse.
	 * gef.Request)
	 */
	public Command getCommand(Request request) {
		// if (REQ_GRIDLAYOUT_SPAN.equals(request.getType())) {
		// return getSpanChildrenCommand(request);
		// }
		if (REQ_RESIZE_CHILDREN.equals(request.getType())) {
			return getResizeCommand(request);
		}
		return super.getCommand(request);
	}

	private Command getResizeCommand(Request request) {
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		CompoundCommand resize = new CompoundCommand();
		Command c;
		GraphicalEditPart child;
		List children = req.getEditParts();

		for (int i = 0; i < children.size(); i++) {
			child = (GraphicalEditPart) children.get(i);
			c = createResizeChildrenCommand(request, child, req.getSizeDelta());
			resize.add(c);
		}
		return resize.unwrap();
	}

	protected Command createResizeChildrenCommand(Request request,
			EditPart child, Object newSize) {
		GridLayoutCommandsFactory factory = new GridLayoutCommandsFactory(
				(CompositeEditPart) getHost());
		return factory.getResizeChildrenCommand(child,
				(ChangeBoundsRequest) request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getAddCommand(org.eclipse
	 * .gef.Request)
	 */
	protected Command getAddCommand(Request request) {
		return getMoveChildrenCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse
	 * .gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		CreateReqHelper reqHelper = new CreateReqHelper(request);
		if (!reqHelper.canCreate(getHost())) {
			return null;
		}
		if (fGridLayoutGridFigure == null) {
			// TODO: Maybe create layout here.
			return UnexecutableCommand.INSTANCE;
		}

		Point position = getLocationFromRequest(request).getCopy();
		GridLayoutRequest gridReq = createGridLayoutRequest(position);
		Point cell = new Point(gridReq.column, gridReq.row);

		Entry entry = (Entry) request.getNewObject();
		XamlNode newObject = EntryHelper.getNode(entry);
		if (newObject == null) {
			return UnexecutableCommand.INSTANCE;
		}
		Object requestType = request.getType();
		helper.startRequest();
		switch (gridReq.type) {
		case GridLayoutRequest.REPLACE_FILLER:
			helper.replaceFiller(newObject, requestType, cell);
			break;
		case GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW:
			helper.insertColWithinRow(cell);
			helper.replaceFillerOrEmpty(newObject, requestType, cell);
			break;
		case GridLayoutRequest.INSERT_COLUMN:
		case GridLayoutRequest.ADD_COLUMN:
			helper.createNewCol(cell.x);
			helper.replaceFillerOrEmpty(newObject, requestType, cell);
			break;
		case GridLayoutRequest.INSERT_ROW:
		case GridLayoutRequest.ADD_ROW:
			helper.createNewRow(cell.y);
			helper.replaceFillerOrEmpty(newObject, requestType, cell);
			break;
		case GridLayoutRequest.ADD_TO_EMPTY_CELL:
			helper.replaceEmptyCell(newObject, requestType, cell);
			break;
		case GridLayoutRequest.ADD_ROW_COL:
			cell.setLocation(helper.getNumColumns(), helper.getNumRows());
			helper.createNewCol(cell.x);
			if (cell.x != 0 || cell.y != 0)
				helper.createNewRow(cell.y); // If other than (0,0) for add row
												// col, we need a new row. If it
												// was (0,0) then we are adding
												// the first entry to the grid.
			helper.replaceFillerOrEmpty(newObject, requestType, cell);
			break;
		case GridLayoutRequest.NO_ADD:
			return UnexecutableCommand.INSTANCE;
		}

		Command result = helper.stopRequest();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand
	 * (org.eclipse.gef.Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		// Get the commands to insert filler labels into the cells where the
		// control used to be
		if (request instanceof ForwardedRequest) {
			EditPart editPart = ((ForwardedRequest) request).getSender();
			if (editPart instanceof CompositeEditPart) {
				helper.setHost((CompositeEditPart) editPart);
				helper.startRequest();
				helper.deleteChild((XamlNode) editPart.getModel());
				return helper.stopRequest();
			}
		}
		return UnexecutableCommand.INSTANCE;
	}

	protected Command getOrphanChildrenCommand(Request request) {
		// Get the commands to insert filler labels into the cells where the
		// control used to be
		if (request instanceof GroupRequest) {
			helper.startRequest();
			List children = getChildren((GroupRequest) request);
			helper.orphanChildren(children);
			return helper.stopRequest();
		} else
			return UnexecutableCommand.INSTANCE;
	}

	public List getChildren(GroupRequest request) {
		List cEP = request.getEditParts();
		List children = new ArrayList(cEP.size());
		Iterator itr = cEP.iterator();
		while (itr.hasNext()) {
			Object child = ((EditPart) itr.next()).getModel();
			children.add(child);
		}
		return children;
	}

	public Rectangle getFullCellBounds(EditPart child) {
		if (getGridLayoutGridFigure() == null)
			return new Rectangle();
		Rectangle dims = helper.getChildDimensions((EObject) child.getModel());
		Rectangle bounds;
		if (dims != null) {
			bounds = getGridLayoutGridFigure()
					.getGridBroundsForCellBounds(dims);
		} else
			bounds = new Rectangle();

		return bounds;
	}

	protected GridLayoutGridFigure getGridLayoutGridFigure() {
		if (fGridLayoutGridFigure == null) {
			fGridLayoutGridFigure = new GridLayoutGridFigure(helper);
		}
		return fGridLayoutGridFigure;
	}

	/**
	 * @return the helper
	 */
	public GridLayoutPolicyHelper getHelper() {
		return helper;
	}

	/*
	 * this gets the location from the request and then makes it absolute
	 * (relative to the bounds of the host figure). If we didn't do this the
	 * point is relative to the viewport that is displayed, not absolute wrt to
	 * the entire canvas.
	 */
	private Point getLocationFromRequest(Request request) {
		Point loc = ((DropRequest) request).getLocation().getCopy();
		getHostFigure().translateToRelative(loc);
		return loc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getMoveChildrenCommand(
	 * org.eclipse.gef.Request)
	 */
	protected Command getMoveChildrenCommand(Request request) {
		if (fGridLayoutGridFigure == null
				|| !(request instanceof ChangeBoundsRequest))
			return UnexecutableCommand.INSTANCE;
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		List editparts = req.getEditParts();
		// Only allow one object to be moved
		if (editparts.size() > 1)
			return UnexecutableCommand.INSTANCE;

		XamlNode trueEObject = (XamlNode) ((EditPart) editparts.get(0))
				.getModel();
		if (trueEObject == null) {
			return UnexecutableCommand.INSTANCE;
		}

		Point position = getLocationFromRequest(request).getCopy();
		GridLayoutRequest gridReq = createGridLayoutRequest(position);
		Point cell = new Point(gridReq.column, gridReq.row);

		Object requestType = request.getType();

		helper.startRequest();
		switch (gridReq.type) {
		case GridLayoutRequest.REPLACE_FILLER:
			helper.replaceFiller(trueEObject, requestType, cell);
			break;
		case GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW:
			helper.insertColWithinRow(cell);
			helper.replaceFillerOrEmpty(trueEObject, requestType, cell);
			break;
		case GridLayoutRequest.INSERT_COLUMN:
		case GridLayoutRequest.ADD_COLUMN:
			helper.createNewCol(cell.x);
			helper.replaceFillerOrEmpty(trueEObject, requestType, cell);
			break;
		case GridLayoutRequest.INSERT_ROW:
		case GridLayoutRequest.ADD_ROW:
			helper.createNewRow(cell.y);
			helper.replaceFillerOrEmpty(trueEObject, requestType, cell);
			break;
		case GridLayoutRequest.ADD_TO_EMPTY_CELL:
			helper.replaceEmptyCell(trueEObject, requestType, cell);
			break;
		case GridLayoutRequest.ADD_ROW_COL:
			cell.setLocation(helper.getNumColumns(), helper.getNumRows());
			helper.createNewCol(cell.x);
			if (cell.x != 0 || cell.y != 0)
				helper.createNewRow(cell.y); // If other than (0,0) for add row
												// col, we need a new row. If it
												// was (0,0) then we are adding
												// the first entry to the grid.
			helper.replaceFillerOrEmpty(trueEObject, requestType, cell);
			break;
		case GridLayoutRequest.NO_ADD:
			return UnexecutableCommand.INSTANCE;
		}

		Command command = helper.stopRequest();
		return command;
	}

	protected Command getSpanChildrenCommand(Request generic) {
		ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
		List editParts = request.getEditParts();
		if (editParts.isEmpty() || editParts.size() > 1)
			return UnexecutableCommand.INSTANCE;

		// Get the cell location that the mouse was dragged to
		Point spanToPosition = getLocationFromRequest(request).getCopy();
		Point spanToCellLocation = getGridLayoutGridFigure().getCellLocation(
				spanToPosition.x, spanToPosition.y);
		// Get the cell location where we started the drag operation
		Dimension dim = request.getSizeDelta();
		int handleSizeOffset = GridSpanHandle.HANDLE_SIZE / 2;
		Point startPosition = new Point(spanToPosition.x - dim.width
				- handleSizeOffset, spanToPosition.y - dim.height
				- handleSizeOffset);
		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart) editParts.get(0);
		EObject child = (EObject) ep.getModel();
		Point childCellLocation = helper.getChildDimensions(child)
				.getLocation();
		Point startCellLocation = getGridLayoutGridFigure().getCellLocation(
				startPosition);
		// If the cell location where the pointer is located is different from
		// the original cell location where we started,
		// create the commands to change the gridwidth or gridheight
		if ((spanToCellLocation.x >= childCellLocation.x && spanToCellLocation.y >= childCellLocation.y)
				&& (spanToCellLocation.x != startCellLocation.x || spanToCellLocation.y != startCellLocation.y)) {
			// Let the helper get the gridWidth or gridHeight commands based on
			// the cell location
			// where the pointer is and the span direction (EAST for gridwidth
			// or SOUTH for gridheight)
			helper.startRequest();
			helper.spanChild(child, new Point(spanToCellLocation.x
					- childCellLocation.x + 1, spanToCellLocation.y
					- childCellLocation.y + 1), request.getResizeDirection(),
					null);
			return helper.stopRequest();
		}
		return NoOpCommand.INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.soyatec.xaml.ve.xwt.editpolicies.gridlayout.IGridListener#
	 * gridHeightChanged(int, int)
	 */
	public void gridHeightChanged(int gridHeight, int oldGridHeight) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.soyatec.xaml.ve.xwt.editpolicies.gridlayout.IGridListener#
	 * gridMarginChanged(int, int)
	 */
	public void gridMarginChanged(int gridMargin, int oldGridMargin) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.soyatec.xaml.ve.xwt.editpolicies.gridlayout.IGridListener#
	 * gridVisibilityChanged(boolean)
	 */
	public void gridVisibilityChanged(boolean showGrid) {
		if (showGrid) {
			showGridFigure();
		} else {
			eraseGridFigure();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.soyatec.xaml.ve.xwt.editpolicies.gridlayout.IGridListener#
	 * gridWidthChanged(int, int)
	 */
	public void gridWidthChanged(int gridWidth, int oldGridWidth) {
		// do nothing
	}

	/*
	 * Return true if ep is a child editpart of the host container
	 */
	private boolean isChildEditPart(EditPart ep) {
		if (ep != null) {
			List children = getHost().getChildren();
			if (!children.isEmpty())
				return (children.indexOf(ep) != -1);
		}
		return false;
	}

	/**
	 * 
	 */
	protected void refreshGridFigure() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (fShowGrid) {
					eraseGridFigure();
					showGridFigure();
				} else {
					fGridLayoutGridFigure = null;
				}
			}
		});
	}

	/**
	 * Show the adding cell outside figure feedback.
	 * 
	 * @param cellBounds
	 * 
	 * @since 1.2.0
	 */
	protected void showAddedCellFeedback(Rectangle cellBounds) {
		cellBounds = cellBounds.getExpanded(cellBounds.width < 10 ? 20 : 0,
				cellBounds.height < 10 ? 20 : 0);
		fRowColFigure = new GridLayoutAddedCellFeedbackFigure();
		fRowColFigure.setBounds(cellBounds);
		addFeedback(fRowColFigure);
	}

	/**
	 * Show a new yellow column inserted into the grid near the column closest
	 * to position and only within that row
	 */
	protected void showColumnFeedBackWithinARow(Rectangle cellBounds) {
		cellBounds = cellBounds.getCopy();
		cellBounds.x -= 3; // start to the right by three from side of cell.
		cellBounds.width = 6; // But only six wide. So it will be centered over
								// the right side of the cell.
		fRowColFigure = new GridLayoutColumnFigure(cellBounds);
		addFeedback(fRowColFigure);
	}

	/**
	 * Show grid figure.
	 */
	private void showGridFigure() {
		if (!fShowGrid) {
			fShowGrid = true;
			addFeedback(getGridLayoutGridFigure());
		}
		fShowGrid = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#showTargetFeedback(org.
	 * eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		if (!(REQ_CREATE.equals(request.getType())
				&& request instanceof CreateRequest && FeedbackHelper
				.showCreationFeedback(fbm, (CreateRequest) request))) {
			super.showTargetFeedback(request);
		}
	}

	/**
	 * Shows an insertion line if there is one or more current children.
	 */
	protected void showLayoutTargetFeedback(Request request) {
		if (!(REQ_CREATE.equals(request.getType()) && FeedbackHelper
				.showCreationFeedback(fbm, (CreateRequest) request))) {
			super.showLayoutTargetFeedback(request);
		}
		if (!fShowGrid)
			addFeedback(getGridLayoutGridFigure());

		if (fRowColFigure != null) {
			removeFeedback(fRowColFigure);
			fRowColFigure = null;
		}

		if (fGridLayoutCellFigure != null) {
			removeFeedback(fGridLayoutCellFigure);
			fGridLayoutCellFigure = null;
		}

		Point position = getLocationFromRequest(request).getCopy();
		GridLayoutRequest gridReq = createGridLayoutRequest(position);

		Point cell = new Point(gridReq.column, gridReq.row);
		Rectangle cellBounds = getGridLayoutGridFigure().getCellBounds(cell);

		// Calculate the bounds of the target cell figure based on whether a
		// column is added,
		// row is added, or it's inserted before another control.
		switch (gridReq.type) {
		case GridLayoutRequest.INSERT_COLUMN:
			// If a column is added, show the target figure in between the two
			// columns
			showNewColumnFeedBack(gridReq.column);
			Rectangle colFigBounds = fRowColFigure.getBounds();
			// mapModelToFigure(cellBounds);
			cellBounds.width = DEFAULT_CELL_WIDTH;
			if (cellBounds.height < 10)
				cellBounds.expand(0, 20);
			cellBounds.x = colFigBounds.x + colFigBounds.width / 2
					- cellBounds.width / 2;
			break;
		case GridLayoutRequest.INSERT_ROW:
			// If a row is added, show the target figure in between the two rows
			showNewRowFeedBack(gridReq.row);
			Rectangle rowFigBounds = fRowColFigure.getBounds();
			// mapModelToFigure(cellBounds);
			cellBounds.height = DEFAULT_CELL_HEIGHT;
			if (cellBounds.width < 10)
				cellBounds.expand(20, 0);
			cellBounds.y = rowFigBounds.y + rowFigBounds.height / 2
					- cellBounds.height / 2;
			break;
		case GridLayoutRequest.INSERT_COLUMN_WITHIN_ROW:
			// In case cell is spanned vertically we need to have the complete
			// cellbounds.
			showColumnFeedBackWithinARow(cellBounds);
			// mapModelToFigure(cellBounds);
			colFigBounds = fRowColFigure.getBounds();
			cellBounds.width = DEFAULT_CELL_WIDTH;
			if (cellBounds.height < 10)
				cellBounds.expand(0, 20);
			cellBounds.x = colFigBounds.x + colFigBounds.width / 2
					- cellBounds.width / 2;
			break;
		case GridLayoutRequest.ADD_COLUMN:
		case GridLayoutRequest.ADD_ROW:
		case GridLayoutRequest.ADD_ROW_COL:
			showAddedCellFeedback(cellBounds);
			colFigBounds = fRowColFigure.getBounds();
			// Center the cell within these bounds.
			cellBounds = colFigBounds.getCopy().shrink(ADDEDCELLBORDER,
					ADDEDCELLBORDER);
			break;
		case GridLayoutRequest.NO_ADD:
			return; // No feedback.
		default:
			// mapModelToFigure(cellBounds.expand(cellBounds.width < 10 ? 20 :
			// 0, cellBounds.height < 10 ? 20 : 0));
			break;
		}

		if (fGridLayoutCellFigure == null) {
			fGridLayoutCellFigure = new GridLayoutFeedbackFigure();
		}
		fGridLayoutCellFigure.setBounds(cellBounds);
		addFeedback(fGridLayoutCellFigure);
	}

	/**
	 * Show a new yellow column inserted into the grid near the column closest
	 * to position
	 */
	protected void showNewColumnFeedBack(int col) {
		Rectangle rect = fGridLayoutGridFigure.getColumnRectangle(col);
		rect.x -= 3;
		rect.width = 6;
		fRowColFigure = new GridLayoutColumnFigure(rect);
		addFeedback(fRowColFigure);
	}

	/**
	 * Show a new yellow row inserted into the grid near the row closest to
	 * position
	 */
	protected void showNewRowFeedBack(int row) {
		Rectangle rect = fGridLayoutGridFigure.getRowRectangle(row);
		rect.translate(-2, -3);
		rect.width += 4;
		rect.height = 6;
		fRowColFigure = new GridLayoutRowFigure(rect);
		addFeedback(fRowColFigure);
	}

	/*
	 * Show target feedback when dragging the span handles of a component.
	 * Highlight the cells the component will occupy based on the begining cell
	 * position and end cell position of the pointer.
	 */
	public void showSpanTargetFeedback(ChangeBoundsRequest request) {
		// If the grid is not on, turn it on
		if (!fShowGrid)
			addFeedback(getGridLayoutGridFigure());

		Point spanToPosition = request.getLocation().getCopy();

		// Get the cell location of the child component
		GraphicalEditPart ep = (GraphicalEditPart) request.getEditParts()
				.get(0);
		EObject child = (EObject) ep.getModel();
		Rectangle childDim = getHelper().getChildDimensions(child);

		// Get the start and end cell bounds in order to determine the entire
		// bounds of the cell feedback figure.
		Rectangle startCellBounds = getGridLayoutGridFigure().getCellBounds(
				childDim.getLocation());
		Rectangle endCellChildBounds = getGridLayoutGridFigure().getCellBounds(
				childDim.getBottomRight().translate(-1, -1)); // This is the
																// lower right
																// of the child
																// itself.
		if (request.getResizeDirection() == PositionConstants.EAST
				|| request.getResizeDirection() == PositionConstants.WEST) {
			spanToPosition.y = endCellChildBounds.y; // This forces us to not
														// span north/south when
														// going east/west. And
														// it will make it tall
														// enough that entire
														// cell spanned height
														// is covered.
		} else {
			spanToPosition.x = endCellChildBounds.x + endCellChildBounds.width
					- 1; // This forces us to not span left/right when going
							// north/south. And it will make it wide enough that
							// entire cell spanned width is covered.
		}
		Rectangle endCellBounds = getGridLayoutGridFigure().getCellBounds(
				getGridLayoutGridFigure().getCellLocation(spanToPosition));
		if (endCellBounds == null || endCellBounds.x < startCellBounds.x
				|| endCellBounds.y < startCellBounds.y) {
			// End is not within a cell, or the end is before the start cell.
			if (fGridLayoutSpanFigure != null) {
				removeFeedback(fGridLayoutSpanFigure);
				fGridLayoutSpanFigure = null;
			}
			return;
		}

		Rectangle spanrect = startCellBounds.union(endCellBounds)
				.resize(-1, -1);
		if (fGridLayoutSpanFigure == null) {
			fGridLayoutSpanFigure = new GridLayoutSpanFeedbackFigure(request
					.getResizeDirection());
		}
		fGridLayoutSpanFigure.setLayoutFigureBounds(spanrect);
		addFeedback(fGridLayoutSpanFigure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy
	 * #refresh()
	 */
	public void refresh() {
		helper.refresh();
		refreshGridFigure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy
	 * #getType()
	 */
	public LayoutType getType() {
		return LayoutType.GridLayout;
	}

}
