/*******************************************************************************
 * Copyright (c) 2010 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.springframework;

import org.eclipse.e4.xwt.ICLRFactory;

/**
 * Arguments from {@link ICLRFactory#createCLR(String)} which is parsed.
 * 
 */
public interface IArguments {

	// Default separator
	String DEFAULT_NAME_SEPARATOR = " ";
	String DEFAULT_VALUE_SEPARATOR = "=";

	/**
	 * Returns the original source coming from
	 * {@link ICLRFactory#createCLR(String)} which was used to build this
	 * arguments.
	 * 
	 * @return
	 */
	String getSource();

	/**
	 * Returns the value of the args name.
	 * 
	 * @param name
	 * @return
	 */
	String getValue(String name);
}
