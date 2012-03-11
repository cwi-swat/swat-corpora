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
package org.eclipse.e4.languages.javascript.jsdi;

import java.util.List;

import org.eclipse.e4.languages.javascript.jsdi.event.EventQueue;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequestManager;

/**
 * Abstract description of a VM with-respect-to Javascript debugging. This interface is used to abstract the platform model from the underlying connection / debugger protocol.
 * 
 * @since 1.0
 */
public interface VirtualMachine {
	/**
	 * Sends a resume request to the VM
	 */
	public void resume();

	/**
	 * Sends a suspend request to the VM
	 */
	public void suspend();

	/**
	 * Terminates and disconnects the VM
	 */
	public void terminate();

	/**
	 * Returns the name of the VM
	 * 
	 * @return the name of the VM
	 */
	public String name();

	/**
	 * Returns the description of the VM
	 * 
	 * @return the description of the VM
	 */
	public String description();

	/**
	 * Returns the version string of the VM
	 * 
	 * @return the version string
	 */
	public String version();

	/**
	 * Returns the live list of threads currently in the VM
	 * 
	 * @return the live list of threads
	 */
	public List allThreads();

	/**
	 * Returns the list of scripts currently loaded in the VM
	 * 
	 * @return the list of loaded scripts
	 */
	public List allScripts();

	/**
	 * Disposes the VM and cleans up held objects
	 */
	public void dispose();

	/**
	 * Returns a new {@link Value} that is undefined
	 * 
	 * @return a new undefined {@link Value}
	 */
	public UndefinedValue mirrorOfUndefined();

	/**
	 * Returns a new {@link Value} that represents <code>null</code>
	 * 
	 * @return a new <code>null</code> {@link Value}
	 */
	public NullValue mirrorOfNull();

	/**
	 * Returns a new boolean {@link Value}
	 * 
	 * @param bool
	 * @return a new boolean {@link Value}
	 */
	public BooleanValue mirrorOf(boolean bool);

	/**
	 * Returns a new Number {@link Value}
	 * 
	 * @param number
	 * @return a new number {@link Value}
	 */
	public NumberValue mirrorOf(Number number);

	/**
	 * Returns a new string {@link Value} initialized to the given string
	 * 
	 * @param string
	 * @return a new string {@link Value}
	 */
	public StringValue mirrorOf(String string);

	/**
	 * Returns the {@link EventRequestManager} associated with this {@link VirtualMachine}
	 * 
	 * @return the {@link EventRequestManager} for this {@link VirtualMachine}
	 */
	public EventRequestManager eventRequestManager();

	/**
	 * Returns the {@link EventQueue} associated with this {@link VirtualMachine}
	 * 
	 * @return the {@link EventQueue} for this {@link VirtualMachine}
	 */
	public EventQueue eventQueue();
}
