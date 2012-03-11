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
package org.eclipse.e4.xwt.tools.ui.designer.layouts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class LayoutsHelper {

	public static List<LayoutType> layoutsList = new ArrayList<LayoutType>();
	static {
		layoutsList.add(LayoutType.NullLayout);
		layoutsList.add(LayoutType.FillLayout);
		layoutsList.add(LayoutType.FormLayout);
		layoutsList.add(LayoutType.GridLayout);
		layoutsList.add(LayoutType.RowLayout);
		layoutsList.add(LayoutType.StackLayout);
	}

	public static LayoutType getLayoutType(EditPart editPart) {
		if (editPart instanceof CompositeEditPart) {
			Layout layout = ((CompositeEditPart) editPart).getLayout();
			return getLayoutType(layout);
		}
		return LayoutType.Unknown;
	}

	public static LayoutDataType getLayoutDataType(EditPart editPart) {
		if (editPart == null) {
			return LayoutDataType.Unknown;
		}
		EditPart parent = editPart.getParent();
		LayoutType layoutType = getLayoutType(parent);
		if (LayoutType.GridLayout == layoutType) {
			return LayoutDataType.GridData;
		} else if (LayoutType.RowLayout == layoutType) {
			return LayoutDataType.RowData;
		} else if (LayoutType.FormLayout == layoutType) {
			return LayoutDataType.FormData;
		}
		return LayoutDataType.Unknown;

	}

	public static LayoutType getLayoutType(Layout layout) {
		if (layout == null) {
			return LayoutType.NullLayout;
		} else if (layout instanceof org.eclipse.swt.layout.GridLayout) {
			return LayoutType.GridLayout;
		} else if (layout instanceof org.eclipse.swt.layout.FillLayout) {
			return LayoutType.FillLayout;
		} else if (layout instanceof org.eclipse.swt.layout.RowLayout) {
			return LayoutType.RowLayout;
		} else if (layout instanceof org.eclipse.swt.custom.StackLayout) {
			return LayoutType.StackLayout;
		} else if (layout instanceof org.eclipse.swt.layout.FormLayout) {
			return LayoutType.FormLayout;
		}
		return LayoutType.NullLayout;
	}

	public static LayoutType getLayoutType(String value) {
		if (value == null) {
			return LayoutType.NullLayout;
		}
		LayoutType[] values = LayoutType.values();
		for (LayoutType layoutType : values) {
			if (value.equals(layoutType.value())) {
				return layoutType;
			}
		}
		return LayoutType.Unknown;
	}

	public static LayoutType getLayoutType(XamlElement element) {
		IMetaclass metaclass = XWTUtility.getMetaclass(element);
		if (metaclass == null || !Composite.class.isAssignableFrom(metaclass.getType())) {
			return LayoutType.Unknown;
		}
		XamlAttribute attribute = element.getAttribute("layout");
		if (attribute == null || attribute.getChildNodes().isEmpty()) {
			return LayoutType.NullLayout;
		}
		String name = attribute.getChildNodes().get(0).getName();
		return getLayoutType(name);
	}

	public static LayoutType getLayoutType(Object object) {
		if (object == null) {
			return LayoutType.NullLayout;
		} else if (object instanceof LayoutType) {
			return (LayoutType) object;
		} else if (object instanceof String) {
			return getLayoutType((String) object);
		} else if (object instanceof EditPart) {
			return getLayoutType((EditPart) object);
		} else if (object instanceof Layout) {
			return getLayoutType((Layout) object);
		} else if (object instanceof XamlElement) {
			return getLayoutType((XamlElement) object);
		}
		return LayoutType.Unknown;
	}

	public static boolean isLayoutNode(XamlNode node) {
		if (node == null || !(node instanceof XamlAttribute)) {
			return false;
		}
		return "layout".equalsIgnoreCase(node.getName());
	}

	public static boolean canSetLayout(EditPart host) {
		if (host == null || host.getModel() == null) {
			return false;
		}
		return canSetLayout(host.getModel());
	}

	public static boolean canSetLayout(Object model) {
		if (model == null) {
			return false;
		}
		if (model instanceof XamlElement) {
			XamlElement XamlElement = (XamlElement) model;
			String name = XamlElement.getName();
			String namespace = XamlElement.getNamespace();
			try {
				IMetaclass metaclass = XWT.getMetaclass(name, namespace);
				if (metaclass != null) {
					Class<?> type = metaclass.getType();
					return type != null && Composite.class.isAssignableFrom(type);
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
}
