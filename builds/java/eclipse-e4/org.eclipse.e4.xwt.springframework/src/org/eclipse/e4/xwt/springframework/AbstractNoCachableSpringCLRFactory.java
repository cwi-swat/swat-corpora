/*******************************************************************************
 * Copyright (c) 2010 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.springframework;

import static java.text.MessageFormat.format;
import static org.eclipse.e4.xwt.springframework.internal.utils.ArgumentsUtils.createArguments;
import static org.eclipse.e4.xwt.springframework.internal.utils.ArgumentsUtils.getValue;

import java.net.URL;
import java.util.Map;

import org.eclipse.e4.xwt.ICLRFactory;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.springframework.internal.DebugHelper;
import org.eclipse.e4.xwt.springframework.internal.Messages;
import org.eclipse.e4.xwt.springframework.internal.utils.ArgumentsUtils.Result;
import org.springframework.context.ApplicationContext;

/**
 * Abstract class for Spring {@link ICLRFactory}. The args of the
 * {@link ICLRFactory#createCLR(String)} is used to fill the Spring bean id with
 * "bean" parameter.
 * 
 * <p>
 * Example :
 * 
 * x:ClassFactory=
 * "org.eclipse.e4.xwt.tests.clrfactory.MySpringCLRFactory bean=myCLR"
 * 
 * </p>
 * 
 * If XWT is used into NO OSGi context, this class must be implemented to return
 * the {@link ApplicationContext}. It's very important to define a singleton of
 * this class.
 */
public abstract class AbstractNoCachableSpringCLRFactory implements ICLRFactory {

	private static final String BEAN_PARAMETER_KEY = "bean";

	private IBeanNameProvider beanNameProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ICLRFactory#createCLR(java.lang.String)
	 */
	public Object createCLR(String args, Map<String, Object> options) {

		try {
			if (DebugHelper.DEBUG) {
				DebugHelper.log("BEGIN " + this.getClass().getSimpleName()
						+ "#createCLR(\"" + args + "\")");
			}

			// 1) Create parameters Map of the CLR arguments
			IArguments arguments = createArguments(args);

			// 2) Get Spring bean parameter
			Result beanResult = getBeanId(arguments, options);
			String beanId = beanResult.value;
			if (DebugHelper.DEBUG) {
				DebugHelper.log("bean parameter=" + beanId + " (from "
						+ beanResult.source + ") [OK].", 1);
			}

			// 3) Get the Spring ApplicationContext
			ApplicationContext applicationContext = getApplicationContext(arguments);
			if (applicationContext == null) {
				throwApplicationContextNotAvailable(arguments);
			}
			if (DebugHelper.DEBUG) {
				DebugHelper.log("Spring ApplicationContext founded [OK].", 1);
			}
			// 4) Create and return the bean
			Object bean = createBean(arguments, beanId, applicationContext);
			if (DebugHelper.DEBUG) {
				DebugHelper.log("END " + this.getClass().getSimpleName()
						+ "#createCLR(\"" + args + "\") [OK].");
			}
			return bean;
		} catch (RuntimeException e) {
			if (DebugHelper.DEBUG) {
				// Print error
				DebugHelper.logError(e);
				DebugHelper.logError("END " + this.getClass().getSimpleName()
						+ "#createCLR(\"" + args + "\") [NOK]");
			}
			throw e;
		}
	}

	protected void throwApplicationContextNotAvailable(IArguments arguments) {
		throw new XWTException(format(
				Messages.APPLICATION_CONTEXT_NOT_AVAILABLE, getClass()
						.getSimpleName()));
	}

	/**
	 * Returns the Spring beanId of the CLR.
	 * 
	 * @param arguments
	 * @return
	 */
	protected Result getBeanId(IArguments arguments, Map<String, Object> options) {
		URL url = (URL) options.get(IXWTLoader.URL_PROPERTY);
		return getValue(BEAN_PARAMETER_KEY, arguments, getBeanNameProvider() , url,
				Messages.BEAN_PARAM_REQUIRED_ERROR);
	}

	/**
	 * 
	 * @param properties
	 * @param beanId
	 * @param applicationContext
	 * @return
	 */
	private Object createBean(IArguments arguments, String beanId,
			ApplicationContext applicationContext) {
		Object bean = applicationContext.getBean(beanId);
		if (bean instanceof ICLRFactoryAware) {
			((ICLRFactoryAware) bean).setCLRFactory(this, arguments);
		}
		return bean;
	}

	/**
	 * 
	 * @param properties
	 * @param propertiesMap
	 * @return
	 */
	protected abstract ApplicationContext getApplicationContext(
			IArguments arguments);

	public IBeanNameProvider getBeanNameProvider() {
		return beanNameProvider;
	}

	public void setBeanNameProvider(IBeanNameProvider beanNameProvider) {
		this.beanNameProvider = beanNameProvider;
	}
	
	
}
