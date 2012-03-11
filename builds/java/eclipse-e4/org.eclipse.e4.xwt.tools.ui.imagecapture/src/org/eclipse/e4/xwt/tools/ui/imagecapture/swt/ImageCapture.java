/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.       *
 * All rights reserved. This program and the accompanying materials            *
 * are made available under the terms of the Eclipse Public License v1.0       *
 * which accompanies this distribution, and is available at                    *
 * http://www.eclipse.org/legal/epl-v10.html                                   *  
 * Contributors:                                                               *  
 *     Soyatec - initial API and implementation                                * 
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.imagecapture.swt;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.internal.swt.targetvm.unix.DisposeUtil;

/**
 * Capture and transmit the image back. There are platform specific subclasses to handle the grab of the image from a control.
 * 
 * @since 1.1.0
 */
public abstract class ImageCapture {
	static ImageCapture instance;

	public Image captureImage(Object control) {
		if (control instanceof Control) {
			Control ctl = (Control) control;
			return captureImage(ctl);
		}
		return null;
	}

	public Image captureImage(Control control) {
		Rectangle rectangle = control.getBounds();
		Display display = control.getDisplay();
		Image image = null;
		if (control instanceof Shell) {
			Shell shell = (Shell) control;
			shell.layout();
			Point parentLocation = control.toDisplay(0, 0);
			image = getImage(control, rectangle.width, rectangle.height, false);

			rectangle.x = parentLocation.x;
			rectangle.y = parentLocation.y;

			GC myImageGC = new GC(image);
			try {
				for (Control child : shell.getChildren()) {
					Rectangle childBounds = child.getBounds();
					// bug of SWT on Win32, child bounds is not correct in the
					// Window is not in the ToolBar
					int x = (rectangle.width - childBounds.width) / 2;
					int y = (rectangle.height - childBounds.height) - x;
					childBounds.x = rectangle.x + x;
					childBounds.y = rectangle.y + y;
					if (!rectangle.intersects(childBounds))
						continue; // Child is completely outside parent.

					Image childImage = new Image(display, child.getBounds());
					GC gc = new GC(childImage);
					child.print(gc);
					DisposeUtil.dispose(gc);
					try {
						myImageGC.drawImage(childImage, x, y);
					} finally {
						childImage.dispose();
					}
				}
			} finally {
				myImageGC.dispose();
			}
		} else {
			image = capture(control);
		}
		return image;
	}

	public Image capture(Control control) {
		Rectangle bounds = control.getBounds();
		return capture(control, bounds.width, bounds.height, false);
	}

	public Image capture(Control control, int width, int height, boolean includeChildren) {
		return getImage(control, width, height, includeChildren);
	}

	protected abstract Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren);

	public static ImageCapture getInstance() {
		if (instance == null) {
			if (Platform.OS_WIN32.equals(Platform.getOS()))
				instance = new org.eclipse.ve.internal.swt.targetvm.win32.ImageCapture();
			else if (Platform.WS_GTK.equals(Platform.getWS())) {
				if (Platform.ARCH_IA64.equals(Platform.getOSArch()) || Platform.ARCH_X86_64.equals(Platform.getOSArch()))
					instance = new org.eclipse.ve.internal.swt.targetvm.unix.bits64.ImageCapture();
				else
					instance = new org.eclipse.ve.internal.swt.targetvm.unix.ImageCapture();
			} else if (Platform.OS_MACOSX.equals(Platform.getOS())) {
				if (Platform.WS_COCOA.equals(Platform.getWS()))
					instance = new org.eclipse.ve.internal.swt.targetvm.macosx.cocoa.ImageCapture();
				else if (Platform.WS_CARBON.equals(Platform.getWS()))
					instance = new org.eclipse.ve.internal.swt.targetvm.macosx.ImageCapture();
				else
					throw new UnsupportedOperationException(Platform.getOS());
			} else {
				throw new UnsupportedOperationException(Platform.getOS());
			}
		}
		return instance;
	}

	public static int getIntHandle(Control control) {
		try {
			Field handleField = control.getClass().getField("handle");
			handleField.setAccessible(true);
			return (Integer) handleField.get(control);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
