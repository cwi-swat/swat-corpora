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
package org.eclipse.e4.xwt.tools.ui.designer.core.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.net.URL;

import org.eclipse.e4.xwt.IConstants;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class XWTCodeGenUtil {
	public static String generate(Class<?> aBean) {
		return generate(aBean, false);
	}

	public static String generate(Class<?> aBean, boolean root) {
		if (aBean == null) {
			return null;
		}
		try {
			StringBuffer result = new StringBuffer();
			if (root) {

				result.append("<Composite xmlns=\"" + IConstants.XWT_NAMESPACE
						+ "\"");

				result.append("\t xmlns:x=\"" + IConstants.XWT_X_NAMESPACE
						+ "\"");
				String packageName = aBean.getPackage() == null ? null : aBean
						.getPackage().getName();
				if (packageName != null/* && packageName.length() > 0 */) {
					result.append("\t xmlns:c=\""
							+ IConstants.XAML_CLR_NAMESPACE_PROTO + packageName
							+ "\"");
				}
				result.append("\t xmlns:j=\""
						+ IConstants.XAML_CLR_NAMESPACE_PROTO + "java.lang\"");
				result.append("\t x:Class=\"" + aBean.getName() + "\">");
			} else {
				result.append("<Composite>");
			}
			result.append("\t <Composite.layout>");
			result.append("\t\t <GridLayout " + " numColumns=\"4\" />");
			result.append("\t </Composite.layout>");
			BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(aBean);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor pd : propertyDescriptors) {
				String name = pd.getName();
				if (name == null || "class".equals(name)) {
					continue;
				}
				Class<?> propertyType = pd.getPropertyType();
				if (propertyType.isPrimitive() || propertyType == String.class
						|| propertyType == URL.class) {
					result.append("\t <Label text=\"" + pd.getDisplayName()
							+ "\"/>");
					result.append("\t <Text x:Style=\"Border\" text=\"{Binding path="
							+ pd.getName() + "}\">");
					result.append("\t\t <Text.layoutData>");
					result.append("\t\t\t <GridData grabExcessHorizontalSpace=\"true\"");
					result.append("\t\t\t\t horizontalAlignment=\"GridData.FILL\" widthHint=\"100\"/>");
					result.append("\t\t </Text.layoutData>");
					result.append("\t </Text>");
				} else if (propertyType.isEnum()) {
					result.append("\t <Label text=\"" + pd.getDisplayName()
							+ "\"/>");
					result.append("\t <Combo text=\"{Binding path="
							+ pd.getName() + "}\">");
					result.append("\t\t <Combo.layoutData>");
					result.append("\t\t\t <GridData grabExcessHorizontalSpace=\"true\"");
					result.append("\t\t\t\t horizontalAlignment=\"GridData.FILL\" widthHint=\"100\"/>");
					result.append("\t\t </Combo.layoutData>");

					result.append("\t\t <Combo.items>");
					for (Object object : propertyType.getEnumConstants()) {
						result.append("\t\t\t <j:String>" + object.toString()
								+ "</j:String>");
					}
					result.append("\t\t </Combo.items>");
					result.append("\t </Combo>");

				} else {
					result.append("\t <Group text=\"" + pd.getDisplayName()
							+ "\">");
					result.append("\t\t <Group.layout>");
					result.append("\t\t\t <FillLayout/>");
					result.append("\t\t </Group.layout>");

					String elementType = propertyType.getSimpleName();
					result.append("\t\t <c:" + elementType
							+ " DataContext=\"{Binding path=" + pd.getName()
							+ "}\"/>");

					result.append("\t\t <Group.layoutData>");
					result.append("\t\t\t <GridData grabExcessHorizontalSpace=\"true\" horizontalSpan=\"4\"");
					result.append("\t\t\t\t horizontalAlignment=\"GridData.FILL\" widthHint=\"200\"/>");
					result.append("\t\t </Group.layoutData>");

					result.append("\t </Group>");
				}
			}
			result.append("</Composite>");
			return result.toString();
		} catch (IntrospectionException e) {
		}
		return null;
	}
}
