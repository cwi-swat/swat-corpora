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
package org.eclipse.e4.languages.javascript.junit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.e4.internal.languages.javascript.junit.DebugUtil;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JavaScriptTestCase extends TestCase {

	private Context context;
	private ScriptableObject globalScope;

	private String testCaseName;
	private String testFunctionName;
	private Collection<?> scripts;
	private ClassLoader applicationClassLoader;
	private ClassLoader originalApplicationClassLoader;
	private boolean superRunTest = false;
	private volatile static int evalCount;
	private static URL ASSERT_SCRIPT = JavaScriptTestCase.class.getResource("assert.js");

	static {
		String rhinoDebug = System.getProperty("rhino.debug");
		if (rhinoDebug != null) {
			try {
				Class.forName("org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger"); //$NON-NLS-1$
				DebugUtil.debug(rhinoDebug);
			} catch (ClassNotFoundException e) {
			}
		}
	}

	public JavaScriptTestCase() {
		this(null, null, null, null);
	}

	public JavaScriptTestCase(String testFunctionName, String testCaseName) {
		this(testFunctionName, testCaseName, null, null);
	}

	public JavaScriptTestCase(String testFunctionName, String testCaseName, ClassLoader applicationClassLoader) {
		this(testFunctionName, testCaseName, null, applicationClassLoader);
	}

	public JavaScriptTestCase(String testFunctionName, String testCaseName, Collection<?> scripts, ClassLoader applicationClassLoader) {
		super(testCaseName == null ? testFunctionName : testFunctionName + " (" + testCaseName + ")");
		// this is done to create a name that is usable like the typical
		// classname/methodname id.
		// The UI hides everything after the brackets

		this.testFunctionName = testFunctionName;
		this.testCaseName = testCaseName;
		this.scripts = scripts;
		this.applicationClassLoader = applicationClassLoader;
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = Context.enter();
		if (applicationClassLoader != null) {
			originalApplicationClassLoader = context.getApplicationClassLoader();
			context.setApplicationClassLoader(applicationClassLoader);
		}
		globalScope = context.initStandardObjects();
		eval(readContents(ASSERT_SCRIPT), "JavaScriptTestCase_assert");
		ScriptableObject assertScope = (ScriptableObject) ScriptableObject.getProperty(globalScope, "Assert");
		ScriptableObject.putProperty(assertScope, "fail", new BaseFunction() { //$NON-NLS-1$
					public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
						try {
							if (args.length > 0) {
								fail(Context.toString(args[0]));
							} else {
								fail();
							}
							return null;
						} catch (AssertionFailedError e) {
							throw new JavaScriptAssertionFailedError(e);
						}
					}
				});
		evalScripts();
	}

	protected Scriptable createJavaScriptTestCaseInstance(String testName) {
		Scriptable currentScope = getGlobalScope();
		StringTokenizer tokenizer = new StringTokenizer(testCaseName, "."); //$NON-NLS-1$
		while (true) {
			String token = tokenizer.nextToken();
			Object value = currentScope.get(token, currentScope);
			if (!tokenizer.hasMoreTokens()) {
				BaseFunction constructor = (BaseFunction) value;
				Object[] arguments = (testName == null) ? new Object[0] : new Object[] { testName };
				return constructor.construct(context, getGlobalScope(), arguments);
			}
			if (value instanceof Scriptable)
				currentScope = (Scriptable) value;
			else
				throw new RuntimeException("Not Found: " + testCaseName + " in " + this.toString()); //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	protected void tearDown() throws Exception {
		if (applicationClassLoader != null) {
			context.setApplicationClassLoader(originalApplicationClassLoader);
			originalApplicationClassLoader = null;
		}
		globalScope = null;
		context = null;
		Context.exit();
		super.tearDown();
	}

	public void useJavaTests() {
		superRunTest = true;
	}

	public ScriptableObject getGlobalScope() {
		return globalScope;
	}

	public Object eval(String source) {
		return eval(source, null);
	}

	public Object eval(String source, String sourceName) {
		if (sourceName == null) {
			sourceName = "eval-" + evalCount++;
		}
		return context.evaluateString(globalScope, source, sourceName, 1, null);
	}

	protected void runTest() throws Throwable {
		if (superRunTest || getName() == null) {
			super.runTest();
			return;
		}
		ScriptableObject testCaseInstance = (ScriptableObject) createJavaScriptTestCaseInstance(testFunctionName);
		BaseFunction runMethod = (BaseFunction) ScriptableObject.getProperty(testCaseInstance, "run");
		runMethod.call(context, globalScope, testCaseInstance, new Object[0]);
	}

	private void evalScripts() {
		if (scripts == null)
			return;
		for (Iterator<?> iterator = scripts.iterator(); iterator.hasNext();) {
			Object script = iterator.next();
			if (script instanceof String)
				eval((String) script, null);
			else if (script instanceof File) {
				File scriptFile = (File) script;
				eval(readContents(scriptFile), scriptFile.getAbsolutePath());
			} else if (script instanceof URL) {
				URL scriptURL = (URL) script;
				eval(readContents(scriptURL), scriptURL.toExternalForm());
			}
		}
	}

	public static String readContents(File scriptFile) {
		try {
			return readContents(new FileInputStream(scriptFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String readContents(URL url) {
		try {
			return readContents(url.openStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String readContents(InputStream is) throws IOException {
		Reader reader = new InputStreamReader(new BufferedInputStream(is));
		try {
			StringBuffer buffer = new StringBuffer();
			int read = 0;
			char[] cbuf = new char[1024];
			while (-1 != (read = reader.read(cbuf))) {
				buffer.append(cbuf, 0, read);
			}
			return buffer.toString();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
}
