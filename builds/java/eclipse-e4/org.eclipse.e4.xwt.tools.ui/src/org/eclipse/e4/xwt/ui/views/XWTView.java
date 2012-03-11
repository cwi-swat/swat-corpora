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
package org.eclipse.e4.xwt.ui.views;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.ILoadingContext;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.ui.ExceptionHandle;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;

public class XWTView extends ViewPart {

	public static final String ID = "org.eclipse.e4.xwt.ui.views.XWTView";

	protected Composite container;
	protected Map<String, Object> options;

	/**
	 * The constructor.
	 */
	public XWTView() {
		options = new HashMap<String, Object>();
		options.put(IXWTLoader.DESIGN_MODE_PROPERTY, Boolean.TRUE);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout());
		container.setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	public void setContent(String code, IFile file) {
		try {
			setContentWithException(code, file, new ProjectContentProvider(file));
		} catch (Exception e) {
			ExceptionHandle.handle(e, "Open view fails");
		}
	}

	public void setContentWithException(String code, IFile file, IContentProvider contentProvider) throws Exception {
		XWTUIPlugin.checkStartup();
		for (Control child : container.getChildren()) {
			child.dispose();
		}
		ILoadingContext loadingContext = contentProvider.getLoadingContext();
		if (loadingContext != null) {
			XWT.setLoadingContext(loadingContext);
		}
		options.put(IXWTLoader.CONTAINER_PROPERTY, container);
		if (code != null) {
			XWT.loadWithOptions(new ByteArrayInputStream(code.getBytes()), file.getLocation().toFile().toURL(), options);
		} else {
			XWT.loadWithOptions(file.getLocation().toFile().toURL(), options);
		}
		container.layout(true, true);
	}

	public void setContentWithException(String code, IFile file) throws Exception {
		setContentWithException(code, file, new ProjectContentProvider(file));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
}
