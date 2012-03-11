/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.jsdi.rhino;

import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.jsdi.FunctionReference;
import org.eclipse.e4.languages.javascript.jsdi.ObjectReference;

/**
 * Rhino implementation of {@link FunctionReference}
 * 
 * @see FunctionReference
 * @see ObjectReference
 * @see ObjectReferenceImpl
 * @since 0.9
 */
public class FunctionReferenceImpl extends ObjectReferenceImpl implements FunctionReference {

	private String functionName;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param body
	 * @param stackFrameImpl
	 */
	public FunctionReferenceImpl(VirtualMachineImpl vm, Map body, StackFrameReferenceImpl stackFrameImpl) {
		super(vm, body, stackFrameImpl);
		this.functionName = (String) body.get(JSONConstants.NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.FunctionReference#functionName()
	 */
	public String functionName() {
		return functionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.rhino.ObjectReferenceImpl#valueString()
	 */
	public String valueString() {
		// TODO return the function body code
		return "Function"; //$NON-NLS-1$
	}
}
