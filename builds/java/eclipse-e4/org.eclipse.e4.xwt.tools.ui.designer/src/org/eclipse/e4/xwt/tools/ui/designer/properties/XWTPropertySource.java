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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.javabean.metadata.properties.EventProperty;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTModelUtil;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTPropertySource implements IPropertySource {

	private PropertyContext fContext;
	private IPropertyDescriptor[] descriptors;
	private StylePropertyHelper styleHelper;

	public XWTPropertySource(PropertyContext propertyContext) {
		Assert.isNotNull(propertyContext);
		fContext = propertyContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return fContext.getComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = createPropertyDescriptors();
		}
		return descriptors;
	}

	private IPropertyDescriptor[] createPropertyDescriptors() {
		List<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		IMetaclass metaclass = fContext.getMetaclass();
		if (metaclass == null) {
			return new IPropertyDescriptor[0];// Fixed bug of switch.
		}
		IProperty[] properties = metaclass.getProperties();
		List<String> done = new ArrayList<String>();
		for (int i = properties.length - 1; i >= 0; i--) {
			IProperty property = properties[i];
			if (canEditable(metaclass, property)) {
				done.add(property.getName());
				IPropertyDescriptor descriptor = createDescriptor(property);
				result.add(descriptor);
			}
		}
		if (!done.contains("style")) {
			Object editableValue = getEditableValue();
			if (editableValue instanceof Widget) {
				styleHelper = new StylePropertyHelper(fContext.getNode());
				styleHelper.setEditDomain(fContext.getEditDomain());
			}
			if (styleHelper != null) {
				result.addAll(styleHelper.createPropertyDescriptors());
			}
		}
		return result.toArray(new IPropertyDescriptor[0]);
	}

	protected IPropertyDescriptor createDescriptor(IProperty property) {
		XWTPropertyDescriptor descriptor = new XWTPropertyDescriptor(fContext, property);
		descriptor.setCategory(fContext.getCategory());
		return descriptor;
	}

	protected boolean canEditable(IMetaclass metaclass, IProperty property) {
		// fail fast
		if (property instanceof EventProperty) {
			return false;
		}
		Class<?> classType = metaclass.getType();
		Class<?> propertyType = property.getType();
		String name = property.getName();
		if (TableViewer.class.isAssignableFrom(classType) && "table".equals(name) && Table.class.isAssignableFrom(propertyType) || TreeViewer.class.isAssignableFrom(classType) && "tree".equals(name) && Tree.class.isAssignableFrom(propertyType) || ListViewer.class.isAssignableFrom(classType) && "list".equals(name) && org.eclipse.swt.widgets.List.class.isAssignableFrom(propertyType) || ComboViewer.class.isAssignableFrom(classType) && "combo".equals(name) && Combo.class.isAssignableFrom(propertyType)) {
			return true;
		}
		try {
			String setMethodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
			Method setMethod = classType.getMethod(setMethodName, propertyType);
			if (setMethod != null && (setMethod.getModifiers() & Modifier.PUBLIC) != 0) {
				return true;
			}
		} catch (Exception e) {
			XWTDesignerPlugin.logInfo(e);
		}
		try {
			Field field = classType.getField(name);
			if (field != null && /*field.getType().equals(propertyType) && */(field.getModifiers() & Modifier.PUBLIC) != 0) {
				return true;
			}
		} catch (Exception e) {
			XWTDesignerPlugin.logInfo(e);
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		Object value = null;
		if (id instanceof IProperty) {
			IProperty p = (IProperty) id;
			try {
				Class<?> type = p.getType();
				XamlAttribute attribute = XWTModelUtil.getAdaptableAttribute(fContext.getNode(), p.getName(), IConstants.XWT_NAMESPACE);
				Object component = fContext.getComponent();
				if (fContext.isDirectEditType(type)) {
					if (attribute != null && attribute.getValue() != null) {
						value = attribute.getValue();
					} else {
						value = p.getValue(component);
					}
				} else if (type.isArray()) {
					value = p.getValue(component);
				} else {
					boolean isAtrrNew = false;
					if (attribute == null) {
						attribute = XamlFactory.eINSTANCE.createAttribute(p.getName(), IConstants.XWT_NAMESPACE);
						isAtrrNew = true;
					}
					Object childSource = p.getValue(component);
					if (childSource != null) {
						Class<? extends Object> childSourceType = childSource.getClass();
						XamlNode child = null;
						if (Widget.class.isAssignableFrom(childSourceType)) {
							child = attribute;
						} else if (!childSourceType.isArray()) {
							String childName = childSource.getClass().getSimpleName();
							child = attribute.getChild(childName, IConstants.XWT_NAMESPACE);
							if (child == null) {
								child = XamlFactory.eINSTANCE.createElement(childName, IConstants.XWT_NAMESPACE);
								if (isAtrrNew) {
									attribute.getChildNodes().add((XamlElement) child);
								}
							}
						}
						if (child != null && childSource != null) {
							value = new PropertyContext(child, fContext);
						}
					}
				}
				if (value == null) {
					if (Boolean.class == type || boolean.class == type) {
						value = false;
					} else if (String.class == type) {
						value = "";
					} else if (Integer.class == type || int.class == type) {
						value = 0;
					}
				}
			} catch (Exception e) {
				XWTDesignerPlugin.logError(e);
			}
		} else if (styleHelper != null) {
			value = styleHelper.getPropertyValue(id);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(Object id) {
		if (id instanceof IProperty) {
			return fContext.getNode().getAttribute(((IProperty) id).getName()) != null;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object id) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		if (id instanceof IProperty) {
			fContext.setPropertyValue(id, value);
		} else if (styleHelper != null) {
			styleHelper.setPropertyValue(id, value);
		}
	}

}
