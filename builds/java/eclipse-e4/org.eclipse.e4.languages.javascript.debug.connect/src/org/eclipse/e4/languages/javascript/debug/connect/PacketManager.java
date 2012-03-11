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

/**
 * Default manager for sending / receiving packets to / from the connected VM
 * 
 * @since 1.0
 */
public abstract class PacketManager implements Runnable {

	/**
	 * Connector that performs IO to Virtual Machine.
	 */
	private final Connection connection;
	/**
	 * Thread that handles the communication the other way (e.g. if we are sending, the receiving thread).
	 */
	private volatile Thread partnerThread;
	/**
	 * The disconnected exception, if there is one
	 */
	private volatile IOException disconnectException;

	/**
	 * Constructor
	 * 
	 * @param connection
	 */
	protected PacketManager(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the backing connection to the VM
	 */
	public Connection getConnection() {
		return this.connection;
	}

	/**
	 * Used to indicate that an IO exception occurred, closes connection to Virtual Machine.
	 * 
	 * @param disconnectException
	 *            the IOException that occurred
	 */
	public void disconnect(IOException disconnectException) {
		this.disconnectException = disconnectException;
		disconnect();
	}

	/**
	 * Closes the connection to the Virtual Machine.
	 */
	public void disconnect() {
		try {
			this.connection.close();
		} catch (IOException e) {
			this.disconnectException = e;
		}
		if (this.partnerThread != null) {
			this.partnerThread.interrupt();
		}
	}

	/**
	 * @return Returns whether an IO exception has occurred.
	 */
	public boolean isDisconnected() {
		return this.connection == null || !this.connection.isOpen();
	}

	/**
	 * @return the IOException that caused this packet manager to disconnect or <code>null</code> if none.
	 */
	public IOException getDisconnectException() {
		return this.disconnectException;
	}

	/**
	 * Assigns thread of partner, to be notified if we have an IO exception.
	 * 
	 * @param thread
	 */
	public void setPartnerThread(Thread thread) {
		this.partnerThread = thread;
	}
}
