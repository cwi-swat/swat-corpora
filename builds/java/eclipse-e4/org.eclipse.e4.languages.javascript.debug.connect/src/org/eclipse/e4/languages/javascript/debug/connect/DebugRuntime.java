/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.connect;

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;

public class DebugRuntime {
	private final PacketReceiveManager packetReceiveManager;
	private final PacketSendManager packetSendManager;

	public DebugRuntime(Connection connection) {
		packetReceiveManager = new PacketReceiveManager(connection);
		Thread receiveThread = new Thread(packetReceiveManager, "Debug Runtime - Receive Manager"); //$NON-NLS-1$
		receiveThread.setDaemon(true);

		packetSendManager = new PacketSendManager(connection);
		Thread sendThread = new Thread(packetSendManager, "Debug Runtime - Send Manager"); //$NON-NLS-1$
		sendThread.setDaemon(true);

		packetReceiveManager.setPartnerThread(sendThread);
		packetSendManager.setPartnerThread(receiveThread);

		receiveThread.start();
		sendThread.start();
	}

	public void dispose() {
		packetReceiveManager.disconnect();
		packetSendManager.disconnect();
	}

	public void sendEvent(EventPacket event) throws DisconnectException {
		packetSendManager.sendPacket(event);
	}

	public void sendResponse(Response response) throws DisconnectException {
		packetSendManager.sendPacket(response);
	}

	public Request receiveRequest(int timeout) throws TimeoutException, DisconnectException {
		return (Request) packetReceiveManager.getCommand(JSONConstants.REQUEST, timeout);
	}
}
