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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

class LogWriter {

	protected FileOutputStream out;
	protected PageStore pageStore;

	/**
	 * Puts the modified pages to the log file.
	 */
	public static void putModifiedPages(PageStore pageStore, Map modifiedPages) throws PageStoreException {
		LogWriter writer = new LogWriter();
		try {
			writer.open(pageStore);
			writer.putModifiedPages(modifiedPages);
		} finally {
			writer.close();
		}
	}

	/**
	 * Opens the log.
	 */
	protected void open(PageStore store) throws PageStoreException {
		this.pageStore = store;
		try {
			out = new FileOutputStream(Log.name(store.getName()));
		} catch (IOException e) {
			throw new PageStoreException(PageStoreException.LogOpenFailure, e);
		}
	}

	/**
	 * Closes the log.
	 */
	protected void close() {
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			// ignore
		}
		out = null;
	}

	/**
	 * Puts the modified pages into the log.
	 */
	protected void putModifiedPages(Map modifiedPages) throws PageStoreException {
		Buffer b4 = new Buffer(4);
		byte[] pageBuffer = new byte[Page.SIZE];
		int numberOfPages = modifiedPages.size();
		b4.put(0, 4, numberOfPages);
		try {
			write(b4.getByteArray());
			Iterator pageStream = modifiedPages.values().iterator();
			while (pageStream.hasNext()) {
				Page page = (Page) pageStream.next();
				int pageNumber = page.getPageNumber();
				b4.put(0, 4, pageNumber);
				write(b4.getByteArray());
				page.toBuffer(pageBuffer);
				write(pageBuffer);
			}
		} catch (IOException e) {
			throw new PageStoreException(PageStoreException.LogWriteFailure, e);
		}
	}

	public void write(byte[] buffer) throws IOException {
		out.write(buffer);
	}

}
