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
package org.eclipse.e4.xwt.ui.workbench.views;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTLoader;
import org.eclipse.e4.xwt.ui.workbench.IPartContentProvider;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 * @author yyang (yves.yang@soyatec.com)
 */
public abstract class XWTDynamicPart extends XWTAbstractPart {
	protected IPartContentProvider contentProvider;

	protected IPartContentProvider getContentProvider() {
		if (contentProvider == null) {
			contentProvider = createContentProvider();
		}
		return contentProvider;
	}

	protected abstract IPartContentProvider createContentProvider();

	@PostConstruct
	protected void init() {
		refresh(getDataContext());
	}

	public void refresh(Object dataContext) {
		if (isConstructing()) {
			return;
		}
		Composite parent = getParent();
		parent.setVisible(false);
		for (Control child : parent.getChildren()) {
			child.dispose();
		}
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					getContentProvider().getClassLoader());
			HashMap<String, Object> newOptions = new HashMap<String, Object>();
			newOptions.put(XWTLoader.CONTAINER_PROPERTY, parent);
			newOptions.put(XWTLoader.DATACONTEXT_PROPERTY, dataContext);
			newOptions.put(XWTLoader.CLASS_PROPERTY, this);
			XWT.loadWithOptions(getContentProvider().getContent(),
					getContentProvider().getBase(), newOptions);
			GridLayoutFactory.fillDefaults().generateLayout(parent);
			parent.layout(true, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Thread.currentThread().setContextClassLoader(classLoader);
			parent.setVisible(true);
		}
	}
}
