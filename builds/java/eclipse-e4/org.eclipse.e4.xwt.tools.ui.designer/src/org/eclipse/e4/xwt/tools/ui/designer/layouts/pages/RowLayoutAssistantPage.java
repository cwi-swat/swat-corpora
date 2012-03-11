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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class RowLayoutAssistantPage extends LayoutAssistantPage {

	public RowLayoutAssistantPage() {
		super(LayoutType.RowLayout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		Composite composite = createComposite(parent);
		composite.setLayout(new GridLayout(2, false));

		String[][] typeRadios = new String[][] { { "Horizontal", "SWT.HORIZONTAL" }, { "Vertical", "SWT.VERTICAL" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		createRadio(composite, Messages.RowLayoutAssistantPage_ORIENTATION_LABEL, Assistants.LAYOUT_ROW_TYPE, typeRadios);

		Group spacingGroup = createGroup(composite, Messages.RowLayoutAssistantPage_MARGIN_SPACING_LABEL, 2);

		createSpinner(spacingGroup, Assistants.LAYOUT_ROW_MARGIN_HEIGHT, Messages.RowLayoutAssistantPage_MARGIN_HEIGHT_LABEL);
		createSpinner(spacingGroup, Assistants.LAYOUT_ROW_MARGIN_WIDTH, Messages.RowLayoutAssistantPage_MARGIN_WIDTH_LABEL);
		createSpinner(spacingGroup, Assistants.LAYOUT_ROW_SPACING, Messages.RowLayoutAssistantPage_SPACING_LABEL);

		Group optionsGroup = createGroup(composite, Messages.RowLayoutAssistantPage_OPTIONS_NAME, 1);

		createCheckBox(optionsGroup, Assistants.LAYOUT_ROW_WRAP, Messages.RowLayoutAssistantPage_WRAP_LABEL);
		createCheckBox(optionsGroup, Assistants.LAYOUT_ROW_PACK, Messages.RowLayoutAssistantPage_PACK_LABEL);
		createCheckBox(optionsGroup, Assistants.LAYOUT_ROW_FILL, Messages.RowLayoutAssistantPage_FILL_LABEL);
		createCheckBox(optionsGroup, Assistants.LAYOUT_ROW_JUSTIFY, Messages.RowLayoutAssistantPage_JUSTIFY_LABEL);

		Group marginSideGroup = createGroup(composite, Messages.RowLayoutAssistantPage_MARGINS_GROUP_LABEL, 2);

		createSpinner(marginSideGroup, Assistants.LAYOUT_ROW_MARGIN_LEFT, Messages.RowLayoutAssistantPage_MARGIN_LEFT_LABEL);
		createSpinner(marginSideGroup, Assistants.LAYOUT_ROW_MARGIN_RIGHT, Messages.RowLayoutAssistantPage_MARGIN_RIGHT_LABEL);
		createSpinner(marginSideGroup, Assistants.LAYOUT_ROW_MARGIN_TOP, Messages.RowLayoutAssistantPage_MARGIN_TOP_LABEL);
		createSpinner(marginSideGroup, Assistants.LAYOUT_ROW_MARGIN_BOTTOM, Messages.RowLayoutAssistantPage_MARGIN_BOTTOM_LABEL);
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
			assistant = new RowLayout();
		}
		return assistant;
	}

}
