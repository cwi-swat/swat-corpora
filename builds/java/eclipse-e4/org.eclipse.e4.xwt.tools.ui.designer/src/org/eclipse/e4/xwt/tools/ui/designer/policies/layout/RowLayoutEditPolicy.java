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

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.e4.xwt.tools.ui.designer.commands.MoveChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.RowLayoutCommandsFactory;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewResizableEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackHelper;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackManager;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class RowLayoutEditPolicy extends FlowLayoutEditPolicy implements ILayoutEditPolicy {
	private static final int HORIZONTAL = 1 << 8;
	protected int type = HORIZONTAL;

	private FeedbackManager fbm = new FeedbackManager(this);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NewResizableEditPolicy(PositionConstants.SOUTH_EAST, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#getLineFeedback()
	 */
	protected Polyline getLineFeedback() {
		Polyline polyline = super.getLineFeedback();
		polyline.setForegroundColor(ColorConstants.red);
		return polyline;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return type == HORIZONTAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		determineType();
	}

	protected void determineType() {
		EditPart host = getHost();
		if (!(host instanceof CompositeEditPart)) {
			return;
		}
		CompositeEditPart composite = (CompositeEditPart) host;
		Layout layout = composite.getLayout();
		if (layout != null && layout instanceof RowLayout) {
			type = ((RowLayout) layout).type;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart, org.eclipse.gef.EditPart)
	 */
	protected Command createAddCommand(EditPart child, EditPart after) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createMoveChildCommand(org.eclipse.gef.EditPart, org.eclipse.gef.EditPart)
	 */
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return new MoveChildCommand(child, after);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (REQ_RESIZE_CHILDREN.equals(request.getType())) {
			return getResizeCommand(request);
		}
		return super.getCommand(request);
	}

	/**
	 * @param request
	 * @return
	 */
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

	protected Command createResizeChildrenCommand(Request request, EditPart child, Object newSize) {
		RowLayoutCommandsFactory factory = new RowLayoutCommandsFactory(child);
		return factory.getResizeChildCommand(newSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		RowLayoutCommandsFactory factory = new RowLayoutCommandsFactory(getHost());
		EditPart insertionReference = getInsertionReference(request);
		return factory.getCreateCommand(request, insertionReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		RowLayoutCommandsFactory factory = new RowLayoutCommandsFactory(getHost());
		return factory.getDeleteCommand(request);
	}

	protected void showLayoutTargetFeedback(Request request) {
		if (REQ_CREATE.equals(request.getType())) {
			if (!FeedbackHelper.showCreationFeedback(fbm, (CreateRequest) request)) {
				super.showLayoutTargetFeedback(request);
			}
		} else {
			super.showLayoutTargetFeedback(request);
		}
	}

	protected void eraseLayoutTargetFeedback(Request request) {
		fbm.eraseFeedback(request);
		super.eraseLayoutTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy#refresh()
	 */
	public void refresh() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy#getType()
	 */
	public LayoutType getType() {
		return LayoutType.RowLayout;
	}
}
