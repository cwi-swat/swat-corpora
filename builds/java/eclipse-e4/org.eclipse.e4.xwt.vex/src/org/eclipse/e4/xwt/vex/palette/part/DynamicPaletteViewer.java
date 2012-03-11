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

import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;

public class DynamicPaletteViewer extends PaletteViewer {
	private boolean visible = true;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public DynamicPaletteViewer() {
		setKeyHandler(new PaletteViewerKeyHandler(this));
		setEditPartFactory(new ToolPaletteEditPartFactory());
	}

}
