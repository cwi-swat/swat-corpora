/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.search;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;

public class JavaElementLine {
	private IJavaScriptElement fElement;
	private int fLine;
	private String fLineContents;
	
	/**
	 * @param element either an IJavaScriptUnit or an IClassFile
	 */
	public JavaElementLine(IJavaScriptElement element, int line, String lineContents) {
		fElement= element;
		fLine= line;
		fLineContents= lineContents;
	}
	
	public IJavaScriptElement getJavaElement() {
		return fElement;
	}
	
	public int getLine() {
		return fLine;
	}
	
	public String getLineContents() {
		return fLineContents;
	}
}
