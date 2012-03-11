/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.graphics.util;

import org.eclipse.swt.SWT;

public class Events {

	public final static int MouseDrag = 44;

	private static int[] eventTypes = {
		SWT.KeyDown,
		SWT.KeyUp,
		SWT.MouseDown,
		SWT.MouseUp,
		SWT.MouseMove,
		MouseDrag,
	//	SWT.MouseHover,
	//	SWT.MouseDoubleClick,
		SWT.MouseEnter,
		SWT.MouseExit,
		SWT.MouseWheel,
		SWT.FocusIn,
		SWT.FocusOut,
	};

	private static String[] eventNames = {
		"SWT.KeyDown",
		"SWT.KeyUp",
		"SWT.MouseDown",
		"SWT.MouseUp",
		"SWT.MouseMove",
		"Events.MouseDrag",
		//	SWT.MouseHover,
		//	SWT.MouseDoubleClick,
		"SWT.MouseEnter",
		"SWT.MouseExit",
		"SWT.MouseWheel",
		"SWT.FocusIn",
		"SWT.FocusOut",
	};
	
	public final static String getEventTypeName(int type) {
		for (int i = 0; i < eventTypes.length; i++) {
			if (type == eventTypes[i]) {
				return eventNames[i];
			}
		}
		return String.valueOf(type);
	}
}
