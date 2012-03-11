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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class FeedbackManager {

	private GraphicalEditPolicy policy;

	public FeedbackManager(GraphicalEditPolicy policy) {
		this.policy = policy;
	}

	private final Map<Request, IFigure> req2feedback = new HashMap<Request, IFigure>(1);

	public synchronized void addFeedback(Request req, IFigure feedback) {
		if (req2feedback.containsKey(req)) {
			return;
		}
		req2feedback.put(req, feedback);
		IFigure feedbackLayer = getFeedbackLayer();
		if (feedbackLayer != null && feedback != null) {
			feedbackLayer.add(feedback);
		}
	}

	public synchronized boolean contains(Request req) {
		return req2feedback.containsKey(req);
	}

	public IFigure getFeedbackLayer() {
		return LayerManager.Helper.find(policy.getHost()).getLayer(LayerConstants.FEEDBACK_LAYER);
	}

	public synchronized IFigure eraseFeedback(Request req) {
		IFigure feedback = req2feedback.remove(req);
		IFigure feedbackLayer = getFeedbackLayer();
		if (feedbackLayer != null && feedback != null) {
			feedbackLayer.remove(feedback);
		}
		return feedback;
	}

	public EditPart getHost() {
		return policy.getHost();
	}

	public IFigure getHostFigure() {
		return ((GraphicalEditPart) getHost()).getFigure();
	}
}
