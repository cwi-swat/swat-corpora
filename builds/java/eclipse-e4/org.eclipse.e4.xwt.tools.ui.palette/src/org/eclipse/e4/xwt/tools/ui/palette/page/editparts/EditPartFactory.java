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
package org.eclipse.e4.xwt.tools.ui.palette.page.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class EditPartFactory extends PaletteEditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.palette.PaletteEditPartFactory#createDrawerEditPart
	 * (org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected EditPart createDrawerEditPart(EditPart parentEditPart, Object model) {
		return new PaletteDrawerEditPart((PaletteDrawer) model);
	}
}
