/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.indexing;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class StoreException extends Exception {
	private static final long serialVersionUID = -6565251688819493750L;
	protected Throwable wrappedException;

	public StoreException(String message) {
		super(message);
	}

	public StoreException(String message, Throwable wrappedException) {
		super(message);
		this.wrappedException = wrappedException;
	}

	/**
	 * Prints a stack trace out for the exception.
	 */
	public void printStackTrace() {
		printStackTrace(System.err);
	}

	/**
	 * Prints a stack trace out for the exception.
	 */
	public void printStackTrace(PrintStream output) {
		synchronized (output) {
			super.printStackTrace(output);
			if (wrappedException != null)
				wrappedException.printStackTrace(output);
		}
	}

	/**
	 * Prints a stack trace out for the exception.
	 */
	public void printStackTrace(PrintWriter output) {
		synchronized (output) {
			super.printStackTrace(output);
			if (wrappedException != null)
				wrappedException.printStackTrace(output);
		}
	}

}

