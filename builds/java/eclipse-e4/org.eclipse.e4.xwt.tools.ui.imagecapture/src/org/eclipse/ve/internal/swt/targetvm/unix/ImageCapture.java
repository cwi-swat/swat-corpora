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
 *  $Revision: 1.3 $  $Date: 2009/12/22 15:40:54 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.unix;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * GTK version of Image Capture
 * 
 * @since 1.0.0
 */
public class ImageCapture extends org.eclipse.e4.xwt.tools.ui.imagecapture.swt.ImageCapture {
	static Field Menu_handler;
	static Field Control_handler;

	static {
		try {
			Menu_handler = Menu.class.getField("handle");
			Control_handler = Control.class.getField("handle");

			System.loadLibrary("swt-gtk-print"); //$NON-NLS-1$
		} catch (Exception error) {
			error.printStackTrace();
		} catch (UnsatisfiedLinkError error) {
			error.printStackTrace();
		}
	}

	static final int OBSCURED = 1 << 6; // Must be the same value as Widget.OBSCURED
	static final String FIELD_STATE_NAME = "state"; //$NON-NLS-1$

	private native int[] getPixels(int handle, int includeChildren, int maxWidth, int maxHeight, int arg4);

	protected Point getTopLeftOfClientarea(Decorations decorations) {
		Point trim = decorations.toControl(decorations.getLocation());
		trim.x = -trim.x;
		trim.y = -trim.y;
		if (decorations.getMenuBar() != null) {
			Menu menu = decorations.getMenuBar();
			try {
				Class<?> osClass = Class.forName("org.eclipse.swt.internal.gtk.OS"); //$NON-NLS-1$
				Method method = osClass.getMethod("GTK_WIDGET_HEIGHT", new Class[] { int.class }); //$NON-NLS-1$
				Object ret = method.invoke(menu, new Object[] { Menu_handler.getInt(menu) });
				if (ret != null) {
					int menuBarHeight = ((Integer) ret).intValue();
					trim.y -= menuBarHeight;
				}
			} catch (Throwable t) {
			}
		}
		return new Point(trim.x, trim.y);
	}

