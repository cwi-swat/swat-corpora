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
package org.eclipse.e4.xwt.tools.ui.palette.root;

import org.eclipse.gef.palette.PaletteDrawer;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class PaletteDrawerEx extends PaletteDrawer {

	/**
	 * @param label
	 */
	public PaletteDrawerEx(String label) {
		super(label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.palette.PaletteDrawer#acceptsType(java.lang.Object)
	 */
	public boolean acceptsType(Object type) {
		return true;
	}
}
