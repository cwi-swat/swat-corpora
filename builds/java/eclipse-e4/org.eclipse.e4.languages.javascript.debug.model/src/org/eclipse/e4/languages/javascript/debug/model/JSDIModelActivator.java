/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.model;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.e4.languages.internal.javascript.debug.launching.ConnectorsManager;

/**
 * Plugin activator for the model
 * 
 * @since 0.9
 */
public class JSDIModelActivator extends Plugin {

	private static ConnectorsManager manager = null;

	/**
	 * Constructor
	 */
	public JSDIModelActivator() {
	}

	/**
	 * Returns the singleton {@link ConnectorsManager} instance
	 * 
	 * @return the {@link ConnectorsManager}
	 */
	public static synchronized ConnectorsManager getConnectionsManager() {
		if (manager == null) {
			manager = new ConnectorsManager();
		}
		return manager;
	}
}
