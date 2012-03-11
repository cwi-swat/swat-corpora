/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.core.deeplink;

/**
 * If the result XML stream cannot be parsed in any way, a DeepLinkResultException
 * is thrown.
 */
public class DeepLinkResultException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeepLinkResultException(String message) {
		super(message);
	}

	public DeepLinkResultException(Throwable cause) {
		super(cause);
	}

	public DeepLinkResultException(String message, Throwable cause) {
		super(message, cause);
	}

}
