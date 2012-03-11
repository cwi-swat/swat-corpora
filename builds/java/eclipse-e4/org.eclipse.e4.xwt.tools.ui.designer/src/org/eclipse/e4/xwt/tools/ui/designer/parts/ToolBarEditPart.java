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
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ToolBarLayoutEditPolicy;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class ToolBarEditPart extends CompositeEditPart {

	public ToolBarEditPart(ToolBar toolBar, XamlNode model) {
		super(toolBar, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.parts.ControlEditPart#collectExternalModels()
	 */
	protected void collectExternalModels(List<Object> collector) {
		ToolBar toolBar = (ToolBar) getWidget();
		if (toolBar != null && !toolBar.isDisposed()) {
			Control[] children = toolBar.getChildren();
			for (Control control : children) {
				Object data = XWTProxy.getModel(control);
				if (data != null) {
					collector.add(data);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ToolBarLayoutEditPolicy());
	}
}
