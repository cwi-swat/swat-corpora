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
package org.eclipse.e4.xwt.tools.ui.designer.loader.metadata;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.xwt.javabean.metadata.properties.AbstractProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class WidthProperty extends AbstractProperty {

	public WidthProperty() {
		super("width", int.class);
	}

	public Object getValue(Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchFieldException {
		if (target == null || !Control.class.isAssignableFrom(target.getClass())) {
			return SWT.DEFAULT;
		}
		Control control = (Control) target;
		int width = control.getSize().x;
		if (width < 0) {
			Layout layout = control.getParent().getLayout();
			Object layoutData = control.getLayoutData();
			if (layout instanceof GridLayout && layoutData instanceof GridData) {
				width = ((GridData) layoutData).widthHint;
			} else if (layout instanceof RowLayout && layoutData instanceof RowData) {
				width = ((RowData) layoutData).width;
			} else if (layout instanceof FormLayout && layoutData instanceof FormData) {
				width = ((FormData) layoutData).width;
			}
		}
		return width;
	}

	public void setValue(Object target, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchFieldException {
		if (target == null || !Control.class.isAssignableFrom(target.getClass())) {
			return;
		}
		if (value == null || !(value instanceof Integer)) {
			return;
		}
		int width = (Integer) value;
		if (width <= 0) {
			width = SWT.DEFAULT;
		}
		Control control = (Control) target;
		Layout layout = control.getParent().getLayout();
		Object layoutData = control.getLayoutData();
		if (layout == null) {
			Point size = control.getSize();
			control.setSize(width, size.y);
		} else if (layout instanceof GridLayout) {
			if (layoutData == null || !(layoutData instanceof GridData)) {
				layoutData = new GridData();
				control.setLayoutData(layoutData);
			}
			((GridData) layoutData).widthHint = width;
		} else if (layout instanceof RowLayout) {
			if (layoutData == null || !(layoutData instanceof RowData)) {
				layoutData = new RowData();
				control.setLayoutData(layoutData);
			}
			((RowData) layoutData).width = width;
		} else if (layout instanceof FormLayout) {
			if (layoutData == null || !(layoutData instanceof FormData)) {
				layoutData = new FormData();
				control.setLayoutData(layoutData);
			}
			((FormData) layoutData).width = width;
		}
	}
}