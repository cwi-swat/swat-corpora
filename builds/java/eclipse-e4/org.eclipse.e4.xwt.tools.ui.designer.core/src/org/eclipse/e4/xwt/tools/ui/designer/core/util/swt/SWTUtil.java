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

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

public class SWTUtil {

	/**
	 * Value is <code>true</code> if we're running on the <a href="http://en.wikipedia.org/wiki/Windows">Microsoft Windows</a> windowing system, <code>false</code> otherwise.
	 */
	public static final boolean IsWindows = "win32".equals(SWT.getPlatform());

	/**
	 * Value is <code>true</code> if we're running on the <a href="http://en.wikipedia.org/wiki/Motif_(widget_toolkit)">Motif</a> windowing system, <code>false</code> otherwise.
	 */
	public static final boolean IsMotif = "motif".equals(SWT.getPlatform());

	/**
	 * Value is <code>true</code> if we're running on the <a href="http://en.wikipedia.org/wiki/GTK">GTK</a> windowing system, <code>false</code> otherwise.
	 */
	public static final boolean IsGTK = "gtk".equals(SWT.getPlatform());

	/**
	 * Value is <code>true</code> if we're running on the Photon (<a href="http://en.wikipedia.org/wiki/QNX">QNX</a>) windowing system, <code>false</code> otherwise.
	 */
	public static final boolean IsPhoton = "photon".equals(SWT.getPlatform());

	/**
	 * Value is <code>true</code> if we're running on the <a href="http://en.wikipedia.org/wiki/Carbon_(API)">Carbon</a> (<a href="http://en.wikipedia.org/wiki/Mac_OS_X">Mac OS X</a>) windowing system, <code>false</code> otherwise.
	 */
	public static final boolean IsCarbon = "carbon".equals(SWT.getPlatform());

	/**
	 * Value is <code>true</code> if we're running on the <a href="http://en.wikipedia.org/wiki/Carbon_(API)">Carbon</a> (<a href="http://en.wikipedia.org/wiki/Mac_OS_X">Mac OS X</a>) windowing system, <code>false</code> otherwise.
	 */
	public static final boolean IsCocoa = "cocoa".equals(SWT.getPlatform());

