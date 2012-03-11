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

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class LoadEPackageFunction extends AbstractFunction {

	private JavascriptSupport javascriptSupport;
	
	public LoadEPackageFunction(JavascriptSupport javascriptSupport) {
		super();
		this.javascriptSupport = javascriptSupport;
	}

	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		String packageUri = typeCheckArgument(args, 0, String.class);
		String altUri = null;
		if (args.length > 1) {
			altUri = typeCheckArgument(args, 1, String.class);
		}
		return javascriptSupport.loadEPackage(packageUri, altUri);
	}
}
