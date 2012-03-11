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
package org.eclipse.e4.xwt.tools.ui.designer.core.visuals.swt;

import org.eclipse.e4.xwt.tools.ui.designer.core.util.Draw2dTools;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.WidgetLocator;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Scrollable;

/**
 * @author jin.liu(jin.liu@soyatec.com)
 */
public class CompositeInfo extends ControlInfo {

	public CompositeInfo(Object visualObject, boolean isRoot) {
		super(visualObject, isRoot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.designer.core.visuals.VisualInfo#getClientArea
	 * ()
	 */
	public org.eclipse.draw2d.geometry.Rectangle getClientArea() {
		if (visualObject instanceof Scrollable) {
			// get the display-relative location.
			Rectangle bounds = WidgetLocator.getBounds(
					(Scrollable) visualObject, true);
			Rectangle clientArea = ((Scrollable) visualObject).getClientArea();
			Rectangle calced = ((Scrollable) visualObject).computeTrim(0, 0, 0, 0);
			bounds.width = clientArea.width;
			bounds.height = clientArea.height;
			bounds.x += -calced.x;
			bounds.y += -calced.y;
			return Draw2dTools.toDraw2d(bounds);
		}
		return super.getClientArea();
	}
}
