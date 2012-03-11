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
package org.eclipse.e4.xwt.tools.ui.designer.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.utils.OffsetUtil;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class NewResizableEditPolicy extends ResizableEditPolicy {
	protected static final int WIDTH = 50;
	protected static final int HEIGHT = 16;

	private boolean diaplayNonHandles = true;
	private Label tooltip;

	public NewResizableEditPolicy(int directions, boolean displayNonHandles) {
		setResizeDirections(directions);
		this.diaplayNonHandles = displayNonHandles;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List<Handle> createSelectionHandles() {
		List<Handle> list = new ArrayList<Handle>();
		int directions = getResizeDirections();
		if (directions == 0 && diaplayNonHandles)
			NonResizableHandleKit.addHandles((GraphicalEditPart) getHost(), list);
		else if (directions != -1) {
			ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), list);
			if ((directions & PositionConstants.EAST) != 0)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.EAST);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.EAST);
			if ((directions & PositionConstants.SOUTH_EAST) == PositionConstants.SOUTH_EAST)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_EAST);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_EAST);
			if ((directions & PositionConstants.SOUTH) != 0)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH);
			if ((directions & PositionConstants.SOUTH_WEST) == PositionConstants.SOUTH_WEST)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_WEST);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_WEST);
			if ((directions & PositionConstants.WEST) != 0)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.WEST);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.WEST);
			if ((directions & PositionConstants.NORTH_WEST) == PositionConstants.NORTH_WEST)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_WEST);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_WEST);
			if ((directions & PositionConstants.NORTH) != 0)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH);
			if ((directions & PositionConstants.NORTH_EAST) == PositionConstants.NORTH_EAST)
				ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_EAST);
			else if (diaplayNonHandles)
				NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_EAST);
		}
		return list;
	}

	protected Label getToolTip() {
		if (tooltip == null) {
			tooltip = new Label();
		}
		if (tooltip.getParent() == null) {
			addFeedback(tooltip);
		}
		return tooltip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		IFigure feedback = getDragSourceFeedbackFigure();

		Rectangle init = getInitialFeedbackBounds().getCopy();
		PrecisionRectangle rect = new PrecisionRectangle(init);
		getHostFigure().translateToAbsolute(rect);
		Point remove = request.getMoveDelta();
		rect.translate(remove);
		Dimension resize = request.getSizeDelta();
		rect.resize(resize);

		feedback.translateToRelative(rect);
		feedback.setBounds(rect);

		Point p = rect.getRight();
		int directions = request.getResizeDirection();
		if (directions != -1) {
			if ((directions & PositionConstants.SOUTH_EAST) == PositionConstants.SOUTH_EAST) {
				p = rect.getLocation().getTranslated(rect.width, rect.height);
			} else if ((directions & PositionConstants.SOUTH_WEST) == PositionConstants.SOUTH_WEST) {
				p = rect.getLocation().getTranslated(-WIDTH, rect.height);
			} else if ((directions & PositionConstants.NORTH_WEST) == PositionConstants.NORTH_WEST) {
				p = rect.getLocation().getTranslated(-WIDTH, -HEIGHT);
			} else if ((directions & PositionConstants.NORTH_EAST) == PositionConstants.NORTH_EAST) {
				p = rect.getLocation().getTranslated(rect.width, -HEIGHT);
			} else if ((directions & PositionConstants.SOUTH) != 0) {
				p = rect.getLocation().getTranslated((rect.width - WIDTH) / 2, rect.height);
			} else if ((directions & PositionConstants.EAST) != 0) {
				p = rect.getLocation().getTranslated(rect.width, rect.height / 2);
			} else if ((directions & PositionConstants.WEST) != 0) {
				p = rect.getLocation().getTranslated(-WIDTH, rect.height / 2);
			} else if ((directions & PositionConstants.NORTH) != 0) {
				p = rect.getLocation().getTranslated((rect.width - WIDTH) / 2, -HEIGHT);
			}
		}

		Dimension size = new Dimension(WIDTH, HEIGHT);
		Label tip = getToolTip();
		// tip.setOpaque(true);
		// tip.setBackgroundColor(ColorConstants.red);
		tip.setBounds(new Rectangle(p, size));
		if (resize != null && (resize.width != 0 || resize.height != 0)) {
			tip.setForegroundColor(ColorConstants.black);
			tip.setText((init.width + resize.width) + "," + (init.height + resize.height));
		} else if (remove != null && (remove.x != 0 || remove.y != 0)) {
			tip.setForegroundColor(ColorConstants.blue);
			Point moveTo = init.getLocation();
			getHostFigure().translateToAbsolute(moveTo);
			moveTo = moveTo.getTranslated(remove);
			getHostFigure().translateToRelative(moveTo);
			p = ((GraphicalEditPart) getHost().getParent()).getContentPane().getClientArea().getLocation().getNegated();
			moveTo.translate(p);
			int xOffset = OffsetUtil.getXOffset(getHost().getParent());
			int yOffset = OffsetUtil.getYOffset(getHost().getParent());
			moveTo.translate(-xOffset, -yOffset);
			tip.setText((moveTo.x) + "," + (moveTo.y));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#eraseChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.eraseChangeBoundsFeedback(request);
		if (tooltip != null && tooltip.getParent() != null) {
			removeFeedback(tooltip);
		}
	}
}
