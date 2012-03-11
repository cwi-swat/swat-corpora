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
package org.eclipse.e4.core.deeplink.handler;

import static org.eclipse.e4.core.functionalprog.optionmonad.None.none;
import static org.eclipse.e4.core.functionalprog.optionmonad.Some.some;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.e4.core.deeplink.api.AbstractDeepLinkTypeHandler;
import org.eclipse.e4.core.deeplink.api.URLPathInfoParser;
import org.eclipse.e4.core.functionalprog.optionmonad.Option;

/**
 * (non-API) class DeepLinkTypeHandlerFactory.
 * <p>
 * Given the following archetypal deeplink:
 * <ul>
 * <li>deeplink://appInstance/handlerType/handlerInstanceId/action?param1=value1&param2=value2...
 * </ul>
 * A DeepLinkTypeHandler is a strategy pattern object that defines how deeplink
 * requests are handled for a particular deeplink handlerType.
 * <p>
 * This class is a factory for retrieving a type handler for a particular handlerType. 
 */
public class DeepLinkTypeHandlerFactory {
	
	private Map<String, AbstractDeepLinkTypeHandler> handlers = null;
	
	public DeepLinkTypeHandlerFactory(Map<String, AbstractDeepLinkTypeHandler> typeHandlerMap) {
		handlers = typeHandlerMap;
	}

	public Option<AbstractDeepLinkTypeHandler> createTypeHandler(HttpServletRequest request,
			HttpServletResponse response) {

		URLPathInfoParser pathParts = new URLPathInfoParser(request.getPathInfo());

		AbstractDeepLinkTypeHandler typeHandler = handlers.get(pathParts.handlerType);
		if (typeHandler != null ) {
			typeHandler.init(request, response, pathParts);
			return some(typeHandler);
		} else {
			return none();
		}
	}

}
