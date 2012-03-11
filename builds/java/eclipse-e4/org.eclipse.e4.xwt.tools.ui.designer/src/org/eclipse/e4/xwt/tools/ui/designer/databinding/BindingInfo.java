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
package org.eclipse.e4.xwt.tools.ui.designer.databinding;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.databinding.BindingMode;
import org.eclipse.e4.xwt.internal.core.ScopeKeeper;
import org.eclipse.e4.xwt.internal.core.UpdateSourceTrigger;
import org.eclipse.e4.xwt.internal.utils.UserData;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AddNewChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.DeleteCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.NamedCommand;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BindingInfo implements IBindingInfo, IBindingHandler, BindingConstants {

	private BindingContext context;

	private String elementName;
	private BindingMode bindingMode;
	private UpdateSourceTrigger triggerMode;
	private String converter;
	private CodeStyles codeStyles;
	private XamlElement bindingNode;

	public BindingInfo(BindingContext bindingContext) {
		Assert.isNotNull(bindingContext);
		this.context = bindingContext;
	}

	public BindingContext getBindingContext() {
		return context;
	}

	public void setBindingNode(XamlElement bindingNode) {
		this.bindingNode = bindingNode;
	}

	public XamlElement getBindingNode() {
		return bindingNode;
	}

	public void setConverter(String converter) {
		this.converter = converter;
		if (bindingNode != null) {
			addAttr(bindingNode, CONVERTER, IConstants.XWT_NAMESPACE, converter);
		}
	}

	public String getConverter() {
		return converter;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
		if (bindingNode != null) {
			addAttr(bindingNode, ELEMENT_NAME, IConstants.XWT_NAMESPACE, elementName);
		}
	}

	public String getElementName() {
		IObservable model = context.getModel();
		if (elementName == null && model != null && model.getType() == IObservable.OBSERVE_SWT_JFACE) {
			Object source = model.getSource();
			elementName = getWidgetName(source);
		}
		return elementName;
	}

	private String getWidgetName(Object widget) {
		ScopeKeeper nameContext = UserData.findScopeKeeper(widget);
		if (nameContext != null) {
			for (String name : nameContext.names()) {
				if (widget == nameContext.getNamedObject(name)) {
					return name;
				}
			}
		}
		return null;
	}

	public void setBindingMode(BindingMode bindingMode) {
		this.bindingMode = bindingMode;
		if (bindingNode != null) {
			addAttr(bindingNode, MODE, IConstants.XWT_NAMESPACE, bindingMode.name());
		}
	}

	public BindingMode getBindingMode() {
		return bindingMode;
	}

	public void setCodeStyles(CodeStyles codeStyles) {
		if (codeStyles != null) {
			this.codeStyles = codeStyles;
		}
	}

	public CodeStyles getCodeStyles() {
		if (codeStyles == null) {
			codeStyles = new CodeStyles();
		}
		return codeStyles;
	}

	private XamlAttribute getAttribute(XamlNode parent, String attrName, String namespace) {
		XamlAttribute attribute = parent.getAttribute(attrName, namespace);
		if (attribute == null) {
			attribute = XamlFactory.eINSTANCE.createAttribute(attrName, namespace);
		}
		return attribute;
	}

	private XamlElement getElement(XamlNode parent, String name, String namespace) {
		XamlElement child = parent.getChild(name, namespace);
		if (child == null) {
			child = XamlFactory.eINSTANCE.createElement(name, namespace);
		}
		return child;
	}

	private XamlAttribute addAttr(XamlNode parent, String attrName, String namespace, String value) {
		if (parent == null || attrName == null || namespace == null) {
			return null;
		}
		XamlAttribute attribute = parent.getAttribute(attrName, namespace);
		if (attribute == null) {
			attribute = XamlFactory.eINSTANCE.createAttribute(attrName, namespace);
		}
		attribute.setValue(value);
		if (!parent.getAttributes().contains(attribute)) {
			parent.getAttributes().add(attribute);
		}

		return attribute;
	}

	private String getCreateName(Object model, CompoundCommand commands) {
		if (!(model instanceof Widget)) {
			return null;
		}
		Widget widget = (Widget) model;
		String original = getWidgetName(widget);
		if (original != null) {
			return original;
		}
		String elementName = getElementName();
		if (elementName == null) {
			elementName = NamedCommand.generateName(widget);
		}
		commands.add(new NamedCommand(widget, elementName));
		UserData.setObjectName(widget, elementName);
		return elementName;
	}

	public boolean canBinding() {
		return context.isValid();
	}

	public Command bindWithCommand() {
		if (!canBinding()) {
			return null;
		}
		CompoundCommand commandList = new CompoundCommand();

		Property targetProperty = context.getTargetProperty();
		Property modelProperty = context.getModelProperty();
		IObservable target = context.getTarget();
		IObservable model = context.getModel();
		Object targetSource = target.getSource();

		Object modelSource = model.getSource();

		XamlNode node = null;
		if (targetSource instanceof Widget) {
			node = XWTProxy.getModel((Widget) targetSource);
		} else if (targetSource instanceof XamlElement) {
			node = (XamlElement) targetSource;
		}

		if (node == null) {
			return UnexecutableCommand.INSTANCE;
		}

		// 1. Get attr of targetProperty, such as < text="{}"/>
		XamlAttribute attribute = node.getAttribute(targetProperty.getName(), IConstants.XWT_NAMESPACE);
		if (attribute != null) {
			commandList.add(new DeleteCommand(attribute));
			attribute = null;
		}
		if (attribute == null) {
			attribute = XamlFactory.eINSTANCE.createAttribute(targetProperty.getName(), IConstants.XWT_NAMESPACE);
		}
		// 2. Binding Node.
		if (bindingNode == null) {
			bindingNode = attribute.getChild(BINDING, IConstants.XWT_NAMESPACE);
		}
		if (bindingNode == null) {
			bindingNode = XamlFactory.eINSTANCE.createElement(BINDING, IConstants.XWT_NAMESPACE);
		}
		// 3. ElementName
		int type = model.getType();
		if (type == IObservable.OBSERVE_SWT_JFACE) {
			addAttr(bindingNode, ELEMENT_NAME, IConstants.XWT_NAMESPACE, getCreateName(modelSource, commandList));
		}
		// 4. DataContext
		else if (IObservable.OBSERVE_JAVA_BAEN == type) {
			XamlDocument ownerDocument = node.getOwnerDocument();
			XamlElement rootElement = ownerDocument.getRootElement();
			DataContext dataContext = (DataContext) modelSource;
			String key = dataContext.getKey();
			if (key != null) {
				boolean canUseDC = getCodeStyles().useDataContext;
				if (canUseDC) {
					XamlAttribute contextNode = getAttribute(rootElement, DATA_CONTEXT, IConstants.XWT_NAMESPACE);
					contextNode.setUseFlatValue(true);

					XamlElement resourceNode = getElement(contextNode, STATIC_RESOURCE, IConstants.XWT_NAMESPACE);
					if (!key.equals(resourceNode.getValue())) {
						if (resourceNode.getValue() != null) {
							canUseDC = false;
						} else {
							resourceNode.setValue(key);
							commandList.add(new AddNewChildCommand(contextNode, resourceNode));
							commandList.add(new AddNewChildCommand(rootElement, contextNode));
						}
					}
				}
				if (!canUseDC || !getCodeStyles().useDataContext) {
					XamlAttribute sourceAttr = getAttribute(bindingNode, SOURCE, IConstants.XWT_NAMESPACE);
					sourceAttr.setUseFlatValue(true);
					XamlElement resourceNode = getElement(sourceAttr, STATIC_RESOURCE, IConstants.XWT_NAMESPACE);

					resourceNode.setValue(key);
					commandList.add(new AddNewChildCommand(sourceAttr, resourceNode));
					commandList.add(new AddNewChildCommand(bindingNode, sourceAttr));
				}
			}
		}

		// 5. Path
		addAttr(bindingNode, PATH, IConstants.XWT_NAMESPACE, modelProperty.toString());

		// 6. BindingMode
		if (bindingMode != null && BindingMode.TwoWay.equals(bindingMode)) {
			addAttr(bindingNode, MODE, IConstants.XWT_NAMESPACE, bindingMode.name());
		}

		// 7. Converter.
		if (converter != null) {
			addAttr(bindingNode, CONVERTER, IConstants.XWT_NAMESPACE, converter);
		}

		// 8. UpdateSourceTrigger
		if (triggerMode != null) {
			addAttr(bindingNode, UPDATE_SOURCE_TRIGGER, IConstants.XWT_NAMESPACE, triggerMode.name());
		}

		// 9. Code Style.
		attribute.setUseFlatValue(getCodeStyles().useFlatVlaue);

		if (!attribute.getChildNodes().contains(bindingNode)) {
			attribute.getChildNodes().add(bindingNode);
		}
		if (!node.getAttributes().contains(attribute)) {
			commandList.add(new AddNewChildCommand(node, attribute));
		}
		return commandList.unwrap();
	}

	public boolean canDelete() {
		return bindingNode != null && bindingNode.eContainer() != null;
	}

	public Command deleteWithCommand() {
		if (canDelete()) {
			XamlAttribute bindAttr = (XamlAttribute) bindingNode.eContainer();
			return new DeleteCommand(bindAttr);
		}
		return null;
	}

	public IObservable getModel() {
		return context.getModel();
	}

	public Property getModelProperty() {
		return context.getModelProperty();
	}

	public IObservable getTarget() {
		return context.getTarget();
	}

	public Property getTargetProperty() {
		return context.getTargetProperty();
	}

	public UpdateSourceTrigger getTriggerMode() {
		return triggerMode;
	}

	public void setTriggerMode(UpdateSourceTrigger triggerMode) {
		this.triggerMode = triggerMode;
	}
}
