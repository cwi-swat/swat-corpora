/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ImageCapture.java,v $
 *  $Revision: 1.7 $  $Date: 2009/12/22 15:40:57 $ 
 */
package org.eclipse.e4.xwt.vex.swt;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Capture and transmit the image back. There are platform specific subclasses to handle the grab of the image from a control.
 * 
 * @since 1.1.0
 */
public abstract class ImageCapture {
	static ImageCapture instance;

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
					// bug of SWT on Win32, child bounds is not correct in the Window is not in the ToolBar
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
			image = defaultCapture(control);
		}
		return image;
	}

	public Image defaultCapture(Control control) {
		Image image = new Image(control.getDisplay(), control.getBounds());
		GC gc = new GC(image);
		try {
			if (control.print(gc)) {
				return image;
			} else {
				image.dispose();
				Rectangle bounds = control.getBounds();
				return getImage(control, bounds.width, bounds.height, true);	
			}	
		} finally {
			DisposeUtil.dispose(gc);
		}
	}

	protected abstract Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren);

	public static ImageCapture getImageCapture() {
		if (instance == null) {
			if (Platform.OS_WIN32.equals(Platform.getOS()))
				instance = new Win32ImageCapture();
			else if (Platform.WS_GTK.equals(Platform.getWS())) {
				if (Platform.ARCH_IA64.equals(Platform.getOSArch()) || Platform.ARCH_X86_64.equals(Platform.getOSArch()))
					instance = new org.eclipse.ve.internal.swt.targetvm.unix.bits64.ImageCapture();
				else
					instance = new org.eclipse.ve.internal.swt.targetvm.unix.ImageCapture();
			} else if (Platform.OS_MACOSX.equals(Platform.getOS())) {
				if (Platform.WS_COCOA.equals(Platform.getWS())) {
					instance = new org.eclipse.ve.internal.swt.targetvm.macosx.cocoa.ImageCapture();
				}
				else if (Platform.WS_CARBON.equals(Platform.getWS())) {
					instance = new org.eclipse.ve.internal.swt.targetvm.macosx.ImageCapture();
				}
				else { 
					throw new UnsupportedOperationException(Platform.getOS());
				}
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
