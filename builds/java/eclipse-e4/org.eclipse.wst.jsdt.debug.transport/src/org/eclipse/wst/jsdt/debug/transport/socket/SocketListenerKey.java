/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.transport.socket;

import org.eclipse.wst.jsdt.debug.transport.ListenerKey;

/**
 * Key implementation for a {@link SocketTransportService}
 * 
 * @since 1.0
 */
public final class SocketListenerKey implements ListenerKey {

	private String address;

	/**
	 * Constructor
	 * @param address
	 */
	public SocketListenerKey(String address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.transport.TransportService.ListenerKey#address()
	 */
	public String address() {
		return address;
	}
}