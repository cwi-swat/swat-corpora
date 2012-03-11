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
package org.eclipse.e4.core.internal.di;

import java.lang.ref.WeakReference;
import java.lang.reflect.AccessibleObject;
import org.eclipse.e4.core.di.IInjector;
import org.eclipse.e4.core.di.annotations.GroupUpdates;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.suppliers.IObjectDescriptor;
import org.eclipse.e4.core.di.suppliers.IRequestor;
import org.eclipse.e4.core.di.suppliers.PrimaryObjectSupplier;

/**
 * @noextend This class is not intended to be subclassed by clients.
 */
abstract public class Requestor implements IRequestor {

	final private WeakReference<Object> objectRef;
	final protected boolean track;
	final private boolean groupUpdates;
	final private boolean isOptional;

	final private IInjector injector;
	final protected PrimaryObjectSupplier primarySupplier;
	private PrimaryObjectSupplier tempSupplier;

	protected Object[] actualArgs;

	private IObjectDescriptor[] objectDescriptors;

	protected abstract IObjectDescriptor[] calcDependentObjects();

	public Requestor(AccessibleObject reflectionObject, IInjector injector, PrimaryObjectSupplier primarySupplier, PrimaryObjectSupplier tempSupplier, Object requestingObject, boolean track) {
		this.injector = injector;
		this.primarySupplier = primarySupplier;
		this.tempSupplier = tempSupplier;
		if (requestingObject != null)
			objectRef = new WeakReference<Object>(requestingObject);
		else
			objectRef = null;
		this.track = track;
		groupUpdates = (reflectionObject == null) ? false : reflectionObject.isAnnotationPresent(GroupUpdates.class);
		isOptional = (reflectionObject == null) ? false : reflectionObject.isAnnotationPresent(Optional.class);
	}

	public IInjector getInjector() {
		return injector;
	}

	public PrimaryObjectSupplier getPrimarySupplier() {
		return primarySupplier;
	}

	public PrimaryObjectSupplier getTempSupplier() {
		return tempSupplier;
	}

	public void clearTempSupplier() {
		tempSupplier = null; // don't keep temporary suppliers in memory after initial processing
	}

	public Object getRequestingObject() {
		if (objectRef == null)
			return null;
		return objectRef.get();
	}

	/**
	 * Determines if the requestor wants to be called whenever one of the dependent object changes.
	 */
	public boolean shouldTrack() {
		return track;
	}

	public boolean shouldGroupUpdates() {
		return groupUpdates;
	}

	public boolean isOptional() {
		return isOptional;
	}

	/**
	 * If actual arguments are resolved for this requestor
	 */
	public boolean isResolved() {
		return (actualArgs != null);
	}

	public void setResolvedArgs(Object[] actualArgs) {
		this.actualArgs = actualArgs;
	}

	public boolean isValid() {
		return (getRequestingObject() != null);
	}

	public void resolveArguments(boolean initial) {
		((InjectorImpl) injector).resolveArguments(this, initial);
	}

	public void disposed(PrimaryObjectSupplier objectSupplier) {
		((InjectorImpl) injector).disposed(objectSupplier);

	}

	public boolean uninject(Object object, PrimaryObjectSupplier objectSupplier) {
		Object originatingObject = getRequestingObject();
		if (originatingObject == null)
			return false;
		if (originatingObject != object)
			return true;
		injector.uninject(object, objectSupplier);
		return false;
	}

	public IObjectDescriptor[] getDependentObjects() {
		if (objectDescriptors == null)
			objectDescriptors = calcDependentObjects();
		return objectDescriptors;
	}

	/**
	 * Don't hold on to the resolved results as it will prevent 
	 * them from being garbage collected. 
	 */
	protected void clearResolvedArgs() {
		if (actualArgs == null)
			return;
		for (int i = 0; i < actualArgs.length; i++) {
			actualArgs[i] = null;
		}
		actualArgs = null;
		return;
	}
}
