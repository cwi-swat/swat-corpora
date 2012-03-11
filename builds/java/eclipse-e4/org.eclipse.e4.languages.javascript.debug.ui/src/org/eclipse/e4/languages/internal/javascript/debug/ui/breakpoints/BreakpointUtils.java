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
package org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint;
import org.eclipse.wst.jsdt.core.IType;

/**
 * Common set of utility methods for handling breakpoint
 * 
 * @since 0.9
 */
public final class BreakpointUtils {

	/**
	 * Returns the {@link IType} that corresponds to the type name infos saved in the breakpoint or <code>null</code> if no such infos exist
	 * @param breakpoint
	 * @return the {@link IType} associated with the breakpoint
	 * @throws CoreException
	 */
	public static IType getType(JSDIBreakpoint breakpoint) throws CoreException {
		String typename = breakpoint.getTypeName();
		if(typename != null) {
			
		}
		return null;
	}
}
