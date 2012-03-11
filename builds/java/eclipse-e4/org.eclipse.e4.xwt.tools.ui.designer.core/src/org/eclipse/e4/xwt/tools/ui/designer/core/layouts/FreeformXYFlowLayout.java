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
package org.eclipse.e4.xwt.tools.ui.designer.core.layouts;

/*
 *  $RCSfile: FreeformXYFlowLayout.java,v $
 *  $Revision: 1.2 $  $Date: 2010/06/18 00:15:22 $ 
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author pwalker
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class FreeformXYFlowLayout extends XYFlowLayout {

	/**
	 * Constructor for FreeformXYFlowLayout.
	 */
	public FreeformXYFlowLayout() {
		super();
	}

	/**
	 * @see org.eclipse.draw2d.XYLayout#getOrigin(IFigure)
	 */
	public Point getOrigin(IFigure parent) {
		return new Point();
	}

}
