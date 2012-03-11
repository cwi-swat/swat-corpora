/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class GridLayoutRequest {
	/*
	 * Class used to identify the type of request relative to GridLayout.
	 */

	public static final int NO_ADD = -1;

	public static final int INSERT_COLUMN = 0;

	public static final int INSERT_COLUMN_WITHIN_ROW = 1;

	public static final int INSERT_ROW = 2;

	public static final int ADD_COLUMN = 3;

	public static final int ADD_ROW = 4;

	public static final int ADD_ROW_COL = 5;

	public static final int REPLACE_FILLER = 6;

	public static final int ADD_TO_EMPTY_CELL = 7;

	public int type;

	public int column;

	public int row;
}
