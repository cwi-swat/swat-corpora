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

class ObjectHeader implements Insertable {

	public static final int SIZE = 4;
	private static final int HeaderTagValue = 0xFFFF;
	private static final int HeaderTagOffset = 0;
	private static final int ObjectLengthOffset = 2;
	private int objectLength;

	/**
	 * ObjectHeader constructor comment.
	 */
	public ObjectHeader(byte[] buffer) throws ObjectStoreException {
		if (buffer.length != SIZE)
			throw new IllegalArgumentException();
		Buffer buf = new Buffer(buffer);
		if (buf.getUInt(HeaderTagOffset, 2) != HeaderTagValue) {
			throw new ObjectStoreException(ObjectStoreException.ObjectHeaderFailure);
		}
		this.objectLength = buf.getUInt(ObjectLengthOffset, 2);
	}

	/**
	 * ObjectHeader constructor comment.
	 */
	public ObjectHeader(int objectLength) {
		this.objectLength = objectLength;
	}

	public int getObjectLength() {
		return objectLength;
	}

	public byte[] toByteArray() {
		Buffer buf = new Buffer(SIZE);
		buf.put(HeaderTagOffset, 2, HeaderTagValue);
		buf.put(ObjectLengthOffset, 2, objectLength);
		return buf.get();
	}
}
