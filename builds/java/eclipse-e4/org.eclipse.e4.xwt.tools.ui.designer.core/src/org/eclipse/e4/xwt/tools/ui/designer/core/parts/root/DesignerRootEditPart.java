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
package org.eclipse.e4.xwt.tools.ui.designer.core.parts.root;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DesignerRootEditPart extends ScalableFreeformRootEditPart {

	protected void createLayers(LayeredPane layeredPane) {
		super.createLayers(layeredPane);
		layeredPane.add(new BindingLayer(), BindingLayer.ID);
	}

	public void setLayerVisible(Object layerId, boolean visible) {
		IFigure layer = getLayer(layerId);
		if (layer != null) {
			layer.setVisible(visible);
		}
	}

	public Boolean getLayerVisible(Object layerId) {
		IFigure layer = getLayer(layerId);
		if (layer != null) {
			return layer.isVisible();
		}
		return Boolean.FALSE;
	}
}
