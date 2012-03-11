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

public class FieldDef {

	public static final int F_BYTE = 1;
	public static final int F_INT = 2;
	public static final int F_UINT = 3;
	public static final int F_LONG = 4;
	public static final int F_BYTES = 5;

	public int offset;
	public int length;
	public int type;

	public FieldDef(int type, int offset, int length) {
		this.type = type;
		this.offset = offset;
		this.length = length;
	}

}
