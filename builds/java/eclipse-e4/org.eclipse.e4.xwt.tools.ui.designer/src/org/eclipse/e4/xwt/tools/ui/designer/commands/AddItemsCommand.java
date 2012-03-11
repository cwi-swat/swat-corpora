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
package org.eclipse.e4.xwt.tools.ui.designer.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.gef.commands.Command;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class AddItemsCommand extends Command {

	private XamlElement parent;
	private String[] newItems;
	private String[] oldItems;

	public AddItemsCommand(XamlElement parent, String[] items) {
		this.parent = parent;
		this.newItems = items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return parent != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		IMetaclass metaclass = XWTUtility.getMetaclass(parent);
		if (metaclass == null || metaclass.findProperty("items") == null) {
			return;
		}
		addItems(newItems);
	}

	private void addItems(String[] newItems) {
		XamlAttribute itemsAttr = parent.getAttribute("items", IConstants.XWT_NAMESPACE);
		XamlElement xArray = itemsAttr == null ? null : itemsAttr.getChild("Array", IConstants.XWT_X_NAMESPACE);
		if (itemsAttr == null) {
			itemsAttr = XamlFactory.eINSTANCE.createAttribute("items", IConstants.XWT_NAMESPACE);
		}
		if (newItems == null || newItems.length == 0) {
			if (xArray != null) {
				itemsAttr.getChildNodes().remove(xArray);
			} else {
				itemsAttr.getChildNodes().clear();
			}
			parent.getAttributes().remove(itemsAttr);
			return;
		}
		List<XamlElement> oldItemsElements = new ArrayList<XamlElement>();
		if (xArray != null) {
			oldItemsElements.addAll(xArray.getChildNodes());
		} else {
			oldItemsElements.addAll(itemsAttr.getChildNodes());
		}
		List<String> items = new ArrayList<String>();
		for (XamlElement xamlElement : oldItemsElements) {
			items.add(xamlElement.getValue());
		}
		if (!items.isEmpty()) {
			oldItems = items.toArray(new String[0]);
		}
		if (Arrays.deepEquals(newItems, oldItems)) {
			return;
		}

		// 1. Create new ones.
		if (oldItemsElements.isEmpty()) {
			for (int i = 0; i < newItems.length; i++) {
				String item = newItems[i];
				XamlElement itemElement = XamlFactory.eINSTANCE.createElement("String", "clr-namespace:java.lang");
				itemElement.setValue(item);
				if (xArray != null) {
					xArray.getChildNodes().add(itemElement);
				} else {
					itemsAttr.getChildNodes().add(itemElement);
				}
			}
		}
		// 2. Update old ones.
		else {
			List<XamlElement> removable = new ArrayList<XamlElement>(oldItemsElements);
			for (int i = 0; i < newItems.length; i++) {
				String item = newItems[i];
				XamlElement itemElement = null;
				try {
					itemElement = oldItemsElements.get(i);
				} catch (Exception e) {
				}
				if (itemElement == null) {
					itemElement = XamlFactory.eINSTANCE.createElement("String", "clr-namespace:java.lang");
				}
				removable.remove(itemElement);
				itemElement.setValue(item);
				if (xArray != null) {
					if (!xArray.getChildNodes().contains(itemElement)) {
						xArray.getChildNodes().add(itemElement);
					}
				} else if (!itemsAttr.getChildNodes().contains(itemElement)) {
					itemsAttr.getChildNodes().add(itemElement);
				}
			}
			for (XamlElement remove : removable) {
				if (xArray != null) {
					xArray.getChildNodes().remove(remove);
				} else {
					itemsAttr.getChildNodes().remove(remove);
				}
			}
		}
		if (!parent.getAttributes().contains(itemsAttr)) {
			parent.getAttributes().add(itemsAttr);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return parent != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		addItems(oldItems);
	}
}
