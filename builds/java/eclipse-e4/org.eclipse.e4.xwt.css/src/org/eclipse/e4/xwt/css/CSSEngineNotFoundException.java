/*******************************************************************************
 * Copyright (c) 2008, 2009 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.css;

import org.eclipse.e4.xwt.XWTException;

/**
 * Exception used when CSS Engine is not retrieved.
 * 
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 */
public class CSSEngineNotFoundException extends XWTException {

	public CSSEngineNotFoundException(Throwable throwable) {
		super(throwable);
	}
}
