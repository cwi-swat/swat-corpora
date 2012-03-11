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

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.e4.languages.javascript.jsdi.NumberValue;
import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;

/**
 * The Javascript debug model
 * 
 * @since 0.9
 */
public class JSDIDebugModel {

	/**
	 * Debug model identifier
	 */
	public static final String MODEL_ID = "org.eclipse.e4.languages.javascript.debug.model"; //$NON-NLS-1$

	/**
	 * Returns a new {@link JSDIDebugTarget} that can disconnect and terminate by default
	 * 
	 * @param vm
	 *            the underlying {@link VirtualMachine}
	 * @param name
	 *            the name for the target
	 * @param process
	 *            the underlying process
	 * @param launch
	 *            the launch that spawned the target
	 * @return a new {@link JSDIDebugTarget}
	 */
	public static IDebugTarget newJSDIDebugTarget(VirtualMachine vm, String name, IProcess process, ILaunch launch) {
		return newJSDIDebugtarget(vm, process, launch, name, true, true);
	}

	/**
	 * Returns a new {@link JSDIDebugTarget}
	 * 
	 * @param vm
	 *            the {@link VirtualMachine} to connect to
	 * @param process
	 *            the underlying {@link IProcess}
	 * @param launch
	 *            the launch that spawned this target
	 * @param name
	 *            the name for the target or <code>null</code>
	 * @param canterminate
	 *            if the target will support terminating
	 * @param candisconnect
	 *            if the target will support disconnecting
	 * @return a new {@link JSDIDebugTarget}
	 */
	static IDebugTarget newJSDIDebugtarget(VirtualMachine vm, IProcess process, ILaunch launch, String name, boolean canterminate, boolean candisconnect) {
		return new JSDIDebugTarget(vm, process, launch, name, canterminate, candisconnect);
	}

	/**
	 * Converts the given double value to a {@link String} removing the trailing .0 in the event the precision is 1
	 * 
	 * @param n
	 *            the number to convert
	 * @return the {@link String} value of the number with trailing .0 removed iff the precision is 1
	 */
	public static String numberToString(Number n) {
		double d = n.doubleValue();
		if (d != d) {
			return NumberValue.NAN;
		}
		if (d == Double.POSITIVE_INFINITY) {
			return NumberValue.INFINITY;
		}
		if (d == Double.NEGATIVE_INFINITY) {
			return NumberValue.NEG_INFINITY;
		}
		if (d == 0.0) {
			return "0"; //$NON-NLS-1$
		}
		// we only care about base 10
		String number = Double.toString(d);
		// we only convert for a precision equal to 1
		if (number.endsWith(".0")) { //$NON-NLS-1$
			number = number.substring(0, number.length() - 2);
		}
		return number;
	}
}
