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
package org.eclipse.e4.xwt.tools.ui.designer.parts;

import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.CoolBarEditPolicy;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;

public class CoolBarEditPart extends CompositeEditPart {

	public CoolBarEditPart(CoolBar coolBar, XamlNode model) {
		super(coolBar, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editparts.WidgetEditPart#collectExternalModels()
	 */
	protected void collectExternalModels(List<Object> collector) {
		CoolBar coolBar = (CoolBar) getWidget();
		if (coolBar != null && !coolBar.isDisposed()) {
			Control[] children = coolBar.getChildren();
			for (Control control : children) {
				Object data = XWTProxy.getModel(control);
				if (data != null) {
					collector.add(data);
				}
			}
		}
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CoolBarEditPolicy());
	}
}
