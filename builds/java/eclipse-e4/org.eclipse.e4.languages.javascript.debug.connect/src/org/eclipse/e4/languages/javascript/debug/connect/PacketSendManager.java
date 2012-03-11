/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.connect;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;

public class PacketSendManager extends PacketManager {
	/** List of packets to be sent to Virtual Machine */
	private final List outgoingPackets = new ArrayList();

	/**
	 * Create a new thread that send packets to the Virtual Machine.
	 */
	public PacketSendManager(Connection connection) {
		super(connection);
	}

	public void disconnect() {
		super.disconnect();
		synchronized (outgoingPackets) {
			outgoingPackets.notifyAll();
		}
	}

	/**
	 * Thread's run method.
	 */
	public void run() {
		while (!isDisconnected()) {
			try {
				sendAvailablePackets();
			}
			// in each case if the remote runtime fails, or has been interrupted, disconnect and force a clean up, don't wait for it to happen
			catch (InterruptedException e) {
				disconnect();
			} catch (InterruptedIOException e) {
				disconnect(e);
			} catch (IOException e) {
				disconnect(e);
			}
		}
	}

	/**
	 * Add a packet to be sent.
	 * 
	 * @throws DisconnectException
	 */
	public void sendPacket(Packet packet) throws DisconnectException {
		if (isDisconnected()) {
			throw new DisconnectException("runtime disconnected", getDisconnectException()); //$NON-NLS-1$
		}

		synchronized (outgoingPackets) {
			// Add packet to list of packets to send.
			outgoingPackets.add(packet);
			// Notify PacketSendThread that data is available.
			outgoingPackets.notifyAll();
		}
	}

	/**
	 * Send available packets to the Virtual Machine.
	 */
	private void sendAvailablePackets() throws InterruptedException, IOException {
		Object[] packetsToSend;
		synchronized (outgoingPackets) {
			while (outgoingPackets.size() == 0) {
				outgoingPackets.wait();
			}
			packetsToSend = outgoingPackets.toArray();
			outgoingPackets.clear();
		}

		// Put available packets on Output Stream.
		for (int i = 0; i < packetsToSend.length; i++) {
			getConnection().writePacket((Packet) packetsToSend[i]);
		}
	}
}
