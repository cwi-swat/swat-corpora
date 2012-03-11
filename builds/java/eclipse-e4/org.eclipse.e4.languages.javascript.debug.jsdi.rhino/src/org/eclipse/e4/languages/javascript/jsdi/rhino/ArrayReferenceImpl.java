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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.jsdi.ArrayReference;
import org.eclipse.e4.languages.javascript.jsdi.ObjectReference;
import org.eclipse.e4.languages.javascript.jsdi.PropertyReference;
import org.eclipse.e4.languages.javascript.jsdi.Value;

/**
 * Rhino implementation of {@link ArrayReference}
 * 
 * @see ArrayReference
 * @see ObjectReference
 * @see ObjectReferenceImpl
 * @since 1.0
 */
public class ArrayReferenceImpl extends ObjectReferenceImpl implements ArrayReference {

	/**
	 * Empty array
	 */
	protected static final Value[] NO_VALUES = new Value[0];

	/**
	 * Raw list of {@link Value}s
	 */
	private ArrayList values = null;
	private int size = 0;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param stackFrameImpl
	 * @param body
	 */
	public ArrayReferenceImpl(VirtualMachineImpl vm, Map body, StackFrameReferenceImpl stackFrameImpl) {
		super(vm, body, stackFrameImpl);
		this.size = properties().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ArrayReference#getValue(int)
	 */
	public Value getValue(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index > length()) {
			throw new IndexOutOfBoundsException();
		}
		return (Value) getValues().get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ArrayReference#getValues()
	 */
	public List getValues() {
		synchronized (this) {
			if (this.values == null) {
				this.values = new ArrayList(this.size);
				List props = properties();
				for (Iterator iter = props.iterator(); iter.hasNext();) {
					this.values.add(((PropertyReference) iter.next()).value());
				}
			}
		}
		return this.values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.ArrayReference#length()
	 */
	public int length() {
		return this.size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.rhino.ObjectReferenceImpl#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Array [").append(this.size).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.rhino.ObjectReferenceImpl#getValueTypeName()
	 */
	public String getValueTypeName() {
		return JSONConstants.ARRAY;
	}
}
