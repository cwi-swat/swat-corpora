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

/*
 * $RCSfile: NonResizableSpannableEditPolicy.java,v $ $Revision: 1.3 $ $Date: 2010/06/18 00:15:49 $
 */

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridSpanHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * EditPolicy for non-resizable handles, but span handles attached.
 * 
 * @since 1.1.0
 */
public class NonResizableSpannableEditPolicy extends NonResizableEditPolicy {

	protected GridLayoutEditPolicy layoutEditPolicy;

	/**
	 * 
	 */
	public NonResizableSpannableEditPolicy(GridLayoutEditPolicy layoutEditPolicy) {
		super();
		this.layoutEditPolicy = layoutEditPolicy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#
	 * createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		List nonResizeHandles = super.createSelectionHandles();
		if (getHost().getSelected() == EditPart.SELECTED_PRIMARY) {
			// Can't hide the Right to left under model coor. here. We need to
			// know if RTL so that we
			// use WEST instead of EAST.
			if (!layoutEditPolicy.getHelper().isRightToLeft())
				nonResizeHandles.add(createHandle(
						(GraphicalEditPart) getHost(), PositionConstants.EAST));
			else
				nonResizeHandles.add(createHandle(
						(GraphicalEditPart) getHost(), PositionConstants.WEST));
			nonResizeHandles.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.SOUTH));
		}
		return nonResizeHandles;
	}

	private Handle createHandle(GraphicalEditPart owner, int direction) {
		GridSpanHandle handle = new GridSpanHandle(owner, direction,
				layoutEditPolicy);
		handle.setDragTracker(new ResizeTracker(owner, direction));
		return handle;
	}

	public Command getCommand(Request request) {
		if (REQ_RESIZE.equals(request.getType())
				&& request instanceof ChangeBoundsRequest) {
			return getSpanCommand((ChangeBoundsRequest) request);
		}
		return super.getCommand(request);
	}

	private Command getSpanCommand(ChangeBoundsRequest request) {
		List editParts = request.getEditParts();
		if (editParts.isEmpty() || editParts.size() > 1) {
			return UnexecutableCommand.INSTANCE;
		}
		ChangeBoundsRequest req = createSpanRequest(request);
		return getHost().getParent().getCommand(req);
	}

	/*
	 * Return a ChangeBoundsRequest with type REQ_GRIDBAGLAYOUT_SPAN from a
	 * REQ_RESIZE ChangeBoundsRequest
	 */
	private ChangeBoundsRequest createSpanRequest(ChangeBoundsRequest request) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(
				GridLayoutEditPolicy.REQ_GRIDLAYOUT_SPAN);
		req.setEditParts(getHost());
		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setResizeDirection(request.getResizeDirection());
		return req;
	}

	public void eraseSourceFeedback(Request request) {
		if (REQ_RESIZE.equals(request.getType())) {
			layoutEditPolicy.eraseTargetFeedback(request);
		} else
			super.eraseSourceFeedback(request);
	}

	public void showSourceFeedback(Request request) {
		if (REQ_RESIZE.equals(request.getType())) {
			if (request instanceof ChangeBoundsRequest
					&& ((ChangeBoundsRequest) request).getEditParts().size() == 1) {
				layoutEditPolicy
						.showSpanTargetFeedback(createSpanRequest((ChangeBoundsRequest) request));
			}
		} else
			super.showSourceFeedback(request);
	}

	public boolean understandsRequest(Request request) {
		if (REQ_RESIZE.equals(request.getType()))
			return true;
		return super.understandsRequest(request);
	}
}
