/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.emf.ecore.javascript.functions;

import org.eclipse.e4.emf.ecore.javascript.AsyncSupport;

public abstract class AsyncSupportFunction extends AbstractFunction {

	protected AsyncSupportFunction() {
		super();
	}

	private AsyncSupport asyncSupport;

	protected AsyncSupportFunction(AsyncSupport asyncSupport) {
		this();
		this.asyncSupport = asyncSupport;
	}

	protected void runAsync(Runnable runnable) {
		if (asyncSupport != null) {
			asyncSupport.run(runnable);
		} else {
			runnable.run();
		}
	}
}
