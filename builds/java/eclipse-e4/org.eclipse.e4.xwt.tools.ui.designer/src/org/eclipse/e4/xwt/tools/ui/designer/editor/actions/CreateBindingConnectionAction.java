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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class CreateBindingConnectionAction extends Action {

	private GraphicalEditPart editPart;
	private Object observe;
	private String property;

	public CreateBindingConnectionAction(EditPart editPart, Object observe, String property, Image image) {
		super(property, image == null ? null : ImageDescriptor.createFromImage(image));
		this.editPart = (GraphicalEditPart) editPart;
		this.observe = observe;
		this.property = property;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (editPart == null) {
			return;
		}
		/**
		 * Create a mouse down event that thinks it is over the blob and dispatch it. This is a bit of a fudge to mimic what the user ought to do.
		 */

		Point point = null;
		point = editPart.getFigure().getClientArea().getCenter();
		Point p = point;
		editPart.getFigure().translateToAbsolute(p);

		Canvas canvas = (Canvas) editPart.getViewer().getControl();
		Event event = new Event();
		event.button = 1;
		event.count = 0;
		event.detail = 0;
		event.end = 0;
		event.height = 0;
		event.keyCode = 0;
		event.start = 0;
		event.stateMask = 0;
		event.time = 9516624; // any old time... doesn't matter
		event.type = SWT.MouseDown;
		event.widget = canvas;
		event.width = 0;
		event.x = p.x + 3;
		event.y = p.y + 3;

		// StructuredProperty tp = new PropertiesProvider().findProperty(observe, property);
		// final ObserveInfo observeInfo = new ObserveInfo(observe, tp, ObserveType.WIDGETS);
		//
		// CreationFactory factory = new CreationFactory() {
		//
		// public Object getNewObject() {
		// return observeInfo;
		// }
		//
		// public Object getObjectType() {
		// return editPart;
		// }
		//
		// };
		// BindingCreateTool tool = new BindingCreateTool(factory);
		// editPart.getViewer().getEditDomain().setActiveTool(tool);
		// canvas.notifyListeners(SWT.MouseDown, event);

		System.out.println(editPart);
		System.out.println(observe);
		System.out.println(property);
	}
}
