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

/**
 * Common constants
 * 
 * @since 1.0
 */
public interface Constants {

	public static final String COLON = ":"; //$NON-NLS-1$
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$
	/**
	 * ID of the plugin <br>
	 * <br>
	 * Value is: <code>org.eclipse.e4.languages.javascript.debug</code>
	 */
	public static final String PLUGIN_ID = "org.eclipse.e4.languages.javascript.debug"; //$NON-NLS-1$
	/**
	 * The name for port attributes <br>
	 * <br>
	 * value is: <code>port</code>
	 */
	public static final String PORT = "port"; //$NON-NLS-1$
	/**
	 * UTF-8 encoding constant <br>
	 * <br>
	 * Value is: <code>UTF-8</code>
	 */
	public static final String UTF_8 = "UTF-8"; //$NON-NLS-1$
	/**
	 * Debug property name <br>
	 * <br>
	 * Value is: <code>rhino.debug</code>
	 */
	public static final String RHINO_DEBUG = "rhino.debug"; //$NON-NLS-1$
}
