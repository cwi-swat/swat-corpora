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

import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AttachedPropertyCreateCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.DefaultCreateCommand;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.CreateReqHelper;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.TabFolderEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewNonResizeEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.CrossFeedback;
import org.eclipse.e4.xwt.tools.ui.designer.policies.feedback.FeedbackHelper;
import org.eclipse.e4.xwt.tools.ui.designer.utils.FigureUtil;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.Item;

/**
 * @author jin.liu (jin.liu@soyatec.com)
 * 
 */
public class TabFolderLayoutEditPolicy extends LayoutEditPolicy {
	private Polygon targetFeedback;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(org
	 * .eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NewNonResizeEditPolicy(false);
	}

	protected void showLayoutTargetFeedback(Request request) {
		if (REQ_CREATE.equals(request.getType())) {
			TabFolderEditPart parent = (TabFolderEditPart) getHost();
			if (targetFeedback == null) {
				targetFeedback = FeedbackHelper.createTargetFeedback();
			}
			FeedbackHelper.updateTargetFeedback(parent, targetFeedback);
			addFeedback(targetFeedback);
		}
		super.showLayoutTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#eraseLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void eraseLayoutTargetFeedback(Request request) {
		if (targetFeedback != null && targetFeedback.getParent() != null) {
			removeFeedback(targetFeedback);
		}
		super.eraseLayoutTargetFeedback(request);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse
	 * .gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		TabFolderEditPart host = (TabFolderEditPart) getHost();
		CreateReqHelper helper = new CreateReqHelper(request);
		XamlNode newObject = helper.getNewObject();
		if (newObject == null) {
			return null;
		}
		IMetaclass metaclass = XWTUtility.getMetaclass(newObject);
		if (metaclass == null) {
			return null;
		}
		if (Item.class.isAssignableFrom(metaclass.getType())) {
			return new DefaultCreateCommand(host, request);
		} else {
			return new AttachedPropertyCreateCommand(host.getActiveItemPart(),
					request, "control");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getMoveChildrenCommand(
	 * org.eclipse.gef.Request)
	 */
	protected Command getMoveChildrenCommand(Request request) {
		return null;
	}

}
