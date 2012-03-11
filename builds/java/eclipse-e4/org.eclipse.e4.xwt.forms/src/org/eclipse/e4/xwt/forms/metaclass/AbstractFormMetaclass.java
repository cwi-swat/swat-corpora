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

import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTLoaderManager;
import org.eclipse.e4.xwt.forms.ToolKitUtil;
import org.eclipse.e4.xwt.javabean.metadata.Metaclass;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 
 * @author Erdal Karaca <erdal.karaca.de@googlemail.com>
 * @author yves.yang (yves.yang@soyatec.com)
 */
public abstract class AbstractFormMetaclass extends Metaclass {

	public AbstractFormMetaclass(Class<?> formFeatureClass) {
		this(formFeatureClass, toSuperMetaclass(formFeatureClass),
				XWTLoaderManager.getActive());
	}

	public AbstractFormMetaclass(Class<?> formFeatureClass,
			IMetaclass parentMetaclass, IXWTLoader loader) {
		super(formFeatureClass, parentMetaclass, loader);
	}

	static private IMetaclass toSuperMetaclass(Class<?> type) {
		Class<?> superType = type.getSuperclass();
		if (superType == null || superType == Object.class) {
			return null;
		}
		return XWT.getMetaclass(superType);
	}

	@Override
	public Object doNewInstance(Object[] parameters) {
		if (parameters.length > 0 && parameters[0] instanceof Composite) {
			Composite parent = (Composite) parameters[0];
			FormToolkit tk = ToolKitUtil.getToolkit(parent);
			int style = SWT.None;
			if (parameters.length > 1 && parameters[1] instanceof Integer) {
				style |= (Integer)parameters[1];
			}
			Control control = doCreateControl(tk, parent, style);
			ToolKitUtil.tagForm(control);
			return control;
		}

		return super.newInstance(parameters);
	}

	protected abstract Control doCreateControl(FormToolkit tk,
			Composite parent, int style);
}
