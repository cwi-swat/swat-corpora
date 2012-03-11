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
package org.eclipse.e4.ui.css.nebula.dom;

import org.eclipse.e4.ui.css.core.dom.IElementProvider;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.nebula.helpers.NebulaElementHelpers;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.Element;

/**
 * {@link IElementProvider} SWT and Nebula widget implementation to retrieve w3c
 * Element {@link SWTElement} linked to SWT widget.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class NebulaElementProvider implements IElementProvider {

	public static final IElementProvider INSTANCE = new NebulaElementProvider();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.css.swt.dom.SWTElementProvider#getElement(java.lang.Object,
	 *      org.eclipse.e4.ui.css.core.engine.CSSEngine)
	 */
	public Element getElement(Object element, CSSEngine engine) {
		if (element instanceof Widget) {
			// element is SWT Widget
			Widget widget = (Widget) element;
			// Return the w3c Element linked to the SWT widget.
			return NebulaElementHelpers.getElement(widget, engine);
		}
		return null;
	}
}
