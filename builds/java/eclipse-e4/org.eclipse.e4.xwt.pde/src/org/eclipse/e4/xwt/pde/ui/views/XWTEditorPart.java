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
package org.eclipse.e4.xwt.pde.ui.views;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.e4.xwt.DefaultLoadingContext;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.databinding.BindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * 
 * @author yyang
 */
public abstract class XWTEditorPart extends EditorPart {
	protected Composite container;
	protected Object dataContext;

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setInput(input);
		setSite(site);
	}

	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new FillLayout());
		container.setBackgroundMode(SWT.INHERIT_DEFAULT);
		updateContent();
	}

	public void setContent(URL file) {
		XWT.setLoadingContext(new DefaultLoadingContext(this.getClass()
				.getClassLoader()));

		for (Control child : container.getChildren()) {
			child.dispose();
		}

		try {
			XWT.load(file);
			container.layout(true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContent(InputStream inputStream, URL base) {
		XWT.setLoadingContext(new DefaultLoadingContext(getClassLoader()));

		for (Control child : container.getChildren()) {
			child.dispose();
		}

		try {
			XWT.load(container, inputStream, base, getDataContext());
			container.layout(true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}

	public Object getDataContext() {
		if (dataContext == null) {
			dataContext = createDataContext();
		}
		return dataContext;
	}

	/**
	 * Create the data context from IEditorInput
	 * 
	 * @return
	 */
	protected abstract Object createDataContext();

	/**
	 * update the editor content
	 */
	protected abstract void updateContent();
}
