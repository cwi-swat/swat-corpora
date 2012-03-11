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
package org.eclipse.e4.emf.ecore.javascript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


class Script {

	private static final Logger log = Logger.getLogger(Script.class.getName());

	Script(URI uri, Scriptable scope) {
		this.uri = uri;
		this.scope = scope;
	}

	Scriptable scope;
	URI uri;
	File file;
	long loadTimestamp;
	
	boolean shouldLoadScript() {
		if (scope == null) {
			return true;
		} else if (file == null && loadTimestamp > 0) {
			return false;
		} else if (file != null && file.exists() && loadTimestamp > 0) {
			long lastModified = file.lastModified();
			if (lastModified > loadTimestamp) {
				return true;
			}
		}
		return false;
	}

	private static Map<URI, org.mozilla.javascript.Script> compiledScripts = new HashMap<URI, org.mozilla.javascript.Script>(); 
	
	void loadScript(URIConverter uriConverter, ScriptClassLoader scriptClassLoader) {
		Exception scriptException = null;
		if (! uriConverter.exists(uri, null)) {
			return;
		}
		URI physicalUri = uriConverter.normalize(uri);
		org.mozilla.javascript.Script compiled = compiledScripts.get(physicalUri);
		if (compiled == null && scriptClassLoader != null) {
			URI classUri = physicalUri.trimFileExtension().appendFileExtension("class");
			compiled = getCompiledScript(classUri, uriConverter, scriptClassLoader);
		}
		if (compiled == null) {
			try {
				compiled = compileScript(getScriptInputStream(physicalUri, uriConverter), physicalUri);
			} catch (Exception e) {
				scriptException = e;
			}
		} else {
			Script.log.info("Reusing compiled script for " + uri);
		}
		if (compiled != null) {
			Context context = Context.enter();
			try {
				compiled.exec(context, scope);
				Script.log.info("Executed script for " + uri + " from " + (file != null ? (Object)file : physicalUri) + " @ " + loadTimestamp);
			} catch (Exception e) {
				scriptException = e;
			} finally {
				Context.exit();
			}
		}
		if (scriptException != null) {
			Script.log.log(Level.WARNING, "Could not load script from " + physicalUri + ": " + scriptException); // , scriptException);
		}
	}

	private static Class<?>[] constructorParameterTypes = new Class<?>[]{Scriptable.class, Context.class, int.class};

	private org.mozilla.javascript.Script getCompiledScript(URI uri, URIConverter uriConverter, ScriptClassLoader scriptClassLoader) {
		scriptClassLoader.setUri(uri, uriConverter);
		String className = ScriptClassLoader.getUriClassName(uri);
		ClassLoader classLoader = scriptClassLoader;
		Class<?> jsClass = null;
		try {
			jsClass = classLoader.loadClass(className);
		} catch (NoClassDefFoundError e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
		Context context = Context.enter();
		try {
			return (org.mozilla.javascript.Script)jsClass.getConstructor(constructorParameterTypes).newInstance(new Object[]{scope, context, 0});
		} catch (Exception e) {
			return null;
		} finally {
			context.exit();
		}
	}
	
	private InputStream getScriptInputStream(URI physicalUri, URIConverter uriConverter) throws Exception {
		InputStream is = null;
		Exception scriptException = null;
		if (physicalUri.isFile()) {
			String fileName = physicalUri.toFileString();
			try {
				is = new FileInputStream(fileName);
				file = new File(fileName);
			} catch (FileNotFoundException e) {
				scriptException = e;
			}
		}
		if (is == null) {
			try {
				is = uriConverter.createInputStream(physicalUri);
			} catch (IOException e) {
				scriptException = e;
			}
		}
		if (is == null) {
			throw scriptException;
		}
		return is;
	}
	
	private org.mozilla.javascript.Script compileScript(InputStream is, URI physicalUri) throws Exception {
		Reader reader = new InputStreamReader(is);
		Context context = Context.enter();
		org.mozilla.javascript.Script compiled = null;
		Exception scriptException = null;
		try {
			compiled = context.compileReader(reader, physicalUri.toFileString(), -1, null);
			Script.log.info("Compiled script for " + uri + " from " + (file != null ? (Object)file : physicalUri) + " @ " + loadTimestamp);
			compiledScripts.put(uri, compiled);
			compiledScripts.put(physicalUri, compiled);
			if (file != null && file.exists()) {
				loadTimestamp = file.lastModified();
			}
		} catch (Exception e) {
			scriptException = e;
		} finally {
			Context.exit();
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		if (compiled == null) {
			throw scriptException;
		}
		return compiled;
	}
}
