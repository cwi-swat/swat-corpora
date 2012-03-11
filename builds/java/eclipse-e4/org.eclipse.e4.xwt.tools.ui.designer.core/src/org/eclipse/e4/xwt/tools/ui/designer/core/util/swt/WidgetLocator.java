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
package org.eclipse.e4.xwt.tools.ui.designer.core.util.swt;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Provides a means to find the coordinates of an SWT Widget in display-space,
 * given an SWT Display object.
 * 
 * @author jliu (jin.liu@soyatec.com)
 */
public class WidgetLocator {
	/**
	 * Helper method to convert a Rectangle, given in the coordinate system of
	 * the given Control's parent, to display-cordinates.
	 */
	public static Rectangle toDisplay(Rectangle bounds, Control control) {

		// If it's a Shell or has no parent then the bounds are already
		// display-relative.
		if (control instanceof Shell || control.getParent() == null)
			return bounds;

		// Convert from parent-relative to display-relative.
		Point point = control.getParent().toDisplay(bounds.x, bounds.y);
		return new Rectangle(point.x, point.y, bounds.width, bounds.height);
	}

	public static Rectangle getBounds(Widget w, boolean toDisplay) {
		if (w instanceof Control) {
			Control control = (Control) w;
			Rectangle r = control.getBounds();
			if (toDisplay) {
				return toDisplay(r, control);
			}
			Composite parent = control.getParent();
			if (parent instanceof Scrollable && !(parent instanceof Group)
					&& !(parent instanceof TabFolder)
					&& !(parent instanceof CTabFolder)) {
				Rectangle calced = parent.computeTrim(0, 0, 0, 0);
				r.x += (- calced.x);
				r.y += (- calced.y);
				if (parent instanceof Shell && SWTUtil.IsWindows) {
					Shell shell = (Shell) parent;
					Menu menu = shell.getMenuBar();
					// Bug 300170 - Shell computeTrim returns a wrong result
					if (menu != null && menu.getItemCount() == 0) {
						r.y -= 19;						
					}
				}
			}
			else if (SWTUtil.IsCocoa) {
				if (parent instanceof Group) {
					Rectangle calced = parent.computeTrim(0, 0, 0, 0);
					r.x += - calced.x;
					r.y += calced.height + calced.y;
				}
			}
			return r;
		}
		if (w instanceof Menu) {
			Menu menu = (Menu) w;
			menu.getDisplay().update();
			return SWTWorkarounds.getBounds(menu);
		}
		if (w instanceof CTabItem) {
			return SWTWorkarounds.getBounds((CTabItem) w);
		}
		if (w instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) w;
			Menu parent = menuItem.getParent();
			Rectangle r = SWTWorkarounds.getBounds(parent);
			Rectangle bounds = SWTWorkarounds.getBounds((MenuItem) w);
			if (!r.isEmpty() && bounds.isEmpty()) {
				bounds = getBounds(menuItem);
			}
			if (bounds.x >= r.x) {
				bounds.x = bounds.x - r.x;
			}
			if (bounds.y >= r.y) {
				bounds.y = bounds.y - r.y;
			}
			return bounds;
		}
		if (w instanceof TabItem) {
			return SWTWorkarounds.getBounds((TabItem) w);
		}
		if (w instanceof TableColumn) {
			return SWTWorkarounds.getBounds((TableColumn) w);
		}
		if (w instanceof TreeColumn) {
			return SWTWorkarounds.getBounds((TreeColumn) w);
		}
		if (w instanceof ScrollBar) {
			return SWTWorkarounds.getBounds((ScrollBar) w);
		}
		if (w instanceof ToolItem) {
			return SWTWorkarounds.getBounds((ToolItem) w);
		}
		if (w instanceof CoolItem) {
			return SWTWorkarounds.getBounds((CoolItem) w);
		}
		if (w instanceof TreeItem) {
			return SWTWorkarounds.getBounds((TreeItem) w);
		}
		if (w instanceof TableItem) {
			return SWTWorkarounds.getBounds((TableItem) w);
		}
		if (w instanceof ExpandItem) {
			return SWTWorkarounds.getBounds((ExpandItem) w);
		}
		return new Rectangle(0, 0, 0, 0);
	}

	private static Rectangle getBounds(MenuItem menuItem) {
		if (menuItem == null || menuItem.isDisposed()) {
			return new Rectangle(0, 0, 0, 0);
		}
		int x = 0, y = 0, width = 0, height = 19;
		if ((SWT.SEPARATOR & menuItem.getStyle()) != 0) {
			height = 1;
		} else {
			String text = menuItem.getText();
			Dimension textExtents = FigureUtilities.getTextExtents(text,
					Display.getCurrent().getSystemFont());
			width = textExtents.width + 12;
			if (menuItem.getImage() != null) {
				width += 20;
			}
		}
		Menu parent = menuItem.getParent();
		int index = parent.indexOf(menuItem);
		for (int i = 0; i < index; i++) {
			if (((i - 1) >= 0)) {
				MenuItem prev = parent.getItem(i - 1);
				Rectangle r = getBounds(prev);
				if ((SWT.BAR & parent.getStyle()) != 0) {
					x = r.x + r.width;
					y = r.y;
				} else {
					y = r.y + r.height;
				}
			}
		}
		return new Rectangle(x, y, width, height);
	}

	public static Point getLocation(Widget widget) {
		Rectangle bounds = getBounds(widget, false);
		if (bounds != null) {
			return new Point(bounds.x, bounds.y);
		}
		return null;
	}
}
