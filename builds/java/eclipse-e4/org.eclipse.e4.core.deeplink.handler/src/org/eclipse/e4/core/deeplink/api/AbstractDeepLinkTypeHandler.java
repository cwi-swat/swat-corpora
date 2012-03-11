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

import static org.eclipse.e4.core.functionalprog.optionmonad.None.none;
import static org.eclipse.e4.core.functionalprog.optionmonad.Some.some;

import static org.apache.commons.lang.StringEscapeUtils.escapeXml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.functionalprog.optionmonad.Option;

/**
 * Given the following archetypal deeplink:
 * <ul>
 * <li>deeplink://appInstance/handlerType/handlerInstanceId/action?param1=value1&param2=value2...
 * </ul>
 * A DeepLinkTypeHandler is a strategy pattern object that defines how deeplink
 * requests are handled for a particular deeplink handlerType.
 * <p>
 * Each DeepLinkTypeHandler must be registered with the system via the
 * deepLinkTypeHandler extension point.
 * <p>
 * This is (more or less) following a Servlet Delegate pattern.   
 * This knows about HTTP stuff (req/resp etc) and should not leak this info to 
 * subclasses or collaborators.
 */
public abstract class AbstractDeepLinkTypeHandler {

	public static final String CLASS = "class";
	private HttpServletRequest request;
	private HttpServletResponse response;

	private URLPathInfoParser parsedPathParts;

	public IStatusFactory statusFactory = new StatusFactory();

	public void init(HttpServletRequest request, HttpServletResponse response, URLPathInfoParser pathParts) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		response.setContentType("text/xml");
		this.request = request;
		this.response = response;
		this.parsedPathParts = pathParts;
	}

	public abstract void processDeepLink() throws IOException;
	
	public void doGet() throws IOException{
		processDeepLink();
	}

	public void doPost() throws IOException{
		processDeepLink();
	}


	private Option<IConfigurationElement> find(IConfigurationElement[] elementsToLookIn, String idToFind) {
		for (IConfigurationElement configurationElement : elementsToLookIn) {
			String id = configurationElement.getAttribute(idToFind);
			if (getHandlerId().equals(id)) {
				return some(configurationElement);
			}
		}
		return none();
	}

	@SuppressWarnings("unchecked")
	protected Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	protected String getHandlerType() {
		return parsedPathParts.handlerType;
	}

	protected String getHandlerId() {
		return parsedPathParts.handlerId;
	}

	protected String getAction() {
		return parsedPathParts.action.getOrSubstitute("");
	}

	/*
	 * FIXME DJO/JADN 2010-02-20. This method is monolithic, thus does not lend
	 * itself to being modified either here or by subclasses eg if the "value"
	 * [from results.outputData.get(key)] needs anything other than .toString()
	 * then you are in trouble.
	 */
	protected void outputResponse(final ParameterProcessResults results) throws IOException {
		ServletOutputStream out = response.getOutputStream();
		out.println("<?xml version=\"1.0\"?>");
		out.println("<deeplink>");
		out.println("   <result id=\"" + parsedPathParts.handlerId + "\" loaded=\"" + results.loaded + "\" callbackRan=\"" + results.activatedParameterCallback + "\"/>");
		if (results.activatedParameterCallback) {
			out.println("   <outputData id=\"" + parsedPathParts.handlerId + "\">");
			if (results.outputData != null) {
				for (String key : results.outputData.keySet()) {
					String value = escapeXml(results.outputData.get(key));
					out.print("      <element key=\"" + key + "\">");
					out.print(value);
					out.println("</element>");
				}
			}
			out.println("   </outputData>");
		}
		out.println("</deeplink>");
	}

	protected IConfigurationElement[] getCongfigElementsFromRegistry(String pluginId, String extPointID) {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		return extensionRegistry.getConfigurationElementsFor(pluginId, extPointID);
	}

	protected void findInstanceHandlerAndExecuteCallback(ParameterProcessResults[] results, IConfigurationElement[] congfigElementsFromRegistry, Map<String, String[]> parameters, String extensionPointID) {
		Option<IConfigurationElement> found = find(congfigElementsFromRegistry, extensionPointID);
		if (found.hasValue()) {
			IConfigurationElement configurationElement = found.get();
			Option<AbstractDeepLinkInstanceHandler> instanceHandlerOption = buildInstanceHandlerFromClassAttribute(configurationElement);
			if (instanceHandlerOption.hasValue()) {
				AbstractDeepLinkInstanceHandler instanceHandler = instanceHandlerOption.get();
				instanceHandler.handle(results, getHandlerId(), getAction(), parameters);
			}
		}
	}

	private Option<AbstractDeepLinkInstanceHandler> buildInstanceHandlerFromClassAttribute(IConfigurationElement configurationElement) {
		try {
			Object interimObject = configurationElement.createExecutableExtension(CLASS);
			if (interimObject instanceof AbstractDeepLinkInstanceHandler) {
				AbstractDeepLinkInstanceHandler handler = (AbstractDeepLinkInstanceHandler) interimObject;
				return some(handler);
			} else {
				Activator.getDefault().getLog().log(new Status(Status.INFO, Activator.PLUGIN_ID, ("Found extension point but not DeepLinkInstanceHandler: " + configurationElement.getName())));
				return none();
			}
		} catch (CoreException e) {
			logExtensionError(configurationElement.getName(), e);
			return none();
		}
	}

	private void logExtensionError(String name, CoreException e) {
		Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, ("Incorrect extension for: " + name), e));
	}

}
