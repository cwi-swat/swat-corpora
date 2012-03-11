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
/*
 *  $RCSfile: EditPartRunnable.java,v $
 *  $Revision: 1.2 $  $Date: 2010/06/18 00:15:24 $ 
 */
package org.eclipse.e4.xwt.tools.ui.designer.core.parts;

import org.eclipse.gef.EditPart;

/**
 * This is a runnable that takes an editpart and when it is run it will validate that the editpart is still active before running it. If not active, it won't run it. This is useful for delayed executions where the runnable will run later and it is possible that the editpart has been deactivated since then.
 * 
 * @since 1.1.0
 */
public abstract class EditPartRunnable implements Runnable {

	private final EditPart host;

	public EditPartRunnable(EditPart host) {
		this.host = host;

	}

	protected final EditPart getHost() {
		return host;
	}

	public final void run() {
		if (getHost().isActive())
			doRun();
	}

	/**
	 * Subclasses should implement this method to do the actual run.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected abstract void doRun();
}
