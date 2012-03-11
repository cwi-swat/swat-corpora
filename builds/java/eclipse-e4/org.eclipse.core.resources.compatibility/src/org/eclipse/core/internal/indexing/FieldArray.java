/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.indexing;

class FieldArray {

	protected Buffer buffer; // buffer over which this array is defined
	protected int offset; // offset within the buffer of the first field in the array
	protected int length; // length of a field
	protected int stride; // the amount of space from one field to the next
	protected int count; // the number of fields in the array

	/**
	 * Constructor.
	 * buffer is the underlying Buffer object.
	 * offset is the offset within that buffer.
	 * length is the length of each field in the array
	 * stride the number of bytes from the beginning of one element to the beginning of the next
	 * count is the number of elements in the array
	 */
	public FieldArray(Buffer buffer, int offset, int length, int stride, int count) {
		this.buffer = buffer;
		this.offset = offset;
		this.length = length;
		this.stride = stride;
		this.count = count;
	}

	/**
	 * Returns the ith field of the array.
	 */
	public Field fieldAt(int i) {
		if (i >= count)
			throw new ArrayIndexOutOfBoundsException();
		return new Field(buffer, offset + (i * stride), length);
	}

	/**
	 * Inserts a new "empty" field before index i.
	 */
	public Field insert(int i) {
		count++;
		if (i >= count)
			throw new ArrayIndexOutOfBoundsException();
		int s = offset + (i * stride); // source offset
		int t = s + stride; // target offset
		int n = (count - (i + 1)) * stride; // number of bytes to move
		buffer.copyInternal(s, t, n);
		return fieldAt(i).clear();
	}

	/**
	 * Removes the entry at index i and "squeezes the space up".  Clears the last entry.
	 */
	public void remove(int i) {
		if (i >= count)
			throw new ArrayIndexOutOfBoundsException();
		int s = offset + ((i + 1) * stride); // source offset
		int t = s - stride; // target offset
		int n = (count - (i + 1)) * stride; // number of bytes to move
		buffer.copyInternal(s, t, n);
		fieldAt(count - 1).clear();
		count--;
	}
}
