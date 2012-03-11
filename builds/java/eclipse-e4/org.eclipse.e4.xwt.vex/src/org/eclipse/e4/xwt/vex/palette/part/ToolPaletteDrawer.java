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
package org.eclipse.e4.xwt.vex.palette.part;

import org.eclipse.gef.palette.PaletteDrawer;

/**
 * 
 */
public class ToolPaletteDrawer extends PaletteDrawer {

	public ToolPaletteDrawer(String label) {
		super(label);
	}

	@Override
	public boolean acceptsType(Object type) {
		return true;
	}
}
