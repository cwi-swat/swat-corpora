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
package org.eclipse.e4.core.deeplink.api;

import org.eclipse.core.runtime.IStatus;

/**
 * A abstraction for injecting specific IStatus objects into an implementation
 * for testability purposes.  It smells, but there doesn't seem to be an easier
 * way to make this code testable since Eclipse's Status doesn't implement
 * equals/hashCode (as-of 3.4).
 */
public interface IStatusFactory {

	public IStatus error(String message);

	public IStatus error(String message, Throwable t);

	public IStatus warning(String message);

	public IStatus info(String message);

}