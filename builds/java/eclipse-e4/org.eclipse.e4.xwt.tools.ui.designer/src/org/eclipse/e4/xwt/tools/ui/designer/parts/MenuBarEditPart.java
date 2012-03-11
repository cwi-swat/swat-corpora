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
package org.eclipse.e4.xwt.tools.ui.designer.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ContentPaneFigure;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.MenuBarLayoutEditPolicy;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.widgets.Menu;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class MenuBarEditPart extends MenuEditPart {

	public MenuBarEditPart(Menu menuBar, XamlNode model) {
		super(menuBar, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.editor.editparts.GraphicalNodeEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		ContentPaneFigure figure = new ContentPaneFigure();
		RectangleFigure rFigure = new RectangleFigure();
		rFigure.setFill(false);
		rFigure.setForegroundColor(ColorConstants.menuBackground);
		rFigure.setLayoutManager(new XYLayout());
		figure.setContentPane(rFigure);
		return figure;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		// removeEditPolicy(EditPolicy.LAYOUT_ROLE);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new MenuBarLayoutEditPolicy());
	}

}
