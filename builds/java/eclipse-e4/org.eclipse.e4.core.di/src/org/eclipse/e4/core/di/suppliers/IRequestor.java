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
package org.eclipse.e4.core.di.suppliers;

import org.eclipse.e4.core.di.InjectionException;

/**
 * Requestor represents an atomary operation performed during the injection.
 * Injecting a field, or calling an injected method are examples of such
 * operations.
 * <p>
 * When an injector is asked to do a task, it splits work into a set of
 * requestors. Requestors are passed to relevant object suppliers so that 
 * requestors can be executed when values in the supplier change. (For instance,
 * an object supplier that provided the value for the injected field, is expected
 * to execute requestor again when it detects change in the injected value).
 * </p>
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IRequestor {

	/**
	 * Forces the requestor to resolve arguments it depends on. 
	 * @param initial <code>true</code> if this is the initial attempt to resolve arguments
	 * @throws InjectionException if an exception occurred while performing this task
	 */
	public void resolveArguments(boolean initial) throws InjectionException;

	/**
	 * Call this method to perform requestor's task. This method should be called
	 * whenever the dependent value changes.
	 * <p> 
	 * @return result of the task
	 * @throws InjectionException if an exception occurred while performing this task
	 */
	public Object execute() throws InjectionException;

	/**
	 * The injected object that initiated this request
	 * @return the object that initiated this request
	 */
	public Object getRequestingObject();

	/**
	 * Determines if this requestor is still valid. Once requestor becomes invalid, it
	 * stays invalid. Invalid requestors can be safely removed from computations.
	 * @return <code>true</code> if this requestor is valid, <code>false</code> otherwise
	 */
	public boolean isValid();

	/**
	 * Notifies the requestor that an object supplier has been disposed of.
	 * @param objectSupplier the object supplier being disposed of
	 * @throws InjectionException if an exception occurred while performing this task
	 */
	public void disposed(PrimaryObjectSupplier objectSupplier) throws InjectionException;

	/**
	 * Notifies the requestor that an object should be un-injected.
	 * @param object domain object that needs to be un-injected
	 * @param objectSupplier the object supplier being un-injected
	 * @throws InjectionException if an exception occurred while performing this task
	 * @return <code>true</code> if the object was uninjected, <code>false</code> otherwise
	 */
	public boolean uninject(Object object, PrimaryObjectSupplier objectSupplier) throws InjectionException;
}
