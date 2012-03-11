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

import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.LabelProviderFactory;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.ArrayCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.BooleanCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.CharacterCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.ColorCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.FontCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.ImageCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.IntegerCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.PointCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors.RectangleCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.properties.editors.LayoutCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.properties.editors.ObjectCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.properties.editors.TextCellEditor;
import org.eclipse.e4.xwt.tools.ui.designer.properties.editors.TypedPropertiesCellEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTPropertyDescriptor extends PropertyDescriptor {

	private PropertyContext fContext;

	public XWTPropertyDescriptor(PropertyContext fContext, IProperty id) {
		super(id, id.getName());
		this.fContext = fContext;
		initLabelProvider(id);
	}

	private void initLabelProvider(IProperty property) {
		Class<?> type = property.getType();
		String name = property.getName();
		ILabelProvider labelProvider = null;
		if (Integer.class == type || int.class == type) {
			if (TypedProperties.getTypes().contains(name)) {
				labelProvider = TypedProperties.getLabelProvider(name);
			}
		} else {
			labelProvider = LabelProviderFactory.getLabelProvider(property.getType());
		}
		setLabelProvider(labelProvider);
	}

	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = createCellEditor((IProperty) getId(), parent);
		if (editor != null && getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}

	public CellEditor createCellEditor(IProperty property, Composite parent) {
		Class<?> type = property.getType();
		String name = property.getName();
		CellEditor editor = null;
		if (Color.class == type) {
			editor = new ColorCellEditor(parent);
		} else if (Font.class == type) {
			editor = new FontCellEditor(parent);
		} else if (Boolean.class == type || boolean.class == type) {
			editor = new BooleanCellEditor(parent);
		} else if (Image.class == type) {
			editor = new ImageCellEditor(parent);
		} else if (Point.class == type) {
			editor = new PointCellEditor(parent);
		} else if (Rectangle.class == type) {
			editor = new RectangleCellEditor(parent);
		} else if (Layout.class == type) {
			editor = new LayoutCellEditor(parent);
		} else if (Integer.class == type || int.class == type) {
			if (TypedProperties.getTypes().contains(name)) {
				editor = new TypedPropertiesCellEditor(parent, name);
			} else {
				editor = new IntegerCellEditor(parent);
			}
		} else if (IContentProvider.class.isAssignableFrom(type) || ILabelProvider.class.isAssignableFrom(type)) {
			editor = new ObjectCellEditor(parent, type);
		} else if (String.class == type) {
			return new TextCellEditor(fContext, property, parent);
		} else if (Character.class == type || char.class == type) {
			return new CharacterCellEditor(parent);
		} else if (String[].class == type) {
			return new ArrayCellEditor(parent);
		}
		return editor;
	}
}
