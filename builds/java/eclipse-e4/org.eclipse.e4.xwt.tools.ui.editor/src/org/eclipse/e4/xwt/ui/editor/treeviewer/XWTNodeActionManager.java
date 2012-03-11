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
package org.eclipse.e4.xwt.ui.editor.treeviewer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;
import org.eclipse.wst.xml.ui.internal.contentoutline.XMLNodeActionManager;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XWTNodeActionManager extends XMLNodeActionManager {

	public XWTNodeActionManager(IStructuredModel model, Viewer viewer) {
		super(model, viewer);
	}

	@Override
	protected void contributeAddChildActions(IMenuManager menu, Node node, int ic, int vc) {
		super.contributeAddChildActions(menu, node, ic, vc);

		contributeXAMLAttributeMenu(menu, node, ic, vc);
		contributeXAMLChildMenu(menu, node, ic, vc);
	}

	protected void contributeXAMLAttributeMenu(IMenuManager menu, Node node, int ic, int vc) {
		IMenuManager addAttributeMenu = null;

		String message = XMLUIMessages._UI_MENU_ADD_ATTRIBUTE;
		for (IContributionItem item : menu.getItems()) {
			if (item instanceof MenuManager) {
				MenuManager manager = (MenuManager) item;
				if (message.equals(manager.getMenuText())) {
					addAttributeMenu = manager;
					break;
				}
			}
		}

		if (addAttributeMenu == null) {
			return;
		}

		IContributionItem[] items = addAttributeMenu.getItems();
		addAttributeMenu.removeAll();

		String tagName = node.getNodeName();
		// Metaclass metaclass = UPF.getMetaclass(tagName, DomHelper.lookupNamespaceURI(node));
		// if (metaclass == null) {
		// return;
		// }

		List<Action> actionPropertyCollector = new ArrayList<Action>();
		List<Action> actionAttachedPropertyCollector = new ArrayList<Action>();
		List<Action> actionEventCollector = new ArrayList<Action>();

		HashSet<String> existing = new HashSet<String>();
		NamedNodeMap namedNodeMap = node.getAttributes();
		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			Node attributeNode = namedNodeMap.item(i);
			String attributeName = attributeNode.getNodeName();
			existing.add(attributeName);
		}

		ImageDescriptor proppertyImageDescriptor = JavaPluginImages.getDescriptor(JavaPluginImages.IMG_FIELD_PUBLIC);
		// ImageDescriptor eventImageDescriptor = ResourceManager.OBJ_EVENT;
		// ImageDescriptor attachedProppertyImageDescriptor = JavaPluginImages
		// .getDescriptor(JavaPluginImages.IMG_FIELD_PROTECTED);
		//
		// {
		// Collection<Property> properties = metaclass.getAllProperties();
		// for (Property property : properties) {
		// if (property.isMany()) {
		// continue;
		// }
		// Metaclass propertyType = property.getType();
		// if (propertyType != null
		// && propertyType.isAssignableFrom(UIElement.metaclass)) {
		// continue;
		// }
		//
		// String propertyName = property.getName();
		//
		// if (!existing.contains(propertyName)) {
		// Object defaultValue = property.getDefaultValue();
		// String defaultValueString = "";
		// if (defaultValue != null) {
		// IValueConverter converter = UPF.findConvertor(
		// defaultValue.getClass(), String.class);
		// if (converter != null) {
		// Object stringValue = converter.convert(
		// defaultValue, String.class, "", Locale
		// .getDefault());
		// if (stringValue != null) {
		// defaultValueString = stringValue.toString();
		// }
		// } else {
		// defaultValueString = defaultValue.toString();
		// }
		// }
		// String replacementString = propertyName + "=\""
		// + defaultValueString + "\" ";
		//
		// CMAttributeDeclarationImpl declaration = new CMAttributeDeclarationImpl(
		// propertyName, CMAttributeDeclaration.OPTIONAL);
		// Action action = createAddAttributeAction((Element) node,
		// declaration);
		// action.setImageDescriptor(proppertyImageDescriptor);
		// actionPropertyCollector.add(action);
		// }
		// }
		//
		// Collection<Event> events = metaclass.getAllEvents();
		// for (Event event : events) {
		// String eventName = event.getName();
		// if (!existing.contains(event)) {
		//
		// CMAttributeDeclarationImpl declaration = new CMAttributeDeclarationImpl(
		// eventName, CMAttributeDeclaration.OPTIONAL);
		// Action action = createAddAttributeAction((Element) node,
		// declaration);
		// action.setImageDescriptor(eventImageDescriptor);
		// actionEventCollector.add(action);
		// }
		// }
		// }
		//
		// {
		// Node parentNode = node.getParentNode();
		// String parentName = parentNode.getNodeName();
		// Metaclass parentMetaclass = UPF.getMetaclass(parentName, DomHelper.lookupNamespaceURI(parentNode));
		// if (parentMetaclass != null) {
		// Property[] properties = parentMetaclass.getAttachedProperties();
		// for (Property property : properties) {
		// Metaclass propertyType = property.getType();
		// if (propertyType != null
		// && propertyType
		// .isAssignableFrom(UIElement.metaclass)) {
		// continue;
		// }
		//
		// String propertyName = property.getName();
		// if (!existing.contains(propertyName)) {
		// Object defaultValue = property.getDefaultValue();
		// String defaultValueString = "";
		// if (defaultValue != null) {
		// IValueConverter converter = UPF.findConvertor(
		// defaultValue.getClass(), String.class);
		// if (converter != null) {
		// Object stringValue = converter.convert(
		// defaultValue, String.class, "", Locale
		// .getDefault());
		// if (stringValue != null) {
		// defaultValueString = stringValue.toString();
		// }
		// } else {
		// defaultValueString = defaultValue.toString();
		// }
		// }
		// String attachedName = parentName + "." + propertyName;
		// String replacementString = attachedName + "=\""
		// + defaultValueString + "\" ";
		//
		// CMAttributeDeclarationImpl declaration = new CMAttributeDeclarationImpl(
		// attachedName, CMAttributeDeclaration.OPTIONAL);
		// Action action = createAddAttributeAction(
		// (Element) node, declaration);
		// action
		// .setImageDescriptor(attachedProppertyImageDescriptor);
		// actionAttachedPropertyCollector.add(action);
		// }
		// }
		// }
		// }
		// addAttributeMenu.add(new Separator());
		// XAMLMenuBuilder imageMenuBuilder = null;
		// if (!(menuBuilder instanceof XAMLMenuBuilder)) {
		// menuBuilder = new XAMLMenuBuilder();
		// }
		// imageMenuBuilder = (XAMLMenuBuilder) menuBuilder;
		//
		// if (!actionPropertyCollector.isEmpty()) {
		// imageMenuBuilder.setImageDescriptor(proppertyImageDescriptor);
		// imageMenuBuilder.populateMenu(addAttributeMenu,
		// actionPropertyCollector, true);
		// addAttributeMenu.add(new Separator());
		// }
		// if (!actionAttachedPropertyCollector.isEmpty()) {
		// imageMenuBuilder
		// .setImageDescriptor(attachedProppertyImageDescriptor);
		// imageMenuBuilder.populateMenu(addAttributeMenu,
		// actionAttachedPropertyCollector, true);
		// addAttributeMenu.add(new Separator());
		// }
		// if (!actionEventCollector.isEmpty()) {
		// imageMenuBuilder.setImageDescriptor(eventImageDescriptor);
		// imageMenuBuilder.populateMenu(addAttributeMenu,
		// actionEventCollector, true);
		// addAttributeMenu.add(new Separator());
		// }
		// imageMenuBuilder.setImageDescriptor(null);
		//
		// for (IContributionItem item : items) {
		// addAttributeMenu.add(item);
		// }
	}

	protected void contributeXAMLChildMenu(IMenuManager menu, Node node, int ic, int vc) {
		IMenuManager addChildMenu = null;
		String message = XMLUIMessages._UI_MENU_ADD_CHILD;
		for (IContributionItem item : menu.getItems()) {
			if (item instanceof MenuManager) {
				MenuManager manager = (MenuManager) item;
				if (message.equals(manager.getMenuText())) {
					addChildMenu = manager;
					break;
				}
			}
		}
		if (addChildMenu == null) {
			return;
		}

		List<Action> propertyElementCollector = new ArrayList<Action>();
		List<Action> elementCollector = new ArrayList<Action>();
		List<Action> resourcesCollector = new ArrayList<Action>();

		IContributionItem[] items = addChildMenu.getItems();
		addChildMenu.removeAll();

		// ImageDescriptor elementImageEDescriptor = ResourceManager.OBJ_ELEMENT;
		// ImageDescriptor resourceImageDescriptor = ResourceManager.OBJ_RESOURCES;
		//
		// String name = node.getNodeName();
		// Metaclass metaclass = UPF.getMetaclass(name, DomHelper.lookupNamespaceURI(node));
		// if (metaclass != null) {
		// Collection<Property> properties = metaclass.getAllProperties();
		// for (Property property : properties) {
		// Metaclass propertyType = property.getType();
		// if (propertyType == null) {
		// continue;
		// }
		// if (metaclass.isAssignableFrom(UIElement.metaclass)) {
		// if (propertyType != null
		// && propertyType
		// .isAssignableFrom(UIElement.metaclass)
		// && !propertyType.isAbstract()) {
		// String typeName = property.getName();
		// String tagName = name + "." + typeName;
		//
		// CMElementDeclarationImpl declaration = new CMElementDeclarationImpl(
		// null, tagName);
		// Action action = createAddElementAction((Element) node,
		// declaration, -1);
		// action.setImageDescriptor(elementImageEDescriptor);
		// propertyElementCollector.add(action);
		// }
		// } else {
		// if (property.getIsComposition()) {
		// Metaclass[] metaclasses = UPF.getAllMetaclasses();
		// for (Metaclass type : metaclasses) {
		// if (TriggerBase.metaclass.isAssignableFrom(type)) {
		// continue;
		// }
		// if (type.isSubclassOf(propertyType)
		// && !type.isAbstract()) {
		// String tagName = type.getName();
		//
		// CMElementDeclarationImpl declaration = new CMElementDeclarationImpl(
		// null, tagName);
		// Action action = createAddElementAction(
		// (Element) node, declaration, -1);
		// action
		// .setImageDescriptor(elementImageEDescriptor);
		// elementCollector.add(action);
		// }
		// }
		// }
		// }
		// }
		// String tagName = name + ".Resources";
		//
		// CMElementDeclarationImpl declaration = new CMElementDeclarationImpl(
		// null, tagName);
		// Action action = createAddElementAction((Element) node, declaration,
		// -1);
		// action.setImageDescriptor(elementImageEDescriptor);
		// resourcesCollector.add(action);
		// }
		//
		// if (metaclass != null) {
		// if (UIElement.metaclass.isAssignableFrom(metaclass)) {
		// Metaclass[] metaclasses = UPF.getAllMetaclasses();
		// for (Metaclass type : metaclasses) {
		// if (ListBoxItem.metaclass.isAssignableFrom(type)
		// && !Selector.metaclass.isAssignableFrom(metaclass)) {
		// continue;
		// }
		// if (TreeViewItem.metaclass.isAssignableFrom(type)
		// && !TreeView.metaclass.isAssignableFrom(metaclass)) {
		// continue;
		// }
		// if (TabItem.metaclass.isAssignableFrom(type)
		// && !TabControl.metaclass
		// .isAssignableFrom(metaclass)) {
		// continue;
		// }
		//
		// if (MenuItem.metaclass.isAssignableFrom(type)
		// && !Menu.metaclass.isAssignableFrom(metaclass)) {
		// continue;
		// }
		//
		// if (type.getType().isAssignableFrom(IScopeObject.class)) {
		// continue;
		// }
		// if (TriggerBase.metaclass.isAssignableFrom(type)) {
		// continue;
		// }
		//
		// if (UIElement.metaclass == type) {
		// continue;
		// }
		//
		// if (SetterBase.metaclass.isAssignableFrom(type)
		// && !Style.metaclass.isAssignableFrom(metaclass)) {
		// continue;
		// }
		//
		// if (MenuItem.metaclass.isAssignableFrom(type)
		// && !Menu.metaclass.isAssignableFrom(metaclass)) {
		// continue;
		// }
		//
		// if (UIElement.metaclass.isAssignableFrom(type)
		// && !type.isAbstract()) {
		// String tagName = type.getName();
		//
		// CMElementDeclarationImpl declaration = new CMElementDeclarationImpl(
		// null, tagName);
		// Action action = createAddElementAction((Element) node,
		// declaration, -1);
		// action.setImageDescriptor(elementImageEDescriptor);
		// elementCollector.add(action);
		// }
		// }
		// }
		// } else if (name.endsWith(".Resources")) {
		// Metaclass[] metaclasses = new Metaclass[] { Style.metaclass,
		// ControlTemplate.metaclass, DataTemplate.metaclass,
		// CollectionViewSource.metaclass };
		// for (Metaclass type : metaclasses) {
		// String typeName = type.getName();
		//
		// CMElementDeclarationImpl declaration = new CMElementDeclarationImpl(
		// null, typeName);
		// Action action = createAddElementAction((Element) node,
		// declaration, -1);
		// action.setImageDescriptor(elementImageEDescriptor);
		// elementCollector.add(action);
		// }
		// } else if (name.endsWith(".Triggers")) {
		// Metaclass[] metaclasses = new Metaclass[] { Style.metaclass,
		// ControlTemplate.metaclass, DataTemplate.metaclass,
		// CollectionViewSource.metaclass };
		// for (Metaclass type : metaclasses) {
		// if (!TriggerBase.metaclass.isAssignableFrom(type)) {
		// continue;
		// }
		//
		// String typeName = type.getName();
		//
		// CMElementDeclarationImpl declaration = new CMElementDeclarationImpl(
		// null, typeName);
		// Action action = createAddElementAction((Element) node,
		// declaration, -1);
		// action.setImageDescriptor(elementImageEDescriptor);
		// elementCollector.add(action);
		// }
		// }
		//
		// addChildMenu.add(new Separator());
		// XAMLMenuBuilder imageMenuBuilder = null;
		// if (!(menuBuilder instanceof XAMLMenuBuilder)) {
		// menuBuilder = new XAMLMenuBuilder();
		// }
		// imageMenuBuilder = (XAMLMenuBuilder) menuBuilder;
		//
		// if (!propertyElementCollector.isEmpty()) {
		// imageMenuBuilder.setImageDescriptor(elementImageEDescriptor);
		// imageMenuBuilder.populateMenu(addChildMenu,
		// propertyElementCollector, true);
		// addChildMenu.add(new Separator());
		// }
		// if (!elementCollector.isEmpty()) {
		// imageMenuBuilder.setImageDescriptor(elementImageEDescriptor);
		// imageMenuBuilder.populateMenu(addChildMenu, elementCollector, true);
		// addChildMenu.add(new Separator());
		// }
		// if (!resourcesCollector.isEmpty()) {
		// imageMenuBuilder.setImageDescriptor(resourceImageDescriptor);
		// imageMenuBuilder.populateMenu(addChildMenu, resourcesCollector,
		// true);
		// addChildMenu.add(new Separator());
		// }
		// imageMenuBuilder.setImageDescriptor(null);
		//
		// for (IContributionItem item : items) {
		// addChildMenu.add(item);
		// }
	}
}
