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
package org.eclipse.e4.languages.javascript.jsdi.request;

import org.eclipse.e4.languages.javascript.jsdi.Location;
import org.eclipse.e4.languages.javascript.jsdi.ThreadReference;

/**
 * A request for breakpoints. The request can be filtered by a given {@link ThreadReference} or by a given {@link String} condition
 * 
 * @since 1.0
 */
public interface BreakpointRequest extends EventRequest {

	/**
	 * Returns the underlying {@link Location} for the breakpoint
	 * 
	 * @return the breakpoint location
	 */
	Location location();

	/**
	 * Adds the given {@link ThreadReference} as a filter to the request. The {@link ThreadReference} is used to prune matching locations for the request.
	 * 
	 * @param thread
	 */
	void addThreadFilter(ThreadReference thread);

	/**
	 * Adds the given condition filter to the request. The condition is used to prune matching locations for the request.
	 * 
	 * @param condition
	 *            the new condition filter
	 */
	void addConditionFilter(String condition);

	/**
	 * Adds the given hit count filter to the request.
	 * 
	 * @param hitcount
	 */
	void addHitCountFilter(int hitcount);
}
