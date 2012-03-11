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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.deeplink.api.AbstractDeepLinkTypeHandler;
import org.eclipse.e4.core.deeplink.api.Activator;
import org.eclipse.e4.core.functionalprog.optionmonad.Option;


/**
 * The deeplink handler servlet.
 */
public class RequestHandler extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static DeepLinkTypeHandlerFactory typeHandlerFactory;

	public RequestHandler() {				
		DeepLinkTypeHandlerMapper deepLinkTypeHandlerMapper = new DeepLinkTypeHandlerMapper(Platform.getExtensionRegistry());
		Map<String, AbstractDeepLinkTypeHandler> handlerMap = deepLinkTypeHandlerMapper.getMap();
		typeHandlerFactory = new DeepLinkTypeHandlerFactory(handlerMap);
	}
	
	// Used to inject mapping dependency for testing
	RequestHandler(Map<String, AbstractDeepLinkTypeHandler> handlerMap) {
		typeHandlerFactory = new DeepLinkTypeHandlerFactory(handlerMap);
	}

	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if( !isRequestFromLocalhost(req) ) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		try {
			Option<AbstractDeepLinkTypeHandler> typeHandlerOption = typeHandlerFactory.createTypeHandler(req, resp);
			if (typeHandlerOption.hasValue()) {
				AbstractDeepLinkTypeHandler typeHandler = typeHandlerOption.get();
				typeHandler.doGet();			
			} else {
				String message = "No deep link handler for URL path: " + req.getPathInfo();
				logInfo(message);
				returnErrorMessageToClient(message, resp);
			}
		} catch (Throwable t) {
			logUnhandledExceptionAndReturnResult(t, resp);
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		
		if( !isRequestFromLocalhost(req) ) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		try {
			Option<AbstractDeepLinkTypeHandler> typeHandlerOption = typeHandlerFactory.createTypeHandler(req, resp);
			if (typeHandlerOption.hasValue()) {
				AbstractDeepLinkTypeHandler typeHandler = typeHandlerOption.get();
				typeHandler.doPost();
			} else {
				String message = "No deep link handler for URL path: " + req.getPathInfo();
				logInfo(message);
				returnErrorMessageToClient(message, resp);
			}
		} catch (Throwable t) {
			logUnhandledExceptionAndReturnResult(t, resp);
		}
	}

	private boolean isRequestFromLocalhost(HttpServletRequest req) {
		String remoteHost = req.getRemoteHost();
		return "localhost".equalsIgnoreCase(remoteHost) || "127.0.0.1".equalsIgnoreCase(remoteHost);
	}
	
	private void logUnhandledExceptionAndReturnResult(Throwable t, HttpServletResponse response) {
		returnErrorMessageToClient(t.getMessage(), response);
		logException("Unhandled exception processing HTTP request: "
								+ t.getMessage(), t);
	}

	/**
	 * FIXME: All result XML generation needs to be centralized in a single
	 * factory.
	 */
	private void returnErrorMessageToClient(String message, HttpServletResponse response) {
		response.setContentType("text/xml");
		try {
			ServletOutputStream out = response.getOutputStream();	
			out.println("<?xml version=\"1.0\"?>");
			out.println("<deeplink>");
			out.println("   <result exception=\"" + message + "\"/>");
			out.println("</deeplink>");
		} catch (IOException e) {
			logException("Unable to return error result to client: " 
					+ e.getMessage(), e);
		}
	}

	private void logException(String message, Throwable t) {
		Activator.getDefault().getLog().log(
				new Status(Status.ERROR, Activator.PLUGIN_ID,
						message, t));
	}
	
	private void logInfo(String message) {
		Activator.getDefault().getLog().log(
				new Status(Status.ERROR, Activator.PLUGIN_ID,
						message));
	}
	
}