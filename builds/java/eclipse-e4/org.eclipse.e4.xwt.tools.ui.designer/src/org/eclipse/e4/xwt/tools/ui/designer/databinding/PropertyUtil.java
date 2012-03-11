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
package org.eclipse.e4.xwt.tools.ui.designer.databinding;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.set.ISetProperty;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PropertyUtil {
	private static Map<String, IWidgetValueProperty> SWT_PROPERTIES;

	private static Map<String, org.eclipse.core.databinding.property.IProperty> JFACE_PROPERTIES;

	private static ViewerPropertiesCollector viewerCollector;
	private static WidgetPropertiesCollector widgetCollector;
	private static BeanPropertiesCollector beanCollector;

	private PropertyUtil() {
	}

	public static synchronized Map<String, Class<?>> getProperties(Object object) {
		if (object == null) {
			return null;
		}
		if (object instanceof Widget) {
			if (widgetCollector == null) {
				widgetCollector = new WidgetPropertiesCollector();
			}
			return widgetCollector.getProperties(object);
		} else if (object instanceof Viewer) {
			if (viewerCollector == null) {
				viewerCollector = new ViewerPropertiesCollector();
			}
			return viewerCollector.getProperties(object);
		} else {
			if (beanCollector == null) {
				beanCollector = new BeanPropertiesCollector();
			}
			return beanCollector.getProperties(object);
		}
	}

	public static synchronized Map<String, org.eclipse.core.databinding.property.IProperty> getJfaceProperties() {
		if (JFACE_PROPERTIES == null) {
			JFACE_PROPERTIES = new HashMap<String, org.eclipse.core.databinding.property.IProperty>();
			Method[] declaredMethods = ViewerProperties.class.getDeclaredMethods();
			for (Method method : declaredMethods) {
				try {
					org.eclipse.core.databinding.property.IProperty property = (org.eclipse.core.databinding.property.IProperty) method.invoke(null, new Object[0]);
					JFACE_PROPERTIES.put(method.getName(), property);
				} catch (Exception e) {
					JFACE_PROPERTIES.put(method.getName(), null);
				}
			}
		}
		return JFACE_PROPERTIES;
	}

	public static synchronized Map<String, IWidgetValueProperty> getSwtProperties() {
		if (SWT_PROPERTIES == null) {
			SWT_PROPERTIES = new HashMap<String, IWidgetValueProperty>();
			Method[] declaredMethods = WidgetProperties.class.getDeclaredMethods();
			for (Method method : declaredMethods) {
				try {
					IWidgetValueProperty property = (IWidgetValueProperty) method.invoke(null, new Object[0]);
					SWT_PROPERTIES.put(method.getName(), property);
				} catch (Exception e) {
				}
			}
		}
		return SWT_PROPERTIES;
	}

	static abstract class PropertiesCollector {
		private Map<Object, Map<String, Class<?>>> cache = new HashMap<Object, Map<String, Class<?>>>();

		Map<String, Class<?>> getProperties(Object object) {
			Map<String, Class<?>> properties = cache.get(object);
			if (properties == null) {
				properties = createProperties(object);
			}
			return properties;
		}

		abstract Map<String, Class<?>> createProperties(Object object);
	}

	static class ViewerPropertiesCollector extends PropertiesCollector {

		protected Map<String, Class<?>> createProperties(Object object) {
			IMetaclass metaclass = XWT.getMetaclass(object);
			if (metaclass == null) {
				return null;
			}

			Map<String, Class<?>> results = new HashMap<String, Class<?>>();
			Map<String, org.eclipse.core.databinding.property.IProperty> jfaceProperties = getJfaceProperties();
			Set<Entry<String, org.eclipse.core.databinding.property.IProperty>> entrySet = jfaceProperties
					.entrySet();
			for (Entry<String, org.eclipse.core.databinding.property.IProperty> entry : entrySet) {
				String propertyName = entry.getKey();
				org.eclipse.core.databinding.property.IProperty host = entry
						.getValue();
				Class<?> type = null;
				if (host instanceof ISetProperty) {
					type = Set.class;
				} else if (host instanceof IListProperty) {
					type = List.class;
				} else {
					type = Object.class;
				}
				results.put(propertyName, type);
			}
			return results;
		}
	}

	static class WidgetPropertiesCollector extends PropertiesCollector {

		private boolean supportWidget(String name, Class<?> propertyType) {
			IWidgetValueProperty property = getSwtProperties().get(name);
			if (property == null) {
				return false;
			}
			Class<?> definedType = (Class<?>) property.getValueType();
			if (propertyType == null || definedType == null) {
				return false;
			}
			boolean support = propertyType.isAssignableFrom(definedType);
			if (support) {
				return true;
			} else if (propertyType.isPrimitive()) {
				try {
					Field typeField = definedType.getDeclaredField("TYPE");
					return propertyType == typeField.get(null);
				} catch (Exception e) {
				}
			} else if (definedType.isPrimitive()) {
				try {
					Field typeField = propertyType.getDeclaredField("TYPE");
					return definedType == typeField.get(null);
				} catch (Exception e) {
				}
			}
			return false;
		}

		protected Map<String, Class<?>> createProperties(Object object) {
			IMetaclass metaclass = XWT.getMetaclass(object);
			if (metaclass == null) {
				return null;
			}
			Map<String, Class<?>> results = new HashMap<String, Class<?>>();
			IProperty[] properties = metaclass.getProperties();
			for (IProperty iProperty : properties) {
				String propertyName = iProperty.getName();
				Class<?> valueType = iProperty.getType();
				if (!supportWidget(propertyName, valueType)) {
					continue;
				}
				results.put(propertyName, valueType);
			}
			return results;
		}
	}

	static class BeanPropertiesCollector extends PropertiesCollector {

		protected Map<String, Class<?>> createProperties(Object object) {
			if (object == null) {
				return null;
			}
			Class<?> type = (object instanceof Class<?>) ? (Class<?>) object : object.getClass();
			Map<String, Class<?>> result = new HashMap<String, Class<?>>();
			try {
				BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(type);
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor pd : propertyDescriptors) {
					String propertyName = pd.getName();
					if (("class").equals(propertyName) || result.containsKey(propertyName) || pd.getPropertyType() == null) {
						continue;
					}
					result.put(propertyName, pd.getPropertyType());
				}
				for (Field f : type.getDeclaredFields()) {
					String propertyName = f.getName().toLowerCase();
					if (("class").equals(propertyName) || result.containsKey(propertyName) || Modifier.isFinal(f.getModifiers()) || !Modifier.isPublic(f.getModifiers()) || f.getType() == null) {
						continue;
					}
					result.put(propertyName, f.getType());
				}

			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
			return result;
		}
	}
}
