/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.model;

import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public class ModelMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.e4.languages.javascript.debug.model.modelmessages"; //$NON-NLS-1$
	public static String JSDIDebugTarget_jsdi_debug_target;
	public static String JSDIDebugTarget_not_support_disconnect;
	public static String JSDIDebugTarget_not_support_terminate;
	public static String JSDIDebugTarget_recieved_unknown_event;
	public static String JSDIDebugTarget_unsupported_operation;
	public static String JSDIStackFrame_stackframe_name;
	public static String JSDIThread_suspended_loading_script;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ModelMessages.class);
	}

	private ModelMessages() {
	}
}
