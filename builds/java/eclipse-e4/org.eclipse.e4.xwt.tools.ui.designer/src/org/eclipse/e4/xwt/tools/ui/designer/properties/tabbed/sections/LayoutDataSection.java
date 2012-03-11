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
package org.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections;

import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutDataType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.AssistancePageFactory;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class LayoutDataSection extends AbstractAttributeSection {

	private IAssistantPage layoutDataPage;
	private Composite pageArea;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection
	 * #createControls(org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		_createControls(parent, aTabbedPropertySheetPage);
		ExpandableComposite expandable = getWidgetFactory()
				.createExpandableComposite(
						parent,
						ExpandableComposite.TWISTIE
								| ExpandableComposite.EXPANDED);
		expandable.setText("Values");
		expandable.setExpanded(true);

		ToolBar toolBar = new ToolBar(expandable, SWT.FLAT);
		toolBar.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		ToolItem clearAction = new ToolItem(toolBar, SWT.PUSH);
		clearAction.setImage(ImageShop.get(ImageShop.IMG_CLEAR_FILTER));
		clearAction.setToolTipText("Clear setting values.");
		clearAction.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				clearValues();
			}
		});
		expandable.setTextClient(toolBar);

		pageArea = getWidgetFactory().createComposite(expandable);
		pageArea.setLayout(new FillLayout());
		expandable.setClient(pageArea);
		WidgetEditPart editPart = getEditPart();
		if (editPart != null) {
			refresh();
		}
	}

	protected void clearValues() {
		if (layoutDataPage != null) {
			layoutDataPage.performDefault();
		}
	}

	public void doRefresh() {
		LayoutDataType layoutDataType = LayoutsHelper
				.getLayoutDataType(getEditPart());
		if (layoutDataType == LayoutDataType.Unknown) {
			return;
		}
		if (layoutDataPage == null) {
			layoutDataPage = AssistancePageFactory.createPage(layoutDataType);
			if (layoutDataPage != null) {
				layoutDataPage.getControl(pageArea);
				pageArea.layout();
			}
		}
		if (layoutDataPage != null) {
			layoutDataPage.setEditPart(getEditPart());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createSection(Composite parent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getAttributeName()
	 */
	protected String getAttributeName() {
		return "layoutData";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getNewValue(org.eclipse.swt.widgets.Event)
	 */
	protected String getNewValue(Event event) {
		return null;
	}
}
