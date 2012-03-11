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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FillLayoutAssistantPage extends LayoutAssistantPage {

	public FillLayoutAssistantPage() {
		super(LayoutType.FillLayout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.ICustomPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		Composite composite = createComposite(parent);
		composite.setLayout(new GridLayout(2, false));

		createRadio(composite, Messages.FillLayoutAssistantPage_ORIENTATION_LABEL, Assistants.LAYOUT_FILL_TYPE, new String[][] { { "Horizontal", "SWT.HORIZONTAL" }, { "Vertical", "SWT.VERTICAL" } }); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		Group spacingGroup = createGroup(composite, Messages.FillLayoutAssistantPage_SPACING_GROUP_LABEL, 2);
		createSpinner(spacingGroup, Assistants.LAYOUT_FILL_MARGIN_HEIGHT, Messages.FillLayoutAssistantPage_MARGIN_HEIGHT_LABEL);
		createSpinner(spacingGroup, Assistants.LAYOUT_FILL_MARGIN_WIDTH, Messages.FillLayoutAssistantPage_MARGIN_WIDTH_LABEL);
		createSpinner(spacingGroup, Assistants.LAYOUT_FILL_SPACING, Messages.FillLayoutAssistantPage_SPACING_LABEL);
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
			assistant = new FillLayout();
		}
		return assistant;
	}

}
