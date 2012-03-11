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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.core.DesignerPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class SWTTools {

	public static Shell activeShell() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
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
				return topShell;
			}
		}
		return null;
	}

	public static Point getLocation(Widget widget) {
		if (widget == null || widget.isDisposed()) {
			return new Point(-1, -1);
		}
		if (widget instanceof Control) {
			Control control = (Control) widget;
			Point location = control.getLocation();
			if (control instanceof Shell) {
				location.x = 20;
				location.y = 20;
				return location;
			}
			Composite parent = control.getParent();
			if (parent instanceof Shell) {
				Point l = getOffset((Shell) parent);
				location.x = location.x + l.x;
				location.y = location.y + l.y;
			} else if (checkStyle(parent, SWT.BORDER)) {
				int borderWidth = parent.getBorderWidth();
				location.x += borderWidth;
				location.y += borderWidth;
			}
			return location;
		}
		return WidgetLocator.getLocation(widget);
	}

	public static Point getOffset(Scrollable scroll) {
		if (scroll == null || scroll.isDisposed()) {
			return new Point(0, 0);
		}
		Rectangle calced = scroll.computeTrim(0, 0, 0, 0);
		Point point = new Point(-calced.x, -calced.y);
		return point;
	}

	public static Point getSize(Control control) {
		if (control == null || control.isDisposed()) {
			return new Point(-1, -1);
		}
		Point size = control.getSize();
		return size;
	}

	public static Rectangle getBounds(Widget widget) {
		if (widget instanceof Control) {
			Control control = (Control) widget;
			Point l = getLocation(control);
			Point s = getSize(control);
			return new Rectangle(l.x, l.y, s.x, s.y);
		}
		return WidgetLocator.getBounds(widget, false);
	}

	public static Widget[] getChildren(Widget widget) {
		if (widget == null || widget.isDisposed()) {
			return new Widget[0];
		}
		List<Widget> children = new ArrayList<Widget>();
		if (widget instanceof Composite) {
			for (Control control : ((Composite) widget).getChildren()) {
				children.add(control);
			}
		}
		if (widget instanceof Decorations) {
			Menu menuBar = ((Decorations) widget).getMenuBar();
			if (menuBar != null) {
				children.add(menuBar);
			}
		}
		// For all items.
		{
			try {
				Method getItemsMethod = widget.getClass().getDeclaredMethod(
						"getItems");
				Object[] items = (Object[]) getItemsMethod.invoke(widget,
						new Object[0]);
				for (Object item : items) {
					if (!(item instanceof Widget)) {
						continue;// items of Combo
					}
					children.add((Widget) item);
				}
			} catch (NoSuchMethodException e) {
			} catch (Exception e) {
				DesignerPlugin.logInfo(e);
			}
		}
		// For controls of items.
		if (widget instanceof Item) {
			try {
				Method getControlMethod = widget.getClass().getDeclaredMethod(
						"getControl");
				Object control = getControlMethod.invoke(widget, new Object[0]);
				if (control != null) {
					children.add((Widget) control);
				}
			} catch (NoSuchMethodException e) {
			} catch (Exception e) {
				DesignerPlugin.logInfo(e);
			}
		}
		// For Context Menu and sub menu of MenuItem
		{
			try {
				Method getMenuMethod = widget.getClass().getDeclaredMethod(
						"getMenu");
				Object menu = getMenuMethod.invoke(widget, new Object[0]);
				if (menu != null) {
					children.add((Widget) menu);
				}
			} catch (NoSuchMethodException e) {
			} catch (Exception e) {
				DesignerPlugin.logInfo(e);
			}
		}
		{// getColumns
			try {
				Method getColumnsMethod = widget.getClass().getDeclaredMethod(
						"getColumns", new Class<?>[0]);
				Object[] columns = (Object[]) getColumnsMethod.invoke(widget,
						new Object[0]);
				for (Object col : columns) {
					children.add((Widget) col);
				}
			} catch (NoSuchMethodException e) {
			} catch (Exception e) {
				DesignerPlugin.logInfo(e);
			}
		}
		return children.toArray(new Widget[children.size()]);
	}

	public static boolean checkStyle(Widget widget, int style) {
		return widget != null && !widget.isDisposed()
				&& checkStyle(widget.getStyle(), style);
	}

	public static boolean checkStyle(int styles, int style) {
		return (styles & style) != 0;
	}

}
