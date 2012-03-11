/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.css;

import org.eclipse.e4.xwt.INamespaceHandler;
import org.eclipse.swt.widgets.Widget;

public class CSSHandler implements INamespaceHandler {
	public static final String NAMESPACE = "http://www.eclipse.org/css";
	public static final CSSHandler handler = new CSSHandler();

	public void handleAttribute(Widget widget, Object target, String name,
			String value) {
		widget.setData("org.eclipse.e4.ui.css." + name, value);
	}
}
