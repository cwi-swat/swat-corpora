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
/**
 * 
 */
package org.eclipse.e4.core.deeplink.api;

import java.util.Map;

/**
 * A data transfer object (since Java can't return multiple values from a 
 * function) encapsulating the result of executing a deeplink.
 */
public class ParameterProcessResults {
	public boolean loaded = false;
	public boolean activatedParameterCallback = false;
	public Map<String, String> outputData = null;
}