/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation with help of Kevin Barnes 
 *               <krbarnes@ca.ibm.com>
 *******************************************************************************/
package org.eclipse.ve.internal.swt.targetvm.macosx.cocoa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author yyang <yves.yang@soyatec.com>
 * @author Kevin Barnes <krbarnes@ca.ibm.com>
 */
public class ImageCapture extends org.eclipse.e4.xwt.tools.ui.imagecapture.swt.ImageCapture {
	static Field GC_handle;
	static Field Control_view;
	static Method NSView_superview;
	static Method NSObject_isEqual;
	static Method NSView_bounds;
	static Method NSGraphicsContext_saveGraphicsState;
	static Method NSGraphicsContext_restoreGraphicsState;
	static Method NSGraphicsContext_setCurrentContext;

	static Method NSAffineTransform_transform;

	static Method NSAffineTransform_translateXBy;
	static Method NSAffineTransform_scaleXBy;
	static Method NSAffineTransform_concat;

	static Method NSView_displayRectIgnoringOpacity;

	static {
		try {
			Control_view = Control.class.getDeclaredField("view"); //$NON-NLS-1$
			Control_view.setAccessible(true);

			GC_handle = GC.class.getDeclaredField("handle"); //$NON-NLS-1$
			GC_handle.setAccessible(true);

			Class<?> NSViewClass = Class.forName("org.eclipse.swt.internal.cocoa.NSView"); //$NON-NLS-1$
			NSView_superview = NSViewClass.getMethod("superview");

			Class<?> idClass = Class.forName("org.eclipse.swt.internal.cocoa.id"); //$NON-NLS-1$
			Class<?> NSObjectClass = Class.forName("org.eclipse.swt.internal.cocoa.NSObject"); //$NON-NLS-1$
			NSObject_isEqual = NSObjectClass.getMethod("isEqual", idClass);

			NSView_bounds = NSViewClass.getMethod("bounds");

			Class<?> NSGraphicsContextClass = Class.forName("org.eclipse.swt.internal.cocoa.NSGraphicsContext"); //$NON-NLS-1$
			NSGraphicsContext_saveGraphicsState = NSGraphicsContextClass.getMethod("saveGraphicsState");
			NSGraphicsContext_setCurrentContext = NSGraphicsContextClass.getMethod("setCurrentContext", NSGraphicsContextClass);
			NSGraphicsContext_restoreGraphicsState = NSGraphicsContextClass.getMethod("restoreGraphicsState");

			Class<?> NSAffineTransformClass = Class.forName("org.eclipse.swt.internal.cocoa.NSAffineTransform"); //$NON-NLS-1$
			NSAffineTransform_transform = NSAffineTransformClass.getMethod("transform");
			NSAffineTransform_translateXBy = NSAffineTransformClass.getMethod("translateXBy", double.class, double.class);
			if (NSAffineTransform_translateXBy == null) {
				NSAffineTransform_translateXBy = NSAffineTransformClass.getMethod("translateXBy", float.class, float.class);
			}
			NSAffineTransform_scaleXBy = NSAffineTransformClass.getMethod("scaleXBy", double.class, double.class);
			if (NSAffineTransform_scaleXBy == null) {
				NSAffineTransform_scaleXBy = NSAffineTransformClass.getMethod("scaleXBy", float.class, float.class);
			}
			NSAffineTransform_concat = NSAffineTransformClass.getMethod("concat");

			Class<?> NSRectClass = Class.forName("org.eclipse.swt.internal.cocoa.NSRect"); //$NON-NLS-1$

			NSView_displayRectIgnoringOpacity = NSViewClass.getMethod("displayRectIgnoringOpacity", NSRectClass, NSGraphicsContextClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {
		if (!(control instanceof Shell)) {
			Rectangle bounds = control.getBounds();
			Image image = new Image(control.getDisplay(), bounds.width, bounds.height);
			GC gc = new GC(image);
			control.print(gc);
			gc.dispose();
			return image;
		}

		Rectangle rectangle = new Rectangle(0, 0, maxWidth, maxHeight);
		Image image = new Image(control.getDisplay(), rectangle);
		GC gc = new GC(image);

		try {
			Object view = Control_view.get(control);
			if (control instanceof Shell) {
				Object superview = NSView_superview.invoke(view);
				while (superview != null) {
					view = superview;
					superview = NSView_superview.invoke(view);
				}
			} else {
				Composite parent = control.getParent();
				Object parentView = Control_view.get(parent);

				while (true) {
					Object superview = NSView_superview.invoke(view);
					Boolean equal = (Boolean) NSObject_isEqual.invoke(superview, parentView);
					if (equal) {
						break;
					}
					view = superview;
				}
			}
			Object gcHandle = GC_handle.get(gc);
			// gc.handle.saveGraphicsState();
			NSGraphicsContext_saveGraphicsState.invoke(gcHandle);
			// NSGraphicsContext.setCurrentContext(gc.handle);
			NSGraphicsContext_setCurrentContext.invoke(null, gcHandle);
			// NSAffineTransform transform = NSAffineTransform.transform ();
			Object transform = NSAffineTransform_transform.invoke(null);
			// transform.translateXBy (0, rectangle.height);
			NSAffineTransform_translateXBy.invoke(transform, 0, rectangle.height);
			// transform.scaleXBy (1, -1);
			NSAffineTransform_scaleXBy.invoke(transform, 1, -1);
			// transform.concat ();
			NSAffineTransform_concat.invoke(transform);
			// view.displayRectIgnoringOpacity(view.bounds(), gc.handle);
			Object bounds = NSView_bounds.invoke(view);
			NSView_displayRectIgnoringOpacity.invoke(view, bounds, gcHandle);

			// gc.handle.restoreGraphicsState();
			NSGraphicsContext_restoreGraphicsState.invoke(gcHandle);
		} catch (Exception e) {
			e.printStackTrace();
		}

		gc.dispose();
		return image;
	}
}