	public static void printStyle(int style, Class<? extends Widget> clazz, Appendable appendable) {
		try {
			appendable.append("[");
			boolean first = true;
			for (int styleBit = 0, styleMask = 1; styleBit < 32; styleBit++, styleMask <<= 1) {
				if ((style & styleMask) != 0) {
					if (!first)
						appendable.append(',');
					else
						first = false;
					appendable.append(toStringStyleBit(styleMask, clazz));
				}
			}
			appendable.append(']');
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toStringStyle(int style, Class<? extends Widget> clazz) {
		StringBuilder builder = new StringBuilder();
		printStyle(style, clazz, builder);
		return builder.toString();
	}

	public static String toStringStyleBit(int style, Class<? extends Widget> clazz) {
		switch (style) {
		case SWT.BAR:
			if (Menu.class.isAssignableFrom(clazz))
				return "bar";
			// case SWT.SEPARATOR:
			if (Label.class.isAssignableFrom(clazz) || MenuItem.class.isAssignableFrom(clazz) || ToolItem.class.isAssignableFrom(clazz))
				return "separator";
			// case SWT.TOGGLE:
			if (Button.class.isAssignableFrom(clazz))
				return "toggle";
			// case SWT.MULTI:
			if (Text.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz))
				return "multi";
			// case SWT.INDETERMINATE:
			if (ProgressBar.class.isAssignableFrom(clazz))
				return "indeterminate";
			break;

		case SWT.DROP_DOWN:
			if (Menu.class.isAssignableFrom(clazz) || ToolItem.class.isAssignableFrom(clazz) || CoolItem.class.isAssignableFrom(clazz) || Combo.class.isAssignableFrom(clazz))
				return "drop-down";
			// case SWT.ARROW:
			if (Button.class.isAssignableFrom(clazz))
				return "arrow";
			// case SWT.SINGLE:
			if (Text.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz) || Table.class.isAssignableFrom(clazz) || Tree.class.isAssignableFrom(clazz))
				return "single";
			// case SWT.SHADOW_IN:
			if (Label.class.isAssignableFrom(clazz) || Group.class.isAssignableFrom(clazz))
				return "shadow-in";
			// case SWT.TOOL:
			if (Decorations.class.isAssignableFrom(clazz))
				return "tool";
			break;

		case SWT.POP_UP:
			if (Menu.class.isAssignableFrom(clazz))
				return "pop-up";
			// case SWT.PUSH:
			if (Button.class.isAssignableFrom(clazz) || MenuItem.class.isAssignableFrom(clazz) || ToolItem.class.isAssignableFrom(clazz))
				return "push";
			// case SWT.READ_ONLY:
			if (Combo.class.isAssignableFrom(clazz) || Text.class.isAssignableFrom(clazz))
				return "read-only";
			// case SWT.SHADOW_OUT:
			if (Label.class.isAssignableFrom(clazz) || Group.class.isAssignableFrom(clazz) || ToolBar.class.isAssignableFrom(clazz))
				return "shadow-out";
			// case SWT.NO_TRIM:
			if (Decorations.class.isAssignableFrom(clazz))
				return "no-trip";
			break;

		case SWT.RADIO:
			if (Button.class.isAssignableFrom(clazz) || MenuItem.class.isAssignableFrom(clazz) || ToolItem.class.isAssignableFrom(clazz))
				return "radio";
			// case SWT.SHADOW_ETCHED_IN:
			if (Group.class.isAssignableFrom(clazz))
				return "shadow-etched-in";
			// case SWT.RESIZE:
			if (Decorations.class.isAssignableFrom(clazz) || Tracker.class.isAssignableFrom(clazz))
				return "resize";
			break;

		case SWT.CHECK:
			if (Button.class.isAssignableFrom(clazz) || MenuItem.class.isAssignableFrom(clazz) || ToolItem.class.isAssignableFrom(clazz) || Table.class.isAssignableFrom(clazz) || Tree.class.isAssignableFrom(clazz))
				return "check";
			// case SWT.SHADOW_NONE:
			if (Label.class.isAssignableFrom(clazz) || Group.class.isAssignableFrom(clazz))
				return "shadow-none";
			// case SWT.TITLE:
			if (Decorations.class.isAssignableFrom(clazz))
				return "title";
			break;

		case SWT.CASCADE:
			if (MenuItem.class.isAssignableFrom(clazz))
				return "cascade";
			// case SWT.WRAP:
			if (Label.class.isAssignableFrom(clazz) || Text.class.isAssignableFrom(clazz) || ToolBar.class.isAssignableFrom(clazz) || Spinner.class.isAssignableFrom(clazz))
				return "wrap";
			// case SWT.SIMPLE:
			if (Combo.class.isAssignableFrom(clazz))
				return "simple";
			// case SWT.SHADOW_ETCHED_OUT:
			if (Group.class.isAssignableFrom(clazz))
				return "shadow-etched-out";
			// case SWT.CLOSE:
			if (Decorations.class.isAssignableFrom(clazz))
				return "close";
			break;

		case SWT.MIN:
			if (Decorations.class.isAssignableFrom(clazz))
				return "min";
			// case SWT.UP:
			if (Button.class.isAssignableFrom(clazz) || Tracker.class.isAssignableFrom(clazz))
				return "up";
			break;

		case SWT.H_SCROLL:
			if (Scrollable.class.isAssignableFrom(clazz))
				return "h-scroll";
			// case SWT.HORIZONTAL:
			if (Label.class.isAssignableFrom(clazz) || ProgressBar.class.isAssignableFrom(clazz) || Sash.class.isAssignableFrom(clazz) || Scale.class.isAssignableFrom(clazz) || ScrollBar.class.isAssignableFrom(clazz) || Slider.class.isAssignableFrom(clazz) || ToolBar.class.isAssignableFrom(clazz))
				return "horizontal";
			break;

		case SWT.V_SCROLL:
			if (Scrollable.class.isAssignableFrom(clazz))
				return "v-scroll";
			// case SWT.VERTICAL:
			if (Label.class.isAssignableFrom(clazz) || ProgressBar.class.isAssignableFrom(clazz) || Sash.class.isAssignableFrom(clazz) || Scale.class.isAssignableFrom(clazz) || ScrollBar.class.isAssignableFrom(clazz) || Slider.class.isAssignableFrom(clazz) || ToolBar.class.isAssignableFrom(clazz) || CoolBar.class.isAssignableFrom(clazz))
				return "vertical";
			break;

		case SWT.MAX:
			if (Decorations.class.isAssignableFrom(clazz))
				return "max";
			// case SWT.DOWN:
			if (Button.class.isAssignableFrom(clazz) || Tracker.class.isAssignableFrom(clazz))
				return "down";
			break;

		case SWT.BORDER:
			if (Control.class.isAssignableFrom(clazz))
				return "border";
			break;

		case SWT.CLIP_CHILDREN:
			if (Control.class.isAssignableFrom(clazz))
				return "clip-children";
			break;

		case SWT.CLIP_SIBLINGS:
			if (Control.class.isAssignableFrom(clazz))
				return "clip-siblings";
			break;

		case SWT.ON_TOP:
			if (Shell.class.isAssignableFrom(clazz))
				return "on-top";
			// case SWT.LEAD:
			if (Button.class.isAssignableFrom(clazz) || Label.class.isAssignableFrom(clazz) || TableColumn.class.isAssignableFrom(clazz) || Tracker.class.isAssignableFrom(clazz))
				return "lead";
			break;

		case SWT.PRIMARY_MODAL:
			if (Shell.class.isAssignableFrom(clazz))
				return "primary-modal";
			// case SWT.HIDE_SELECTION:
			if (Table.class.isAssignableFrom(clazz))
				return "hide-selection";
			break;

		case SWT.APPLICATION_MODAL:
			if (Shell.class.isAssignableFrom(clazz))
				return "application-modal";
			// case SWT.FULL_SELECTION:
			if (StyledText.class.isAssignableFrom(clazz) || Table.class.isAssignableFrom(clazz) || Tree.class.isAssignableFrom(clazz))
				return "full-selection";
			// case SWT.SMOOTH:
			if (ProgressBar.class.isAssignableFrom(clazz) || Sash.class.isAssignableFrom(clazz))
				return "smooth";
			break;

		case SWT.SYSTEM_MODAL:
			if (Shell.class.isAssignableFrom(clazz))
				return "system-modal";
			// case SWT.TRAIL:
			if (Button.class.isAssignableFrom(clazz) || Label.class.isAssignableFrom(clazz) || TableColumn.class.isAssignableFrom(clazz) || Tracker.class.isAssignableFrom(clazz))
				return "trail";
			break;

		case SWT.NO_BACKGROUND:
			if (Composite.class.isAssignableFrom(clazz))
				return "no-background";
			break;

		case SWT.NO_FOCUS:
			if (Composite.class.isAssignableFrom(clazz))
				return "no-focus";
			break;

		case SWT.NO_REDRAW_RESIZE:
			if (Composite.class.isAssignableFrom(clazz))
				return "no-redraw-resize";
			break;

		case SWT.NO_MERGE_PAINTS:
			if (Composite.class.isAssignableFrom(clazz))
				return "no-merge-paints";
			break;

		case SWT.PASSWORD:
			if (Text.class.isAssignableFrom(clazz))
				return "password";
			// case SWT.NO_RADIO_GROUP:
			if (Composite.class.isAssignableFrom(clazz))
				return "no-radio-group";
			break;

		case SWT.FLAT:
			if (Button.class.isAssignableFrom(clazz) || ToolBar.class.isAssignableFrom(clazz))
				return "flat";
			break;

		case SWT.EMBEDDED:
			if (Composite.class.isAssignableFrom(clazz))
				return "embedded";
			// case SWT.CENTER:
			if (Button.class.isAssignableFrom(clazz) || Label.class.isAssignableFrom(clazz) || TableColumn.class.isAssignableFrom(clazz))
				return "center";
			break;

		case SWT.LEFT_TO_RIGHT:
			// Omit "left-to-right" because it's ubiquitous.
			// if (Control.class.isAssignableFrom(clazz) || Menu.class.isAssignableFrom(clazz))
			// return "left-to-right";
			break;

		case SWT.RIGHT_TO_LEFT:
			if (Control.class.isAssignableFrom(clazz) || Menu.class.isAssignableFrom(clazz))
				return "right-to-left";
			break;

		case SWT.MIRRORED:
			if (Control.class.isAssignableFrom(clazz) || Menu.class.isAssignableFrom(clazz))
				return "mirrored";
			break;

		case SWT.VIRTUAL:
			if (Table.class.isAssignableFrom(clazz) || Tree.class.isAssignableFrom(clazz))
				return "virtual";
			break;

		case SWT.DOUBLE_BUFFERED:
			if (Control.class.isAssignableFrom(clazz))
				return "double-buffered";
			break;
		}
		return String.format("%08x", style);
	}

}
