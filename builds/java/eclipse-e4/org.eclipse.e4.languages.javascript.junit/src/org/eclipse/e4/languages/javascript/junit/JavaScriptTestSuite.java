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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import junit.framework.TestSuite;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.ScriptableObject;

public class JavaScriptTestSuite extends TestSuite {


	public JavaScriptTestSuite() {
		super();
	}
	
	public JavaScriptTestSuite(String testCaseName) {
		super(testCaseName);
	}
	

	public JavaScriptTestSuite(String testCaseName, Collection<?> scripts) {
		this(testCaseName, scripts, null);
	}

	public JavaScriptTestSuite(String testCaseName, Collection<?> scripts, ClassLoader applicationClassLoader) {
		super(testCaseName);
		try {
			findTests(scripts, applicationClassLoader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void findTests(Collection<?> scripts, ClassLoader applicationClassLoader) throws Exception {
		JavaScriptTestCase testCase = new JavaScriptTestCase(null, getName(), scripts, applicationClassLoader);
		testCase.setUp();
		try {
			ArrayList<String> testFunctionNames = new ArrayList<String>();
			ScriptableObject testCaseInstance = (ScriptableObject) testCase.createJavaScriptTestCaseInstance(null);
			Object[] ids = ScriptableObject.getPropertyIds(testCaseInstance);
			for (int i = 0; i < ids.length; i++) {
				Object id = ids[i];
				Object value = null;
				if (id instanceof String) {
					String fieldName = (String) id;
					if (!fieldName.startsWith("test"))
						continue;
					value = ScriptableObject.getProperty(testCaseInstance, fieldName);
					if (value instanceof BaseFunction)
						testFunctionNames.add(fieldName);
				}
			}
			Collections.sort(testFunctionNames);
			for (Iterator<String> iterator = testFunctionNames.iterator(); iterator.hasNext();) {
				String testFunctionName = iterator.next();
				addTest(new JavaScriptTestCase(testFunctionName, getName(), scripts, applicationClassLoader));
			}
		} finally {
			testCase.tearDown();
		}
	}
}
