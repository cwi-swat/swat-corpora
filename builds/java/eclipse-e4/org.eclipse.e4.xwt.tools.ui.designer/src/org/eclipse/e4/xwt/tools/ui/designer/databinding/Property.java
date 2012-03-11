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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class Property {

	private IObservable parent;
	private Property parentProperty;
	private String name;
	private Class<?> type;
	private List<Property> properties;

	Property(Property parentProperty, String name, Class<?> type) {
		this((IObservable) null, name, type);
		this.parentProperty = parentProperty;
	}

	Property(IObservable parent, String name, Class<?> type) {
		this.parent = parent;
		this.name = name;
		this.type = type;
	}

	public Property[] getProperties() {
		if (properties == null) {
			properties = new ArrayList<Property>();
			if (isPrimitive()) {
			} else if (type.isArray()) {
				System.out.println("Array");
			} else if (Collection.class.isAssignableFrom(type)) {
				System.out.println("Collection");
			} else {
				Map<String, Class<?>> propertiesMap = PropertyUtil.getProperties(type);
				Set<Entry<String, Class<?>>> entrySet = propertiesMap
						.entrySet();
				for (Entry<String, Class<?>> entry : entrySet) {
					properties.add(new Property(this, entry.getKey(), entry
							.getValue()));
				}
			}
		}
		return properties.toArray(new Property[0]);
	}

	private boolean isPrimitive() {
		return type.isPrimitive() || String.class == type || Image.class == type || Color.class == type || Point.class == type || Rectangle.class == type || Font.class == type;
	}

	public String getName() {
		return name;
	}

	public IObservable getParent() {
		if (parent == null && parentProperty != null) {
			parent = parentProperty.getParent();
		}
		return parent;
	}

	public boolean hasChildren() {
		return getProperties().length > 0;
	}

	public String toString() {
		return getDisplayName();
	}

	public String getDisplayName() {
		String thisName = name == null ? "" : name;
		if (parentProperty != null && parentProperty.name != null) {
			return parentProperty.name + "." + thisName;
		}
		return thisName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * toString().hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		return toString().equalsIgnoreCase(other.toString());
	}

	public Class<?> getType() {
		return type;
	}

	public Property getProperty(String propertyName) {
		if (propertyName == null) {
			return null;
		}
		if (propertyName.startsWith("(") && propertyName.endsWith(")")) {
			return getDetailProperty(propertyName);
		}
		int index = propertyName.indexOf(".");
		if (index != -1) {
			String parentName = propertyName.substring(0, index);
			propertyName = propertyName.substring(index + 1);
			Property parent = getProperty(parentName);
			return parent.getProperty(propertyName);
		}
		Property[] children = getProperties();
		if (children == null) {
			return null;
		}
		for (Property p : children) {
			if (propertyName.equalsIgnoreCase(p.getName())) {
				return p;
			}
		}
		return null;
	}

	private Property getDetailProperty(String propertyName) {
		return this;
	}
}
