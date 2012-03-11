/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.javascript.registry;

import java.util.Hashtable;
import java.util.StringTokenizer;
import org.eclipse.core.runtime.*;
import org.eclipse.e4.internal.javascript.registry.Activator;
import org.eclipse.e4.javascript.JSBundle;
import org.mozilla.javascript.*;
import org.osgi.framework.Bundle;

public class JavaScriptFactory implements IExecutableExtensionFactory, IExecutableExtension {

	private IConfigurationElement config;
	private String functionName;

	/**
	 * documented to avoid warning
	 * @throws CoreException  
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		this.config = config;
		if (data != null) {
			if (data instanceof String)
				functionName = (String) data;
			else if (data instanceof Hashtable) {
				functionName = (String) ((Hashtable) data).get("name"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * documented to avoid warning
	 * @throws CoreException  
	 */
	public Object create() throws CoreException {
		Bundle bundle = Activator.getBundle(config.getContributor().getName());
		JSBundle jsBundle = Activator.getJSBundle(bundle);
		if (jsBundle == null)
			throw error("No associated JavaScript Bundle found");

		Scriptable currentScope = jsBundle.getScope();
		if (currentScope == null)
			throw error("Associate JavaScript bundle is not resolved");

		if (functionName == null)
			throw error("Function name cannot be null");

		StringTokenizer tokenizer = new StringTokenizer(functionName, "."); //$NON-NLS-1$
		while (true) {
			String token = tokenizer.nextToken();
			Object value = currentScope.get(token, currentScope);
			if (!tokenizer.hasMoreTokens()) {
				if (!(value instanceof Function))
					throw error(functionName + " is not a JavaScript Function");

				final Function function = (Function) value;
				final Scriptable callScope = currentScope;
				Object result = jsBundle.call(new ContextAction() {
					public Object run(Context cx) {
						return function.call(cx, callScope, callScope, null);
					}
				});
				if (result != null && result instanceof Wrapper)
					result = ((Wrapper) result).unwrap();
				return result;
			}

			if (value instanceof Scriptable)
				currentScope = (Scriptable) value;
			else
				throw error(functionName + " not found");
		}
	}

	private static CoreException error(String message) {
		return new CoreException(new Status(IStatus.ERROR, Activator.ID, message));
	}
}
