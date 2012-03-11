/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.core.contexts;

import java.lang.annotation.Annotation;
import javax.inject.Scope;
import javax.inject.Singleton;
import org.eclipse.e4.core.di.IInjector;
import org.eclipse.e4.core.di.InjectionException;
import org.eclipse.e4.core.di.InjectorFactory;
import org.eclipse.e4.core.di.suppliers.PrimaryObjectSupplier;
import org.eclipse.e4.core.internal.contexts.ContextObjectSupplier;
import org.eclipse.e4.core.internal.contexts.EclipseContext;

/**
 * An injection factory is used to inject data and services from a context into a domain object. The
 * injection will try to find fields and methods in the user objects that correspond to the names of
 * the services present in the context. Subsequent changes to the context after injection will cause
 * the affected items to be re-injected into the object. Thus the object will remain synchronized
 * with the context once it has been injected.
 * <p>
 * If annotations are supported by the runtime, matching of methods and fields to be injected is
 * also performed using the annotations defined in packages javax.inject and
 * org.eclipse.e4.core.services.annotations.
 * </p>
 * <p>
 * The injection of values is generally done as a number of calls. User objects that want to
 * finalize the injected data (for instance, to perform calculations based on multiple injected
 * values) can place such calculations in a method with the <tt>javax.annotation.PostConstruct</tt> annotation.
 * </p>
 * <p>
 * When injecting values, all fields are injected prior to injection of methods. When values are
 * removed from the context or the context is disposed, injection of null values occurs in the
 * reverse order: methods and then fields. As a result, injection methods can safely make use of
 * injected field values. The order in which methods are injected is undefined, so
 * injection methods should not rely on other injection methods having been run already. Methods
 * and field on superclasses are injected before methods in fields on the subclasses. 
 * </p>
 * <p>
 * When a context is disposed, the injection factory will attempt to notify all injected objects by
 * calling methods with the <tt>javax.annotation.PreDestroy</tt> annotation.
 * 
 * @noextend This class is not intended to be extended by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
final public class ContextInjectionFactory {

	final private static IInjector injector = InjectorFactory.getDefault();

	private ContextInjectionFactory() {
		// prevents instantiations
	}

	/**
	 * Injects a context into a domain object. See the class comment for details on the injection
	 * algorithm that is used.
	 * 
	 * @param object The object to perform injection on
	 * @param context The context to obtain injected values from
	 * @throws InjectionException if an exception occurred while performing this operation
	 */
	static public void inject(Object object, IEclipseContext context) throws InjectionException {
		PrimaryObjectSupplier supplier = ContextObjectSupplier.getObjectSupplier(context, injector);
		injector.inject(object, supplier);
	}

	/**
	 * Call a method, injecting the parameters from the context.
	 * 
	 * @param object The object to perform injection on
	 * @param qualifier the annotation tagging method to be called
	 * @param context The context to obtain injected values from
	 * @return the return value of the method call, might be <code>null</code>
	 * @throws InjectionException if an exception occurred while performing this operation
	 */
	static public Object invoke(Object object, Class<? extends Annotation> qualifier, IEclipseContext context) throws InjectionException {
		PrimaryObjectSupplier supplier = ContextObjectSupplier.getObjectSupplier(context, injector);
		return injector.invoke(object, qualifier, supplier);
	}

	/**
	 * Call a method, injecting the parameters from the context.
	 * 
	 * @param object The object to perform injection on
	 * @param qualifier the annotation tagging method to be called
	 * @param context The context to obtain injected values from
	 * @param defaultValue A value to be returned if the method cannot be called, might be <code>null</code>
	 * @return the return value of the method call, might be <code>null</code>
	 * @throws InjectionException if an exception occurred while performing this operation
	 */
	static public Object invoke(Object object, Class<? extends Annotation> qualifier, IEclipseContext context, Object defaultValue) throws InjectionException {
		PrimaryObjectSupplier supplier = ContextObjectSupplier.getObjectSupplier(context, injector);
		return injector.invoke(object, qualifier, defaultValue, supplier);
	}

	/**
	 * Call a method, injecting the parameters from two contexts. This method is useful when the method needs
	 * to receive some values not present in the context. In this case a local context can be created and
	 * populated with additional values.
	 * <p>
	 * If values for the same key present in both the context and the local context, the values from
	 * the local context are injected.
	 * </p> 
	 * @param object The object to perform injection on
	 * @param qualifier the annotation tagging method to be called
	 * @param context The context to obtain injected values from
	 * @param localContext The context to obtain addition injected values from
	 * @param defaultValue A value to be returned if the method cannot be called, might be <code>null</code>
	 * @return the return value of the method call, might be <code>null</code>
	 * @throws InjectionException if an exception occurred while performing this operation
	 */
	static public Object invoke(Object object, Class<? extends Annotation> qualifier, IEclipseContext context, IEclipseContext localContext, Object defaultValue) throws InjectionException {
		PrimaryObjectSupplier supplier = ContextObjectSupplier.getObjectSupplier(context, injector);
		PrimaryObjectSupplier tempSupplier = ContextObjectSupplier.getObjectSupplier(localContext, injector);
		return injector.invoke(object, qualifier, defaultValue, supplier, tempSupplier);
	}

	/**
	 * Un-injects the context from the object. The un-injection requires that all injected 
	 * values were marked as optional, or the un-injection will fail.
	 * 
	 * @param object The domain object previously injected with the context
	 * @param context The context previously injected into the object
	 * @throws InjectionException if an exception occurred while performing this operation
	 */
	static public void uninject(Object object, IEclipseContext context) throws InjectionException {
		((EclipseContext) context).removeListenersTo(object);
	}

	/**
	 * Obtain an instance of the specified class and inject it with the context.
	 * <p>
	 * Class'es scope dictates if a new instance of the class will be created, or existing instance
	 * will be reused.
	 * </p>
	 * @param clazz The class to be instantiated
	 * @param context The context to obtain injected values from
	 * @return an instance of the specified class
	 * @throws InjectionException if an exception occurred while performing this operation
	 * @see Scope
	 * @see Singleton
	 */
	static public <T> T make(Class<T> clazz, IEclipseContext context) throws InjectionException {
		PrimaryObjectSupplier supplier = ContextObjectSupplier.getObjectSupplier(context, injector);
		return injector.make(clazz, supplier);
	}

	/**
	 * Obtain an instance of the specified class and inject it with the context. This method
	 * allows extra values that don't need to be tracked to be passed to the object using staticContext.
	 * <p>
	 * If values for the same key present in both the context and the static context, the values from
	 * the static context are injected.
	 * </p> 
	 * <p>
	 * Class'es scope dictates if a new instance of the class will be created, or existing instance
	 * will be reused.
	 * </p>
	 * @param clazz The class to be instantiated
	 * @param context The context to obtain injected values from
	 * @param staticContext The context containing extra values; not tracked
	 * @return an instance of the specified class
	 * @throws InjectionException if an exception occurred while performing this operation
	 * @see #make(Class, IEclipseContext)
	 */
	static public <T> T make(Class<T> clazz, IEclipseContext context, IEclipseContext staticContext) throws InjectionException {
		PrimaryObjectSupplier supplier = ContextObjectSupplier.getObjectSupplier(context, injector);
		PrimaryObjectSupplier tempSupplier = ContextObjectSupplier.getObjectSupplier(staticContext, injector);
		return injector.make(clazz, supplier, tempSupplier);
	}
}
