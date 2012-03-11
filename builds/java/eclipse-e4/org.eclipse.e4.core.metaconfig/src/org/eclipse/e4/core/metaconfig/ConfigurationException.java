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
package org.eclipse.e4.core.metaconfig;

/***
 * When thrown, this exception means that something has gone fatally wrong with
 * the config plugin. The root cause (eg an IO exception) will be passed into
 * the ConigurationException.
 * <p>
 * When thrown, the client of the Configuration plugin is responsible for taking
 * the appropriate action.
 */
public class ConfigurationException extends RuntimeException {

	public ConfigurationException(String string) {
		super(string);
	}

	public ConfigurationException(String string, Exception e) {
		super(string, e);
	}

	private static final long serialVersionUID = 1L;
}
