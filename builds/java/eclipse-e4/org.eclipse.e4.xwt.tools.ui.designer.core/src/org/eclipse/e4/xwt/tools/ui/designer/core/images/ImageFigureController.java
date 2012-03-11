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
package org.eclipse.e4.xwt.tools.ui.designer.core.images;

import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ImageFigure;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ImageFigureController {

	private ImageFigure figure;
	private IImageNotifier imageNotifier;
	private Image image;

	private IImageListener imageListener = new IImageListener() {
		public void imageChanged(Image newImage) {
			setImage(newImage);
		}
	};

	public void setImageFigure(ImageFigure imageFigure) {
		this.figure = imageFigure;
	}

	/**
	 * @param notifier
	 *            the imageNotifier to set
	 */
	public void setImageNotifier(IImageNotifier notifier) {
		if (notifier != null) {
			imageNotifier = notifier;
			imageNotifier.addImageListener(imageListener);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					imageNotifier.refreshImage();
				}
			});
		}
	}

	public void deactivate() {
		if (imageNotifier != null) {
			imageNotifier.removeImageListener(imageListener);
		}
		if (figure != null) {
			figure.setImage(null);
		}
		if (image != null) {
			image.dispose();
		}
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Image newImage) {
		if (image != null) {
			image.dispose();
		}
		image = newImage;
		if (figure != null) {
			figure.setImage(image);
		}
	}
}
