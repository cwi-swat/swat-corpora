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
package org.eclipse.e4.xwt.tools.ui.designer.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.core.style.StyleGroup;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.StringUtil;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.commands.Command;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class SetStyleCommand extends Command {

	private XamlNode parent;
	private String newStyle;
	private StyleGroup group;
	private boolean remove;
	private Command delegate;

	public SetStyleCommand(XamlNode parent, String newStyle) {
		this.parent = parent;
		this.newStyle = newStyle;
	}

	public SetStyleCommand(XamlNode parent, String newStyle, StyleGroup group) {
		this(parent, newStyle);
		this.group = group;
	}

	public SetStyleCommand(XamlNode parent, String newStyle, boolean remove) {
		this(parent, newStyle);
		this.remove = remove;
	}

	public void setGroup(StyleGroup group) {
		this.group = group;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return parent != null && newStyle != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return delegate != null && delegate.canUndo();
	}

	private Command createCommand(String newStyle) {
		return new ApplyAttributeSettingCommand(parent, "style", IConstants.XWT_X_NAMESPACE, newStyle);
	}

	private Command createDelegate() {
		XamlAttribute attribute = parent.getAttribute("style", IConstants.XWT_X_NAMESPACE);
		if (attribute == null || attribute.getValue() == null) {
			return createCommand(newStyle);
		} else {
			String value = attribute.getValue();
			List<String> oldValues = new ArrayList<String>();
			StringTokenizer stk = new StringTokenizer(value, "|");
			while (stk.hasMoreTokens()) {
				oldValues.add(stk.nextToken().trim().toUpperCase());
			}

			// 1. Maybe not a string.
			if (oldValues.isEmpty()) {
				return createCommand(newStyle);
			}
			// 2. String style.
			if (group != null && !"default".equals(group.getGroupName())) {
				String[] styles = group.getStyles();
				for (String str : styles) {
					if (oldValues.contains(str)) {
						oldValues.remove(str);
					}
					if (oldValues.contains("SWT." + str)) {
						oldValues.remove("SWT." + str);
					}
				}
				oldValues.add(newStyle);
				String newStyleValue = StringUtil.format(oldValues.toArray(new String[oldValues.size()]), "|");
				return createCommand(newStyleValue);
			} else {
				if (remove) {
					// remove
					if (oldValues.contains(newStyle)) {
						oldValues.remove(newStyle);
					}
					if (oldValues.contains("SWT." + newStyle)) {
						oldValues.remove("SWT." + newStyle);
					}
					String newStyleValue = StringUtil.format(oldValues.toArray(new String[oldValues.size()]), "|");
					return createCommand(newStyleValue);
				} else {
					// new add.
					if (oldValues.contains(newStyle) || oldValues.contains("SWT." + newStyle)) {
						return null;
					}
					String styleValue = value + "|" + newStyle;
					return createCommand(styleValue);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		delegate = createDelegate();
		if (delegate != null && delegate.canExecute()) {
			delegate.execute();
		}
	}
}