	protected Image getImageOfControl(Control control, int includeChildren, int maxWidth, int maxHeight) {
		Image image = null;
		if (control instanceof Shell) {
			Shell shell = (Shell) control;
			int handle = readIntFieldValue(shell.getClass(), shell, "shellHandle"); //$NON-NLS-1$
			if (handle > 0) {
				image = getImageOfHandle(handle, shell.getDisplay(), includeChildren, maxWidth, maxHeight);
			}
		}
		try {
			if (image == null) {
				image = getImageOfHandle(Control_handler.getInt(control), control.getDisplay(), includeChildren, maxWidth, maxHeight);
			}
			if (control instanceof Decorations) {
				Decorations decorations = (Decorations) control;
				Rectangle shellBounds = decorations.getBounds();
				Point topLeft = getTopLeftOfClientarea(decorations);
				Image realShellImage = new Image(decorations.getDisplay(), shellBounds.width, shellBounds.height);
				Image origImage = image;
				try {
					simulateDecoration(decorations, realShellImage, decorations.getBounds(), decorations.getClientArea(), topLeft);
					GC gc = new GC(realShellImage);
					gc.drawImage(image, topLeft.x, topLeft.y);
					DisposeUtil.dispose(gc);
					image = realShellImage;
				} finally {
					origImage.dispose();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	protected Image getImageOfHandle(int handle, Display display, int includeChildren, int maxWidth, int maxHeight) {
		int[] tcData = getPixels(handle, includeChildren, maxWidth, maxHeight, 0);
		int depth = display.getDepth();
		if (depth == 15)
			depth = 16; // SWT cant handle depth of 15. Similar to 16
		if (depth > 24)
			depth = 24;
		if (tcData != null) {
			int tcWidth = tcData[0];
			int tcHeight = tcData[1];
			int type = tcData[2];
			if (type == 1) {
				// Direct RGB values
				int red_mask = tcData[3] == -1 ? 0x00FF : tcData[3];
				int green_mask = tcData[4] == -1 ? 0x00FF00 : tcData[4];
				int blue_mask = tcData[5] == -1 ? 0x00FF0000 : tcData[5];
				// System.err.println("Masks: "+Integer.toHexString(red_mask)+","+Integer.toHexString(green_mask)+","+Integer.toHexString(blue_mask));
				int[] tcPixels = new int[tcData.length - 6];
				System.arraycopy(tcData, 6, tcPixels, 0, tcPixels.length);
				ImageData tcImageData = new ImageData(tcWidth, tcHeight, depth, new PaletteData(red_mask, green_mask, blue_mask));
				tcImageData.setPixels(0, 0, tcPixels.length, tcPixels, 0);
				Image tcImage = new Image(display, tcImageData);
				return tcImage;
			} else if (type == 2) {
				// Indexed values
				int numColors = tcData[3];
				RGB[] rgb = new RGB[numColors];
				// System.err.println("### Num colors = "+numColors);
				for (int colCount = 0; colCount < numColors; colCount++) {
					int r = tcData[4 + (colCount * 3) + 0];
					int g = tcData[4 + (colCount * 3) + 1];
					int b = tcData[4 + (colCount * 3) + 2];
					rgb[colCount] = new RGB(r, g, b);
				}
				PaletteData pd = new PaletteData(rgb);
				ImageData id = new ImageData(tcWidth, tcHeight, depth, pd);
				int offset = (4 + (rgb.length * 3));
				int pixels[] = new int[tcData.length - offset];
				System.arraycopy(tcData, offset, pixels, 0, pixels.length);
				id.setPixels(0, 0, pixels.length, pixels, 0);
				Image tcImage = new Image(display, id);
				// System.err.println("### returning image");
				return tcImage;
			} else {
				System.err.println("JNI Returned unknown image type"); //$NON-NLS-1$
			}
		}
		return null;
	}

	/**
	 * @param decoration
	 * @param realShellImage
	 * @param bounds
	 * @param clientArea
	 * @param topLeft
	 * 
	 * @since 1.0.0
	 */
	private void simulateDecoration(Decorations decoration, Image realShellImage, Rectangle bounds, Rectangle clientArea, Point topLeft) {
		GC gc = new GC(realShellImage);
		try {
			gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(0, 0, bounds.width, bounds.height);
			gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
			gc.drawRectangle(topLeft.x - 1, topLeft.y - 1, clientArea.width + 2, clientArea.height + 2);

			// little squares at bottom corners
			gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
			gc.fillRectangle(0, bounds.height - topLeft.y, topLeft.y, bounds.height);
			gc.fillRectangle(bounds.width - topLeft.y, bounds.height - topLeft.y, bounds.width, bounds.height);

			// title bar
			if ((decoration.getStyle() & (SWT.TITLE | SWT.CLOSE | SWT.MAX | SWT.MIN)) != 0 && topLeft.y > 2) {
				int barHeight = topLeft.y - 2;
				// There will be a title bar - draw the text
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
				gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
				gc.fillGradientRectangle(0, 0, bounds.width, barHeight, false);
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
				gc.drawText(decoration.getText(), topLeft.y, 2, true);
				if (decoration.getImage() != null && !decoration.getImage().isDisposed()) {
					Rectangle imageBounds = decoration.getImage().getBounds();
					if (imageBounds.height <= barHeight) {
						gc.drawImage(decoration.getImage(), 0, 0);
					} else {
						ImageData imageData = decoration.getImage().getImageData();
						double factor = (double) barHeight / (double) imageBounds.height;
						int newWidth = (int) (imageBounds.width * factor);
						imageData = imageData.scaledTo(newWidth, barHeight);
						Image newImage = new Image(decoration.getDisplay(), imageData);
						gc.drawImage(newImage, 0, 0);
						newImage.dispose();
					}
				}

				int rightx = bounds.width - topLeft.y;

				// title bar buttons
				if ((decoration.getStyle() & SWT.CLOSE) != 0) {
					gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					gc.fillRectangle(rightx, 0, topLeft.y, topLeft.y);
					gc.setLineWidth(1);
					gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
					gc.drawRectangle(rightx, 0, topLeft.y, topLeft.y);
					int lineWidth = topLeft.y / 6;
					if (lineWidth < 1)
						lineWidth = 1;
					gc.setLineWidth(lineWidth);
					lineWidth = lineWidth * 2;
					gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER));
					gc.drawLine(rightx + lineWidth, lineWidth, rightx + topLeft.y - lineWidth, topLeft.y - lineWidth);
					gc.drawLine(rightx + lineWidth, topLeft.y - lineWidth, rightx + topLeft.y - lineWidth, lineWidth);
					rightx -= topLeft.y;
				}
				if ((decoration.getStyle() & SWT.MAX) != 0) {
					gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					gc.fillRectangle(rightx, 0, topLeft.y, topLeft.y);
					gc.setLineWidth(1);
					gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
					gc.drawRectangle(rightx, 0, topLeft.y, topLeft.y);
					int lineWidth = topLeft.y / 6;
					if (lineWidth < 1)
						lineWidth = 1;
					gc.setLineWidth(lineWidth);
					lineWidth = lineWidth * 2;
					gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER));
					gc.drawRectangle(rightx + lineWidth, lineWidth, topLeft.y - (2 * lineWidth), topLeft.y - (2 * lineWidth));
					rightx -= topLeft.y;
				}
				if ((decoration.getStyle() & SWT.MIN) != 0) {
					gc.setLineWidth(1);
					gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					gc.fillRectangle(rightx, 0, topLeft.y - 1, topLeft.y - 1);
					gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
					gc.drawRectangle(rightx, 0, topLeft.y - 1, topLeft.y - 1);
					int lineWidth = topLeft.y / 6;
					if (lineWidth < 1)
						lineWidth = 1;
					gc.setLineWidth(lineWidth);
					lineWidth = lineWidth * 2;
					gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER));
					gc.drawLine(rightx + lineWidth, topLeft.y - lineWidth, rightx + topLeft.y - lineWidth, topLeft.y - lineWidth);
					rightx -= topLeft.y;
				}

				gc.setLineWidth(1);
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
				gc.drawLine(0, topLeft.y - 1, bounds.width, topLeft.y - 1);
			}
		} finally {
			gc.dispose();
		}
	}

	protected Map<Class<?>, Map<String, Object>> fieldAccessors = new HashMap<Class<?>, Map<String, Object>>(); // Map of Class->fieldName->field reflect

	/**
	 * @param object
	 * @param fieldName
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private int readIntFieldValue(Class<?> klass, Object object, String fieldName) {
		try {
			Field field = getField(klass, fieldName);
			return field != null ? field.getInt(object) : 0;
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return -1;
	}

	private static final Object NO_FIELD = new Object();

	private Field getField(Class<?> klass, String fieldName) {
		Map<String, Object> nameToField = fieldAccessors.get(klass);
		if (nameToField == null) {
			fieldAccessors.put(klass, nameToField = new HashMap<String, Object>());
		}
		Object field = nameToField.get(fieldName);
		if (field == null) {
			try {
				field = klass.getDeclaredField(fieldName);
				((Field) field).setAccessible(true);
			} catch (SecurityException e) {
				field = NO_FIELD;
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				field = NO_FIELD;
				e.printStackTrace();
			}
			nameToField.put(fieldName, field);
		}
		return (Field) (field != NO_FIELD ? field : null);
	}

	/**
	 * @param object
	 * @param fieldName
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private void writeIntFieldValue(Class<?> klass, Object object, String fieldName, int newInt) {
		try {
			Field field = getField(klass, fieldName);
			field.setInt(object, newInt);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}

	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {
		int ic = includeChildren ? 1 : 0;
		Map<Control, Integer> map = new HashMap<Control, Integer>();
		changeObscured(control, map, false);
		Image image = null;
		if (!(control instanceof Shell)) {
			Rectangle bounds = control.getBounds();
			image = new Image(control.getDisplay(), bounds.width, bounds.height);
			GC gc = new GC(image);
			control.print(gc);
			gc.dispose();
			return image;
		}

		try {
			image = getImageOfControl(control, ic, maxWidth, maxHeight);
		} finally {
			changeObscured(control, map, true);
		}
		return image;
	}

	/**
	 * @param control
	 * @param map
	 * 
	 * @since 1.0.0
	 */
	private void changeObscured(Control control, Map<Control, Integer> map, boolean on) {
		if (on) {
			// restoring the obscured flags
			if (map.containsKey(control)) {
				// control had obscured flag changed - reset it
				Integer originalValue = (Integer) map.get(control);
				writeIntFieldValue(Widget.class, control, FIELD_STATE_NAME, originalValue.intValue());
			}
		} else {
			// disabling the obscure flags
			int stateValue = readIntFieldValue(Widget.class, control, FIELD_STATE_NAME);
			if ((stateValue & OBSCURED) != 0) {
				// obscured - disable flag and remember
				map.put(control, new Integer(stateValue));
				stateValue &= ~OBSCURED;
				writeIntFieldValue(Widget.class, control, FIELD_STATE_NAME, stateValue);
			}
		}
		if (control instanceof CCombo) {
			// CCombo has a text field whose OBSCURED field needs to be changed
			CCombo ccombo = (CCombo) control;
			Object val = readObjectFieldValue(CCombo.class, ccombo, "text"); //$NON-NLS-1$
			if (val instanceof Text) {
				Text text = (Text) val;
				changeObscured(text, map, on);
			}
		} else if (control instanceof Composite) {
			Composite composite = (Composite) control;
			Control[] children = composite.getChildren();
			for (int cc = 0; children != null && cc < children.length; cc++) {
				changeObscured(children[cc], map, on);
			}
		}
	}

	/**
	 * @param object
	 * @param fieldName
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	private Object readObjectFieldValue(Class<?> klass, Object object, String fieldName) {
		try {
			Field field = klass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return null;
	}

}
