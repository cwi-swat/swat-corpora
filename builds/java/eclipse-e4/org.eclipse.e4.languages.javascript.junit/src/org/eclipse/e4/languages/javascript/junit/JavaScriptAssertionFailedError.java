package org.eclipse.e4.languages.javascript.junit;

import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.mozilla.javascript.EvaluatorException;

public class JavaScriptAssertionFailedError extends AssertionFailedError {

	private static final long serialVersionUID = 8518724972493487259L;

	public JavaScriptAssertionFailedError(AssertionFailedError e) {
		super(e.getMessage());
		initCause(e);
		initStackTrace();
	}

	private void initStackTrace() {
		EvaluatorException jsException = new EvaluatorException(null);
		List<StackTraceElement> targetTrace = new ArrayList<StackTraceElement>();

		StackTraceElement[] traceElements = jsException.getStackTrace();
		for (int i = 0; i < traceElements.length; i++) {
			StackTraceElement traceElement = traceElements[i];
			if (!filter(traceElement))
				targetTrace.add(new StackTraceElement("[JavaScript]", "", traceElement.getFileName(), traceElement.getLineNumber()));
		}

		setStackTrace((StackTraceElement[]) targetTrace.toArray(new StackTraceElement[targetTrace.size()]));
	}

	private boolean filter(StackTraceElement traceElement) {
		if (!traceElement.getClassName().startsWith("org.mozilla.javascript"))
			return true;
		if (traceElement.getLineNumber() < 1)
			return true;

		if (traceElement.getFileName().endsWith(".java"))
			return true;
		if (traceElement.getFileName().startsWith("JavaScriptTestCase_"))
			return true;

		return false;
	}
}