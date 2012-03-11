/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

var Assert = {
	assertTrue : function(message, asserted) {
		if (arguments.length != 2) {
			asserted = message;
			message = null;
		}
		if (!asserted) {
			Assert.fail(message ? message : 'assertTrue failed');
		}
	},
	assertFalse : function(message, asserted) {
		if (arguments.length != 2) {
			asserted = message;
			message = null;
		}
		if (asserted) {
			Assert.fail(message ? message : 'assertFalse failed');
		}
	},
	assertNull : function(message, asserted) {
		if (arguments.length != 2) {
			asserted = message;
			message = null;
		}
		if (asserted !== null) {
			Assert.fail(message ? message : 'assertNull failed: [' + asserted +']');
		}
	},
	assertNotNull : function(message, asserted) {
		if (arguments.length != 2) {
			asserted = message;
			message = null;
		}
		if (asserted === null) {
			Assert.fail(message ? message : 'assertNotNull failed');
		}
	},
	assertUndefined : function(message, asserted) {
		if (arguments.length != 2) {
			asserted = message;
			message = null;
		}
		if (typeof asserted != 'undefined') {
			Assert.fail(message ? message : 'assertUndefined failed: [' + asserted +']');
		}
	},
	assertNotUndefined : function(message, asserted) {
		if (arguments.length != 2) {
			asserted = message;
			message = null;
		}
		if (typeof asserted == 'undefined') {
			Assert.fail(message ? message : 'assertNotUndefined failed');
		}
	},
	assertSame : function(message, expected, value) {
		if (arguments.length != 3) {
			value = expected;
			expected = message;
			message = null;
		}
		if (expected === value)
			return;
		var expectedMessage = ' - expected: [' + expected + '] but was [' + value + '].';
		Assert.fail(message ? message : 'assertSame failed' + expectedMessage);
	},
	assertNotSame : function(message, expected, value) {
		if (arguments.length != 3) {
			value = expected;
			expected = message;
			message = null;
		}
		if (expected === value)
			Assert.fail(message ? message : 'assertNotSame failed [' + value + '].');
	},
	assertEquals : function(message, expected, value) {
		if (arguments.length != 3) {
			value = expected;
			expected = message;
			message = null;
		}
		if (expected === value)
			return;
		if (expected !== null && typeof expected === 'object' && typeof expected.equals === 'function' && expected.equals(value))
			return;
		var expectedMessage = ' - expected: [' + expected + '] but was [' + value + '].';
		Assert.fail(message ? message : 'assertEquals failed' + expectedMessage);
	},
	assertNotEquals : function(message, expected, value) {
		if (arguments.length != 3) {
			value = expected;
			expected = message;
			message = null;
		}
		if (expected === value)
			Assert.fail(message ? message : 'assertNotEquals failed [' + value + '].');
		if (expected !== null && typeof expected === 'object' && typeof expected.equals === 'function' && expected.equals(value))
			Assert.fail(message ? message : 'assertNotEquals failed [' + value + '].');
	},
	// For JSDT JUnit integration this method is over-ridden with a Java version
	// with better stack support
	fail : function(message) {
		throw (message ? message : 'failed');
	},
	scopeAssert : function(scope) {
		scope.fail = Assert.fail;
		scope.assertNotEquals = Assert.assertNotEquals
		scope.assertEquals = Assert.assertEquals;
		scope.assertTrue = Assert.assertTrue;
		scope.assertFalse = Assert.assertFalse;
		scope.assertNotSame = Assert.assertNotSame;
		scope.assertSame = Assert.assertSame;
		scope.assertNull = Assert.assertNull;
		scope.assertNotNull = Assert.assertNotNull;
		scope.assertUndefined = Assert.assertUndefined;
		scope.assertNotUndefined = Assert.assertNotUndefined;
	}
};
Assert.scopeAssert(this);

function TestCase(name) {
	if (name)
		this.setName(name);
}
TestCase.prototype = {
	getName : function() {
		return (this._testName) ? this._testName : null;
	},
	setName : function(name) {
		this._testName = name;
	},
	getTestCaseName : function() {
		return (this._testCaseName) ? this._testCaseName : null;
	},
	setTestCaseName : function(name) {
		this._testCaseName = name;
	},
	setUp : function() {
	},
	tearDown : function() {
	},
	run : function() {
		this.setUp();
		try {
			var testMethodName = this.getName();
			if (!testMethodName) {
				Assert.fail("Test method not set");
			}
			var testMethod = this[testMethodName];
			if (!testMethod) {
				Assert.fail("test method '" + testMethodName + "' not found in TestCase.");
			}
			if (!typeof testMethod === "function") {
				Assert.fail("test method '" + testMethodName + "' is not a function.");
			}
			this[testMethodName]();
		} finally {
			this.tearDown();
		}
	}
};
TestCase.create = function(testCaseName, protoObject) {
	var F = function(testName) {
		TestCase.call(this, testName);
	};
	var base = new TestCase();
	base.setTestCaseName(testCaseName);
	if (protoObject) {
		for ( var prop in protoObject) {
			base[prop] = protoObject[prop];
		}
	}
	F.prototype = base;
	return F;
};
