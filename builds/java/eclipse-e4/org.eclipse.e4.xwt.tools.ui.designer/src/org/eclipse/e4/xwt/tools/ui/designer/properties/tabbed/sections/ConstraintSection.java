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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.converters.StringToInteger;
import org.eclipse.e4.xwt.converters.StringToPoint;
import org.eclipse.e4.xwt.converters.StringToRectangle;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ChangeConstraintCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ResizeCommand;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.model.RefreshAdapter;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ConstraintSection extends AbstractAttributeSection {

	private Spinner xSpinner;
	private Spinner heightSpinner;
	private Spinner widthSpinner;
	private Spinner ySpinner;
	private int increment = 10;
	private Spinner incrementSpinner;
	private int widthCache = 0, heightCache = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getAttributeName()
	 */
	protected String getAttributeName() {
		return "";
	}

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
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite comp = getWidgetFactory().createComposite(parent);
		comp.setLayout(new GridLayout(2, false));
		comp
				.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
						3, 1));
		getWidgetFactory().createLabel(comp, "Increment of spinner:");
		incrementSpinner = createSpinner(comp);
		incrementSpinner.setIncrement(1);
		incrementSpinner.setMaximum(100);
		incrementSpinner.setMinimum(1);
		incrementSpinner.setSelection(increment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection
	 * #createClearButton(org.eclipse.swt.widgets.Composite)
	 */
	protected void createClearButton(Composite parent) {
		// remove the clear action.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createSection(Composite parent) {
		Composite control = getWidgetFactory().createComposite(parent);
		control.setLayout(new GridLayout(2, false));
		Group locationGroup = getWidgetFactory().createGroup(control,
				"Location");
		locationGroup.setLayout(new GridLayout(4, false));
		getWidgetFactory().createLabel(locationGroup, "x");
		xSpinner = createSpinner(locationGroup);
		getWidgetFactory().createLabel(locationGroup, "y");
		ySpinner = createSpinner(locationGroup);

		Group sizeGroup = getWidgetFactory().createGroup(control, "Size");
		sizeGroup.setLayout(new GridLayout(4, false));
		getWidgetFactory().createLabel(sizeGroup, "width");
		widthSpinner = createSpinner(sizeGroup);
		getWidgetFactory().createLabel(sizeGroup, "height");
		heightSpinner = createSpinner(sizeGroup);
		return control;
	}

	private Spinner createSpinner(Composite parent) {
		Spinner spinner = new Spinner(parent, SWT.BORDER);
		spinner.setMaximum(Integer.MAX_VALUE);
		spinner.setMinimum(-1);
		spinner.addListener(SWT.Selection, this);
		spinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		spinner.setIncrement(increment);
		return spinner;
	}

	public void doRefresh() {
		Rectangle r = getModelValue();
		if (r == null) {
			return;
		}
		if (xSpinner != null && !xSpinner.isDisposed()
				&& r.x != xSpinner.getSelection()) {
			xSpinner.removeListener(SWT.Selection, this);
			xSpinner.setSelection(r.x);
			xSpinner.addListener(SWT.Selection, this);
		}
		if (ySpinner != null && !ySpinner.isDisposed()
				&& r.y != ySpinner.getSelection()) {
			ySpinner.removeListener(SWT.Selection, this);
			ySpinner.setSelection(r.y);
			ySpinner.addListener(SWT.Selection, this);
		}
		if (widthSpinner != null && !widthSpinner.isDisposed()
				&& r.width != widthSpinner.getSelection()) {
			widthSpinner.removeListener(SWT.Selection, this);
			widthSpinner.setSelection(r.width);
			widthSpinner.addListener(SWT.Selection, this);
		}
		if (heightSpinner != null && !heightSpinner.isDisposed()
				&& r.height != heightSpinner.getSelection()) {
			heightSpinner.removeListener(SWT.Selection, this);
			heightSpinner.setSelection(r.height);
			heightSpinner.addListener(SWT.Selection, this);
		}
		widthCache = r.width;
		heightCache = r.height;
		LayoutType containerLayout = getContainerLayout();
		if (LayoutType.Unknown == containerLayout
				|| LayoutType.NullLayout == containerLayout) {
			xSpinner.setEnabled(true);
			ySpinner.setEnabled(true);
		} else {
			xSpinner.setEnabled(false);
			ySpinner.setEnabled(false);
		}
	}

	private Rectangle getModelValue() {
		Rectangle r = new Rectangle(-1, -1, -1, -1);
		LayoutType containerLayout = getContainerLayout();
		XamlNode parent = getParent();
		if (parent == null) {
			return r;
		}
		if (LayoutType.NullLayout == containerLayout) {
			XamlAttribute attribute = parent.getAttribute("bounds");
			if (attribute != null && attribute.getValue() != null) {
				org.eclipse.swt.graphics.Rectangle rect = (org.eclipse.swt.graphics.Rectangle) StringToRectangle.instance
						.convert(attribute.getValue());
				r.setLocation(rect.x, rect.y);
				r.setSize(rect.width, rect.height);
			} else {
				attribute = parent.getAttribute("location");
				if (attribute != null && attribute.getValue() != null) {
					Point location = (Point) StringToPoint.instance
							.convert(attribute.getValue());
					r.setLocation(location.x, location.y);
				}
				attribute = parent.getAttribute("size");
				if (attribute != null && attribute.getValue() != null) {
					Point size = (Point) StringToPoint.instance
							.convert(attribute.getValue());
					r.setSize(size.x, size.y);
				}
			}
		} else if (LayoutType.Unknown != containerLayout) {
			WidgetEditPart editPart = getEditPart();
			Widget widget = editPart.getWidget();
			IMetaclass metaclass = XWTUtility.getMetaclass(parent);
			XamlAttribute widthAttr = parent.getAttribute("width");
			if (widthAttr != null) {
				r.width = (Integer) StringToInteger.instance.convert(widthAttr
						.getValue());
			} else if (metaclass != null) {
				try {
					IProperty widthProperty = metaclass.findProperty("width");
					if (widthProperty != null) {
						r.width = (Integer) widthProperty.getValue(widget);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			XamlAttribute heightAttr = parent.getAttribute("height");
			if (heightAttr != null) {
				r.height = (Integer) StringToInteger.instance
						.convert(heightAttr.getValue());
			} else if (metaclass != null) {
				try {
					IProperty heightProperty = metaclass.findProperty("height");
					if (heightProperty != null) {
						r.height = (Integer) heightProperty.getValue(widget);
					}
				} catch (Exception e) {
					System.out.println();
				}
			}
		}
		return r;
	}

	private LayoutType getContainerLayout() {
		WidgetEditPart editPart = getEditPart();
		if (editPart == null) {
			return null;
		}
		return LayoutsHelper.getLayoutType(editPart.getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget == incrementSpinner) {
			increment = incrementSpinner.getSelection();
			xSpinner.setIncrement(increment);
			ySpinner.setIncrement(increment);
			widthSpinner.setIncrement(increment);
			heightSpinner.setIncrement(increment);
			return;
		}
		LayoutType containerLayout = getContainerLayout();
		if (LayoutType.NullLayout == containerLayout) {
			Rectangle r = new Rectangle();
			r.x = xSpinner.getSelection();
			r.y = ySpinner.getSelection();
			r.width = widthCache = widthSpinner.getSelection();
			r.height = heightCache = heightSpinner.getSelection();
			ChangeConstraintCommand cmd = new ChangeConstraintCommand(
					getEditPart(), r);
			executeCommand(cmd);
		} else if (LayoutType.Unknown != containerLayout) {
			Dimension growth = new Dimension();
			if (event.widget == widthSpinner) {
				growth.width = widthSpinner.getSelection() - widthCache;
				widthCache = widthSpinner.getSelection();
			} else if (event.widget == heightSpinner) {
				growth.height = heightSpinner.getSelection() - heightCache;
				heightCache = heightSpinner.getSelection();
			}
			ResizeCommand cmd = new ResizeCommand(getEditPart(), growth);
			executeCommand(cmd);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getRefresher()
	 */
	protected RefreshAdapter getRefresher() {
		RefreshAdapter refresher = super.getRefresher();
		refresher.addListenedAttr("bounds");
		refresher.addListenedAttr("size");
		refresher.addListenedAttr("location");
		refresher.addListenedAttr("width");
		refresher.addListenedAttr("height");
		return refresher;
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
