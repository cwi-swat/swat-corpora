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
package org.eclipse.e4.xwt.tools.ui.designer.policies.feedback;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.OutlineBorder;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.CreateReqHelper;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ShellEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.OffsetUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class FeedbackHelper {

	public static boolean showCreationFeedback(FeedbackManager fbm, CreateRequest createReq) {
		if (fbm.contains(createReq)) {
			return true;
		}
		CreateReqHelper helper = new CreateReqHelper(createReq);
		if (helper.canCreate(fbm.getHost())) {
			Rectangle r = fbm.getHostFigure().getBounds().getCopy();
			if (helper.isCreate("layout") || helper.isCreate("menu")) {
				return showFillFeedback(fbm, createReq);
			} else if (helper.isCreate("menuBar")) {
				r = getMenuFeedbackBounds(fbm);
				if (r != null) {
					Label feedback = new Label("Menu bar would be placed here");
					feedback.setBounds(r);
					feedback.setBackgroundColor(ColorConstants.button);
					feedback.setOpaque(true);
					feedback.setBorder(new OutlineBorder(ColorConstants.blue, ColorConstants.button));
					fbm.addFeedback(createReq, feedback);
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	public static Polygon createTargetFeedback() {
		Polygon polygon = new Polygon();
		polygon.addPoint(new Point());
		polygon.addPoint(new Point());
		polygon.addPoint(new Point());
		polygon.addPoint(new Point());
		polygon.setForegroundColor(ColorConstants.blue);
		return polygon;
	}
	
	public static void updateTargetFeedback(GraphicalEditPart editPart, Polygon polygon ) {
		IFigure figure = editPart.getFigure();
		Rectangle rectangle = figure.getBounds().getCopy();
		figure.translateToAbsolute(rectangle);
		
		Point p = rectangle.getBottomLeft();
		polygon.translateToRelative(p);
		polygon.setPoint(p, 0);
		
		p = rectangle.getBottomRight();
		polygon.translateToRelative(p);
		polygon.setPoint(p, 1);

		p = rectangle.getTopRight();
		polygon.translateToRelative(p);
		polygon.setPoint(p, 2);
		
		p = rectangle.getTopLeft();
		polygon.translateToRelative(p);
		polygon.setPoint(p, 3);
	}

	public static boolean showFillFeedback(FeedbackManager fbm, CreateRequest createReq) {
		if (fbm.contains(createReq)) {
			return true;
		}
		CreateReqHelper helper = new CreateReqHelper(createReq);
		if (helper.canCreate(fbm.getHost())) {
			Rectangle r = fbm.getHostFigure().getBounds().getCopy();
			FillFeedback feedback = new FillFeedback(new Rectangle(r.x, r.y, r.width, r.height));
			fbm.addFeedback(createReq, feedback);
			return true;
		}
		return false;
	}

	private static Rectangle getMenuFeedbackBounds(FeedbackManager fbm) {
		EditPart host = fbm.getHost();
		if (host instanceof ShellEditPart) {
			int yOffset = OffsetUtil.getYOffset(host);
			Rectangle r = fbm.getHostFigure().getBounds().getCopy();
			return new Rectangle(r.x + 2, r.y + yOffset - 3, r.width - 4, 22);
		}
		return null;
	}
}
