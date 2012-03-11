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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.commands.NullLayoutCommandsFactory;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewResizableEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.CrossFeedback;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackHelper;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackManager;
import org.eclipse.e4.xwt.tools.ui.designer.utils.FigureUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class NullLayoutEditPolicy extends XYLayoutEditPolicy implements ILayoutEditPolicy {

	private FeedbackManager fbm = new FeedbackManager(this);
	private CrossFeedback crossFeedback;
	private Polygon targetFeedback;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		NullLayoutCommandsFactory factory = new NullLayoutCommandsFactory(child);
		return factory.getChangeConstraintCommand(constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NewResizableEditPolicy(PositionConstants.NSEW, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		NullLayoutCommandsFactory factory = new NullLayoutCommandsFactory(getHost());
		return factory.getCreateCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		NullLayoutCommandsFactory factory = new NullLayoutCommandsFactory(getHost());
		return factory.getDeleteCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#showLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void showLayoutTargetFeedback(Request request) {
		if (REQ_CREATE.equals(request.getType())) {
			if (!FeedbackHelper.showCreationFeedback(fbm, (CreateRequest) request)) {
				Point location = ((CreateRequest) request).getLocation().getCopy();
				GraphicalEditPart parent = (GraphicalEditPart) getHost();
				if (!(parent instanceof CompositeEditPart)) {
					return;
				}
				if (crossFeedback == null) {
					crossFeedback = new CrossFeedback(location);
				}
				if (targetFeedback == null) {
					targetFeedback = FeedbackHelper.createTargetFeedback();
				}
				FeedbackHelper.updateTargetFeedback(parent, targetFeedback);
				addFeedback(targetFeedback);
				
				Point center = FigureUtil.translateToRelative(parent, location);
				crossFeedback.setTooltipText("(" + center.x + "," + center.y + ")");
				crossFeedback.setCenter(location);
				addFeedback(crossFeedback);
			}
		} else {
			super.showLayoutTargetFeedback(request);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#eraseLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void eraseLayoutTargetFeedback(Request request) {
		fbm.eraseFeedback(request);
		if (crossFeedback != null && crossFeedback.getParent() != null) {
			removeFeedback(crossFeedback);
		}
		if (targetFeedback != null && targetFeedback.getParent() != null) {
			removeFeedback(targetFeedback);
		}
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
		return LayoutType.NullLayout;
	}
}
