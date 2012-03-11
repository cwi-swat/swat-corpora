package org.eclipse.jdt.internal.compiler.as;

import java.util.HashMap;
import java.util.Map;

public class ActionScriptScribe {
	static class LineTracker {
		private Map mapping = new HashMap();
		public void record(int currentLineNumber, int mappedLineNumber) {
			// record the mapping between the current as line and the corresponding java line number
			this.mapping.put(new Integer(currentLineNumber), new Integer(mappedLineNumber));
		}
		public Map getMapping() {
			return this.mapping;
		}
	}
	private StringBuffer buffer;
	private int currentLineNumber;
	private final int INDENT_LEVEL_MAX = 20;
	private int indentLevel = 0, effectiveIndentLevel = 0;
	private LineTracker lineTracker;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$
	int currentMappedLineNumber;
	String javaFileName;
	
	public ActionScriptScribe(String javaFileName) {
		this.buffer = new StringBuffer(500);
		this.currentLineNumber = 1;
		this.currentMappedLineNumber = 1;
		this.lineTracker = new LineTracker();
		this.javaFileName = javaFileName;
	}

	public ActionScriptScribe append(boolean b) {
		this.buffer.append(b);
		return this;
	}

	public ActionScriptScribe append(char c) {
		this.buffer.append(c);
		return this;
	}

	public ActionScriptScribe append(char[] chars) {
		this.buffer.append(chars);
		return this;
	}

	public ActionScriptScribe append(double d) {
		this.buffer.append(d);
		return this;
	}

	public ActionScriptScribe append(float f) {
		this.buffer.append(f);
		return this;
	}

	public ActionScriptScribe append(int i) {
		this.buffer.append(i);
		return this;
	}

	public ActionScriptScribe append(long l) {
		this.buffer.append(l);
		return this;
	}
	
	public ActionScriptScribe append(Object o) {
		this.buffer.append(o);
		return this;
	}

	public ActionScriptScribe append(String s) {
		this.buffer.append(s);
		return this;
	}

	public ActionScriptScribe appendNewLine() {
		this.lineTracker.record(this.currentLineNumber, this.currentMappedLineNumber);
		this.buffer.append(LINE_SEPARATOR);
		this.currentLineNumber++;
		return this;
	}
	
	public void indent() {
		if (++this.indentLevel <= this.INDENT_LEVEL_MAX) {
			this.effectiveIndentLevel = this.indentLevel;
		}
	}

	public int length() {
		return this.buffer.length();
	}
	public void outdent() {
		if (--this.indentLevel < 0) {
			this.indentLevel = 0;
		} else if (this.indentLevel < this.effectiveIndentLevel) {
			this.effectiveIndentLevel = this.indentLevel;
		}
	}

	
	void printIndent() {
		for (int i = 0, max = this.effectiveIndentLevel; i < max; i++) {
			this.buffer.append(' ').append(' ');
		}
	}
	
	public String toString() {
		return String.valueOf(this.buffer);
	}

	public void trimEnd(int start, int end) {
		// trim right (remove trailing whitespaces and last semi-colon)
		int semicolonCount = 0;
		trimRight: for (int i = end; i > start; i--) {
			switch (this.buffer.charAt(i)) {
				case ' ' :
				case '\t' :
					continue trimRight;
				case '\r' :
				case '\n' :
					continue trimRight;
				case ';' :
					if (semicolonCount == 0) {
						semicolonCount++;
						continue trimRight;
					}
					//$FALL-THROUGH$
				default:
					if (i < end) {
						// we have to count the number of new lines that has been removed
						this.buffer.delete(i+1, end+1);
					}
					break trimRight;
			}
		}
		// trim left (remove leading whitespace)
		trimLeft: for (int i = start; i < end; i++) {
			switch (this.buffer.charAt(i)) {
				case ' ' :
				case '\t' :
					continue trimLeft;
				case '\n' :
				case '\r' :
					continue trimLeft;
				default:
					if (i > start) {
						this.buffer.delete(start, i);
					}
					break trimLeft;
			}
		}
	}
	
	public String getContents() {
		return String.valueOf(this.buffer);
	}
	
	public void setCurrentMappedLineNumber(int lineNumber) {
		this.currentMappedLineNumber = lineNumber;
	}
	
	public Map getLineMapping() {
		return this.lineTracker.getMapping();
	}
}
