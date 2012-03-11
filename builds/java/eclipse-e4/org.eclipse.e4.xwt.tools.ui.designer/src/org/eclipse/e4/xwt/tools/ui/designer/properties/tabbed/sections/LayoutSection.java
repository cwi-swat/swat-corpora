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

import java.util.List;
import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.designer.commands.ChangeLayoutCommand;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.AssistancePageFactory;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class LayoutSection extends AbstractAttributeSection {

	private CCombo layoutCombo;
	private Composite layoutPage;
	private StackLayout pageLayout;
	private Label noneInfoLable;

	private Map<Object, IAssistantPage> pages = AssistancePageFactory
			.newPages();

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		_createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory().createComposite(parent);
		composite.setLayout(new GridLayout(3, false));

		getWidgetFactory().createLabel(composite, "Type:");

		Control control = createSection(composite);
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createClearButton(composite);

		ExpandableComposite expandable = getWidgetFactory()
				.createExpandableComposite(
						composite,
						ExpandableComposite.TWISTIE
								| ExpandableComposite.EXPANDED);
		expandable.setText("Values");
		expandable.setExpanded(true);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalSpan = 3;
		expandable.setLayoutData(layoutData);
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

		layoutPage = getWidgetFactory().createComposite(expandable);
		expandable.setClient(layoutPage);
		pageLayout = new StackLayout();
		layoutPage.setLayout(pageLayout);
		noneInfoLable = getWidgetFactory().createLabel(layoutPage,
				"There is no layout values.", SWT.TOP | SWT.CENTER);
	}

	protected void clearValues() {
		Object data = pageLayout.topControl.getData();
		if (data instanceof IAssistantPage) {
			((IAssistantPage) data).performDefault();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createSection(Composite parent) {
		layoutCombo = getWidgetFactory().createCCombo(parent, SWT.READ_ONLY);
		List<LayoutType> layoutsList = LayoutsHelper.layoutsList;
		String[] items = new String[layoutsList.size()];
		for (int i = 0; i < layoutsList.size(); i++) {
			items[i] = layoutsList.get(i).value();
		}
		layoutCombo.setItems(items);
		layoutCombo.addListener(SWT.Selection, this);

		return layoutCombo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getAttributeName()
	 */
	protected String getAttributeName() {
		return "layout";
	}

	public void doRefresh() {
		if (layoutCombo == null || layoutCombo.isDisposed()) {
			return;
		}
		LayoutType layoutType = LayoutsHelper.getLayoutType(getEditPart());
		int index = layoutCombo.indexOf(layoutType.value());
		if (index >= 0 && index != layoutCombo.getSelectionIndex()) {
			layoutCombo.removeListener(SWT.Selection, this);
			layoutCombo.select(index);
			refreshLayoutPage(layoutType);
			layoutCombo.addListener(SWT.Selection, this);
		}
	}

	private void refreshLayoutPage(LayoutType layoutType) {
		if (layoutPage == null || layoutPage.isDisposed()) {
			return;
		}
		WidgetEditPart editPart = getEditPart();
		IAssistantPage page = pages.get(layoutType);
		if (page != null) {
			pageLayout.topControl = page.getControl(layoutPage);
			page.setEditPart(editPart);
		} else {
			pageLayout.topControl = noneInfoLable;
		}
		layoutPage.layout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		WidgetEditPart editPart = getEditPart();
		if (editPart != null && event.widget == layoutCombo) {
			String item = layoutCombo.getItem(layoutCombo.getSelectionIndex());
			LayoutType layoutType = LayoutsHelper.getLayoutType(item);
			executeCommand(new ChangeLayoutCommand(editPart, layoutType));
			refreshLayoutPage(layoutType);
		}
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
