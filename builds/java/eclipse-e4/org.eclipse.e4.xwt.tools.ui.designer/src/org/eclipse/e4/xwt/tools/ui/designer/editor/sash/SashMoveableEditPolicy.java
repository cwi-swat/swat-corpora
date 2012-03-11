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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.tools.SelectionHandle;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.SashUtil;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ControlEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.SashEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.SashFormEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.misc.CompositeEditPartHelper;
import org.eclipse.e4.xwt.tools.ui.designer.parts.misc.DragSashTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.custom.SashForm;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class SashMoveableEditPolicy extends ResizableEditPolicy {

	private SashEditPart editPart;
	private Label label;
	private Point location = new Point();

	public SashMoveableEditPolicy(SashEditPart editPart) {
		this.editPart = editPart;
	}

	/**
	 * Creates the figure used for feedback.
	 * 
	 * @return the new feedback figure
	 */
	protected IFigure createDragSourceFeedbackFigure() {
		label = new Label();
		label.setForegroundColor(ColorConstants.blue);
		getFeedbackLayer().add(label);
		return super.createDragSourceFeedbackFigure();
	}

	@Override
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.eraseChangeBoundsFeedback(request);
		if (label != null) {
			removeFeedback(label);
		}
		label = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
	 */
	protected List<SelectionHandle> createSelectionHandles() {
		List<SelectionHandle> list = new ArrayList<SelectionHandle>();
		list.add(new SelectionHandle(editPart));
		ResizableHandleKit.moveHandle(editPart, new DragSashTracker(editPart),
				null);
		return list;
	}

	/**
	 * When we drag Sash
	 * 
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		IFigure feedback = getDragSourceFeedbackFigure();

		Transposer transposer = new Transposer();
		transposer.setEnabled(!editPart.isHorizontal());

		Rectangle rect = getInitialFeedbackBounds().getCopy();
		getHostFigure().translateToAbsolute(rect);
		rect = transposer.t(rect);

		Point moveDelta = request.getMoveDelta().getCopy();
		moveDelta = transposer.t(moveDelta);

		rect.performTranslate(0, moveDelta.y);

		SashFormEditPart sashFormEditPart = (SashFormEditPart) editPart
				.getParent();
		List<ControlEditPart> children = CompositeEditPartHelper.getChildren(sashFormEditPart);
		SashForm sashForm = (SashForm) sashFormEditPart.getWidget();
		int[] weights = sashForm.getWeights();

		int index = children.indexOf(editPart);
		int start;
		int end;

		if (index - 2 < 0) {
			GraphicalEditPart previous = (GraphicalEditPart) children.get(0);
			IFigure figure = previous.getFigure();
			Rectangle rectangle = figure.getBounds().getCopy();
			figure.translateToAbsolute(rectangle);
			rectangle = transposer.t(rectangle);
			start = rectangle.y;
		} else {
			GraphicalEditPart previous = (GraphicalEditPart) children
					.get(index - 2);
			IFigure figure = previous.getFigure();
			Rectangle rectangle = figure.getBounds().getCopy();
			figure.translateToAbsolute(rectangle);
			rectangle = transposer.t(rectangle);
			start = rectangle.y + rectangle.height;
		}

		if (index + 2 > children.size() - 1) {
			GraphicalEditPart next = (GraphicalEditPart) children.get(children
					.size() - 1);
			IFigure figure = next.getFigure();
			Rectangle rectangle = figure.getBounds().getCopy();
			figure.translateToAbsolute(rectangle);
			rectangle = transposer.t(rectangle);
			end = rectangle.y + rectangle.height;
		} else {
			GraphicalEditPart next = (GraphicalEditPart) children
					.get(index + 2);
			IFigure figure = next.getFigure();
			Rectangle rectangle = figure.getBounds().getCopy();
			figure.translateToAbsolute(rectangle);
			rectangle = transposer.t(rectangle);
			end = rectangle.y;
		}

		if (rect.y < start) {
			rect.y = start;
		}
		if (rect.y + rect.height > end) {
			rect.y = end - rect.height;
		}

		int weightIndex = index / 2;
		int total = weights[weightIndex] + weights[weightIndex + 1];

		{
			int previousWeight = (int) ((rect.y - start) * total / (end - start - rect.height));
			weights[weightIndex] = previousWeight;
			weights[weightIndex + 1] = total - previousWeight;

			label.setText(SashUtil.weightsDisplayString(weights));
			Dimension dimension = label.getPreferredSize();
			label.setSize(dimension);
			dimension = transposer.t(dimension);

			location.y = (int) rect.y + 10;
			location.x = (int) rect.x + (rect.width - dimension.width) / 2;
		}
		location = transposer.t(location);

		rect = transposer.t(rect);

		feedback.translateToRelative(rect);
		feedback.setBounds(rect);

		label.translateToRelative(location);
		label.setLocation(location);
	}
}
