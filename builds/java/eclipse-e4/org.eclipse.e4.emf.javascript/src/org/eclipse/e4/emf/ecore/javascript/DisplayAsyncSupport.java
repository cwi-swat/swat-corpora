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
package org.eclipse.e4.emf.ecore.javascript;

import org.eclipse.swt.widgets.Display;

public class DisplayAsyncSupport implements AsyncSupport {

	private Display display;
	
	public DisplayAsyncSupport(Display display) {
		super();
		this.display = display;
	}

	public void run(Runnable runnable) {
		display.asyncExec(runnable);
	}
}
