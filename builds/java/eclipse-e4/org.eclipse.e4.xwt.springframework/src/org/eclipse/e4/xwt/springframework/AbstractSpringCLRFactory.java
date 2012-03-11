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

import org.eclipse.e4.xwt.ICLRFactory;
import org.springframework.context.ApplicationContext;

/**
 * Abstract class for Spring {@link ICLRFactory} to implements into Java
 * "standard" context. createApplicationContext
 * 
 * 
 * <p>
 * The method
 * {@link AbstractSpringCLRFactory#createApplicationContext(IArguments)} is
 * called the first time when the {@link ApplicationContext} must be created and
 * this instance is cached.
 * </p>
 * 
 * <p>
 * IMPORTANT : It's very important to define a singleton of teh implementation
 * of this class and use this singleton into XWT fiel to avoid loading
 * {@link ApplicationContext} each time that x:ClassFactory need to create CLR.
 * </p>
 * 
 * <p>
 * If ui.MySpringCLRFactory implements this class (and define a singleton, you
 * can use it like this) :
 * 
 * <code><Shell xmlns="http://www.eclipse.org/xwt/presentation"
    xmlns:x="http://www.eclipse.org/xwt"
    x:ClassFactory="ui.MySpringCLRFactory.INSTANCE bean=myUI">
    ...
    </code>
 * </p>
 */
public abstract class AbstractSpringCLRFactory extends
		AbstractNoCachableSpringCLRFactory {

	private ApplicationContext applicationContext;

	@Override
	protected ApplicationContext getApplicationContext(IArguments arguments) {
		if (applicationContext != null) {
			return applicationContext;
		}
		applicationContext = createApplicationContext(arguments);
		return applicationContext;
	}

	/**
	 * Create instance of Spring {@link ApplicationContext} which declare CLR
	 * bean.
	 * 
	 * @param arguments
	 * @return
	 */
	protected abstract ApplicationContext createApplicationContext(
			IArguments arguments);
}
