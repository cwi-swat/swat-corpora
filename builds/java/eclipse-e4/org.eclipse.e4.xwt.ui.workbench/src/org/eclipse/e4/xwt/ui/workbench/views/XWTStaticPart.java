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

import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.ui.workbench.IStaticPart;

/**
 * The default class to handle the connection with e4 workbench. As the value of
 * data context. by default, there are two possibilities: Selection and
 * DataContext
 * 
 * @author yyang (yves.yang@soyatec.com)
 */
public class XWTStaticPart extends XWTAbstractPart implements IStaticPart {
	
	@PostConstruct
	protected void postInit() {
		refresh();
	}

	protected void refresh() {
		if (isConstructing()) {
			return;
		}
		refresh(getURL(), getDataContext(), getClassLoader());
	}

	public URL getURL() {
		return this.getClass().getResource(
				this.getClass().getSimpleName()
						+ IConstants.XWT_EXTENSION_SUFFIX);
	}
	
	
	@Override
	public void setDataContext(Object dataContext) {
		super.setDataContext(dataContext);
		try {
			if (!isConstructing()) {
				refresh();
			}
		} catch (Exception e) {
		}
	}
}
