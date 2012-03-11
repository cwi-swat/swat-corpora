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
package org.eclipse.e4.xwt.tools.ui.designer.core.util.image;

import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTUtil;
import org.eclipse.e4.xwt.tools.ui.imagecapture.swt.ImageCapture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ImageCollector {
	static int i = 0;

	public static Shell activeShell(Control control) {
		Shell controlShell = control.getShell();
		Display display = control.getDisplay();
		Control focusControl = display.getFocusControl();
		if (focusControl != null && !focusControl.isDisposed()) {
			focusControl.getShell().open();
			return focusControl.getShell();
		}
		Shell shell = display.getActiveShell();
		if (shell != null) {
			return shell;
		}
		Shell[] shells = display.getShells();
		if (shells != null) {
			for (Shell topShell : shells) {
				if (topShell != controlShell) {
					topShell.open();
					return topShell;
				}
			}
		}
		return null;
	}

	public static void collectImage(Control control,
			ImageCollectedRunnable imageRunnable) {
		if (control == null || control.isDisposed() || imageRunnable == null) {
			return;
		}
		Rectangle bounds = control.getBounds();
		if (bounds.isEmpty()) {
			imageRunnable.imageNotCollected();
		} else {
			Image image = null;
			// If the toolBar is located on a CoolBar, the background was lost
			// by using print() method.
			if (control instanceof Shell || control instanceof ToolBar
					|| SWTUtil.IsGTK) {
				Shell shell = null;
				if (control instanceof Shell) {
					shell = (Shell) control;
				} else {
					shell = control.getShell();
					shell.pack();
					shell.setLocation(0, 0);
					while (control.getDisplay().readAndDispatch())
						;
				}
				bounds = control.getBounds();

				shell.setAlpha(0);
				if (SWTUtil.IsCocoa) {
					if (!shell.isVisible()) {
						shell.open();
					}
				} else {
					shell.moveBelow(null);
					if (!shell.isVisible()) {
						shell.setVisible(true);
					}
				}
				if (control instanceof Shell) {
					image = ImageCapture.getInstance().capture(control,
							bounds.width, bounds.height, true);
				} else {
					image = new Image(control.getDisplay(), bounds.width,
							bounds.height);
					GC gc = new GC(image);
					control.print(gc);
					gc.dispose(); 
				}
				shell.setVisible(false);
			} else {
				image = new Image(control.getDisplay(), bounds.width,
						bounds.height);
				GC gc = new GC(image);
				control.print(gc);
				gc.dispose();
			}
			if (image != null) {
				// saveImage(image, "/home/yyang/image" + (i++) + ".jpg");
				imageRunnable.imageCollected(image);
			} else {
				imageRunnable.imageNotCollected();
			}
		}
	}

	private static void saveImage(Image image, String path) {
		ImageLoader imageLoader = new ImageLoader();
		ImageData imageData = image.getImageData();
		imageLoader.data = new ImageData[] { imageData };
		imageLoader.save(path, SWT.IMAGE_JPEG);
	}

}
