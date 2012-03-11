/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.connect;

public class TimeoutException extends Exception {
	private static final long serialVersionUID = 5259283325294894499L;

	public TimeoutException() {
		super();
	}

	public TimeoutException(String message) {
		super(message);
	}

}
