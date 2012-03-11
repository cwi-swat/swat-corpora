/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.workbench.addons.minmax;

import org.eclipse.osgi.util.NLS;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public class Messages extends NLS {

	public static String TrimStack_SharedAreaTooltip;
	public static String TrimStack_CloseText;
	public static String TrimStack_RestoreText;

	private static final String BUNDLE_NAME = "org.eclipse.e4.ui.workbench.addons.minmax.messages";//$NON-NLS-1$

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
