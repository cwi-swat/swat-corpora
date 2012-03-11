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

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AddItemsCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AddNewChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ApplyAttributeSettingCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ChangeLayoutCommand;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.LabelProviderFactory;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ViewerEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Layout;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PropertyContext {
	// private Object source;
	private EditPart editPart;
	private XamlNode node;
	private EditDomain editDomain;
	private String category = "Attributes";
	private PropertyContext parent;

	public PropertyContext(EditPart editPart, PropertyContext parent) {
		this((XamlNode) null, parent);
		Assert.isNotNull(editPart);
		this.editPart = editPart;
	}

	public PropertyContext(XamlNode node, PropertyContext parent) {
		this.node = node;
		this.parent = parent;
	}

	public Object getComponent() {
		if (editPart == null) {
			return null;
		}
		if (editPart instanceof WidgetEditPart) {
			return ((WidgetEditPart) editPart).getWidget();
		} else if (editPart instanceof ViewerEditPart) {
			return ((ViewerEditPart) editPart).getViewer();
		}
		return null;
	}

	public Class<?> getType() {
		IMetaclass metaclass = getMetaclass();
		if (metaclass != null) {
			return metaclass.getType();
		}
		throw new NullPointerException("Unknown object type");
	}

	public IMetaclass getMetaclass() {
		IMetaclass metaclass = null;
		if (node != null) {
			metaclass = XWTUtility.getMetaclass(node);
		}
		if (metaclass == null) {
			Object component = getComponent();
			if (component != null) {
				metaclass = XWT.getMetaclass(component);
			}
		}
		return metaclass;
	}

	public XamlNode getNode() {
		if (node == null && editPart != null) {
			Object model = editPart.getModel();
			if (model instanceof XamlNode) {
				node = (XamlNode) model;
			}
		}
		return node;
	}

	/**
	 * @param editDomain
	 *            the editDomain to set
	 */
	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}

	/**
	 * @return the editDomain
	 */
	public EditDomain getEditDomain() {
		if (editDomain == null && parent != null) {
			editDomain = parent.getEditDomain();
		}
		return editDomain;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return the parent
	 */
	public PropertyContext getParent() {
		return parent;
	}

	public boolean isDirectEditType(Class<?> type) {
		return type.isPrimitive() || String.class == type || Color.class == type || Font.class == type || Rectangle.class == type || Point.class == type || Image.class == type || Boolean.class == type || Character.class == type || Integer.class == type || Long.class == type || Double.class == type || Float.class == type || Byte.class == type;
	}

	public void setPropertyValue(Object id, Object value) {
		execute(createSetValueCommand(id, value));
	}

	private Command createSetValueCommand(Object id, Object value) {
		CompoundCommand cmd = new CompoundCommand();
		IProperty property = (IProperty) id;
		Class<?> type = property.getType();
		if (isDirectEditType(type)) {
			ILabelProvider lp = LabelProviderFactory.getLabelProvider(type);
			cmd.add(new ApplyAttributeSettingCommand(node, property.getName(), IConstants.XWT_NAMESPACE, lp.getText(value)));
			// make sure node is added.
			XamlNode parentNode = parent == null ? null : parent.getNode();
			XamlNode current = node;
			XamlNode container = (XamlNode) current.eContainer();
			if (parentNode != null) {
				while (container != null && container != parentNode) {
					current = container;
					container = (XamlNode) current.eContainer();
				}
				if (container == null) {
					container = parentNode;
					cmd.add(new AddNewChildCommand(container, current));
				}
			}
		} else if (Layout.class == type && editPart != null) {
			cmd.add(new ChangeLayoutCommand(editPart, LayoutsHelper.getLayoutType(value)));
		} else if (type.isArray() && type.getComponentType() == String.class && value.getClass() == String[].class) {
			cmd.add(new AddItemsCommand((XamlElement) node, (String[]) value));
		}
		return cmd.unwrap();
	}

	private void execute(Command command) {
		if (command == null || !command.canExecute()) {
			return;
		}
		EditDomain editDomain = getEditDomain();
		if (editDomain == null || editDomain.getCommandStack() == null) {
			command.execute();
		} else {
			editDomain.getCommandStack().execute(command);
		}
	}
}
