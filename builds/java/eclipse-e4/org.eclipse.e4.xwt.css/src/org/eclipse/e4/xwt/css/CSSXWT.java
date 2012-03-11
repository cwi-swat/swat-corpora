/*******************************************************************************
 * Copyright (c) 2008, 2009 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.css;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * Helper method for get CSS engine from SWT widgets.
 * 
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class CSSXWT {

	private static final String CSSENGINE_KEY = "___CSSENGINE___";

	public static CSSEngine getCSSEngine(Widget widget) {
		// CSS engine is stored into Shell
		if (widget instanceof Control) {
			Shell shell = null;
			if (widget instanceof Shell) {
				shell = (Shell) widget;
			} else {
				shell = ((Control) widget).getShell();
			}
			return (CSSEngine) shell.getData(CSSENGINE_KEY);
		}
		return null;
	}

	public static void setCSSEngine(Shell shell, CSSEngine engine) {
		shell.setData(CSSENGINE_KEY, engine);
	}
}
