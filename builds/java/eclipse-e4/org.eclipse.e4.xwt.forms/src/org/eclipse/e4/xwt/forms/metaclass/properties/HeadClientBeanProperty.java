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
package org.eclipse.e4.xwt.forms.metaclass.properties;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.xwt.javabean.metadata.properties.BeanProperty;
import org.eclipse.ui.forms.widgets.Form;

public class HeadClientBeanProperty extends BeanProperty {

	public HeadClientBeanProperty(PropertyDescriptor descriptor) {
		super(descriptor);
		setValueAsParent(true);
	}
		
	@Override
	public Object getValue(Object target) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchFieldException {
		Object value = super.getValue(target);
		if (value == null) {
			Form form = (Form) target;
			return form.getHead();
		}
		return value;
	}
}
