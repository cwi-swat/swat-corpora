/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec and Erdal Karaca - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.forms.metaclass;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.nio.channels.IllegalSelectorException;

import org.eclipse.e4.xwt.forms.metaclass.properties.DecoratingHeading;
import org.eclipse.e4.xwt.forms.metaclass.properties.HeadClientBeanProperty;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 
 * @author Erdal Karaca <erdal.karaca.de@googlemail.com>
 * @author yves.yang (yves.yang@soyatec.com)
 */
public class FormMetaclass extends AbstractFormMetaclass {
	public FormMetaclass() {
		super(Form.class);
		addProperty(new DecoratingHeading());
		addProperty(new HeadClientBeanProperty(getHeadClient()));
	}
	
	static PropertyDescriptor getHeadClient() {
		try {
			BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(Form.class);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor p : propertyDescriptors) {
				String propertyName = p.getName();
				if (propertyName.equalsIgnoreCase("headClient")) {
					return p;
				}
			}
		} catch (IntrospectionException e) {
		}
		throw new IllegalSelectorException();
	}

	@Override
	protected Control doCreateControl(FormToolkit tk, Composite parent,
			int style) {
		return tk.createForm(parent);
	}
}
