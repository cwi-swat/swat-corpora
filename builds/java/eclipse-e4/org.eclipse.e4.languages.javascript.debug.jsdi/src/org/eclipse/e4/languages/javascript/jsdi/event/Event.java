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

import org.eclipse.e4.languages.javascript.jsdi.Mirror;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequest;

/**
 * An {@link Event} is an object that represents a jsdi event to the model
 * 
 * @since 1.0
 */
public interface Event extends Mirror {

	/**
	 * The underlying request object for the event
	 * 
	 * @return
	 */
	EventRequest request();
}
