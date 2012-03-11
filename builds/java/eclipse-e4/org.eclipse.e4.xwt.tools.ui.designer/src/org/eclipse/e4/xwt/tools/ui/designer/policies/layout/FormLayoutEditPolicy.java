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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.commands.FormDataCreateCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.FormDataDeleteCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ResizeCommand;
import org.eclipse.e4.xwt.tools.ui.designer.editor.dnd.EntryCreateRequest;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.CreateReqHelper;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ControlEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewResizableEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackHelper;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackManager;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form.FormDataFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form.FormLayoutData;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form.FormLayoutHelper;
import org.eclipse.e4.xwt.tools.ui.designer.utils.FigureUtil;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.ForwardedRequest;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class FormLayoutEditPolicy extends XYLayoutEditPolicy implements
		ILayoutEditPolicy {
	private FeedbackManager fbm = new FeedbackManager(this);
	private FormLayoutHelper helper;
	private FormDataFigure feedback;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse
	 * .gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return getFormDataCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#activate()
	 */
	public void activate() {
		helper = new FormLayoutHelper((CompositeEditPart) getHost());
		super.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy
	 * #refresh()
	 */
	public void refresh() {

	}

	private FormLayoutData computeData(Request request) {
		if (helper == null) {
			helper = new FormLayoutHelper((CompositeEditPart) getHost());
		}
		Point location = null;
		Dimension size = null;
		Control control = null;
		if (REQ_CREATE == request.getType()) {
			location = ((EntryCreateRequest) request).getLocation().getCopy();
			// size = ((EntryCreateRequest) request).getInitSize();
		} else if (REQ_MOVE == request.getType()) {
			location = ((ChangeBoundsRequest) request).getLocation().getCopy();
			EditPart part = (EditPart) ((ChangeBoundsRequest) request)
					.getEditParts().get(0);
			if (part != null && part instanceof ControlEditPart) {
				control = (Control) ((ControlEditPart) part).getWidget();
				if (control != null && !control.isDisposed()) {
					size = ((ControlEditPart) part).getVisualInfo().getBounds()
							.getSize();
				}
			}
		}
		if (size == null) {
			size = new Dimension(10, 10);
		}
		if (location != null && size != null) {
			Point relative = FigureUtil.translateToRelative(getHost(), location
					.getCopy());
			FormLayoutData layoutData = helper.computeData(new Rectangle(
					relative, size), control);
			if (layoutData != null) {
				layoutData.bounds = new Rectangle(location, size);
			}
			return layoutData;
		}
		return null;
	}

	private Command getFormDataCommand(Request request) {
		EditPart parent = getHost();
		XamlNode forCreate = null;
		if (REQ_CREATE.equals(request.getType())) {
			CreateReqHelper reqHelper = new CreateReqHelper(
					(CreateRequest) request);
			if (reqHelper.canCreate(parent)) {
				forCreate = reqHelper.getNewObject();
			}
		} else if (REQ_MOVE_CHILDREN.equals(request.getType())) {
			EditPart part = (EditPart) ((ChangeBoundsRequest) request)
					.getEditParts().get(0);
			if (part != null) {
				forCreate = (XamlNode) part.getModel();
			}
		}
		if (forCreate == null) {
			return null;
		}
		FormLayoutData layoutData = null;
		if (feedback != null) {
			layoutData = feedback.getLayoutData();
		}
		if (layoutData == null) {
			layoutData = computeData(request);
		}
		if (layoutData == null || layoutData.data == null) {
			return null;
		}
		return new FormDataCreateCommand(parent, forCreate, layoutData.data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.policies.layout.NullLayoutEditPolicy
	 * #showLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void showLayoutTargetFeedback(Request request) {
		if (REQ_CREATE.equals(request.getType())
				&& FeedbackHelper.showCreationFeedback(fbm,
						(CreateRequest) request)) {
			return;
		}
		FormLayoutData data = computeData(request);
		if (data != null) {
			showFormdataFeedback(data);
		} else {
			super.showLayoutTargetFeedback(request);
		}
	}

	private void showFormdataFeedback(FormLayoutData layoutData) {
		if (layoutData == null || layoutData.data == null
				|| layoutData.bounds == null) {
			return;
		}
		if (feedback == null) {
			feedback = new FormDataFigure(layoutData);
		}
		feedback.setLayoutData(layoutData);
		feedback.setBounds(getHostFigure().getBounds());
		addFeedback(feedback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.policies.layout.NullLayoutEditPolicy
	 * #eraseLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void eraseLayoutTargetFeedback(Request request) {
		if (feedback != null) {
			removeFeedback(feedback);
			feedback = null;
		}
		super.eraseLayoutTargetFeedback(request);
		fbm.eraseFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy
	 * #getType()
	 */
	public LayoutType getType() {
		return LayoutType.FormLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * getMoveChildrenCommand(org.eclipse.gef.Request)
	 */
	protected Command getMoveChildrenCommand(Request request) {
		return getFormDataCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * getResizeChildrenCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected Command getResizeChildrenCommand(ChangeBoundsRequest request) {
		CompoundCommand command = new CompoundCommand();
		Dimension growth = request.getSizeDelta();
		List editParts = request.getEditParts();
		for (Iterator iterator = editParts.iterator(); iterator.hasNext();) {
			EditPart child = (EditPart) iterator.next();
			command.add(new ResizeCommand(child, growth));
		}
		return command.unwrap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand
	 * (org.eclipse.gef.Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		ForwardedRequest forwarded = (ForwardedRequest) request;
		EditPart sender = forwarded.getSender();
		return new FormDataDeleteCommand(getHost(), sender);
	}

	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NewResizableEditPolicy(PositionConstants.NSEW, true);
	}
}
