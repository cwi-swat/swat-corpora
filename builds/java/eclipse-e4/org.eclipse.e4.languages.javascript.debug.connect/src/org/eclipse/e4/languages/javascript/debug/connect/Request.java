/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.connect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;

/**
 * Default request implementation using JSON
 * 
 * @since 1.0
 */
public class Request extends Packet {

	private final String command;
	private final Map arguments = Collections.synchronizedMap(new HashMap());

	/**
	 * Constructor
	 * 
	 * @param command
	 */
	public Request(String command) {
		super(JSONConstants.REQUEST);
		this.command = command.intern();
	}

	/**
	 * Constructor
	 * 
	 * @param json
	 *            map of JSON attributes
	 */
	public Request(Map json) {
		super(json);
		String packetCommand = (String) json.get(JSONConstants.COMMAND);
		this.command = packetCommand.intern();

		Map packetArguments = (Map) json.get(JSONConstants.ARGUMENTS);
		arguments.putAll(packetArguments);
	}

	/**
	 * Returns the command that this request will was created for
	 * 
	 * @return the underlying command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Returns the complete collection of JSON arguments in the request
	 * 
	 * @return the arguments
	 */
	public Map getArguments() {
		return arguments;
	}

	/**
	 * Sets the given argument in the JSON map
	 * 
	 * @param key
	 * @param argument
	 */
	public void setArgument(String key, Object argument) {
		arguments.put(key, argument);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.connect.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(JSONConstants.COMMAND, command);
		json.put(JSONConstants.ARGUMENTS, arguments);
		return json;
	}
}
