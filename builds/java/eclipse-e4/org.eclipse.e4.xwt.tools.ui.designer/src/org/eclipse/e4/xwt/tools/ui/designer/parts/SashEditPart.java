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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.parts.misc.DragSashTracker;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Sash;

/**
 * @author jin.liu (jin.liu@soyatec.com)
 * 
 */
public class SashEditPart extends ControlEditPart {

	public SashEditPart(Sash sash, XamlNode model) {
		super(sash, model);
		if (model == null) {
			model = XamlFactory.eINSTANCE.createElement("Sash",
					IConstants.XWT_NAMESPACE);
			setModel(model);
		}
	}
	
	@Override
	protected IFigure createContentPane() {
		RectangleFigure rectangle = new RectangleFigure();
		rectangle.setForegroundColor(ColorConstants.lightGray);
		rectangle.setFill(true);
		return rectangle;
	}

	@Override
	public Command getCommand(Request request) {
		if (request.getType().equals(RequestConstants.REQ_DELETE)) {
			return UnexecutableCommand.INSTANCE;
		}
		return super.getCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org
	 * .eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new DragSashTracker(this);
	}

	public Cursor getDefaultCursor() {
		if (isHorizontal()) {
			return SharedCursors.SIZENS;			
		}
		return SharedCursors.SIZEWE;			
	}
	
	public boolean isHorizontal() {
		Sash sash = (Sash) getWidget();
		if (sash != null && !sash.isDisposed()) {
			return ((sash.getStyle() & SWT.HORIZONTAL) == SWT.HORIZONTAL);
		}
		return true;
	}
}