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
package org.eclipse.core.internal.localstore;

import org.eclipse.core.internal.indexing.IndexedStoreException;

public interface IHistoryStoreVisitor {
	/**
	 * Performs required behaviour whenever a match is found in the history store query.
	 * 
	 * @param state State to be visited in IndexedStore.
	 */
	public boolean visit(HistoryStoreEntry state) throws IndexedStoreException;
}
