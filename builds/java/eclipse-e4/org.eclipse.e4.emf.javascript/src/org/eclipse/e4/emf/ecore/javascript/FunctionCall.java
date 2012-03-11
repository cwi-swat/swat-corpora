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
package org.eclipse.e4.emf.ecore.javascript;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

public class FunctionCall {
	private Object scope;
	private String functionName;
	private Object[] args;
	private EObject thisEObject;

	private FunctionCall(Object scope, String functionName, Object[] args, EObject thisEObject) {
		super();
		this.scope = scope;
		this.functionName = functionName;
		this.args = args;
		this.thisEObject = thisEObject;
	}
	public FunctionCall(Object scope, String functionName, Object[] args) {
		this(scope, functionName, args, null);
	}
	public FunctionCall(Object scope, EObject thisEObject, String methodName, Object[] args) {
		this(scope, methodName, args, thisEObject);
	}

	Object call(JavascriptSupport javascriptSupport, boolean rethrowException) {
		return javascriptSupport.call(scope, functionName, args, thisEObject, rethrowException);
	}

	public Collection<EObject> getEObjects() {
		List<EObject> eObjects = new ArrayList<EObject>((thisEObject != null ? 1 : 0) + args.length);
		if (thisEObject != null) {
			eObjects.add(thisEObject);
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof EObject) {
				eObjects.add((EObject)args[i]);
			}
		}
		return eObjects;
	}
}