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

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;

/**
 * This part uses the Selection variable as data context
 * 
 * @author yyang (yves.yang@soyatec.com)
 */
public class XWTSelectionStaticPart extends XWTStaticPart {

	public XWTSelectionStaticPart() {
	}

	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.SELECTION) Object selection) {
		dataContext = selection;
		if (isConstructing()) {
			return;
		}
		refresh();
	}
}
