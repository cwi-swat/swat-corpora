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
package org.eclipse.e4.xwt.tools.ui.designer.parts.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Property;
import org.eclipse.swt.SWT;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DataBindingConnection extends PolylineConnection {

	private BindingInfo bindingInfo;
	private Label sourceEndpoint;
	private Label targetEndpoint;

	public DataBindingConnection(BindingInfo bindingInfo) {
		this.bindingInfo = bindingInfo;
		setForegroundColor(ColorConstants.red);
		setConnectionRouter(new ManhattanConnectionRouter());
		setLineJoin(SWT.JOIN_ROUND);

		setTargetDecoration(createTargetDecoration());

		setDisplayEndpoints(true);
	}

	protected void addTargetEndpoint() {
		if (bindingInfo == null) {
			return;
		}
		IObservable observeTarget = bindingInfo.getTarget();
		if (observeTarget == null) {
			return;
		}
		Property property = bindingInfo.getTargetProperty();
		if (property == null) {
			return;
		}
		targetEndpoint = new Label(property.toString());
		targetEndpoint.setOpaque(false);
		targetEndpoint.setBackgroundColor(ColorConstants.buttonLightest);
		targetEndpoint.setForegroundColor(ColorConstants.blue);
		targetEndpoint.setBorder(new LineBorder());
		ConnectionEndpointLocator locator = new ConnectionEndpointLocator(this, true);
		locator.setUDistance(2);
		locator.setVDistance(3);
		add(targetEndpoint, locator);
	}

	protected void addSourceEndpoint() {
		if (bindingInfo == null) {
			return;
		}
		IObservable model = bindingInfo.getModel();
		if (model == null) {
			return;
		}
		Property property = bindingInfo.getModelProperty();
		if (property == null) {
			return;
		}
		sourceEndpoint = new Label(property.toString());
		sourceEndpoint.setOpaque(false);
		sourceEndpoint.setBackgroundColor(ColorConstants.buttonLightest);
		sourceEndpoint.setForegroundColor(ColorConstants.blue);
		sourceEndpoint.setBorder(new LineBorder());
		ConnectionEndpointLocator locator = new ConnectionEndpointLocator(this, false);
		locator.setUDistance(2);
		locator.setVDistance(-3);
		add(sourceEndpoint, locator);
	}

	protected RotatableDecoration createTargetDecoration() {
		return new PolygonDecoration();
	}

	protected RotatableDecoration createSourceDecoration() {
		PolylineDecoration sourceDec = new PolylineDecoration();

		PointList pl = new PointList();
		pl.addPoint(-2, 1);
		pl.addPoint(0, 1);
		pl.addPoint(0, -1);
		pl.addPoint(-2, -1);
		sourceDec.setTemplate(pl);
		sourceDec.setScale(7, 3);

		return sourceDec;
	}

	public void setDisplayEndpoints(boolean display) {
		if (display) {
			addSourceEndpoint();
			addTargetEndpoint();
		} else {
			if (sourceEndpoint != null && this == sourceEndpoint.getParent()) {
				remove(sourceEndpoint);
				sourceEndpoint = null;
			}
			if (targetEndpoint != null && this == targetEndpoint.getParent()) {
				remove(targetEndpoint);
				targetEndpoint = null;
			}
		}
	}
}
