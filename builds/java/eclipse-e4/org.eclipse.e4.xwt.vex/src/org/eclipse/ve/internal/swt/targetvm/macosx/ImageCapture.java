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
 *  $Revision: 1.6 $  $Date: 2010/06/18 00:17:02 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.macosx;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Image Capture for Mac OS X platforms.
 * 
 * @since 1.2.0
 */

public class ImageCapture extends org.eclipse.e4.xwt.vex.swt.ImageCapture {

	private static Field shellHandleField = null;
	private static Method HIViewGetRootMethod = null;
	private static Method carbon_newMethod = null;

	static {

		System.loadLibrary("swt-carbon-print"); //$NON-NLS-1$

		try {
			shellHandleField = Shell.class.getDeclaredField("shellHandle"); //$NON-NLS-1$
			shellHandleField.setAccessible(true);

			Class<?> osClass = Class.forName("org.eclipse.swt.internal.carbon.OS"); //$NON-NLS-1$
			HIViewGetRootMethod = osClass.getMethod("HIViewGetRoot", new Class[] { int.class }); //$NON-NLS-1$

			carbon_newMethod = Image.class.getMethod("carbon_new", new Class[] { Device.class, int.class, int.class, int.class }); //$NON-NLS-1$

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private native int captureImage(int controlHandle, int shellHandle);

	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {

		Rectangle rect = control.getBounds();
		if (rect.width <= 0 || rect.height <= 0)
			return null;

		int width = Math.min(rect.width, maxWidth);
		int height = Math.min(rect.height, maxHeight);

		Image image = null;

		int controlHandle = -1;
		int shellHandle = -1;

		try {
			if (control instanceof Shell) {
				shellHandle = shellHandleField.getInt(control);
				if (shellHandle != -1) {
					Integer result = (Integer) HIViewGetRootMethod.invoke(null, new Object[] { new Integer(shellHandle) });
					if (result != null) {
						controlHandle = result.intValue();
					}
				}
			} else {
				controlHandle = getIntHandle(control);
				shellHandle = shellHandleField.getInt(control.getShell());
			}

			int imageHandle = captureImage(controlHandle, shellHandle);

			if (imageHandle != 0) {
				// Create a temporary image using the captured image's handle
				Image tempImage = (Image) carbon_newMethod.invoke(null, new Object[] { control.getDisplay(), new Integer(SWT.BITMAP), new Integer(imageHandle), new Integer(0) });
				// image = Image.carbon_new(control.getDisplay(), SWT.BITMAP, imageHandle, 0);

				// Create the result image
				image = new Image(control.getDisplay(), width, height);

				// Manually copy because the image's data handle isn't available
				GC gc = new GC(tempImage);
				gc.copyArea(image, 0, 0);
				gc.dispose();

				// Dispose of the temporary image allocated in the native call
				tempImage.dispose();
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return image;
	}
}
