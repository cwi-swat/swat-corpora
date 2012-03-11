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
package org.eclipse.e4.xwt.tools.ui.designer.layouts.pages;

import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutDataType;
import org.eclipse.e4.xwt.tools.ui.designer.resources.Messages;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class RowDataAssistantPage extends LayoutDataAssistantPage {

	public RowDataAssistantPage() {
		super(LayoutDataType.RowData);
	}

	/**
	 * @see org.soyatec.xaml.ve.xwt.editparts.layouts.pages.CustomizeLayoutPage#creatControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createControl(Composite parent) {
		Composite composite = createComposite(parent);
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		composite.setLayout(grid);

		Group sizeGroup = createGroup(composite, Messages.RowDataAssistantPage_SIZE_GROUP_LABEL, 2);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		sizeGroup.setLayoutData(gd);

		createSpinner(sizeGroup, Assistants.LAYOUTDATA_ROW_WIDTH, Messages.RowDataAssistantPage_WIDTH_LABEL);
		createSpinner(sizeGroup, Assistants.LAYOUTDATA_ROW_HEIGHT, Messages.RowDataAssistantPage_HEIGHT_LABEL);
		createCheckBox(composite, Assistants.LAYOUTDATA_ROW_EXCLUDE, Messages.RowDataAssistantPage_EXCLUDE_LABEL);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.AssistantPage#getAssistant()
	 */
	protected Object getAssistant() {
		Object assistant = super.getAssistant();
		if (assistant == null) {
			assistant = new RowData();
		}
		return assistant;
	}

}
