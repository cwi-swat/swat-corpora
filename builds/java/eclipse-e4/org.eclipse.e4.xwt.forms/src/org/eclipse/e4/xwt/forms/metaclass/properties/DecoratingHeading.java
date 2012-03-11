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
package org.eclipse.e4.xwt.forms.metaclass.properties;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.xwt.forms.ToolKitUtil;
import org.eclipse.e4.xwt.javabean.metadata.properties.AbstractProperty;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * A property to handle the decoration of heading
 * 
 * @author yyang 
 */
public class DecoratingHeading extends AbstractProperty {

	public DecoratingHeading() {
		super("DecorateHeading", Boolean.class);
	}

	public Object getValue(Object target) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchFieldException {
		return null;
	}

	public void setValue(Object target, Object value)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchFieldException {
		Form form = null;
		if (target instanceof ScrolledForm) {
			ScrolledForm scrolledForm = (ScrolledForm) target;
			form = scrolledForm.getForm();
		}
		else if (target instanceof Form) {
			form = (Form) target;
		}
		Boolean booleanValue = (Boolean) value;
		if (value == null) {
			return;
		}
		if (booleanValue) {
			FormToolkit toolkit = ToolKitUtil.findToolkit(form);
			toolkit.decorateFormHeading(form);
		}
	}
}
