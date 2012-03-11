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
package org.eclipse.e4.xwt.tools.ui.designer.editor.palette;

import java.lang.reflect.Constructor;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class CreateReqHelper {

	public static final Object UNKNOWN_TYPE = new Object();

	private CreateRequest createReq;

	public CreateReqHelper(CreateRequest createReq) {
		this.createReq = createReq;
	}

	public XamlNode getNewObject() {
		return EntryHelper.getNode(createReq);
	}

	public String getNewObjectType() {
		Object type = createReq.getType();
		if (type != null) {
			return type.toString();
		}
		return null;
	}

	public Object getCreateType() {
		return getCreateType(getNewObject());
	}

	public static Object getCreateType(Object newObject) {
		if (newObject != null && newObject instanceof XamlElement) {
			return getType((XamlElement) newObject);
		} else if (newObject instanceof XamlAttribute) {
			return ((XamlAttribute) newObject).getName();
		}
		return UNKNOWN_TYPE;
	}

	public static Class<?> getType(XamlNode element) {
		if (element == null) {
			return null;
		}
		IMetaclass metaclass = XWTUtility.getMetaclass(element);
		if (metaclass != null) {
			return metaclass.getType();
		}
		return null;
	}

	public boolean isCreate(Object targetType) {
		if (targetType == null) {
			return false;
		}
		Object createType = getCreateType(getNewObject());
		if (targetType instanceof String && createType instanceof String) {
			return ((String) targetType).equalsIgnoreCase((String) createType);
		}
		return targetType == createType;
	}

	public static boolean canCreate(Object parent, Object child) {
		try {
			XamlNode parentNode = null;
			Class<?> parentType = null;
			if (parent instanceof Class<?>) {
				parentType = (Class<?>) parent;
			} else if (parent instanceof XamlNode) {
				parentNode = (XamlNode) parent;
			} else if (parent instanceof EditPart) {
				Object model = ((EditPart) parent).getModel();
				if (model instanceof XamlNode) {
					parentNode = (XamlNode) model;
				}
			}
			if (parentType == null && parentNode != null) {
				parentType = getType(parentNode);
			}
			if (parentType == null || parentType == CCombo.class) {
				return false;
			}
			boolean canCreate = false;
			Object createType = getCreateType(child);
			if (createType instanceof Class<?>) {
				Class<?> type = (Class<?>) createType;
				if (ExpandItem.class.isAssignableFrom(type)) {
					return ExpandBar.class.isAssignableFrom(parentType);
				} else if (ToolItem.class.isAssignableFrom(type)) {
					return ToolBar.class.isAssignableFrom(parentType);
				} else if (CoolItem.class.isAssignableFrom(type)) {
					return CoolBar.class.isAssignableFrom(parentType);
				} else if (TabItem.class.isAssignableFrom(type)) {
					return TabFolder.class.isAssignableFrom(parentType);
				} else if (CTabItem.class.isAssignableFrom(type)) {
					return CTabFolder.class.isAssignableFrom(parentType);
				}
				Constructor<?>[] constructors = ((Class<?>) createType)
						.getConstructors();
				for (Constructor<?> constructor : constructors) {
					Class<?>[] parameterTypes = constructor.getParameterTypes();
					if (parameterTypes.length == 0) {
						continue;
					}
					if (parameterTypes[0].isAssignableFrom(parentType)) {
						canCreate = true;
						break;
					}
				}
			} else if (createType instanceof String) {
				String stringType = (String) createType;
				IMetaclass metaclass = XWT.getMetaclass(parentType);
				if (metaclass != null) {
					canCreate = metaclass.findProperty((String) createType) != null;
					if ("menuBar".equals(stringType)
							|| "menu".equals(stringType)
							|| "layoutData".equals(stringType)) {
						canCreate = canCreate
								&& parentNode.getAttribute(stringType) == null;
					}
				}
			}
			// System.out.println("Parent: " + parentType.getSimpleName() +
			// " |Create: " + createType + " |-->Result: " + canCreate);
			return canCreate;
		} catch (Exception e) {
		}
		return false;
	}

	public boolean canCreate(Object parent) {
		return canCreate(parent, getNewObject());
	}

	public boolean isKindOf(String nodeName, String namespace, String targetType) {
		if (nodeName == null || namespace == null || targetType == null) {
			return false;
		}
		if ("Composite".equalsIgnoreCase(targetType)) {
			IMetaclass metaclass = XWT.getMetaclass(nodeName, namespace);
			if (metaclass == null) {
				return false;
			} else {
				return Composite.class.isAssignableFrom(metaclass.getType());
			}
		} else if ("Control".equalsIgnoreCase(targetType)) {
			IMetaclass metaclass = XWT.getMetaclass(nodeName, namespace);
			if (metaclass == null) {
				return false;
			} else {
				return Control.class.isAssignableFrom(metaclass.getType());
			}
		}
		return targetType.equalsIgnoreCase(nodeName);
	}

}
