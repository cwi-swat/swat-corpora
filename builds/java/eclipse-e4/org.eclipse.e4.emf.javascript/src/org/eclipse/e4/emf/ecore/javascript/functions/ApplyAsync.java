/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class ApplyAsync extends ApplyFunction {
	
	public ApplyAsync(AsyncSupport asyncSupport) {
		super(asyncSupport);
	}

	protected Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Function function, final Object[] funArgs) {
		runAsync(new Runnable() {
			public void run() {
				ApplyAsync.super.call(cx, scope, thisObj, function, funArgs);
			}
		});
		return null;
	}
}
