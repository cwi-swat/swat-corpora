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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.jsdi.ArrayReference;
import org.eclipse.e4.languages.javascript.jsdi.FunctionReference;
import org.eclipse.e4.languages.javascript.jsdi.NullValue;
import org.eclipse.e4.languages.javascript.jsdi.NumberValue;
import org.eclipse.e4.languages.javascript.jsdi.ObjectReference;
import org.eclipse.e4.languages.javascript.jsdi.PropertyReference;
import org.eclipse.e4.languages.javascript.jsdi.Value;

/**
 * Default implementation of {@link IValue}
 * 
 * @since 1.0
 */
public class JSDIValue extends JSDIDebugElement implements IValue {

	private Value value;
	private String name;
	private List properties;

	/**
	 * Constructor
	 * 
	 * @param variable
	 * @param value
	 */
	public JSDIValue(JSDIVariable variable, Value value) {
		super(variable.getJSDITarget());
		this.name = variable.getName();
		this.value = value;
	}

	/**
	 * Constructor
	 * 
	 * @param variable
	 * @param value
	 */
	public JSDIValue(JSDIProperty variable, Value value) {
		super(variable.getJSDITarget());
		this.name = variable.getName();
		this.value = value;
	}

	/**
	 * Hook to return the underlying value for this {@link JSDIValue}
	 * 
	 * @return the underlying {@link Value}
	 */
	protected Value getUnderlyingValue() {
		return this.value;
	}

	/**
	 * @return the detail string that is typically shown in the details pane
	 */
	public String getDetailString() {
		if (this.value instanceof ArrayReference) {
			ArrayReference array = (ArrayReference) this.value;
			return array.getValues().toString();
		}
		if (this.value instanceof FunctionReference) {
			return ((FunctionReference) this.value).valueString();
		}
		if (this.value instanceof NumberValue) {
			NumberValue nvalue = (NumberValue) this.value;
			if (!nvalue.isNaN()) {
				return JSDIDebugModel.numberToString(nvalue.value());
			}
		}
		return this.value.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		if (this.value != null) {
			return this.value.getValueTypeName();
		}
		return null;
	}

	/**
	 * Return the name to display for the variable
	 * 
	 * @return the variable name
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (this.value instanceof ObjectReference) {
			// TODO resolve id - needs work for lifecycle
			ObjectReference ref = (ObjectReference) this.value;
			StringBuffer buffer = new StringBuffer();
			buffer.append(this.value.valueString()).append(JSONConstants.SPACE).append("(id=").append(ref.id().toString()).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
			return buffer.toString();
		}
		if (this.value instanceof NumberValue) {
			NumberValue nvalue = (NumberValue) this.value;
			if (!nvalue.isNaN()) {
				return JSDIDebugModel.numberToString(nvalue.value());
			}
		}
		return this.value.valueString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	public synchronized IVariable[] getVariables() throws DebugException {
		if (!hasVariables()) {
			return null;
		}
		if (this.properties == null) {
			ObjectReference reference = (ObjectReference) this.value;
			List underlyingProperties = reference.properties();
			this.properties = new ArrayList(underlyingProperties.size());
			for (Iterator iterator = underlyingProperties.iterator(); iterator.hasNext();) {
				PropertyReference property = (PropertyReference) iterator.next();
				JSDIProperty jsdiProperty = new JSDIProperty(this, property);
				this.properties.add(jsdiProperty);
			}
		}
		return (IVariable[]) this.properties.toArray(new IVariable[this.properties.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		if (this.value instanceof ObjectReference) {
			return !(this.value instanceof NullValue);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	public boolean isAllocated() throws DebugException {
		return this.properties != null;
	}
}
