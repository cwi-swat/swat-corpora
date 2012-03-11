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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.databinding.BindingMode;
import org.eclipse.e4.xwt.internal.core.UpdateSourceTrigger;
import org.eclipse.e4.xwt.internal.utils.UserData;
import org.eclipse.e4.xwt.jface.JFacesHelper;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTTools;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ViewerEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BindingHelper implements BindingConstants {

	private static Map<XamlNode, BindingInfo> bindingCache = new HashMap<XamlNode, BindingInfo>();

	public static List<BindingInfo> getBindings(EditPart editPart) {
		if (editPart == null) {
			return Collections.emptyList();
		}
		Object component = null;
		if (editPart instanceof WidgetEditPart) {
			component = ((WidgetEditPart) editPart).getWidget();
		} else if (editPart instanceof ViewerEditPart) {
			component = ((ViewerEditPart) editPart).getJfaceViewer();
		}
		if (component == null) {
			return Collections.emptyList();
		}
		List<BindingInfo> bindings = new ArrayList<BindingInfo>();
		collectBindings(component, bindings);
		return bindings;
	}

	private static void collectBindings(Object component, List<BindingInfo> bindings) {
		if (component == null) {
			return;
		}
		List<BindingInfo> childBindings = getBindings(component);
		if (childBindings != null) {
			bindings.addAll(childBindings);
		}
		if (JFacesHelper.isViewer(component)) {
			// If JFace Viewer, just come here.
			return;
		}
		Viewer viewer = UserData.getLocalViewer(component);
		if (viewer != null) {
			collectBindings(viewer, bindings);
		}
		if (component instanceof Widget) {
			Widget[] children = SWTTools.getChildren((Widget) component);
			for (Widget widget : children) {
				collectBindings(widget, bindings);
			}
		}
	}

	public static List<BindingInfo> getBindings(Object component) {
		XamlNode node = XWTProxy.getModel(component);
		if (node == null) {
			return Collections.emptyList();
		}
		List<BindingInfo> bindings = new ArrayList<BindingInfo>();
		for (XamlAttribute attribute : node.getAttributes()) {
			XamlElement bindNode = attribute.getChild(BINDING);
			if (bindNode != null) {
				BindingInfo binding = createBindingInfo(component, attribute.getName(), bindNode);
				if (binding != null) {
					bindings.add(binding);
				}
			}
		}
		return bindings;
	}

	private static BindingInfo createBindingInfo(Object targetSource, String propertyName, XamlElement bindingNode) {
		BindingInfo bindingInfo = bindingCache.get(bindingNode);
		if (bindingInfo != null) {
			// TODO: To check if binding changed.
			return bindingInfo;
		}

		Observable target = ObservableUtil.getObservable(targetSource);
		Property targetProperty = target.getProperty(propertyName);

		String elementName = null;
		String modelPropertyName = null;
		String bindingMode = null;
		String updateTtrigger = null;
		String converter = null;
		XamlAttribute attribute = bindingNode.getAttribute(ELEMENT_NAME);
		if (attribute != null) {
			elementName = attribute.getValue();
		}
		attribute = bindingNode.getAttribute(PATH);
		if (attribute != null) {
			modelPropertyName = attribute.getValue();
		}
		attribute = bindingNode.getAttribute(MODE);
		if (attribute != null) {
			bindingMode = attribute.getValue();
		}
		attribute = bindingNode.getAttribute(CONVERTER);
		if (attribute != null) {
			converter = attribute.getValue();
		}
		attribute = bindingNode.getAttribute(UPDATE_SOURCE_TRIGGER);
		if (attribute != null) {
			updateTtrigger = attribute.getValue();
		}
		Object modelSource;
		if (elementName != null) {
			modelSource = XWT.findElementByName(targetSource, elementName);
		} else {
			modelSource = getDataContext(targetSource);
		}
		IObservable model = ObservableUtil.getObservable(modelSource);
		if (model == null) {
			return null;
		}
		Property modelProperty = model.getProperty(modelPropertyName);

		bindingInfo = new BindingInfo(new BindingContext(target, targetProperty, model, modelProperty));
		bindingInfo.setElementName(elementName);
		if (bindingMode != null) {
			bindingInfo.setBindingMode(BindingMode.valueOf(bindingMode));
		}
		if (updateTtrigger != null) {
			bindingInfo.setTriggerMode(UpdateSourceTrigger.valueOf(updateTtrigger));
		}
		bindingInfo.setConverter(converter);
		bindingInfo.setBindingNode(bindingNode);
		bindingCache.put(bindingNode, bindingInfo);
		return bindingInfo;
	}

	public static DataContext getDataContext(Object widget) {
		if (widget == null) {
			return null;
		}
		try {
			Object dataContext = XWT.getDataContext(widget);
			Map<?, ?> resources = UserData.getResources(widget);
			if (resources == null) {
				return null;
			}
			if (dataContext != null && !resources.isEmpty()) {
				Set<?> entrySet = resources.entrySet();
				for (Object object : entrySet) {
					Entry<?, ?> entry = (Entry<?, ?>) object;
					Object value = entry.getValue();
					if (!dataContext.equals(value)) {
						continue;
					}
					Object key = entry.getKey();
					return new DataContext(key.toString(), value);
				}
			} else {
				Set<?> entrySet = resources.entrySet();
				for (Object object : entrySet) {
					Entry<?, ?> entry = (Entry<?, ?>) object;
					Object key = entry.getKey();
					Object value = entry.getValue();
					return new DataContext(key.toString(), value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
