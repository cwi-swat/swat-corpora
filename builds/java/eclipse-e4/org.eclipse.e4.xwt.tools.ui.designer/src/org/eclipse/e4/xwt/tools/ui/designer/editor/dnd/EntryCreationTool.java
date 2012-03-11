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
package org.eclipse.e4.xwt.tools.ui.designer.editor.dnd;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ControlEditPart;
import org.eclipse.e4.xwt.tools.ui.imagecapture.swt.ImageCapture;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class EntryCreationTool extends CreationTool {
	static int shadow = 2;

	private Shell shell;
	private Dimension initSize;

	public EntryCreationTool() {
		super();
		setUnloadWhenFinished(true);
	}

	public EntryCreationTool(CreationFactory aFactory) {
		super(aFactory);
		setUnloadWhenFinished(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#activate()
	 */
	public void activate() {
		super.activate();
//		CreationFactory factory = getFactory();
//		if (factory != null && factory instanceof EntryCreationFactory) {
//			Entry entry = (Entry) factory.getNewObject();
//			XamlNode node = EntryHelper.getNode(entry);
//			if (node != null && node instanceof XamlElement) {
//				Image image = getImageFrom((XamlElement) node);
//				if (image != null) {
//					cursor = new Cursor(null, image.getImageData(), 0, 0);
//					setDefaultCursor(cursor);
//				}
//			}
//		}
	}

	private Image getImageFrom(XamlElement node) {
		if (shell == null || shell.isDisposed()) {
			shell = new Shell();
			shell.setLayout(new RowLayout());
		}
		XWTProxy proxy = new XWTProxy(null);
		Object widget;
		try {
			widget = proxy.createWidget(shell, (XamlElement) node);
			if (widget == null || !(widget instanceof Control)) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		shell.layout();
		shell.pack();

		Image image = null;
		Control control = (Control) widget;
		Rectangle bounds = control.getBounds();
		initSize = new Dimension(bounds.width + (shadow * 2), bounds.height + (shadow * 2));
		if (control instanceof Shell) {
			image = ImageCapture.getInstance().capture(control);
		} else {
			image = new Image(control.getDisplay(), initSize.width, initSize.height);
			GC gc = new GC(image);
			control.print(gc);
			gc.copyArea(0, 0, bounds.width, bounds.height, shadow, shadow);
			int width = initSize.width - 1;
			int height = initSize.height - 1;
			
			gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
			for (int i = 0; i < shadow; i++) {
				gc.drawLine(i, i, width, i);
				gc.drawLine(width, i, width, height);
				gc.drawLine(width, height, i, height);
				gc.drawLine(i, height, i, i);
				width -= 1;
				height -= 1;
			}
			gc.dispose();
		}
		return image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.CreationTool#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
//		if (shell != null) {
//			shell.dispose();
//		}
//		if (cursor != null) {
//			cursor.dispose();
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#getTargetRequest()
	 */
	protected Request getTargetRequest() {
		Request req = super.getTargetRequest();
		if (req instanceof EntryCreateRequest && initSize != null) {
			((EntryCreateRequest) req).setInitSize(initSize);
		}
		return req;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.CreationTool#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		EntryCreateRequest request = new EntryCreateRequest();
		request.setFactory(getFactory());
		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#setTargetEditPart(org.eclipse.gef.EditPart)
	 */
	protected void setTargetEditPart(EditPart editpart) {
		if (editpart != null) {
			Class<?> type = editpart.getClass();
			if (!CompositeEditPart.class.isAssignableFrom(type) && ControlEditPart.class.isAssignableFrom(type)) {
				editpart = editpart.getParent();
			}
		}
		super.setTargetEditPart(editpart);
	}

}
