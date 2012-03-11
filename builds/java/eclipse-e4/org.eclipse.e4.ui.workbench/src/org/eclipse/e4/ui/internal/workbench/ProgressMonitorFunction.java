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
package org.eclipse.e4.ui.internal.workbench;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;

/**
 * This class provides a context function that returns a default progress monitor. This is generally
 * used near the root of a context tree to provide a reasonable default monitor for cases where more
 * specific contexts have not provided one.
 */
public class ProgressMonitorFunction extends ContextFunction {

	public Object compute(IEclipseContext context) {
		return new NullProgressMonitor();
	}

}
