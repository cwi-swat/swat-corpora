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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.e4.xwt.tools.ui.designer.core.DesignerPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * This class adapts SWT to Abbot, e.g. where SWT methods are not public.
 * 
 * @author jliu (jin.liu@soyatec.com)
 */
public class SWTWorkarounds {
	static final Rectangle EMPTY = new Rectangle(0, 0, 0, 0);

	/*************************** COMMON *****************************/
	public static Rectangle getBounds(Object object) {		
		if (SWTUtil.IsWindows) {
			if (object instanceof TabItem) {
				return win32_getBounds((TabItem)object);
			}
			if (object instanceof TableColumn) {
				return win32_getBounds((TableColumn)object);
			}
			if (object instanceof TreeColumn) {
				return win32_getBounds((TreeColumn)object);
			}
		}
		if (SWTUtil.IsGTK) {
			if (object instanceof TabItem) {
				return gtk_getBounds((TabItem)object);
			}
			if (object instanceof TableColumn) {
				return gtk_getBounds((TableColumn)object);
			}
			if (object instanceof TreeColumn) {
				return gtk_getBounds((TreeColumn)object);
			}
		}
		if (SWTUtil.IsMotif) {
			if (object instanceof TabItem) {
				return motif_getBounds((TabItem)object);
			}
			if (object instanceof TableColumn) {
				return motif_getBounds((TableColumn)object);
			}
			if (object instanceof TreeColumn) {
				return motif_getBounds((TreeColumn)object);
			}
		}
		if (SWTUtil.IsCarbon) {
			if (object instanceof TabItem) {
				return carbon_getBounds((TabItem)object);
			}
			if (object instanceof TableColumn) {
				return carbon_getBounds((TableColumn)object);
			}
			if (object instanceof TreeColumn) {
				return carbon_getBounds((TreeColumn)object);
			}
			if (object instanceof Menu || object instanceof MenuItem) {
				return EMPTY;
			}
		}
		if (SWTUtil.IsCocoa) {
			if (object instanceof TabItem) {
				return cocoa_getBounds((TabItem)object);
			}
			if (object instanceof TableColumn) {
				return cocoa_getBounds((TableColumn)object);
			}
			if (object instanceof TreeColumn) {
				return cocoa_getBounds((TreeColumn)object);
			}
			if (object instanceof Menu || object instanceof MenuItem) {
				return EMPTY;
			}
		}
		try {
			Method method = object.getClass().getDeclaredMethod("getBounds");
			method.setAccessible(true);
			return (Rectangle) method.invoke(object);
		} catch (Throwable e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static Rectangle getBounds(MenuItem menuItem) {
		Rectangle itemRect = getBounds((Object) menuItem);
		Rectangle menuRect = getBounds(menuItem.getParent());
		if ((menuItem.getParent().getStyle() & SWT.RIGHT_TO_LEFT) != 0) {
			itemRect.x = menuRect.x + menuRect.width - itemRect.width - itemRect.x;
		} else {
			itemRect.x += menuRect.x;
		}
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=38436#c143
		itemRect.y += menuRect.y;
		return itemRect;
	}

	public static Rectangle getBounds(Menu menu) {
		Rectangle result = getBounds((Object) menu);
		Decorations parent = menu.getParent();
		if (parent == null || parent.isDisposed()) {
			return result;
		}
		Rectangle bounds = parent.getBounds();
		if (!result.isEmpty() && parent != null) {
			return new Rectangle(result.x - bounds.x, result.y - bounds.y,
					result.width, result.height);
		} else if (SWTTools.checkStyle(menu, SWT.BAR)) {
			if (menu.getItemCount() == 0) {
				Point offset = SWTTools.getOffset(parent);
				return new Rectangle(offset.x, offset.y - 19, bounds.width - offset.x * 2, 1);				
			}
			int y = SWTTools.getOffset(parent).y;
			int x = SWTTools.getOffset(parent).x;
			return new Rectangle(x, y - 19, bounds.width - x * 2, 19);
		}
		return result;
	}

	public static Rectangle getBounds(ScrollBar bar) {

		// Set x,y to the location of the bar relative to its parent.
		Scrollable parent = bar.getParent();
		Point parentSize = parent.getSize();
		Point size = bar.getSize();
		int x, y;
		if ((bar.getStyle() & SWT.HORIZONTAL) != 0) {
			x = 0;
			y = parentSize.y - size.y;
		} else {
			x = parentSize.x - size.x;
			y = 0;
		}

		// Return the bar's bounds in display coordinates.
		return bar.getDisplay().map(parent, null, x, y, size.x, size.y);
	}

	/*************************** WIN32 *****************************/
	static int SendMessage(int hWnd, int Msg, int wParam, int[] lParam) {
		int result = 0;
		try {
			Class<?> clazz = Class.forName("org.eclipse.swt.internal.win32.OS");
			Class<?>[] params = new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, lParam.getClass(), };
			Method method = clazz.getMethod("SendMessage", params);
			Object[] args = new Object[] { Integer.valueOf(hWnd),
					Integer.valueOf(Msg), Integer.valueOf(wParam), lParam, };
			result = ((Integer) method.invoke(clazz, args)).intValue();
		} catch (Throwable e) {
			throw new UnsupportedOperationException(e);
		}
		return result;
	}

	static Rectangle win32_getBounds(TabItem tabItem) {
		TabFolder parent = tabItem.getParent();
		int index = parent.indexOf(tabItem);
		if (index != -1) {
			int[] rect = new int[4];
			int width;
			int height;
			try {
				Field field = parent.getClass().getField("handle");
				SendMessage(field.getInt(parent), /* TCM_GETITEMRECT */0x130a, index, rect);
				width = rect[2] - rect[0];
				height = rect[3] - rect[1];
				Rectangle bounds = new Rectangle(rect[0], rect[1], width, height);
				return bounds;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Rectangle(0, 0, 0, 0);
		// return tabItem.getDisplay().map(tabItem.getParent(), null, bounds);
	}

	static Rectangle win32_getBounds(TableColumn tableColumn) {
		Table parent = tableColumn.getParent();
		int index = parent.indexOf(tableColumn);
		if (index != -1) {
			try {
				Field field = parent.getClass().getField("handle");
				int hwndHeader = SendMessage(field.getInt(parent), /* LVM_GETHEADER */0x101f, 0, new int[0]);
				int[] rect = new int[4];
				SendMessage(hwndHeader, /* HDM_GETITEMRECT */0x1200 + 7, index, rect);
				int width = rect[2] - rect[0];
				int height = rect[3] - rect[1];
				Rectangle bounds = new Rectangle(rect[0], rect[1], width, height);
				// TODO - coordinate system may change when the API is added to SWT
				return bounds;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Rectangle(0, 0, 0, 0);
		// return tableColumn.getDisplay().map(parent, null, bounds);
	}

	static Rectangle win32_getBounds(TreeColumn treeColumn) {
		Tree parent = treeColumn.getParent();
		int index = parent.indexOf(treeColumn);
		if (index == -1)
			return new Rectangle(0, 0, 0, 0);
		int hwndHeader = 0;
		try {
			Field getHeaderField = parent.getClass().getDeclaredField("hwndHeader");
			getHeaderField.setAccessible(true);
			hwndHeader = (Integer) getHeaderField.get(parent);
		} catch (Exception e) {
			DesignerPlugin.logInfo(e);
		}
		if (hwndHeader == 0) {
			return new Rectangle(0, 0, 0, 0);
		}
		int[] rect = new int[4];
		SendMessage(hwndHeader, /* HDM_GETITEMRECT */0x1200 + 7, index, rect);
		int width = rect[2] - rect[0];
		int height = rect[3] - rect[1];
		Rectangle bounds = new Rectangle(rect[0], rect[1], width, height);
		return bounds;
	}

	/*************************** GTK *****************************/
	static void gtk_getBounds(int handle, Rectangle bounds) {
		try {
			Class<?> clazz = Class.forName("org.eclipse.swt.internal.gtk.OS");
			Class<?>[] params = new Class[] { Integer.TYPE };
			Object[] args = new Object[] { Integer.valueOf(handle) };
			Method method = clazz.getMethod("GTK_WIDGET_X", params);
			bounds.x = ((Integer) method.invoke(clazz, args)).intValue();
			method = clazz.getMethod("GTK_WIDGET_Y", params);
			bounds.y = ((Integer) method.invoke(clazz, args)).intValue();
			method = clazz.getMethod("GTK_WIDGET_WIDTH", params);
			bounds.width = ((Integer) method.invoke(clazz, args)).intValue();
			method = clazz.getMethod("GTK_WIDGET_HEIGHT", params);
			bounds.height = ((Integer) method.invoke(clazz, args)).intValue();
		} catch (Throwable e) {
			// TODO - decide what should happen when the method is unavailable
		}
	}

	static Rectangle gtk_getBounds(TableColumn tableColumn) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		try {
			Class<?> c = tableColumn.getClass();
			Field f = c.getDeclaredField("buttonHandle");
			f.setAccessible(true);
			int handle = f.getInt(tableColumn);
			gtk_getBounds(handle, bounds);
		} catch (Throwable e) {
			// TODO - decide what should happen when the method is unavailable
		}
		return bounds;
		// return tableColumn.getDisplay().map(parent, null, bounds);
	}

	static Rectangle gtk_getBounds(TreeColumn treeColumn) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		try {
			Class<?> c = treeColumn.getClass();
			Field f = c.getDeclaredField("buttonHandle");
			f.setAccessible(true);
			int handle = f.getInt(treeColumn);
			gtk_getBounds(handle, bounds);
		} catch (Throwable e) {
			// TODO - decide what should happen when the method is unavailable
		}
		return bounds;
		// return tableColumn.getDisplay().map(parent, null, bounds);
	}

	static Rectangle gtk_getBounds(TabItem tabItem) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		try {
			Class<?> c = Class.forName("org.eclipse.swt.widgets.Widget");
			Field f = c.getDeclaredField("handle");
			f.setAccessible(true);
			int handle = f.getInt(tabItem);
			gtk_getBounds(handle, bounds);
		} catch (Throwable e) {
			// TODO - decide what should happen when the method is unavailable
		}
		return bounds;
		// return tableColumn.getDisplay().map(parent, null, bounds);
	}

	/*************************** MOTIF *****************************/
	static Rectangle motif_getBounds(TabItem tabItem) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		try {
			Class<?> c = tabItem.getClass();
			Method m = c.getDeclaredMethod("getBounds");
			m.setAccessible(true);
			bounds = (Rectangle) m.invoke(tabItem);
			int margin = 2;
			bounds.x += margin;
			bounds.y += margin;
			bounds.width -= 2 * margin;
			bounds.height -= margin;
		} catch (Throwable e) {
			// TODO - decide what should happen when the method is unavailable
		}
		return bounds;
		// return tableColumn.getDisplay().map(parent, null, bounds);
	}

	static Rectangle motif_getBounds(TableColumn tableColumn) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		try {
			Class<?> c = tableColumn.getClass();
			Method m = c.getDeclaredMethod("getX");
			m.setAccessible(true);
			bounds.x = ((Integer) m.invoke(tableColumn)).intValue();
			bounds.width = tableColumn.getWidth() - 2;
			bounds.height = tableColumn.getParent().getHeaderHeight() - 2;
		} catch (Throwable e) {
			// TODO - decide what should happen when the method is unavailable
		}
		return bounds;
		// return tableColumn.getDisplay().map(parent, null, bounds);
	}

