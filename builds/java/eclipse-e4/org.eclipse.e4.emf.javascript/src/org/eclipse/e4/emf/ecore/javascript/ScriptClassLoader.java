/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.emf.ecore.javascript;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

public class ScriptClassLoader extends ClassLoader {

	public ScriptClassLoader(ClassLoader parent) {
		super(parent);
	}

	private URI uri;
	private URIConverter uriConverter;

	void setUri(URI uri, URIConverter uriConverter) {
		this.uri = uri;
		this.uriConverter = uriConverter;
	}

    public Class<?> loadClass(String name) throws ClassNotFoundException {
    	return super.loadClass(name);
    }

	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] bytes = null;
		if (name.equals(getUriClassName(uri))) {
			try {
				bytes = loadClassData(name);
			} catch (IOException e) {
			}
		}
		return (bytes != null ? defineClass(name, bytes, 0, bytes.length) : super.findClass(name));
	}

	private final static String jsClassNameSuffix = "$" + JavascriptSupport.JAVASCRIPT_EXTENSION;

	public static String getUriClassName(URI uri) {
		String className = uri.trimFileExtension().lastSegment();
		if (uri.isPlatform()) {
			String[] segments = uri.segments();
			for (int i = 2; i < segments.length - 1; i++) {
				className = segments[i] + "." + className;
			}
		}
		return (className.endsWith(jsClassNameSuffix) ? className : className + jsClassNameSuffix);
	}
	public static boolean hasUriClassNameMarker(String className) {
		return className.endsWith(jsClassNameSuffix);
	}

	private byte[] buffer = new byte[10240];
	private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	
	private byte[] loadClassData(String name) throws IOException {
		InputStream input = uriConverter.createInputStream(uri);
		int length = 0;
		byteArrayOutputStream.reset() ;
		while ((length = input.read(buffer, 0, buffer.length)) >= 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}
		return byteArrayOutputStream.toByteArray();
	}
}
