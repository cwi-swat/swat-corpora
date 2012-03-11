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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.DisconnectException;
import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.debug.connect.Request;
import org.eclipse.e4.languages.javascript.debug.connect.Response;
import org.eclipse.e4.languages.javascript.debug.connect.TimeoutException;
import org.eclipse.e4.languages.javascript.jsdi.Location;
import org.eclipse.e4.languages.javascript.jsdi.StackFrameReference;
import org.eclipse.e4.languages.javascript.jsdi.Value;
import org.eclipse.e4.languages.javascript.jsdi.Variable;

/**
 * Rhino implementation of {@link StackFrameReference}
 * 
 * @see Location
 * @see MirrorImpl
 * @see StackFrameReference
 * @since 1.0
 */
public class StackFrameReferenceImpl extends MirrorImpl implements StackFrameReference {

	private final Number frameId;
	private final Number threadId;
	private final Number contextId;
	private final Number ref;
	private final Location location;
	private List variables;
	private VariableImpl thisVariable;
	private HashMap cache = new HashMap();

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param jsonFrame
	 */
	public StackFrameReferenceImpl(VirtualMachineImpl vm, Map jsonFrame) {
		super(vm);
		this.threadId = (Number) jsonFrame.get(JSONConstants.THREAD_ID);
		this.frameId = (Number) jsonFrame.get(JSONConstants.FRAME_ID);
		this.contextId = (Number) jsonFrame.get(JSONConstants.CONTEXT_ID);
		this.ref = (Number) jsonFrame.get(JSONConstants.REF);

		Long scriptId = new Long(((Number) jsonFrame.get(JSONConstants.SCRIPT_ID)).longValue());
		ScriptReferenceImpl script = vm.script(scriptId);

		String function = (String) jsonFrame.get(JSONConstants.FUNCTION);
		if (function != null) {
			this.location = script.functionLocation(function);
		} else {
			Number line = (Number) jsonFrame.get(JSONConstants.LINE);
			Location lineLocation = script.lineLocation(line.intValue());
			this.location = lineLocation != null ? lineLocation : new LocationImpl(vm, function, line.intValue(), script);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.StackFrame#evaluate(java.lang .String)
	 */
	public Value evaluate(String expression) {
		Request request = new Request(JSONConstants.EVALUATE);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		request.getArguments().put(JSONConstants.FRAME_ID, frameId);
		request.getArguments().put(JSONConstants.EXPRESSION, expression);
		try {
			Response response = vm.sendRequest(request, 30000);
			return createValue(response.getBody(), true);
		} catch (DisconnectException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Utility method to look up the {@link Value} with the given reference id <br>
	 * or <code>null</code> if there is no {@link Value} with the given id
	 * 
	 * @param ref
	 * @return the {@link Value} or <code>null</code>
	 */
	public Value lookupValue(Number ref) {
		Value value = (Value) this.cache.get(ref);
		if (value == null) {
			Request request = new Request(JSONConstants.LOOKUP);
			request.getArguments().put(JSONConstants.THREAD_ID, threadId);
			request.getArguments().put(JSONConstants.FRAME_ID, frameId);
			request.getArguments().put(JSONConstants.HANDLE, ref);
			try {
				Response response = vm.sendRequest(request, 30000);
				value = createValue(response.getBody(), false);
				this.cache.put(ref, value);
				return value;
			} catch (DisconnectException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * Creates a new type value for the given type description in the map
	 * 
	 * @param body
	 * @param iseval
	 * @return the new type
	 * @throws IllegalStateException
	 *             if the type description in the map is unknown
	 */
	private Value createValue(Map body, boolean iseval) throws IllegalStateException {
		Map value = (Map) body.get((iseval ? JSONConstants.EVALUATE : JSONConstants.LOOKUP));
		String type = (String) value.get(JSONConstants.TYPE);
		// "undefined", "null", "boolean", "number", "string", "object",
		// "function"

		if (type.equals(JSONConstants.UNDEFINED)) {
			return vm.undefinedValue;
		} else if (type.equals(JSONConstants.NULL)) {
			return vm.nullValue;
		} else if (type.equals(JSONConstants.BOOLEAN)) {
			return new BooleanValueImpl(vm, value);
		} else if (type.equals(JSONConstants.NUMBER)) {
			return new NumberValueImpl(vm, value);
		} else if (type.equals(JSONConstants.STRING)) {
			return new StringValueImpl(vm, value);
		} else if (type.equals(JSONConstants.OBJECT)) {
			return new ObjectReferenceImpl(vm, value, this);
		} else if (type.equals(JSONConstants.FUNCTION)) {
			return new FunctionReferenceImpl(vm, value, this);
		} else if (type.equals(JSONConstants.ARRAY)) {
			return new ArrayReferenceImpl(vm, value, this);
		}
		throw new IllegalStateException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.StackFrame#location()
	 */
	public Location location() {
		return location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.StackFrame#thisObject()
	 */
	public synchronized Variable thisObject() {
		initializeVariables();
		return thisVariable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.jsdi.StackFrame#variables()
	 */
	public synchronized List variables() {
		initializeVariables();
		return variables;
	}

	/**
	 * Initializes and caches the live set of variables
	 */
	private void initializeVariables() {
		if (variables != null) {
			return;
		}
		Request request = new Request(JSONConstants.LOOKUP);
		request.getArguments().put(JSONConstants.THREAD_ID, threadId);
		request.getArguments().put(JSONConstants.FRAME_ID, frameId);
		request.getArguments().put(JSONConstants.HANDLE, ref);
		try {
			Response response = vm.sendRequest(request, 30000);
			Map lookup = (Map) response.getBody().get(JSONConstants.LOOKUP);
			List properties = (List) lookup.get(JSONConstants.PROPERTIES);
			variables = new ArrayList();
			for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
				Map property = (Map) iterator.next();
				String name = (String) property.get(JSONConstants.NAME);
				Number ref = (Number) property.get(JSONConstants.REF);
				VariableImpl variable = new VariableImpl(vm, this, name, ref);
				if (name.equals(JSONConstants.THIS))
					thisVariable = variable;
				else
					variables.add(variable);
			}
		} catch (DisconnectException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns if this stack frame is visible
	 * 
	 * @param variable
	 * @return true if this frame is visible, false otherwise
	 */
	public synchronized boolean isVisible(VariableImpl variable) {
		return variables != null && (thisVariable == variable || variables.contains(variable));
	}

	/**
	 * @return the context id for this frame
	 */
	public Number getContextId() {
		return contextId;
	}
}
