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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.converters.StringToInteger;
import org.eclipse.e4.xwt.tools.ui.designer.commands.SetStyleCommand;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.core.style.SWTStyles;
import org.eclipse.e4.xwt.tools.ui.designer.core.style.StyleGroup;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.StyleHelper;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class StyleAction extends Action implements IMenuCreator {

	public static final String ID = StyleAction.class.getName();
	private WidgetEditPart editPart;
	private MenuManager menuManager;

	public StyleAction(WidgetEditPart editPart) {
		this.editPart = editPart;
		setId(ID);
		setText("Style");
		setMenuCreator(this);
	}

	@Override
	public boolean isEnabled() {
		Object model = editPart.getModel();
		if (model instanceof EObject) {
			EObject object = (EObject)model;
			if (object.eContainer() == null) {
				return false;
			}
		}
		return super.isEnabled();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.editor.actions.AbstractDropDownAction#createMenuManager()
	 */
	protected MenuManager createMenuManager() {
		if (menuManager == null) {

			menuManager = new MenuManager();
			Widget widget = editPart.getWidget();
			if (widget != null && !widget.isDisposed()) {
				int masterStyle = widget.getStyle();
				StyleGroup[] styles = SWTStyles.getStyles(widget.getClass());
				Separator last = null;
				for (StyleGroup styleGroup : styles) {
					if (!styleGroup.match(masterStyle)) {
						continue;
					}
					String[] items = styleGroup.getStyles();
					String groupName = styleGroup.getGroupName();
					for (String style : items) {
						XamlNode node = editPart.getCastModel();
						SetStyleAction action = new SetStyleAction(node, styleGroup, style, "default".equals(groupName) ? AS_CHECK_BOX : AS_RADIO_BUTTON);
						boolean checked = StyleHelper.checkStyle(masterStyle, style);
						
						boolean specified = false;
						XamlAttribute attribute = node.getAttribute("style", IConstants.XWT_X_NAMESPACE);
						if (attribute != null) {
							int xmlValue = (Integer) StringToInteger.instance.convert(attribute.getValue());
							int value = (Integer) StringToInteger.instance.convert(style);
							specified = StyleHelper.checkStyle(xmlValue, value);
						}
						if (checked) {
							action.setEnabled(specified);
						}
						else {
							action.setEnabled(true);
						}
						action.setChecked(checked);
						menuManager.add(action);
					}
					menuManager.add(last = new Separator(groupName));
				}
				if (last != null) {
					menuManager.remove(last);
				}
			}
		}
		return menuManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
		if (menuManager != null) {
			menuManager.dispose();
			menuManager = null;
		}
	}

	public Menu getMenu(Control parent) {
		MenuManager mm = createMenuManager();
		if (mm != null) {
			return mm.createContextMenu(parent);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		MenuManager mm = createMenuManager();
		if (mm != null) {
			Menu menu = new Menu(parent);
			IContributionItem[] items = mm.getItems();
			for (int i = 0; i < items.length; i++) {
				IContributionItem item = items[i];
				IContributionItem newItem = item;
				if (item instanceof ActionContributionItem) {
					newItem = new ActionContributionItem(((ActionContributionItem) item).getAction());
				}
				newItem.fill(menu, -1);
			}
			return menu;
		}
		return null;
	}

	private class SetStyleAction extends Action {
		private String newStyle;
		private XamlNode node;
		private StyleGroup group;

		public SetStyleAction(XamlNode node, StyleGroup group, String newStyle, int actionStyle) {
			super(newStyle, actionStyle);
			this.node = node;
			this.group = group;
			this.newStyle = newStyle;
		}

		public void run() {
			// fail fast
			if (getStyle() == AS_RADIO_BUTTON && !isChecked()) {
				return;
			}
			SetStyleCommand command = new SetStyleCommand(node, newStyle, group);
			if (getStyle() == AS_CHECK_BOX) {
				command.setRemove(!isChecked());
			}
			EditDomain.getEditDomain(editPart).getCommandStack().execute(command);
		}
	}

}
