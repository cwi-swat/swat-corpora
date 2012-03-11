/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.stringconverters;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class BundleClassStringConverter extends AbstractClassStringConverter {

	public BundleClassStringConverter() {
		super.trim = true;
	}

	private Bundle bundle = null;
	
	protected  Class<?> resolveLoading(String className) {
		int pos = className.indexOf('/');
		if (pos < 0) {
			pos = className.indexOf(':');
		}
		if (pos < 0) {
			return null;
		}
		String bundleName = className.substring(0, pos);
		this.bundle = Platform.getBundle(bundleName);
		try {
			return super.resolveLoading(className.substring(pos + 1));
		} finally {
			this.bundle = null;
		}
	}
	
	protected Class<?> loadClass(String name) throws Exception {
		return bundle.loadClass(name);
	}
}
