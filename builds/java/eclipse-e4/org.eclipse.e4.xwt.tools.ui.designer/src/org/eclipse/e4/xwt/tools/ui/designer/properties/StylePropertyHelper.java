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
package org.eclipse.e4.xwt.tools.ui.designer.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.e4.xwt.converters.StringToInteger;
import org.eclipse.e4.xwt.jface.ComboBoxCellEditor;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.commands.SetStyleCommand;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.BooleanCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.style.SWTStyles;
import org.eclipse.e4.xwt.tools.ui.designer.core.style.StyleGroup;
import org.eclipse.e4.xwt.tools.ui.designer.utils.StyleHelper;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class StylePropertyHelper {

	private static final String CATEGORY = "Style";
	private static final String PREFIX_STAR = "*";
	// private Widget widget;
	private XamlNode model;
	private EditDomain editDomain;

	public StylePropertyHelper(XamlNode model) {
		this.model = model;
	}

	public List<IPropertyDescriptor> createPropertyDescriptors() {
		if (model == null) {
			return Collections.emptyList();
		}
		IMetaclass metaclass = XWTUtility.getMetaclass(model);
		if (metaclass == null) {
			return Collections.emptyList();
		}
		Class<?> type = metaclass.getType();
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		StyleGroup[] styleGrps = SWTStyles.getStyles(type);
		for (StyleGroup styleGroup : styleGrps) {
			if (!styleGroup.match(type)){
				continue;
			}
			String groupName = styleGroup.getGroupName();
			String[] styles = styleGroup.getStyles();
			if ("default".equals(groupName)) {
				for (String style : styles) {
					descriptors.add(createDescriptor(style));
				}
			} else {
				descriptors.add(createDescriptor(styleGroup));
			}
		}
		return descriptors;
	}

	private IPropertyDescriptor createDescriptor(final StyleGroup styleGroup) {
		PropertyDescriptor descriptor = new PropertyDescriptor(styleGroup, styleGroup.getGroupName()) {
			public CellEditor createPropertyEditor(Composite parent) {
				return new ComboBoxCellEditor(parent, styleGroup.getStyles());
			}
		};
		descriptor.setCategory(CATEGORY);
		return descriptor;
	}

	private IPropertyDescriptor createDescriptor(String style) {
		PropertyDescriptor descriptor = new PropertyDescriptor(PREFIX_STAR + style, style.toLowerCase()) {
			public CellEditor createPropertyEditor(Composite parent) {
				return new BooleanCellEditor(parent);
			}
		};
		descriptor.setCategory(CATEGORY);
		return descriptor;
	}

	public Object getPropertyValue(Object id) {
		if (id == null) {
			return null;
		} else if (id.toString().startsWith(PREFIX_STAR)) {
			String style = id.toString().substring(1);
			int intStyle = (Integer) StringToInteger.instance.convert(style);
			if (StyleHelper.checkStyle(model, intStyle)) {
				return "true";
			}
			return "false";
		} else if (id instanceof StyleGroup) {
			StyleGroup styles = (StyleGroup) id;
			for (String style : styles.getStyles()) {
				int intStyle = (Integer) StringToInteger.instance.convert(style);
				if (StyleHelper.checkStyle(model, intStyle)) {
					return style;
				}
			}
		}
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		if (id == null || model == null) {
			return;
		}
		SetStyleCommand command = null;
		if (id.toString().startsWith(PREFIX_STAR)) {
			String newStyle = id.toString().substring(PREFIX_STAR.length());
			command = new SetStyleCommand(model, newStyle, Boolean.FALSE.equals(value));
		} else if (id instanceof StyleGroup) {
			command = new SetStyleCommand(model, value.toString(), (StyleGroup) id);
		}
		if (command != null && command.canExecute()) {
			if (editDomain != null) {
				editDomain.getCommandStack().execute(command);
			} else {
				command.execute();
			}
		}
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}

}
