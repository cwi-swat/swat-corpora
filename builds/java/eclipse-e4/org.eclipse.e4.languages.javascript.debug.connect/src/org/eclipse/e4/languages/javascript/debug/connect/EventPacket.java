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

public class EventPacket extends Packet {

	public static final String TYPE = JSONConstants.EVENT;
	private final String event;
	private final Map body = Collections.synchronizedMap(new HashMap());

	public EventPacket(String event) {
		super(TYPE);
		this.event = event.intern();
	}

	public EventPacket(Map json) {
		super(json);

		String packetEvent = (String) json.get(JSONConstants.EVENT);
		event = packetEvent.intern();

		Map packetBody = (Map) json.get(JSONConstants.BODY);
		body.putAll(packetBody);
	}

	public String getEvent() {
		return event;
	}

	public Map getBody() {
		return body;
	}

	public Map toJSON() {
		Map json = super.toJSON();
		json.put(JSONConstants.EVENT, event);
		json.put(JSONConstants.BODY, body);
		return json;
	}
}
