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

import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.IVisualFactory;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.DataContext;
import org.eclipse.e4.xwt.tools.ui.designer.utils.StyleHelper;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.palette.page.editparts.EditPartFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class XWTEditPartFactory extends EditPartFactory {

	private IVisualFactory factory;

	public void setVisualFactory(IVisualFactory factory) {
		this.factory = factory;
	}

	public IVisualFactory getVisualFactory() {
		return factory;
	}

	/**
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof XamlDocument) {
			return new DiagramEditPart((XamlDocument) model);
		} else if (model instanceof DataContext) {
			return new DataContextEditPart((DataContext) model);
		} else if (model instanceof BindingInfo) {
			return new BindingConnectionEditPart((BindingInfo) model);
		} else if (model instanceof XamlNode) {
			XamlNode element = (XamlNode) model;
			Object visual = null;
			if (factory != null) {
				visual = factory.getVisual(element);
			}
			Class<?> type = null;
			if (visual != null) {
				type = visual.getClass();
			} else if (element instanceof XamlElement) {
				IMetaclass metaclass = XWTUtility
						.getMetaclass((XamlElement) model);
				if (metaclass != null && metaclass.getType() != null) {
					type = metaclass.getType();
				}
			}
			if (type == null) {
				throw new UnsupportedOperationException("Unsupport Type: "
						+ model.toString());
			}
			if (Shell.class.isAssignableFrom(type)) {
				return new ShellEditPart((Shell) visual, element);
			} else if (TabFolder.class.isAssignableFrom(type)
					|| CTabFolder.class.isAssignableFrom(type)) {
				return new TabFolderEditPart((Composite) visual, element);
			} else if (CoolBar.class.isAssignableFrom(type)) {
				return new CoolBarEditPart((CoolBar) visual, element);
			} else if (ToolBar.class.isAssignableFrom(type)) {
				return new ToolBarEditPart((ToolBar) visual, element);
			} else if (ExpandBar.class.isAssignableFrom(type)) {
				return new ExpandBarEditPart((ExpandBar) visual, element);
			} else if (SashForm.class.isAssignableFrom(type)){
				return new SashFormEditPart((SashForm) visual, element);
			}else if (Composite.class.isAssignableFrom(type)) {
				return new CompositeEditPart((Composite) visual, element);
			} else if (Label.class.isAssignableFrom(type)) {
				return new LabelEditPart((Label) visual, element);
			} else if (Control.class.isAssignableFrom(type)) {
				return new ControlEditPart((Control) visual, element);
			} else if (TabItem.class.isAssignableFrom(type)
					|| CTabItem.class.isAssignableFrom(type)) {
				return new TabItemEditPart((Item) visual, element);
			} else if (MenuItem.class.isAssignableFrom(type)) {
				return new MenuItemEditPart((MenuItem) visual, element);
			} else if (CoolItem.class.isAssignableFrom(type)) {
				return new CoolItemEditPart((CoolItem) visual, element);
			} else if (ToolItem.class.isAssignableFrom(type)) {
				return new ToolItemEditPart((ToolItem) visual, element);
			} else if (ExpandItem.class.isAssignableFrom(type)) {
				return new ExpandItemEditPart((ExpandItem) visual, element);
			} else if (TableColumn.class.isAssignableFrom(type)
					|| TreeColumn.class.isAssignableFrom(type)) {
				return new ColumnEditPart((Item) visual, element);
			} else if (Menu.class.isAssignableFrom(type)) {
				if (StyleHelper.checkStyle(element, SWT.BAR)) {
					return new MenuBarEditPart((Menu) visual, element);
				} else {
					return new MenuEditPart((Menu) visual, element);
				}
			} else if (Item.class.isAssignableFrom(type)) {
				return new ItemEditPart((Item) visual, element);
			} else if (Widget.class.isAssignableFrom(type)) {
				return new WidgetEditPart((Widget) visual, element);
			} else {
				try {
					Class<?> jfaceViewer = Class
							.forName("org.eclipse.jface.viewers.Viewer");
					if (jfaceViewer.isAssignableFrom(type)) {
						return new ViewerEditPart((Viewer) visual, element);
					}
				} catch (ClassNotFoundException e) {
				}
			}
		}
		throw new UnsupportedOperationException("Unsupport Type: "
				+ model.toString());
	}
}
