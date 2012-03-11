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
package org.eclipse.e4.xwt.springframework.internal;

/**
 * Error Messages used into Spring support for XWT.
 * 
 */
public class Messages {

	public static final String BEAN_PARAM_REQUIRED_ERROR = "Parameter <{0}> which define the bean Spring is required. It is not founded into x:ClassFactory arguments <{1}>.";

	public static final String BUNDLE_PARAM_REQUIRED_ERROR = "Parameter <{0}> which define the bundle which contains the XML Spring file is required. It is not founded into x:ClassFactory arguments <{1}>.";

	public static final String APPLICATION_CONTEXT_NOT_AVAILABLE = "Impossible to retrieve Spring ApplicationContext with Spring CLR factory <{0}>.";

	public static final String OSGI_APPLICATION_CONTEXT_NOT_AVAILABLE = "Impossible to retrieve Spring ApplicationContext from the bundle <{0}>.";

	public static final String BUNDLE_NOT_FOUNDED = "Impossible to find Bundle <{0}> used to get Spring ApplicationContext.";

	public static final String ACTIVATOR_NOT_STARTED = "Bunde <org.eclipse.e4.xwt.springframework> must be started before using SpringCLRFactory.";

}
