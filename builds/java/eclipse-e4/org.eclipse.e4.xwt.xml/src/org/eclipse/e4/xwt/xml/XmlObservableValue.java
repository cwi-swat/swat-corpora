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
package org.eclipse.e4.xwt.xml;

import org.eclipse.e4.xwt.databinding.XWTObservableValue;
import org.w3c.dom.Node;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XmlObservableValue extends XWTObservableValue {

	public XmlObservableValue(Class<?> valueType, Node node, String path) {
		super(valueType, node, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.dataproviders.xml.XWTObservableValue#doSetApprovedValue
	 * (java.lang.Object)
	 */
	protected void doSetApprovedValue(Object value) {
		String newValue = value == null ? "" : value.toString();
		((Node) getObserved()).setNodeValue(newValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.dataproviders.xml.XWTObservableValue#doGetValue()
	 */
	protected Object doGetValue() {
		return ((Node) getObserved()).getNodeValue();
	}
}
