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
package org.eclipse.e4.xwt.tools.ui.designer.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.Tracking;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.core.IDynamicBinding;
import org.eclipse.e4.xwt.internal.core.Binding;
import org.eclipse.e4.xwt.internal.utils.LoggerManager;
import org.eclipse.e4.xwt.internal.utils.ObjectUtil;
import org.eclipse.e4.xwt.jface.JFacesHelper;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.swt.widgets.Widget;

/**
 * @author liang.zhang
 * 
 */
public class DataBindingTrack {
	private String bindingError = "";
	private Set<String> errorElements = null;

	private List<XamlElement> widgetList = null;
	private Map<XamlElement, String> bindingMap;

	public DataBindingTrack() {
		widgetList = new ArrayList<XamlElement>();
		errorElements = new HashSet<String>();
		bindingMap = new HashMap<XamlElement, String>();
	}

	public void addWidgetElement(XamlElement element) {
		widgetList.add(element);
	}

	private void addErrorWidgetId(String elementId) {
		errorElements.add(elementId);
	}

	private void addBindingMessageToMap(XamlElement element, String bindingMessage) {
		bindingMap.put(element, bindingMessage);
	}

	private int validateParentElementError(XamlElement bindingElement) {
		int isError = 0;
		if (!bindingError.equals("")) {
			Object parentObj = bindingElement.eContainer();
			while (parentObj != null) {
				bindingElement = (XamlElement) parentObj;
				if (errorElements.contains(bindingElement.getId())) {
					isError = 1;
					break;
				}
				parentObj = bindingElement.eContainer();
			}
		}
		return isError;
	}

	private String getCurrentWidgetPosition(XamlElement element) {
		if (element.eContainer() != null) {
			return " " + getCurrentWidgetPosition((XamlElement) element.eContainer());
		}
		return "";
	}

	public String getDataBindMessage() {
		StringBuffer message = new StringBuffer("");
		Iterator<XamlElement> widgetIt = widgetList.iterator();
		Set<XamlElement> keys = bindingMap.keySet();
		while (widgetIt.hasNext()) {
			XamlElement element = widgetIt.next();
			int parentHasError = validateParentElementError(element);
			if (parentHasError == 0) {
				String content = getCurrentWidgetPosition(element);
				if (!message.toString().equals("")) {
					content += "+ ";
				}
				content = content + element.getName();
				if (XWT.isTracking(Tracking.NAME)) {
					XamlAttribute nameAttr = element.getAttribute(IConstants.XWT_X_NAMESPACE, "Name");
					if (nameAttr != null) {
						content += " <" + nameAttr.getValue() + ">";
					}
				}
				message.append(content);
				if (XWT.isTracking(Tracking.DATABINDING)) {
					if (keys.contains(element)) {
						message.append(bindingMap.get(element));
					} else {
						message.append("\n");
					}
				}
			}
		}
		return message.toString();
	}

	private void setBindingErrorMessage(String bindingError) {
		this.bindingError = bindingError;
	}

	public void tracking(Object swtObject, XamlElement element, Object dataContext) {
		String bindingMessage = "";
		if (swtObject instanceof IDynamicBinding) {
			String error = "";
			Binding newInstance = (Binding) swtObject;
			String path = null;
			XamlAttribute attr = element.getAttribute("Path", IConstants.XWT_NAMESPACE);
			if (null == attr)
				attr = element.getAttribute("path", IConstants.XWT_NAMESPACE);
			if (null != attr)
				path = attr.getValue();
			Object dataContext2 = null;
			try {
				dataContext2 = newInstance.getValue(null);
				if (path != null && path.length() > 0) {
					String[] paths = path.trim().split("\\.");
					if (paths.length > 1) {
						String path1 = "";
						for (int i = 0; i < paths.length - 1; i++) {
							path1 = paths[i];
							if (dataContext2 != null) {
								dataContext2 = getObserveData(dataContext2, path1);
							}
						}
					}
				}
			} catch (Exception ex) {
				addErrorWidgetId(((XamlNode) element.eContainer().eContainer()).getId());
				setBindingErrorMessage("-> Error");
				error = "-> Error";
			}
			if (dataContext2 != null) {
				bindingMessage = " (DataContext=" + dataContext2.getClass().getSimpleName() + ", Path=" + path + ")" + error + "\n";
				addBindingMessageToMap((XamlElement) element.eContainer().eContainer(), bindingMessage);// bindingMap.put((XamlElement) element.getParent().getParent(), bindingMessage);
			}
		} else if (swtObject instanceof Widget) {
			addWidgetElement(element);
			if (dataContext != null) {
				bindingMessage = " (DataContext=" + dataContext.getClass().getSimpleName() + ")\n";
				addBindingMessageToMap(element, bindingMessage);
			}
		} else if (JFacesHelper.isViewer(swtObject)) {
			if (dataContext != null) {
				bindingMessage = " (DataContext=" + dataContext.getClass().getSimpleName() + ")\n";
				addBindingMessageToMap(element, bindingMessage);// bindingMap.put(element, bindingMessage);
			}
		} else if (element.attributeNames(IConstants.XWT_X_NAMESPACE).size() > 0) {
			// ??
			if (element.eContainer() != null && element.eContainer().eContainer() != null) {
				bindingMessage = " (DataContext=" + element.getName() + ")\n";
				addBindingMessageToMap((XamlElement) element.eContainer().eContainer(), bindingMessage);// bindingMap.put((XamlElement) element.getParent().getParent(), bindingMessage);
			}
		}
	}

	public static Object getObserveData(Object dataContext, String path) {
		try {
			Class<?> dataContextClass = dataContext.getClass();
			Method getMethod = ObjectUtil.findGetter(dataContextClass, path, null);
			if (getMethod != null) {
				return getMethod.invoke(dataContext, new Object[] {});
			}
		} catch (SecurityException e) {
			LoggerManager.log(e);
		} catch (IllegalArgumentException e) {
			LoggerManager.log(e);
		} catch (IllegalAccessException e) {
			LoggerManager.log(e);
		} catch (InvocationTargetException e) {
			LoggerManager.log(e);
		}
		return null;
	}

}
