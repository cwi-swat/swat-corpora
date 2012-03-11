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

import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.resources.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class GridLayoutAssistantPage extends LayoutAssistantPage {

	public GridLayoutAssistantPage() {
		super(LayoutType.GridLayout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		Composite control = createComposite(parent);
		control.setLayout(new GridLayout());

		Composite numsComp = createComposite(control);
		// numsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		numsComp.setLayout(layout);
		createSpinner(numsComp, Assistants.LAYOUT_GRID_NUM_CLUMNS, Messages.GridLayoutAssistantPage_NUM_COLUMNS_LABEL);

		createCheckBox(numsComp, Assistants.LAYOUT_GRID_MAKE_COLUMNS_EQUAL_WIDTH, Messages.GridLayoutAssistantPage_EQUAL_WIDTH_LABEL);

		Composite bottom = createComposite(control);
		GridLayout bottomLayout = new GridLayout(2, false);
		bottomLayout.marginWidth = 0;
		bottomLayout.marginHeight = 0;
		bottom.setLayout(bottomLayout);
		bottom.setLayoutData(new GridData(GridData.FILL_BOTH));
		{// Margins
			Group bottomLeft = createGroup(bottom, Messages.GridLayoutAssistantPage_MARGINS_SIDES_GROUP_LABEL, 2);
			GridData layoutData = new GridData();
			layoutData.verticalAlignment = SWT.TOP;
			bottomLeft.setLayoutData(layoutData);
			createSpinner(bottomLeft, Assistants.LAYOUT_GRID_MARGIN_LEFT, Messages.GridLayoutAssistantPage_M_LEFT_LABEL);

			createSpinner(bottomLeft, Assistants.LAYOUT_GRID_MARGIN_TOP, Messages.GridLayoutAssistantPage_M_TOP_LABEL);

			createSpinner(bottomLeft, Assistants.LAYOUT_GRID_MARGIN_RIGHT, Messages.GridLayoutAssistantPage_M_RIGHT_LABEL);

			createSpinner(bottomLeft, Assistants.LAYOUT_GRID_MARGIN_BOTTOM, Messages.GridLayoutAssistantPage_M_BOTTOM_LABEL);
		}
		Composite bottomRight = createComposite(bottom);
		GridLayout bottomRightLayout = new GridLayout();
		bottomRightLayout.marginWidth = 0;
		bottomRightLayout.marginHeight = 0;
		bottomRight.setLayout(bottomRightLayout);
		bottomRight.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		{//
			Group marginsGrp = createGroup(bottomRight, Messages.GridLayoutAssistantPage_MARGINS_GROUP_LABEL, 2);
			marginsGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			createSpinner(marginsGrp, Assistants.LAYOUT_GRID_MARGIN_WIDTH, Messages.GridLayoutAssistantPage_M_WIDTH_LABEL);
			createSpinner(marginsGrp, Assistants.LAYOUT_GRID_MARGIN_HEIGHT, Messages.GridLayoutAssistantPage_M_HEIGHT_LABEL);
		}
		{//
			Group marginsGrp = createGroup(bottomRight, Messages.GridLayoutAssistantPage_SPACING_GROUP_LABEL, 2);
			marginsGrp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			createSpinner(marginsGrp, Assistants.LAYOUT_GRID_HORIZONTAL_SPACING, Messages.GridLayoutAssistantPage_H_SPACING_LABEL);
			createSpinner(marginsGrp, Assistants.LAYOUT_GRID_VERTICAL_SPACING, Messages.GridLayoutAssistantPage_V_SPACING_LABEL);
		}
		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.AssistantPage#getAssistant()
	 */
	protected Object getAssistant() {
		Object assistant = super.getAssistant();
		if (assistant == null) {
			assistant = new GridLayout();
		}
		return assistant;
	}

}
