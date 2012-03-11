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
 * Default response implementation using JSON
 * 
 * @since 1.0
 */
public class Response extends Packet {

	private final String command;
	private final int requestSequence;
	private final Map body = Collections.synchronizedMap(new HashMap());
	private volatile boolean success = true;
	private volatile boolean running = true;
	private volatile String message;

	/**
	 * Constructor
	 * 
	 * @param requestSequence
	 * @param command
	 */
	public Response(int requestSequence, String command) {
		super(JSONConstants.RESPONSE);
		this.requestSequence = requestSequence;
		this.command = command.intern();
	}

	/**
	 * Constructor
	 * 
	 * @param json
	 */
	public Response(Map json) {
		super(json);

		Number packetRequestSeq = (Number) json.get(JSONConstants.REQUEST_SEQ);
		requestSequence = packetRequestSeq.intValue();

		String packetCommand = (String) json.get(JSONConstants.COMMAND);
		command = packetCommand.intern();

		Map packetBody = (Map) json.get(JSONConstants.BODY);
		body.putAll(packetBody);

		Boolean packetSuccess = (Boolean) json.get(JSONConstants.SUCCESS);
		success = packetSuccess.booleanValue();

		Boolean packetRunning = (Boolean) json.get(JSONConstants.RUNNING);
		running = packetRunning.booleanValue();

		message = (String) json.get(JSONConstants.MESSAGE);
	}

	/**
	 * @return the request sequence
	 */
	public int getRequestSequence() {
		return requestSequence;
	}

	/**
	 * @return the underlying command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return the body of the JSON response
	 */
	public Map getBody() {
		return body;
	}

	/**
	 * @return true if the JSON request was successful
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Set the success flag for the response
	 * 
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return true if the underlying command is running or not
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Sets the running state of the underlying command
	 * 
	 * @param running
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * @return the JSON status message for this response
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the JSON message for this response
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.connect.Packet#toJSON()
	 */
	public Map toJSON() {
		Map json = super.toJSON();
		json.put(JSONConstants.REQUEST_SEQ, new Integer(requestSequence));
		json.put(JSONConstants.COMMAND, command);
		json.put(JSONConstants.BODY, body);
		json.put(JSONConstants.SUCCESS, new Boolean(success));
		json.put(JSONConstants.RUNNING, new Boolean(running));
		if (message != null)
			json.put(JSONConstants.MESSAGE, message);
		return json;
	}
}
