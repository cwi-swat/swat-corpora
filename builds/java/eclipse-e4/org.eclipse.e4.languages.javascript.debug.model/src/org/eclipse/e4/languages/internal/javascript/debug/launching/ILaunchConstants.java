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
package org.eclipse.e4.languages.internal.javascript.debug.launching;

import org.eclipse.e4.languages.javascript.jsdi.connect.Connector;

/**
 * Constants used for launching
 * 
 * @since 0.9
 */
public interface ILaunchConstants {

	/**
	 * Constant for the host name attribute
	 */
	public static final String HOST = "host"; //$NON-NLS-1$

	/**
	 * Constant for the port attribute
	 */
	public static final String PORT = "port"; //$NON-NLS-1$

	/**
	 * Constant for the argument map for a connector attribute
	 */
	public static final String ARGUMENT_MAP = "argument_map"; //$NON-NLS-1$

	/**
	 * Constant for the id of the {@link Connector} used in a connector-based launch
	 */
	public static final String CONNECTOR_ID = "connector_id"; //$NON-NLS-1$
}
