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

import org.eclipse.e4.xwt.IConstants;

/**
 * 
 * {@link IBeanNameProvider} default implementation which returns the filename
 * (from the {@link URL}) (without '.xwt' extension) as a Spring bean name.
 * 
 */
public class DefaultBeanNameProvider implements IBeanNameProvider {

	public static IBeanNameProvider INSTANCE = new DefaultBeanNameProvider();

	protected DefaultBeanNameProvider() {
		// Don't Instantiate this class!!!
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.springframework.IBeanNameProvider#getBeanName(java
	 * .net.URL)
	 */
	public String getBeanName(URL url) {
		// Get file name
		String fileName = getFileName(url);
		if (fileName.endsWith(IConstants.XWT_EXTENSION)) {
			// remove '.xwt' extension
			return fileName.substring(0, fileName.length() - 1
					- IConstants.XWT_EXTENSION.length());
		}
		return fileName;
	}

	/**
	 * Return the file name of the {@link URL}.
	 * 
	 * @param url
	 * @return
	 */
	protected String getFileName(URL url) {
		String path = url.getPath();
		int index = path.lastIndexOf('/');
		if (index == -1) {
			return path;
		}
		return path.substring(index + 1, path.length());
	}

}
