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
package org.eclipse.e4.xwt.tools.ui.designer.editor.sash;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.e4.xwt.tools.ui.designer.parts.SashFormEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewResizableEditPolicy;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.SelectionRequest;

/**
 * 
 * @author yyang <yves.yang@soyatec.com>
 * 
 */
public class SashFormChildResizableEditPolicy extends NewResizableEditPolicy {
	public static final int WIDTH = 10;

	public SashFormChildResizableEditPolicy(int directions,
			boolean displayNonHandles) {
		super(directions, displayNonHandles);
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		EditPart part = getTargetEditPart(request,
				(GraphicalEditPart) getHost());
		if (part != null) {
			return part;
		}
		return super.getTargetEditPart(request);
	}

	public static EditPart getTargetEditPart(Request request,
			GraphicalEditPart editPart) {
		SashFormEditPart sashFormEditPart = (SashFormEditPart) editPart
				.getParent();
		IFigure figure = editPart.getFigure();
		Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);

		Transposer transposer = new Transposer();
		transposer.setEnabled(!sashFormEditPart.isHorizontal());
		bounds = transposer.t(bounds);

		if (request instanceof CreateRequest) {
			CreateRequest createRequest = (CreateRequest) request;
			Point location = createRequest.getLocation().getCopy();
			location = transposer.t(location);

			if (location.x <= bounds.x + WIDTH
					|| location.x > bounds.x + bounds.width - WIDTH) {
				return sashFormEditPart;
			}
		} else if (request instanceof SelectionRequest) {
			SelectionRequest locationRequest = (SelectionRequest) request;
			Point location = locationRequest.getLocation().getCopy();
			location = transposer.t(location);
			List<EditPart> children = sashFormEditPart.getChildren();
			int index = children.indexOf(editPart);
			if (index != 0 && location.x <= bounds.x + WIDTH) {
				return children.get(index - 1);
			} else if (location.x > bounds.x + bounds.width - WIDTH
					&& index != (children.size() - 1)) {
				return children.get(index + 1);
			}
		}
		return null;
	}
}
