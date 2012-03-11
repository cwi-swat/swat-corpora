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
 * If a deeplink:// URL cannot be parsed, the framework throws this exception.
 */
public class DeepLinkURLException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeepLinkURLException(String message) {
		super(message);
	}

	public DeepLinkURLException(Throwable cause) {
		super(cause);
	}

	public DeepLinkURLException(String message, Throwable cause) {
		super(message, cause);
	}

}
