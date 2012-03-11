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
package org.eclipse.e4.xwt.tools.ui.designer.core.problems;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class Problem {

	// Problem Types.
	public static final int NONE = 0;
	public static final int WARNING = 1;
	public static final int INFO = 2;
	public static final int ERROR = 3;

	private String message;
	private int type = NONE;

	public int start;
	public int end;
	public int line;

	private Object source;

	public Problem(String message, int type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
}
