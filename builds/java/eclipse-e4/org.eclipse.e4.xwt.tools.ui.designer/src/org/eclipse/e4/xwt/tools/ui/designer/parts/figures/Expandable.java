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
package org.eclipse.e4.xwt.tools.ui.designer.parts.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public interface Expandable extends IFigure {

	public void expand();

	public void collapse();

	public GraphicalEditPart getHost();
}
