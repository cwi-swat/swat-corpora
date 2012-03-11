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
package org.eclipse.e4.xwt.tools.ui.designer.layouts;

/**
 * @author jliu jin.liu@soyatec.com
 */
public enum LayoutType {
	Unknown(null), NullLayout("Null(default)"), GridLayout("GridLayout"), FillLayout("FillLayout"), RowLayout("RowLayout"), StackLayout("StackLayout"), FormLayout("FormLayout");
	private String value;

	private LayoutType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
