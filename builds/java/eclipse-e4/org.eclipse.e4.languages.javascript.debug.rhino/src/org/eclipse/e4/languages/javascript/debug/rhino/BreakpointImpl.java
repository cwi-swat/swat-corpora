/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.rhino;

import java.util.HashMap;


public class BreakpointImpl {

	private final Long breakpointId;
	private final ScriptImpl script;
	private final Integer lineNumber;
	private final Object functionName;
	private final String condition;
	private final Long threadId;

	public BreakpointImpl(Long breakpointId, ScriptImpl script, Integer lineNumber, String functionName, String condition, Long threadId) {
		this.breakpointId = breakpointId;
		this.script = script;
		this.lineNumber = lineNumber;
		this.functionName = functionName;
		this.condition = condition;
		this.threadId = threadId;
	}

	public Object toJSON() {
		HashMap result = new HashMap();
		result.put(JSONConstants.BREAKPOINT_ID, breakpointId);
		result.put(JSONConstants.SCRIPT_ID, script.getId());
		if (lineNumber != null)
			result.put(JSONConstants.LINE, lineNumber);
		if (functionName != null)
			result.put(JSONConstants.FUNCTION, functionName);
		if (condition != null)
			result.put(JSONConstants.CONDITION, condition);
		if (threadId != null)
			result.put(JSONConstants.THREAD_ID, threadId);
		return result;
	}

	public Long getId() {
		return breakpointId;
	}

	public ScriptImpl getScript() {
		return script;
	}

	public boolean matches(String functionName, Integer lineNumber, DebugFrameImpl frame) {
		if (this.lineNumber == null) {
			if (functionName == null)
				return lineNumber.intValue() == 1 && this.functionName == null && checkThread(frame) && checkCondition(frame);

			return functionName.equals(this.functionName) && checkThread(frame) && checkCondition(frame);
		}
		return this.lineNumber.equals(lineNumber) && checkThread(frame) && checkCondition(frame);
	}

	private boolean checkThread(DebugFrameImpl frame) {
		if (threadId == null)
			return true;

		return frame.getThreadId().equals(threadId);
	}

	private boolean checkCondition(DebugFrameImpl frame) {
		if (condition == null)
			return true;

		return frame.evaluateCondition(condition);
	}

}
