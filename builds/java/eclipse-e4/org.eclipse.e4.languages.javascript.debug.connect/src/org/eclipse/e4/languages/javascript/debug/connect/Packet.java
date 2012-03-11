/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.connect;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;

abstract public class Packet {

	private static int currentSequence = 0;
	private final int sequence;
	private final String type;

	protected Packet(String type) {
		this.sequence = nextSequence();
		this.type = type.intern();
	}

	protected Packet(Map json) {
		Number packetSeq = (Number) json.get(JSONConstants.SEQ);
		this.sequence = packetSeq.intValue();
		String packetType = (String) json.get(JSONConstants.TYPE);
		this.type = packetType.intern();
	}

	private static synchronized int nextSequence() {
		return ++currentSequence;
	}

	public int getSequence() {
		return sequence;
	}

	public String getType() {
		return type;
	}

	public Map toJSON() {
		Map json = new HashMap();
		json.put(JSONConstants.SEQ, new Integer(sequence));
		json.put(JSONConstants.TYPE, type);
		return json;
	}

	public static String getType(Map json) {
		return (String) json.get(JSONConstants.TYPE);
	}

}
