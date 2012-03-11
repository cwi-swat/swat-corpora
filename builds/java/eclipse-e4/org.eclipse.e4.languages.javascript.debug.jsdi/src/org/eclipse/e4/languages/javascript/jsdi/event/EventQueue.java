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
package org.eclipse.e4.languages.javascript.jsdi.event;

/**
 * A queue for JSDI {@link EventSet}s
 * 
 * @since 1.0
 */
public interface EventQueue {

	/**
	 * Removes an {@link EventSet} from the queue waiting for the default timeout if needed.
	 * 
	 * @return the top {@link EventSet} on the queue or <code>null</code> if an {@link EventSet} is not available or there was an exception retrieving the top {@link EventSet}
	 */
	public EventSet remove();

	/**
	 * Removes an {@link EventSet} from the queue waiting for the specified timeout if needed.
	 * 
	 * @return the top {@link EventSet} on the queue or <code>null</code> if an {@link EventSet} is not available or there was an exception retrieving the top {@link EventSet}
	 */
	public EventSet remove(int timeout);

}
