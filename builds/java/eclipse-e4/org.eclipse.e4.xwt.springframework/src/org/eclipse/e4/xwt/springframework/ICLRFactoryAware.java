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

/**
 * Implement this interface for the CLR which wish get {@link ICLRFactory} and
 * {@link IArguments}.
 * 
 */
public interface ICLRFactoryAware {

	/**
	 * Set CLR factory and teh args to create the bean.
	 * 
	 * @param factory
	 * @param args
	 */
	void setCLRFactory(ICLRFactory factory, IArguments args);
}
