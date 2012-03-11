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
package org.eclipse.e4.xwt.ui.editor.render;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ScrollBar;

public class CanvasManager {

	private Image originalImage;
	private Canvas canvas;
	private Image displayImage;
	/* The width and height value of canvas image. */
	public int imageWidth = -1, imageHeight = -1;
	/* The visible width and height of canvas. */
	private int visibleWidth, visibleHeight;
	private ManagerContext displayMC = new ManagerContext();
	private ManagerContext imageMC = new ManagerContext();

	public CanvasManager(Canvas canvas) {
		this.canvas = canvas;
		initialize();
	}

	private void initialize() {
		displayMC.gc = new GC(canvas);

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				handlePaint(event);
			}
		});

		canvas.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				handleResize();
			}
		});

		ScrollBar horizontal = getHorizontalBar();
		if (horizontal != null) {
			horizontal.setVisible(true);
			horizontal.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					scrollHorizontally((ScrollBar) event.widget);
				}
			});
		}
		ScrollBar vertical = getVerticalBar();
		if (vertical != null) {
			vertical.setVisible(true);
			vertical.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					scrollVertically((ScrollBar) event.widget);
				}
			});
		}

		canvas.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (displayMC.gc != null) {
					displayMC.gc.dispose();
				}
			}
		});
		handleResize();
	}

	/**
	 * Build or refresh the canvas with given image.
	 * 
	 * @param image
	 */
	public synchronized void setImage(Image image) {
		if (image != null) {
			originalImage = image;
			clearImage();
			createDisplayImage();
			canvas.redraw();
			canvas.getDisplay().update();
		}
	}

	private void createDisplayImage() {
		if (originalImage == null || originalImage.isDisposed()) {
			return;
		}
		Rectangle bounds = originalImage.getBounds();
		int width = bounds.width;
		int height = bounds.height;

		Rectangle rect = canvas.getBounds();

		imageWidth = Math.max(width + 17, rect.width);
		imageHeight = Math.max(height + 17, rect.height);

		displayImage = new Image(canvas.getDisplay(), imageWidth, imageHeight);

		imageMC.gc = new GC(displayImage);
		imageMC.gc.fillRectangle(0, 0, imageWidth, imageHeight);
		imageMC.gc.drawImage(originalImage, 0, 0, width, height, (imageWidth - width - 17) / 2, (imageHeight - height - 17) / 2, width, height);

		handleResize();
	}

	/**
	 * Clear old images on the current canvas.
	 */
	private void clearImage() {
		if (displayImage != null) {
			displayImage.dispose();
			displayImage = null;
		}
		if (imageMC.gc != null) {
			imageMC.gc.dispose();
			imageMC.gc = null;
		}
	}

	public void dispose() {
		canvas = null;
		clearImage();
		if (displayMC.gc != null) {
			displayMC.gc.dispose();
			displayMC = null;
		}
		if (displayImage != null) {
			displayImage.dispose();
		}
	}

	protected void scrollVertically(ScrollBar scrollBar) {
		if (displayImage == null)
			return;
		if (imageHeight > visibleHeight) {
			final int oldOffset = displayMC.yOffset;
			final int newOffset = Math.min(scrollBar.getSelection(), imageHeight - visibleHeight);
			if (oldOffset != newOffset) {
				canvas.update();
				displayMC.yOffset = newOffset;
				canvas.scroll(0, Math.max(oldOffset - newOffset, 0), 0, Math.max(newOffset - oldOffset, 0), visibleWidth, visibleHeight, false);
			}
		}
	}

	protected void scrollHorizontally(ScrollBar scrollBar) {
		if (displayImage == null)
			return;
		if (imageWidth > visibleWidth) {
			final int oldOffset = displayMC.xOffset;
			final int newOffset = Math.min(scrollBar.getSelection(), imageWidth - visibleWidth);
			if (oldOffset != newOffset) {
				canvas.update();
				displayMC.xOffset = newOffset;
				canvas.scroll(Math.max(oldOffset - newOffset, 0), 0, Math.max(newOffset - oldOffset, 0), 0, visibleWidth, visibleHeight, false);
			}
		}
	}

	protected void handleResize() {

		canvas.update();

		Rectangle visibleRect = canvas.getBounds();
		visibleWidth = visibleRect.width;
		visibleHeight = visibleRect.height;

		ScrollBar horizontal = getHorizontalBar();
		if (horizontal != null) {
			displayMC.xOffset = Math.min(horizontal.getSelection(), imageWidth - visibleWidth);
			if (imageWidth <= visibleWidth) {
				horizontal.setVisible(false);
				horizontal.setEnabled(false);
				horizontal.setSelection(0);
			} else {
				horizontal.setVisible(true);
				horizontal.setEnabled(true);
				horizontal.setValues(displayMC.xOffset, 0, imageWidth, visibleWidth, 8, visibleWidth);
			}
		}

		ScrollBar vertical = getVerticalBar();
		if (vertical != null) {
			displayMC.yOffset = Math.min(vertical.getSelection(), imageHeight - visibleHeight);
			if (imageHeight <= visibleHeight) {
				vertical.setEnabled(false);
				vertical.setVisible(false);
				vertical.setSelection(0);
			} else {
				vertical.setEnabled(true);
				vertical.setVisible(true);
				vertical.setValues(displayMC.yOffset, 0, imageHeight, visibleHeight, 8, visibleHeight);
			}
		}
	}

	private ScrollBar getVerticalBar() {
		ScrollBar vertical = canvas.getVerticalBar();
		if (vertical == null) {
			vertical = canvas.getParent().getVerticalBar();
		}
		return vertical;
	}

	private ScrollBar getHorizontalBar() {
		ScrollBar horizontal = canvas.getHorizontalBar();
		if (horizontal == null) {
			horizontal = canvas.getParent().getHorizontalBar();
		}
		return horizontal;
	}

	private void handlePaint(PaintEvent event) {
		if (displayImage == null) {
			return;
		}
		// srcX > 0 && srcY > 0
		int srcX = displayMC.xOffset + event.x;
		int srcY = displayMC.yOffset + event.y;
		srcX = srcX < 0 ? 0 : srcX;
		srcY = srcY < 0 ? 0 : srcY;
		Rectangle bounds = displayImage.getBounds();
		if ((bounds.width - srcX) < event.width || (bounds.height - srcY) < event.height) {
			createDisplayImage();
			handlePaint(event);
		} else {
			event.gc.drawImage(displayImage, srcX, srcY, event.width, event.height, event.x, event.y, event.width, event.height);
		}
	}

}
