/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.internal.javascript;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class JSBundleData {
	private final int bundleId;
	private final String location;
	private final Map headers;
	private final ClassLoader contextClassLoader;
	private URL base;

	public JSBundleData(int bundleId, String location, Map headers, ClassLoader contextClassLoader) {
		this.bundleId = bundleId;
		this.location = location;
		this.headers = Collections.unmodifiableMap(new HashMap(headers));
		this.contextClassLoader = contextClassLoader;
	}

	private synchronized void initBase() throws MalformedURLException {
		if (base != null)
			return;

		base = new URL(location);
	}

	public int getBundleId() {
		return bundleId;
	}

	public String getLocation() {
		return location;
	}

	public Map getHeaders() {
		return headers;
	}

	public URL getEntry(String path) {
		try {
			initBase();
			return new URL(base, path);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public ClassLoader getContextClassLoader() {
		return contextClassLoader;
	}
}
