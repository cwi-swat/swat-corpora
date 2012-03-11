/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.indexing;

import java.util.HashMap;
import java.util.Map;

public class Reservation {

	protected int freeSlots = 0;
	protected int freeSpace = 0;
	protected int reservedSpace = 0;
	protected int initialEntry = 0;
	protected Map reservedItems = new HashMap();

	public Reservation(int freeSpace, int freeSlots, int initialEntry) {
		this.freeSlots = freeSlots;
		this.freeSpace = freeSpace;
		this.initialEntry = initialEntry;
	}

	public void add(int slot, int bytes) {
		reservedSpace += bytes;
		reservedItems.put(new Integer(slot), new Integer(bytes));
	}

	public void remove(int slot) {
		Integer bytes = (Integer) reservedItems.remove(new Integer(slot));
		if (bytes == null)
			return;
		reservedSpace -= bytes.intValue();
	}

	boolean contains(int slot) {
		return reservedItems.containsKey(new Integer(slot));
	}

	int getFreeSpace() {
		if (reservedItems.size() >= freeSlots)
			return 0;
		return Math.max(0, freeSpace - reservedSpace);
	}

	public int getInitialEntry() {
		return initialEntry;
	}

	public void setInitialEntry(int n) {
		initialEntry = n;
	}

}
