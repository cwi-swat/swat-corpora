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
package org.eclipse.e4.xwt.ui.workbench.views;

/**
 * The default class to handle the connection with e4 workbench.
 * 
 * @author yyang (yves.yang@soyatec.com)
 */
public class XWTInputPart extends XWTStaticPart {
	protected Object input;
	protected Class<?> inputType;

	public XWTInputPart() {
	}

	public Class<?> getInputType() {
		return inputType;
	}

	public void setInput(Object input) {
		if (this.input == input) {
			return;
		}
		Class<?> inputType = getInputType();
		if (inputType == null || inputType.isInstance(input)) {
			refresh(getURL(), input, getClassLoader());
		}
		this.input = input;
	}
}
