/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt.targetvm.win32;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

/**
 * Image Capture for Win32 platforms.
 * 
 * @since 1.1.0
 * 
 *        yyang (yves.yang@soyatec.com): change to reflection calls on OS
 * 
 */
public class ImageCapture extends org.eclipse.e4.xwt.tools.ui.imagecapture.swt.ImageCapture {

	static Field Control_handler;
	static Field GC_handler;
	static Class<?> OleFrameClass;

	static Method GetParent;

	static Method SendMessage;

	static {
		try {
			OleFrameClass = Class.forName("org.eclipse.swt.ole.win32.OleFrame");

			Class<?> osClass = Class.forName("org.eclipse.swt.internal.win32.OS");
			GetParent = osClass.getDeclaredMethod("GetParent", int.class);

			Control_handler = Control.class.getField("handle");
			GC_handler = GC.class.getField("handle");
			GC_handler.setAccessible(true);
			
			SendMessage = osClass.getDeclaredMethod("SendMessage", int.class, int.class, int.class, int.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {
//		if (!(control instanceof Shell)) {
//			Rectangle bounds = control.getBounds();
//			Image image = new Image(control.getDisplay(), bounds.width, bounds.height);
//			GC gc = new GC(image);
//			control.print(gc);
//			gc.dispose();
//			return image;
//		}
		Image myImage = getImage(control, maxWidth, maxHeight);
		// We need to be able to handle right-to-left coordinates too. In that case the bounds rectangle will be reversed from what we
		// think. We think the origin is upper-left, but the origin is really upper-right. To get out of this thinking we will
		// instead convert all bounds to display bounds so that they will all be left-to-right.
		if (myImage != null) {
			// Get the images of all of the children
			if (includeChildren && control instanceof Composite) {
				Display display = control.getDisplay();
				Rectangle parentBounds = control.getParent() == null ? control.getBounds() : display.map(control.getParent(), null, control.getBounds());
				// Need to clip the bounds to the size of the image so we get just what we need.
				Rectangle imgBounds = myImage.getBounds();
				parentBounds.width = imgBounds.width;
				parentBounds.height = imgBounds.height;
				int parentRight = parentBounds.width + parentBounds.x;
				int parentBottom = parentBounds.height + parentBounds.y;
				Control[] children = ((Composite) control).getChildren();
				GC myImageGC = new GC(myImage);
				try {
					int i = children.length;
					while (--i >= 0) {
						Control child = children[i];
						// If the child is not visible then don't try and get its image
						// An example of where this would cause a problem is TabFolder where all the controls
						// for each page are children of the TabFolder, but only the visible one is being shown on the active page
						if (!child.isVisible())
							continue;
						Rectangle childBounds = display.map(control, null, child.getBounds());
						if (!parentBounds.intersects(childBounds))
							continue; // Child is completely outside parent.
						Image childImage = getImage(child, parentRight - childBounds.x, parentBottom - childBounds.y, true);
						if (childImage != null) {
							try {
								// Paint the child image on top of our one
								// Since the child bounds and parent bounds are both in display coors, the difference between
								// the two is the offset of the child from the parent.
								myImageGC.drawImage(childImage, childBounds.x - parentBounds.x, childBounds.y - parentBounds.y);
							} finally {
								childImage.dispose();
							}
						}
					}
				} finally {
					myImageGC.dispose();
				}
			}
		}
		return myImage;
	}

	/**
	 * Return the image of the argument. This includes the client and non-client area, but does not include any child controls. To get child control use {@link ImageCapture#getImage(Control, int, int, boolean)}.
	 * 
	 * @param aControl
	 * @param maxWidth
	 * @param maxHeight
	 * @return image or <code>null</code> if not valid for some reason. (Like not yet sized).
	 * 
	 * @since 1.1.0
	 */
	protected Image getImage(Control aControl, int maxWidth, int maxHeight) {

		Rectangle rect = aControl.getBounds();
		if (rect.width <= 0 || rect.height <= 0)
			return null;

		Image image = new Image(aControl.getDisplay(), Math.min(rect.width, maxWidth), Math.min(rect.height, maxHeight));
		int WM_PRINT = 0x0317;
		// int WM_PRINTCLIENT = 0x0318;
		// int PRF_CHECKVISIBLE = 0x00000001;
		int PRF_NONCLIENT = 0x00000002;
		int PRF_CLIENT = 0x00000004;
		int PRF_ERASEBKGND = 0x00000008;
		int PRF_CHILDREN = 0x00000010;
		// int PRF_OWNED = 0x00000020;
		int print_bits = PRF_NONCLIENT | PRF_CLIENT | PRF_ERASEBKGND;
		// This method does not print immediate children because the z-order doesn't work correctly and needs to be
		// dealt with separately, however Table's TableColumn widgets are children so must be handled differently
		boolean specialClass = aControl instanceof Table || aControl instanceof Browser || OleFrameClass.isInstance(aControl) || aControl instanceof CCombo;
		try {
			specialClass |= aControl instanceof Spinner;
		} catch (NoClassDefFoundError e) {
		} // might not be on 3.1 of SWT
		if (specialClass) {
			print_bits |= PRF_CHILDREN;
		}
		GC gc = new GC(image);

		// Need to handle cases where the GC font isn't automatically set by the control's image (e.g. CLabel)
		// see bug 98830 (https://bugs.eclipse.org/bugs/show_bug.cgi?id=98830)
		Font f = aControl.getFont();
		if (f != null)
			gc.setFont(f);

		try {
			int hwnd = Control_handler.getInt(aControl);
			if (aControl instanceof Tree) {
				int hwndParent = (Integer) GetParent.invoke(null, hwnd);
				if (hwndParent != Control_handler.getInt(aControl.getParent())) {
					hwnd = hwndParent;
					print_bits |= PRF_CHILDREN;
				}
			}
			Object handle = GC_handler.get(gc);
			SendMessage.invoke(null, hwnd, WM_PRINT, handle, print_bits);
		} catch (Exception e) {
			e.printStackTrace();
		}

		gc.dispose();
		return image;
	}
}
