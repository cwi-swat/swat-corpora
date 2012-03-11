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
package org.eclipse.e4.core.deeplink.api;

import org.eclipse.e4.core.functionalprog.optionmonad.Option;

import static org.eclipse.e4.core.functionalprog.optionmonad.None.none;
import static org.eclipse.e4.core.functionalprog.optionmonad.Some.some;

/**
 * Archetypal deeplink:
 * deeplink://appInstance/handlerType/handlerInstanceId/action?param1=value1&param2=value2...
 * 
 * Example:
 * deeplink://myApp/perspective/org.eclipse.myApp.myPerspectiveID/do-stuff?animal=monkey&colour=blue&mood=angry
 *
 */
public class URLPathInfoParser {
	public final String handlerType;
	public final String handlerId;
	public final Option<String> action;

	public URLPathInfoParser(String urlPathInfo) {
		if (urlPathInfo == null || "/".equals(urlPathInfo)) {
			throw new IllegalArgumentException("Empty URL path: nothing to do");
		}
		
		String[] urlParts = urlPathInfo.split("/+");
		if (urlParts.length < 3) {
			throw new IllegalArgumentException("Need both of: '/handlerType/handlerInstanceId' in URL but got: " + urlPathInfo);
		}
		handlerType = urlParts[1];
		handlerId = urlParts[2];
		if (urlParts.length > 3) {
			action = some(urlParts[3]);
		} else {
			action = none();
		}
	}
	
	@Override
	public String toString() {
		return handlerType + "/" + handlerId + (action.hasValue() ? "/" + action.get() : "");
	}
}