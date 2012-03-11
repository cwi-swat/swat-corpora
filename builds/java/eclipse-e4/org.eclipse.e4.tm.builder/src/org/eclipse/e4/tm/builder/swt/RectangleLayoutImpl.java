/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public final class RectangleLayoutImpl extends Layout {

	public RectangleLayoutImpl () {
	}

	public Rectangle defaultLayoutData = new Rectangle(0, 0, SWT.DEFAULT, SWT.DEFAULT);
	
	protected void layout(Composite composite, boolean flushCache) {
		layout(composite, SWT.DEFAULT, SWT.DEFAULT, true, false);
	}
	
	protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
		return layout(composite, wHint, hHint, false, true);
	}

	private Point layout(Composite composite, int wHint, int hHint, boolean layout, boolean computeSize) {
		Rectangle clientArea = composite.getClientArea();
		Rectangle mergedRect = computeSize ? new Rectangle(clientArea.x, clientArea.y, wHint, hHint) : null;
		Control [] children = composite.getChildren ();
		for (int i = 0; i < children.length; i++) {
			Control control = children[i];
			Object o = control.getLayoutData();
			int x = defaultLayoutData.x, y = defaultLayoutData.y, w = defaultLayoutData.width, h = defaultLayoutData.height;
			if (o instanceof Rectangle) {
				Rectangle rect = (Rectangle)o;
				x = rect.x; y = rect.y; w = rect.width; h = rect.height;
			} else if (o instanceof Point) {
				Point p = (Point)o;
				w = p.x; h = p.y;
			}
			Point size = control.computeSize(w > 0 ? w : -1, h > 0 ? h : -1);
			if (layout) {
				control.setBounds(clientArea.x + x, clientArea.y + y, size.x, size.y);
			}
			if (mergedRect != null) {
				mergedRect.add(new Rectangle(x, y, size.x, size.y));
			}
		}
		return (mergedRect != null ? new Point(mergedRect.width, mergedRect.height) : null);
	}

	protected boolean flushCache (Control control) {
		return true;
	}

	public String toString () {
		String string = " RectangleLayout {";
		string += "}";
		return string;
	}
}
