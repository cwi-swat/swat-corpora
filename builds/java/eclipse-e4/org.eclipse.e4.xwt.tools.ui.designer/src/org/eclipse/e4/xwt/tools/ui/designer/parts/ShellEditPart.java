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
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ShellEditPart extends CompositeEditPart {

	public ShellEditPart(Shell shell, XamlNode model) {
		super(shell, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editparts.WidgetEditPart#collectExternalModels()
	 */
	protected void collectExternalModels(List<Object> collector) {
		super.collectExternalModels(collector);
		Shell shell = (Shell) getWidget();
		if (shell != null && !shell.isDisposed()) {
			Menu menuBar = shell.getMenuBar();
			if (menuBar != null && !menuBar.isDisposed()) {
				Object data = XWTProxy.getModel(menuBar);
				if (data != null) {
					collector.add(data);
				}
			}
		}
	}
}
