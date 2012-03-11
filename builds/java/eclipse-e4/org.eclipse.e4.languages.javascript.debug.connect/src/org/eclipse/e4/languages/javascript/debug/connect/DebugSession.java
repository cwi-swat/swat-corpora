/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.connect;


/**
 * Represents a debug session which starts the packet send / receive managers for communication.
 * 
 * @since 1.0
 */
public class DebugSession {
	/**
	 * The default receive manager
	 */
	private final PacketReceiveManager packetReceiveManager;
	/**
	 * The default send manager
	 */
	private final PacketSendManager packetSendManager;

	/**
	 * Constructor
	 * 
	 * Starts the send / receive managers on the given connection.
	 * 
	 * @param connection
	 */
	public DebugSession(Connection connection) {
		packetReceiveManager = new PacketReceiveManager(connection);
		Thread receiveThread = new Thread(packetReceiveManager, "Debug Session - Receive Manager"); //$NON-NLS-1$
		receiveThread.setDaemon(true);

		packetSendManager = new PacketSendManager(connection);
		Thread sendThread = new Thread(packetSendManager, "Debug Session - Send Manager"); //$NON-NLS-1$
		sendThread.setDaemon(true);

		packetReceiveManager.setPartnerThread(sendThread);
		packetSendManager.setPartnerThread(receiveThread);

		receiveThread.start();
		sendThread.start();
	}

	/**
	 * Stops the debug sessions and disconnects the send / receive managers
	 */
	public void dispose() {
		packetReceiveManager.disconnect();
		packetSendManager.disconnect();
	}

	/**
	 * Sends a {@link Request}
	 * 
	 * @param request
	 * @throws DisconnectException
	 */
	public void sendRequest(Request request) throws DisconnectException {
		packetSendManager.sendPacket(request);
	}

	/**
	 * Receives a {@link Response} from the receive manager with the given sequence. The manager will try to receive the packet with the specified sequence id for the given time-out time.
	 * 
	 * @param requestSequence
	 * @param timeout
	 * @return the desired {@link Response}, never <code>null</code>. If the manager cannot get the packet either a {@link TimeoutException} or a {@link DisconnectException} will be thrown
	 * @throws TimeoutException
	 * @throws DisconnectException
	 */
	public Response receiveResponse(int requestSequence, int timeout) throws TimeoutException, DisconnectException {
		return packetReceiveManager.getResponse(requestSequence, timeout);
	}

	/**
	 * Receives the given {@link EventPacket} from the receive manager. The manager will try to receive the packet for the given time-out time.
	 * 
	 * @param timeout
	 * @return the desired {@link EventPacket}, never <code>null</code>. If the manager cannot get the packet either a {@link TimeoutException} or a {@link DisconnectException} will be thrown
	 * @throws TimeoutException
	 * @throws DisconnectException
	 */
	public EventPacket receiveEvent(int timeout) throws TimeoutException, DisconnectException {
		return (EventPacket) packetReceiveManager.getCommand(EventPacket.TYPE, timeout);
	}

}
