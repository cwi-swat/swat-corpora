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

public class Field implements Insertable {
	protected Buffer buffer; // contents
	protected int offset; // offset of the field within the buffer
	protected int length; // length of the field

	/**
	 * Constructor for a new Field.
	 */
	public Field(byte[] bytes) {
		this.buffer = new Buffer(bytes);
		this.offset = 0;
		this.length = bytes.length;
	}

	/**
	 * Constructor for a new Field.
	 */
	public Field(int n) {
		this.buffer = new Buffer(n);
		this.offset = 0;
		this.length = n;
	}

	/**
	 * Constructor for a new Field.
	 */
	public Field(Buffer buffer, int offset, int length) {
		this.buffer = buffer;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Constructor for a new Field.
	 */
	public Field(Insertable anObject) {
		buffer = new Buffer(anObject);
		offset = 0;
		length = buffer.length();
	}

	public Field clear() {
		buffer.clear(offset, length);
		return this;
	}

	public int compareTo(Field that) {
		return Buffer.compare(this.buffer, this.offset, this.length, that.buffer, that.offset, that.length);
	}

	public Field subfield(int offset, int length) {
		if (offset + length > this.length)
			throw new IllegalArgumentException();
		return buffer.getField(this.offset + offset, length);
	}

	public Field subfield(FieldDef d) {
		if (d.offset + d.length > this.length)
			throw new IllegalArgumentException();
		return buffer.getField(this.offset + d.offset, d.length);
	}

	public Field subfield(int offset) {
		return subfield(offset, this.length - offset);
	}

	public byte[] get() {
		return buffer.get(offset, length);
	}

	public int getInt() {
		return buffer.getInt(offset, length);
	}

	public long getLong() {
		return buffer.getLong(offset, length);
	}

	public int getUInt() {
		return buffer.getUInt(offset, length);
	}

	public byte[] get(FieldDef d) {
		return subfield(d).get();
	}

	public int getInt(FieldDef d) {
		return subfield(d).getInt();
	}

	public int length() {
		return length;
	}

	public Pointer pointTo(int offset) {
		return new Pointer(buffer, this.offset + offset);
	}

	public Field put(byte[] b) {
		buffer.put(offset, length, b);
		return this;
	}

	public Field put(int n) {
		buffer.put(offset, length, n);
		return this;
	}

	public Field put(long n) {
		buffer.put(offset, length, n);
		return this;
	}

	public Field put(Insertable anObject) {
		put(anObject.toByteArray());
		return this;
	}

	public Field put(FieldDef d, int n) {
		subfield(d).put(n);
		return this;
	}

	public Field put(FieldDef d, Insertable anObject) {
		subfield(d).put(anObject.toByteArray());
		return this;
	}

	/**
	 * Implementation of the Insertable interface.
	 */
	public byte[] toByteArray() {
		return get();
	}

}
