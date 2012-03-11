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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ContentPaneFigure;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.swt.widgets.Item;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ItemEditPart extends WidgetEditPart {

	public ItemEditPart(Item item, XamlNode model) {
		super(item, model);
	}

	protected IFigure createFigure() {
		ContentPaneFigure figure = new ContentPaneFigure();
		figure.add(new Label());
		return figure;
	}
}
