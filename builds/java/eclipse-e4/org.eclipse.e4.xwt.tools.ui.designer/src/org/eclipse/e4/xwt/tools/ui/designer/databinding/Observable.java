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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class Observable implements IObservable {

	private Object source;
	private List<Property> properties;
	private Observable parent;

	private List<IObservable> children;

	Observable(Object source, Observable parent) {
		Assert.isNotNull(source);
		this.source = source;
		this.parent = parent;
		if (parent != null) {
			parent.add(this);
		}
	}

	private void add(Observable observable) {
		if (children == null) {
			children = new ArrayList<IObservable>();
		}
		if (!children.contains(observable)) {
			children.add(observable);
		}
	}

	public boolean hasChildren() {
		return children != null && children.size() > 0;
	}

	public int getType() {
		if (source == null) {
			return OBSERVE_UNKNOWN;
		}
		if (source instanceof Widget || source instanceof Viewer) {
			return OBSERVE_SWT_JFACE;
		}
		return OBSERVE_JAVA_BAEN;
	}

	public Property[] getProperties() {
		if (properties == null) {
			properties = new ArrayList<Property>();
			Object sourceObj = source;
			if (source instanceof DataContext) {
				sourceObj = ((DataContext) source).getData();
			}
			Map<String, Class<?>> propertiesMap = PropertyUtil.getProperties(sourceObj);
			Set<Entry<String, Class<?>>> entrySet = propertiesMap.entrySet();
			for (Entry<String, Class<?>> entry : entrySet) {
				Property property = new Property(this, entry.getKey(), entry
						.getValue());
				properties.add(property);
			}
		}
		return properties.toArray(new Property[properties.size()]);
	}

	public Object getSource() {
		return source;
	}

	public String getDisplayName() {
		String simpleName = source.getClass().getSimpleName();
		if (source instanceof DataContext) {
			simpleName = simpleName + "(" + ((DataContext) source).getKey() + ")";
		}
		return simpleName;
	}

	public IObservable[] getChildren() {
		return children == null ? new IObservable[0] : children.toArray(new IObservable[children.size()]);
	}

	public IObservable getParent() {
		return parent;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Observable other = (Observable) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	public Property getProperty(String propertyName) {
		if (propertyName == null) {
			return null;
		}
		int index = propertyName.indexOf(".");
		if (index != -1) {
			String parentName = propertyName.substring(0, index);
			propertyName = propertyName.substring(index + 1);
			Property parent = getProperty(parentName);
			if (parent != null) {
				return parent.getProperty(propertyName);
			}
		}
		Property[] properties = getProperties();
		if (properties == null) {
			return null;
		}
		for (Property p : properties) {
			if (propertyName.equalsIgnoreCase(p.getName())) {
				return p;
			}
		}
		return null;
	}
}
