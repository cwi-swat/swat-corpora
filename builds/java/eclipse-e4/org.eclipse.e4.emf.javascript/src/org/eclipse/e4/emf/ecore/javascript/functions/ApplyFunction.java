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
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public abstract class ApplyFunction extends AsyncSupportFunction {

	protected ApplyFunction() {
		super();
	}

	protected int getAdditionalArity() {
		return 0; // Function, ...args
	}
	public int getArity() {
		return 1 + getAdditionalArity(); // Function, ...args
	}

	protected ApplyFunction(AsyncSupport asyncSupport) {
		super(asyncSupport);
	}

	protected Object[] getFunArgs(Object[] args) {
		return copyArgs(args, 1);
	}
	
	protected static Object[] copyArgs(Object[] args, int start) {
		Object funArgs[] = new Object[args.length - start];
		System.arraycopy(args, start, funArgs, 0, funArgs.length);
		return funArgs;
	}
	
	public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, Object[] args) {
		if (args.length < getArity()) {
			throw new IllegalArgumentException("Needed at least " + getArity() + " arguments, received " + args.length);
		}
		Function function = typeCheckArgument(args, 0, Function.class);
		Object[] funArgs = getFunArgs(args);
		return call(cx, scope, thisObj, function, funArgs);
	}

	protected Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Function function, final Object funArgs[]) {
		return function.call(cx, scope, thisObj, funArgs);
	}
}
