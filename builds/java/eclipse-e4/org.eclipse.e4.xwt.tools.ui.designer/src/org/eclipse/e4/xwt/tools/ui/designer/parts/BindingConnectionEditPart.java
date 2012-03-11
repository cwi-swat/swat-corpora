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
package org.eclipse.e4.xwt.tools.ui.designer.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.root.BindingLayer;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.parts.figures.DataBindingConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BindingConnectionEditPart extends AbstractConnectionEditPart {

	public BindingConnectionEditPart(BindingInfo bindingInfo) {
		setModel(bindingInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#activateFigure()
	 */
	protected void activateFigure() {
		getLayer(BindingLayer.ID).add(getFigure());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#deactivateFigure()
	 */
	protected void deactivateFigure() {
		getLayer(BindingLayer.ID).remove(getFigure());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		return new DataBindingConnection((BindingInfo) getModel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// Selection handle edit policy.
		// Makes the connection show a feedback, when selected by the user.
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
	}
}
