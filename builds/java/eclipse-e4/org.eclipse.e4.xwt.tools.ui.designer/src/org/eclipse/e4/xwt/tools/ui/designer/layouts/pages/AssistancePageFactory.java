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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutDataType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class AssistancePageFactory {

	public static LayoutAssistantPage createPage(LayoutType layoutType) {
		if (!isSupport(layoutType)) {
			return null;
		} else if (LayoutType.FillLayout == layoutType) {
			return new FillLayoutAssistantPage();
		} else if (LayoutType.GridLayout == layoutType) {
			return new GridLayoutAssistantPage();
		} else if (LayoutType.RowLayout == layoutType) {
			return new RowLayoutAssistantPage();
		}
		return null;
	}

	public static boolean isSupport(LayoutType layoutType) {
		return LayoutType.FillLayout == layoutType || LayoutType.GridLayout == layoutType || LayoutType.RowLayout == layoutType;
	}

	public static boolean isSupport(LayoutDataType layoutDataType) {
		return LayoutDataType.GridData == layoutDataType || LayoutDataType.RowData == layoutDataType;
	}

	public static LayoutDataAssistantPage createPage(LayoutDataType layoutDataType) {
		if (!isSupport(layoutDataType)) {
			return null;
		} else if (LayoutDataType.GridData == layoutDataType) {
			return new GridDataAssistantPage();
		} else if (LayoutDataType.RowData == layoutDataType) {
			return new RowDataAssistantPage();
		}
		return null;
	}

	public static Map<Object, IAssistantPage> newPages() {
		Map<Object, IAssistantPage> map = new HashMap<Object, IAssistantPage>();
		for (LayoutType layoutType : LayoutsHelper.layoutsList) {
			LayoutAssistantPage page = createPage(layoutType);
			if (page == null) {
				continue;
			}
			map.put(layoutType, page);
		}
		map.put(LayoutDataType.RowData, createPage(LayoutDataType.RowData));
		map.put(LayoutDataType.GridData, createPage(LayoutDataType.GridData));
		return map;
	}
}
