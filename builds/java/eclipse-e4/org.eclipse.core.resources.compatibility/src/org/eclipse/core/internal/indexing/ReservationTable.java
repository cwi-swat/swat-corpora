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

public class ReservationTable {

	protected Map table = new HashMap();

	public ReservationTable() {
		super();
	}

	public Reservation get(int pageNumber) {
		return (Reservation) table.get(new Integer(pageNumber));
	}

	public void put(int pageNumber, Reservation r) {
		table.put(new Integer(pageNumber), r);
	}

	public boolean contains(int pageNumber) {
		return table.containsKey(new Integer(pageNumber));
	}

	public boolean contains(ObjectAddress address) {
		int pageNumber = address.getPageNumber();
		int objectNumber = address.getObjectNumber();
		if (contains(pageNumber)) {
			if (get(pageNumber).contains(objectNumber))
				return true;
		}
		return false;
	}

	public void remove(ObjectAddress address) {
		int pageNumber = address.getPageNumber();
		int objectNumber = address.getObjectNumber();
		Reservation r = (Reservation) table.get(new Integer(pageNumber));
		if (r == null)
			return;
		r.remove(objectNumber);
	}

	public void clear() {
		table.clear();
	}

}
