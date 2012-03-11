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
package org.eclipse.e4.xwt.tools.ui.designer.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ApplyAttributeSettingCommand;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class ElementDirectEditPolicy extends DirectEditPolicy {
	private boolean setting = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected Command getDirectEditCommand(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		XamlElement element = (XamlElement) (getHost().getModel());
		return new ApplyAttributeSettingCommand(element, "text", IConstants.XWT_NAMESPACE, value);
	}

	@Override
	protected void eraseDirectEditFeedback(DirectEditRequest request) {
		super.eraseDirectEditFeedback(request);
		setting = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected void showCurrentEditValue(DirectEditRequest request) {
	}

	@Override
	protected void showDirectEditFeedback(DirectEditRequest request) {
		CellEditor cellEditor = request.getCellEditor();
		if (cellEditor == null) {
			return;
		}
		if (!setting) {
			GraphicalEditPart graphicalEditPart = (GraphicalEditPart) getHost();
			IFigure figure = graphicalEditPart.getFigure();
			
			XamlElement element = (XamlElement) (getHost().getModel());
			XamlAttribute attribute = element.getAttribute("text", IConstants.XWT_NAMESPACE);
			if (attribute == null) {
				return;
			}
			String value = attribute.getValue();
			cellEditor.setValue(value);
			Control control = request.getCellEditor().getControl();
			if (control instanceof Text) {
				Rectangle rectangle = figure.getBounds();
				Text text = (Text) control;
				text.setLocation(rectangle.x, rectangle.y);
				text.selectAll();
			}
		}
		setting = true;
	}
	
	@Override
	public boolean understandsRequest(Request request) {
		if (request instanceof DirectEditRequest) {
			DirectEditRequest directEditRequest = (DirectEditRequest) request;
			XamlElement element = (XamlElement) (getHost().getModel());
			XamlAttribute attribute = element.getAttribute("text", IConstants.XWT_NAMESPACE);
			if (directEditRequest.getCellEditor() == null || attribute == null) {
				return false;
			}
		}
		return super.understandsRequest(request);
	}
}