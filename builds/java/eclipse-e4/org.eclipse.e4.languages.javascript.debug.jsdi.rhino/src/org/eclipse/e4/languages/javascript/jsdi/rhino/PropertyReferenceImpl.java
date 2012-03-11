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

import org.eclipse.e4.languages.javascript.jsdi.PropertyReference;
import org.eclipse.e4.languages.javascript.jsdi.Value;

/**
 * Rhino implementation of {@link PropertyReference}
 * 
 * @see MirrorImpl
 * @see PropertyReference
 * @see StackFrameReferenceImpl
 * @see Value
 * @since 1.0
 */
public class PropertyReferenceImpl extends MirrorImpl implements PropertyReference {

	/**
	 * The name of the property
	 */
	private String name;
	/**
	 * The reference used to look up the property value in the stackframe context
	 */
	private Number ref;
	/**
	 * The stackframe context
	 */
	private StackFrameReferenceImpl frame = null;
	/**
	 * The underlying value - lazily computed in {@link #value()}
	 */
	private Value value = null;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param name
	 * @param ref
	 */
	public PropertyReferenceImpl(VirtualMachineImpl vm, StackFrameReferenceImpl frame, String name, Number ref) {
		super(vm);
		this.frame = frame;
		this.name = name;
		this.ref = ref;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Property#name()
	 */
	public String name() {
		return name;
	}

	/**
	 * The reference id of this property
	 * 
	 * @return the reference id of this property
	 */
	public Number getRef() {
		return ref;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.Property#value()
	 */
	public Value value() {
		synchronized (this.frame) {
			if (this.value == null) {
				this.value = frame.lookupValue(ref);
			}
		}
		return this.value;
	}
}
