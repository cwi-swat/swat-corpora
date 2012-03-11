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
package org.eclipse.e4.xwt.vex.swt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class AnimatedImage extends CLabel {
	protected InputStream imageFile;
	protected boolean stop = true;
	protected boolean useGIFBackground = false;
	protected int horizontalAlignment = SWT.LEFT;
	protected int verticalAlignment = SWT.TOP;

	protected AnimationRunnable animateThread = new AnimationRunnable();

	class AnimationRunnable implements Runnable {
		protected ImageLoader loader;
		protected Image image;
		protected ImageData[] imageDataArray;
		protected GC targetGC;
		protected Image offScreenImage;
		protected GC offScreenImageGC;
		protected int imageDataIndex = 0;
		protected int repeatCount;

		public void run() {
			if (isDisposed() || stop) {
				clearStop();
				return;
			}
			Display display = getDisplay();
			Color background = getBackground();
			if (loader == null) {
				setText("");
				loader = new ImageLoader();
				imageDataArray = loader.load(imageFile);
				targetGC = new GC(AnimatedImage.this);
				/*
				 * Create an off-screen image to draw on, and fill it with the shell background.
				 */
				offScreenImage = new Image(display, loader.logicalScreenWidth, loader.logicalScreenHeight);
				offScreenImageGC = new GC(offScreenImage);
				offScreenImageGC.setBackground(background);
				offScreenImageGC.fillRectangle(0, 0, loader.logicalScreenWidth, loader.logicalScreenHeight);

				/*
				 * Create the first image and draw it on the off-screen image.
				 */
				imageDataIndex = 0;
				ImageData imageData = imageDataArray[imageDataIndex];
				if (image != null && !image.isDisposed())
					image.dispose();
				image = new Image(display, imageData);
				offScreenImageGC.drawImage(image, 0, 0, imageData.width, imageData.height, imageData.x, imageData.y, imageData.width, imageData.height);
				repeatCount = loader.repeatCount;
			}

			try {
				/*
				 * Now loop through the images, creating and drawing each one on the off-screen image before drawing it on the shell.
				 */
				ImageData imageData = imageDataArray[imageDataIndex];
				if (loader.repeatCount == 0 || repeatCount > 0) {
					switch (imageData.disposalMethod) {
					case SWT.DM_FILL_BACKGROUND:
						/*
						 * Fill with the background color before drawing.
						 */
						Color bgColor = null;
						if (useGIFBackground && loader.backgroundPixel != -1) {
							bgColor = new Color(display, imageData.palette.getRGB(loader.backgroundPixel));
						}
						offScreenImageGC.setBackground(bgColor != null ? bgColor : background);
						offScreenImageGC.fillRectangle(imageData.x, imageData.y, imageData.width, imageData.height);
						if (bgColor != null)
							bgColor.dispose();
						break;
					case SWT.DM_FILL_PREVIOUS:
						/*
						 * Restore the previous image before drawing.
						 */
						offScreenImageGC.drawImage(image, 0, 0, imageData.width, imageData.height, imageData.x, imageData.y, imageData.width, imageData.height);
						break;
					}

					imageDataIndex = (imageDataIndex + 1) % imageDataArray.length;
					imageData = imageDataArray[imageDataIndex];
					image.dispose();
					image = new Image(display, imageData);
					offScreenImageGC.drawImage(image, 0, 0, imageData.width, imageData.height, imageData.x, imageData.y, imageData.width, imageData.height);

					/* Draw the off-screen image to the composite. */
					Rectangle container = getBounds();
					Rectangle imageBound = offScreenImage.getBounds();
					int x = 0;
					int y = 0;
					switch (horizontalAlignment) {
					case SWT.LEFT:
						break;
					case SWT.RIGHT:
						x = (container.width - imageBound.width);
						break;
					case SWT.CENTER:
						x = (container.width - imageBound.width) / 2;
						break;
					default:
						throw new IllegalStateException("HorizontalAlignment " + horizontalAlignment);
					}
					switch (verticalAlignment) {
					case SWT.TOP:
						break;
					case SWT.CENTER:
						y = (container.height - imageBound.height) / 2;
						break;
					case SWT.BOTTOM:
						y = (container.height - imageBound.height);
						break;
					default:
						throw new IllegalStateException("VerticalAlignment " + verticalAlignment);
					}

					if (x < 0) {
						x = 0;
					}
					if (y < 0) {
						y = 0;
					}

					targetGC.drawImage(offScreenImage, x, y);

					/*
					 * Sleep for the specified delay time (adding commonly-used slow-down fudge factors).
					 */
					{
						int ms = imageData.delayTime * 10;
						if (ms < 20)
							ms += 30;
						if (ms < 30)
							ms += 10;
						getDisplay().timerExec(ms, this);
					}

					/*
					 * If we have just drawn the last image, decrement the repeat count and start again.
					 */
					if (imageDataIndex == imageDataArray.length - 1)
						repeatCount--;
				} else {
					clearStop();
				}
			} catch (SWTException ex) {
				System.out.println("There was an error animating the GIF");
			} finally {
			}
		}

		protected void clearStop() {
			if (offScreenImage != null && !offScreenImage.isDisposed()) {
				try {
					offScreenImage.dispose();
				} catch (Exception e) {
				}
			}
			if (offScreenImageGC != null && !offScreenImageGC.isDisposed()) {
				try {
					offScreenImageGC.dispose();
				} catch (Exception e) {
				}
			}
			if (image != null && !image.isDisposed()) {
				try {
					image.dispose();
				} catch (Exception e) {
				}
			}
			if (targetGC != null && !targetGC.isDisposed()) {
				try {
					targetGC.dispose();
				} catch (Exception e) {
				}
			}
			targetGC = null;
			image = null;
			offScreenImageGC = null;
			offScreenImage = null;
			loader = null;
			imageDataArray = null;
			imageDataIndex = 0;
			repeatCount = 0;
			stop = true;
		}
	};

	public AnimatedImage(Composite parent, int style) {
		super(parent, style);
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public int getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(int verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public void setImageFile(URL imageFile) throws IOException {
		if (!stop) {
			throw new IllegalStateException("Animation is running");
		}

		this.imageFile = imageFile.openStream();
		if (imageFile != null) {
			start();
		}
	}

	public void start() {
		stop = false;
		getDisplay().asyncExec(animateThread);
	}

	public void stop() {
		stop = true;
	}

	@Override
	public void dispose() {
		stop = true;
		super.dispose();
	}
}
