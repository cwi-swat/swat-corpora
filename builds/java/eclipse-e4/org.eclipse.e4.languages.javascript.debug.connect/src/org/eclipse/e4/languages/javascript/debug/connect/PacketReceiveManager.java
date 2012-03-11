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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Default implementation of {@link PacketManager} for receiving packets from the VM
 * 
 * @since 1.0
 */
public class PacketReceiveManager extends PacketManager {

	/**
	 * Generic timeout value for not blocking. <br>
	 * Value is: <code>0</code>
	 */
	public static final int TIMEOUT_NOT_BLOCKING = 0;
	/**
	 * Generic timeout value for infinite timeout. <br>
	 * Value is: <code>-1</code>
	 */
	public static final int TIMEOUT_INFINITE = -1;
	/**
	 * List of Command packets received from Virtual Machine.
	 */
	private List commandPackets = new LinkedList();
	/**
	 * List of Reply packets received from Virtual Machine.
	 */
	private List responsePackets = new LinkedList();
	/**
	 * List of Packets that have timed out already. Maintained so that responses can be discarded if/when they are received.
	 */
	private List timedOutPackets = new ArrayList();

	/**
	 * Constructor
	 * 
	 * @param connection
	 */
	public PacketReceiveManager(Connection connection) {
		super(connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.connect.PacketManager#disconnect()
	 */
	public void disconnect() {
		super.disconnect();
		synchronized (this.commandPackets) {
			this.commandPackets.notifyAll();
		}
		synchronized (this.responsePackets) {
			this.responsePackets.notifyAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while (!isDisconnected()) {
				// Read a packet from the input stream.
				readAvailablePacket();
			}
		}
		// if the remote runtime is interrupted, drop the connection and clean up, don't wait for it to happen on its own
		catch (InterruptedIOException e) {
			disconnect(e);
		} catch (IOException e) {
			disconnect(e);
		}
	}

	/**
	 * @return Returns a specified Command Packet from the Virtual Machine.
	 * @throws TimeoutException
	 * @throws DisconnectException
	 */
	public Packet getCommand(String type, long timeToWait) throws TimeoutException, DisconnectException {
		synchronized (this.commandPackets) {
			long remainingTime = timeToWait;
			// Wait until command is available.
			while (!isDisconnected()) {
				Packet packet = removeCommandPacket(type);
				if (packet != null)
					return packet;

				if (remainingTime < 0 && timeToWait != TIMEOUT_INFINITE)
					break;

				long timeBeforeWait = System.currentTimeMillis();
				try {
					waitForPacketAvailable(remainingTime, this.commandPackets);
				} catch (InterruptedException e) {
					break;
				}
				long waitedTime = System.currentTimeMillis() - timeBeforeWait;
				remainingTime -= waitedTime;
			}
		}
		// Check for an IO Exception.
		if (isDisconnected()) {
			throw new DisconnectException("runtime disconnected", getDisconnectException()); //$NON-NLS-1$
		}
		// Check for a timeout.
		throw new TimeoutException();
	}

	/**
	 * Returns a specified response packet from the VM
	 * 
	 * @param requestSequence
	 * @param timeToWait
	 * @return a specified response packet from the VM
	 * @throws TimeoutException
	 * @throws DisconnectException
	 */
	public Response getResponse(int requestSequence, long timeToWait) throws TimeoutException, DisconnectException {
		synchronized (this.responsePackets) {
			long remainingTime = timeToWait;
			// Wait until command is available.
			while (!isDisconnected()) {
				Response response = removeResponsePacket(requestSequence);
				if (response != null)
					return response;

				if (remainingTime < 0 && timeToWait != TIMEOUT_INFINITE)
					break;

				long timeBeforeWait = System.currentTimeMillis();
				try {
					waitForPacketAvailable(remainingTime, this.responsePackets);
				} catch (InterruptedException e) {
					break;
				}
				long waitedTime = System.currentTimeMillis() - timeBeforeWait;
				remainingTime -= waitedTime;
			}
		}
		// Check for an IO Exception.
		if (isDisconnected()) {
			throw new DisconnectException("runtime disconnected", getDisconnectException()); //$NON-NLS-1$
		}

		synchronized (this.responsePackets) {
			this.timedOutPackets.add(new Integer(requestSequence));
		}
		throw new TimeoutException("Waiting for: " + requestSequence); //$NON-NLS-1$
	}

	/**
	 * Wait for an available packet from the Virtual Machine.
	 */
	private void waitForPacketAvailable(long timeToWait, Object lock) throws InterruptedException {
		if (timeToWait == 0)
			return;
		else if (timeToWait < 0)
			lock.wait();
		else
			lock.wait(timeToWait);
	}

	/**
	 * Removes and returns the command packet for the given type or <code>null</code> if there is no command packet for the given type.
	 * 
	 * @param type
	 * @return The command packet for the given type that has been removed
	 */
	private Packet removeCommandPacket(String type) {
		ListIterator iter = this.commandPackets.listIterator();
		while (iter.hasNext()) {
			Packet packet = (Packet) iter.next();
			if (type == null || packet.getType().equals(type)) {
				iter.remove();
				return packet;
			}
		}
		return null;
	}

	/**
	 * Removes and returns the response packet with the given request sequence or <code>null</code> if there is no response packet with the given request sequence
	 * 
	 * @param requestSequence
	 * @return the response packet with the given request sequence that has been removed
	 */
	private Response removeResponsePacket(int requestSequence) {
		ListIterator iter = this.responsePackets.listIterator();
		while (iter.hasNext()) {
			Response response = (Response) iter.next();
			if (requestSequence == response.getRequestSequence()) {
				iter.remove();
				return response;
			}
		}
		return null;
	}

	/**
	 * Adds the given command packet to the listing of command packets. This method is a no-op if the given packet is <code>null</code>
	 * 
	 * @param packet
	 */
	private void addCommandPacket(Packet packet) {
		if (packet == null) {
			return;
		}
		synchronized (this.commandPackets) {
			this.commandPackets.add(packet);
			this.commandPackets.notifyAll();
		}
	}

	/**
	 * Adds the given response packet to the listing of response packets. This method is a no-op if the given packet is <code>null</code>
	 * 
	 * @param response
	 */
	private void addResponsePacket(Response response) {
		if (response == null) {
			return;
		}
		synchronized (this.responsePackets) {
			if (!this.timedOutPackets.isEmpty()) {
				Integer requestSeq = new Integer(response.getRequestSequence());
				if (this.timedOutPackets.remove(requestSeq))
					return; // already timed out. No need to keep this one
			}
			this.responsePackets.add(response);
			this.responsePackets.notifyAll();
		}
	}

	/**
	 * Reads the next packet from the underlying connection and adds it to the correct packet listing.
	 * 
	 * @throws IOException
	 */
	private void readAvailablePacket() throws IOException {
		// Read a packet from the Input Stream.
		Packet packet = getConnection().readPacket();
		if (packet instanceof Response)
			addResponsePacket((Response) packet);
		else
			addCommandPacket(packet);
	}
}
