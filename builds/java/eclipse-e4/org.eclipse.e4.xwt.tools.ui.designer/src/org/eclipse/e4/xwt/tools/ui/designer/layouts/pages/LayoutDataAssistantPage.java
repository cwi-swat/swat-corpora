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
package org.eclipse.e4.xwt.tools.ui.designer.layouts.pages;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutDataType;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class LayoutDataAssistantPage extends AssistantPage {

	private LayoutDataType layoutDataType;

	public LayoutDataAssistantPage(LayoutDataType layoutDataType) {
		this.layoutDataType = layoutDataType;
	}

	public XamlElement createAssistModel(XamlNode model) {
		if (model == null) {
			return null;
		}
		XamlAttribute attribute = getAssistParent();
		XamlElement layoutDataModel = attribute.getChild(layoutDataType.name());
		if (layoutDataModel == null) {
			layoutDataModel = XamlFactory.eINSTANCE.createElement(layoutDataType.name(), IConstants.XWT_NAMESPACE);
		}
		if (attribute.eContainer() == null) {
			attribute.getChildNodes().add(layoutDataModel);
		}
		return layoutDataModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.AssistantPage#createAssistParent(org.soyatec.tools.designer.xaml.XamlNode)
	 */
	protected XamlAttribute createAssistParent(XamlNode parent) {
		XamlAttribute attribute = parent.getAttribute("layoutData");
		if (attribute == null) {
			attribute = XamlFactory.eINSTANCE.createAttribute("layoutData", IConstants.XWT_NAMESPACE);
		}
		return attribute;
	}

}
