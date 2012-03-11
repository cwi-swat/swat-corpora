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
package org.eclipse.e4.xwt.tools.ui.designer.core.style;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class StyleGroup {

	public static final StyleGroup ALIGNMENT = new StyleGroup(new Class[] { Button.class, Label.class }, -1, SWT.SEPARATOR | SWT.ARROW, "alignment", new String[] { "LEFT", "CENTER", "RIGHT" });
	public static final StyleGroup SELECTION = new StyleGroup(new Class[] { org.eclipse.swt.widgets.List.class, Text.class, Table.class, Tree.class, StyledText.class }, "selection", new String[] { "SINGLE", "MUTIL" });
	public static final StyleGroup TYPE = new StyleGroup(new Class[] { Button.class, ToolItem.class }, "type", new String[] { "PUSH", "CHECK", "RADIO", "TOGGLE", "ARROW" });

	// for Arrow
	public static final StyleGroup ARROW = new StyleGroup(new Class[] { Button.class }, SWT.ARROW, "arrow", new String[] { "LEFT", "RIGHT", "UP", "DOWN" });

	// for Label
	public static final StyleGroup LABEL_SHADOW = new StyleGroup(new Class[] { Label.class }, SWT.SEPARATOR, "shadow", new String[] { "SHADOW_IN", "SHADOW_OUT", "SHADOW_NONE" });
	public static final StyleGroup CLABEL_SHADOW = new StyleGroup(CLabel.class, "shadow", new String[] { "SHADOW_IN", "SHADOW_OUT", "SHADOW_NONE" });

	public static final StyleGroup ORIENTATION = new StyleGroup(Control.class, "orientation", new String[] { "LEFT_TO_RIGHT", "RIGHT_TO_LEFT" });

	public static final StyleGroup PROGRESS_TYPE = new StyleGroup(new Class<?>[] { ProgressBar.class, CoolBar.class, ToolBar.class, SashForm.class, Scale.class, Slider.class, Label.class }, "type", new String[] { "VERTICAL", "HORIZONTAL" });

	public static final StyleGroup GROUP_SHADOW = new StyleGroup(Group.class, "shadow", new String[] { "SHADOW_ETCHED_IN", "SHADOW_ETCHED_OUT", "SHADOW_IN", "SHADOW_OUT", "SHADOW_NONE" });

	// for Shell
	public static final StyleGroup MODALITY = new StyleGroup(Shell.class, "modality", new String[] { "PRIMARY_MODAL", "MODELESS", "APPLICATION_MODAL", "SYSTEM_MODAL" });
	public static final StyleGroup TRIM = new StyleGroup(Shell.class, "trim", new String[] { "SHELL_TRIM", "DIALOG_TRIM", "NO_TRIM" });

	// for Text
	public static final StyleGroup LINES = new StyleGroup(Text.class, "lines", new String[] { "SINGLE", "MUTIL" });

	// for Date
	public static final StyleGroup DATE_TYPE = new StyleGroup(DateTime.class, "type", new String[] { "DATE", "TIME", "CALENDAR" });
	public static final StyleGroup DATE_FORMAT = new StyleGroup(DateTime.class, "format", new String[] { "MEDIUM", "SHORT", "LONG" });

	public static final StyleGroup LOCATION = new StyleGroup(new Class[] { TabFolder.class, CTabFolder.class }, "location", new String[] { "TOP", "BOTTOM" });

	private String groupName;
	private String[] styles;
	private int accessibleStyle = -1;
	private int unaccessibleStyle = -1;
	private Class<?>[] accessibleTypes;

	public StyleGroup(Class<?> type, String groupName, String[] styles) {
		this(new Class[] { type }, groupName, styles);
	}

	public StyleGroup(Class<?>[] accessibleTypes, String groupName, String[] styles) {
		this(accessibleTypes, -1, groupName, styles);
	}

	public StyleGroup(Class<?>[] accessibleTypes, int accessibleStyle, String groupName, String[] styles) {
		this(accessibleTypes, accessibleStyle, -1, groupName, styles);
	}

	public StyleGroup(Class<?>[] accessibleTypes, int accessibleStyle, int unaccessibleStyle, String groupName, String[] styles) {
		this.accessibleTypes = accessibleTypes;
		this.accessibleStyle = accessibleStyle;
		this.unaccessibleStyle = unaccessibleStyle;
		this.groupName = groupName;
		this.styles = styles;
	}

	public String[] getStyles() {
		return styles;
	}

	public String getGroupName() {
		return groupName;
	}

	public boolean match(Class<?> type, List<String> styleList) {
		return match(type) && match(styleList);
	}

	public boolean match(List<String> styleList) {
		if (styles == null || styleList == null) {
			return false;
		}
		return styleList.containsAll(Arrays.asList(styles));
	}

	public boolean match(Class<?> type) {
		if (accessibleTypes == null || type == null) {
			return false;
		}
		for (Class<?> t : accessibleTypes) {
			if (t.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}

	public boolean match(int style) {
		if (unaccessibleStyle != -1 && (unaccessibleStyle & style) != 0) {
			return false;
		}
		if (accessibleStyle == -1) {
			return true;
		}
		return (accessibleStyle & style) != 0;
	}
}
