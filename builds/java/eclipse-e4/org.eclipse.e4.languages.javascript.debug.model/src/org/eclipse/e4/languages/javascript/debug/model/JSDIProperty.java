/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.e4.languages.javascript.jsdi.PropertyReference;

/**
 * Default implementation of an {@link IVariable} with-respect-to Javascript debugging
 * 
 * @see JSDIValue
 * @see JSDIStackFrame
 * @since 1.0
 */
public class JSDIProperty extends JSDIDebugElement implements IVariable {

	private PropertyReference property;
	private JSDIValue value = null;

	/**
	 * Constructor
	 * 
	 * @param target
	 */
	public JSDIProperty(JSDIValue jsdiValue, PropertyReference property) {
		super(jsdiValue.getJSDITarget());
		this.property = property;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	public String getName() {
		return property.name();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return getValue().getReferenceTypeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		if (value == null) {
			value = new JSDIValue(this, property.value());
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */
	public boolean hasValueChanged() throws DebugException {
		// TODO need to consider this if we want to be able to edit variables
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == JSDIProperty.class) {
			return this;
		}
		return super.getAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(java.lang.String )
	 */
	public void setValue(String expression) throws DebugException {
		notSupported("Javascript variables do not support being edited", null); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(org.eclipse. debug.core.model.IValue)
	 */
	public void setValue(IValue value) throws DebugException {
		notSupported("Javascript variables do not support being edited", null); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#supportsValueModification ()
	 */
	public boolean supportsValueModification() {
		// TODO Do we want to support this?
		// if so we need to impl verifyValue()*
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(java.lang .String)
	 */
	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(org.eclipse .debug.core.model.IValue)
	 */
	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}
}
