/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.vex.swt;

import org.eclipse.swt.graphics.GC;

public class DisposeUtil {
	
	/**
	 * in MAC Cocoa, we should not dispose the GC for an image. The image will dispose
	 * the gc automatically.
	 * 
	 * @param gc
	 */
	public static void dispose(GC gc) {
		gc.dispose();
	}
}