	static Rectangle motif_getBounds(TreeColumn treeColumn) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		try {
			Class<?> c = treeColumn.getClass();
			Method m = c.getDeclaredMethod("getX");
			m.setAccessible(true);
			bounds.x = ((Integer) m.invoke(treeColumn, (Object[]) null)).intValue();
			bounds.width = treeColumn.getWidth() - 2;
			bounds.height = treeColumn.getParent().getHeaderHeight() - 2;
		} catch (Throwable e) {
			// TODO - decide what should happen when the method is unavailable
		}
		return bounds;
		// return tableColumn.getDisplay().map(parent, null, bounds);
	}

	/*************************** CARBON *****************************/
	static Rectangle carbon_getBounds(TabItem tabItem) {
		return tabItem.getBounds();
	}

	static Rectangle carbon_getBounds(TableColumn tableColumn) {
		return null;
	}

	static Rectangle carbon_getBounds(TreeColumn treeColumn) {
		return null;
	}

	/*************************** COCOA *****************************/
	static Rectangle cocoa_getBounds(TabItem tabItem) {
		return tabItem.getBounds();
	}

	static Rectangle cocoa_getBounds(TableColumn tableColumn) {
		return null;
	}

	static Rectangle cocoa_getBounds(TreeColumn treeColumn) {
		return null;
	}

	public static Rectangle getBounds(TabItem tabItem) {
		if (SWTUtil.IsWindows) {
			return win32_getBounds(tabItem);
		}
		if (SWTUtil.IsGTK) {
			return gtk_getBounds(tabItem);
		}
		if (SWTUtil.IsMotif) {
			return motif_getBounds(tabItem);
		}
		if (SWTUtil.IsCarbon) {
			return carbon_getBounds(tabItem);
		}
		if (SWTUtil.IsCocoa) {
			return cocoa_getBounds(tabItem);
		}
		return null;
	}

	public static Rectangle getBounds(TableColumn tableColumn) {
		if (SWTUtil.IsWindows) {
			return win32_getBounds(tableColumn);
		}
		if (SWTUtil.IsGTK) {
			return gtk_getBounds(tableColumn);
		}
		if (SWTUtil.IsMotif) {
			return motif_getBounds(tableColumn);
		}
		if (SWTUtil.IsCarbon) {
			return carbon_getBounds(tableColumn);
		}
		if (SWTUtil.IsCocoa) {
			return cocoa_getBounds(tableColumn);
		}
		return null;
	}

	public static Rectangle getBounds(TreeColumn treeColumn) {
		if (SWTUtil.IsWindows) {
			return win32_getBounds(treeColumn);
		}
		if (SWTUtil.IsGTK) {
			return gtk_getBounds(treeColumn);
		}
		if (SWTUtil.IsMotif) {
			return motif_getBounds(treeColumn);
		}
		if (SWTUtil.IsCarbon) {
			return carbon_getBounds(treeColumn);
		}
		if (SWTUtil.IsCocoa) {
			return cocoa_getBounds(treeColumn);
		}
		return null;
	}

	public static Rectangle getBounds(TableItem item) {
		return item.getBounds(0);
		// return item.getDisplay().map(item.getParent(), null, item.getBounds(0));
	}

	public static Rectangle getBounds(TreeItem item) {
		return item.getBounds();
		// return item.getDisplay().map(item.getParent(), null, item.getBounds());
	}

	public static Rectangle getBounds(CTabItem item) {
		return item.getBounds();
		// return item.getDisplay().map(item.getParent(), null, item.getBounds());
	}

	public static Rectangle getBounds(ToolItem item) {
		return item.getBounds();
		// return item.getDisplay().map(item.getParent(), null, item.getBounds());
	}

	public static Rectangle getBounds(CoolItem item) {
		return item.getBounds();
		// return item.getDisplay().map(item.getParent(), null, item.getBounds());
	}

	public static Rectangle getBounds(ExpandItem item) {
		if (item == null || item.isDisposed()) {
			return new Rectangle(0, 0, 0, 0);
		}
		ExpandBar parent = item.getParent();
		int x = parent.getSpacing();
		int y = 0 + 20;
		int width = parent.getSize().x - 2 * parent.getSpacing();
		int height = item.getHeaderHeight();
		if (item.getExpanded()) {
			height = item.getHeaderHeight() + item.getHeight();
		}
		if ((SWT.SEPARATOR & item.getStyle()) != 0) {
			height = 1;
		} else {
			if (item.getImage() != null) {
				width += item.getImage().getBounds().width;
			}
		}
		int index = parent.indexOf(item);
		int Dheight = 0;
		// according count the sum height of the item which is before current item to get current item's position.
		for (int i = 0; i < index; i++) {
			ExpandItem prev = parent.getItem(i);
			if (prev.getExpanded())
				Dheight = Dheight + prev.getHeight() + prev.getHeaderHeight();
			else
				Dheight = Dheight + prev.getHeaderHeight();
		}
		y = parent.getSpacing() * (index + 1) + parent.getBorderWidth() + Dheight;
		return new Rectangle(x, y, width, height);
	}
}
