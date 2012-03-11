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

import java.net.URL;

/**
 * Provider to retrieve Spring bean name from the XWT file {@link URL}.
 * 
 */
public interface IBeanNameProvider {

	/**
	 * Returns the Spring bean name from the XWT file {@link URL}.
	 * 
	 * @param url
	 *            XWT file {@link URL}.
	 * @return Spring bean name.
	 */
	String getBeanName(URL url);

}
