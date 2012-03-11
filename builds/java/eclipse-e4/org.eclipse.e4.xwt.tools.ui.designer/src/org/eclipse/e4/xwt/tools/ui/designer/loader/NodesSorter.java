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

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;

public class NodesSorter {

	public static List<XamlNode> sortWithAttr(XamlNode[] sourceArray, String attrName) {
		return sortWithAttr(sourceArray, new AttrComparator(attrName), attrName);
	}

	public static List<XamlNode> sortWithAttr(XamlNode[] sourceArray, Comparator<XamlElement> comparator, String attrName) {
		List<XamlNode> columnsList = new LinkedList<XamlNode>();
		for (XamlNode column : sourceArray) {
			XamlElement columnElement = (XamlElement) column;
			XamlAttribute indexAttr = columnElement.getAttribute(attrName, IConstants.XWT_NAMESPACE);
			if (indexAttr == null) {
				columnsList.add(column);
			}
		}

		List<XamlElement> tempList = new LinkedList<XamlElement>();
		for (XamlNode column : sourceArray) {
			XamlElement columnElement = (XamlElement) column;
			XamlAttribute indexAttr = columnElement.getAttribute(attrName, IConstants.XWT_NAMESPACE);
			if (indexAttr != null) {
				tempList.add(columnElement);
			}
		}

		if (comparator == null) {
			comparator = new AttrComparator(attrName);
		}
		Collections.sort(tempList, comparator);

		for (XamlElement columnElement : tempList) {
			XamlAttribute indexAttr = columnElement.getAttribute(attrName, IConstants.XWT_NAMESPACE);
			try {
				int index = Integer.parseInt(indexAttr.getValue());
				columnsList.add(index, columnElement);
			} catch (Exception e) {
				columnsList.add(columnsList.size(), columnElement);
			}
		}

		return columnsList;
	}

	private static class AttrComparator implements Comparator<XamlElement>,
			Serializable {
		private static final long serialVersionUID = 5974723422066058166L;
		private String attrName;

		public AttrComparator(String attrName) {
			this.attrName = attrName;
		}

		public int compare(XamlElement e1, XamlElement e2) {
			XamlAttribute attr1 = e1.getAttribute(attrName, IConstants.XWT_NAMESPACE);
			XamlAttribute attr2 = e2.getAttribute(attrName, IConstants.XWT_NAMESPACE);
			String compare1 = attr1.getValue();
			String compare2 = attr2.getValue();
			if (compare1 == null || compare2 == null) {
				return 0;
			}
			try {
				int index1 = Integer.parseInt(compare1);
				int index2 = Integer.parseInt(compare2);
				return index1 > index2 ? 1 : -1;
			} catch (Exception e) {
				return compare1.compareTo(compare2);
			}
		}
	}
}
