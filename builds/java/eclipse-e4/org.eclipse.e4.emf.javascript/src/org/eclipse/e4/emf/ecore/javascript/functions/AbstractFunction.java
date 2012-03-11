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

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;

public abstract class AbstractFunction extends BaseFunction {

	public AbstractFunction() {
		super();
	}

	public String getFunctionName() {
		String name = getClass().getName();
		int pos = name.lastIndexOf('.');
		if (pos > 0) {
			name = name.substring(pos + 1);
		}
		String suffix = "Function";
		if (name.endsWith(suffix)) {
			name = name.substring(0, name.length() - suffix.length());
		}
		return name;
	}

	protected <T> T typeCheck(Object o, Class<T> c, String ref) {
		if (c.isInstance(o)) {
			return (T)o;
		}
		o = Context.jsToJava(o, c);
		if (c.isInstance(o)) {
			return (T)o;
		}
		throw new IllegalArgumentException(ref + " should have been of " + c + ", but was " + o);
	}

	protected <T> T typeCheckArgument(Object[] args, int argNum, Class<T> c) {
		if (args.length <= argNum) {
			throw new IllegalArgumentException("To few arguments, argument " + argNum + " is missing, should have been of " + c);
		}
		return typeCheck(args[argNum], c, "Argument " + argNum);
	}
	
	protected final Object[] EMPTY_ARGS = new Object[0];
}
