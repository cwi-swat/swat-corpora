/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript;

public class JSBundleException extends Exception {
	private static final long serialVersionUID = -7654673673511777575L;

	public JSBundleException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSBundleException(String message) {
		super(message);
	}

}
