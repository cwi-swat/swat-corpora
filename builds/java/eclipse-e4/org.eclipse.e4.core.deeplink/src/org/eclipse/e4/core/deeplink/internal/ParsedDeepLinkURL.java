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
package org.eclipse.e4.core.deeplink.internal;

import org.eclipse.e4.core.deeplink.DeepLinkURLException;


/**
 * Parse a deeplink:// URL so that the installation and the rest of the URL
 * can be processed separately.
 */
public class ParsedDeepLinkURL {

	private static final String PROTOCOL_STRING = "deeplink://";
	
	public final String installation;
	public final String restOfURL;

	public ParsedDeepLinkURL(String urlString) throws DeepLinkURLException {
		if (!urlString.startsWith(PROTOCOL_STRING)) {
			throw new DeepLinkURLException("Invalid deep link URL: " + urlString);
		}
		String urlWithoutProtocol = urlString.replace(PROTOCOL_STRING, "").trim();
		if (urlWithoutProtocol.length() < 1 || urlWithoutProtocol.startsWith("/")) {
			throw new DeepLinkURLException("Invalid deep link URL: " + urlString);
		}
		installation = urlWithoutProtocol.split("/+")[0];
		restOfURL = urlWithoutProtocol.replaceFirst(installation, "").trim();
	}
}
