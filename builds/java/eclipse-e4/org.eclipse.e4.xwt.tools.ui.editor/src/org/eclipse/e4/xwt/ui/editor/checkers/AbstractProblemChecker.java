/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.ui.editor.checkers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.vex.problems.IProblemChecker;
import org.eclipse.e4.xwt.vex.problems.Problem;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Node;

public abstract class AbstractProblemChecker implements IProblemChecker {

	protected IJavaProject getJavaProject(StructuredTextEditor fTextEditor) {
		return JavaCore.create(getFile(fTextEditor).getProject());
	}

	protected IFile getFile(StructuredTextEditor fTextEditor) {
		return (IFile) fTextEditor.getEditorInput().getAdapter(IFile.class);
	}

	protected Node getRoot(StructuredTextEditor fTextEditor) {
		StructuredTextViewer textViewer = fTextEditor.getTextViewer();
		if (textViewer == null) {
			return null;
		}
		IndexedRegion root = ContentAssistUtils.getNodeAt(textViewer, 0);
		if (root instanceof Node) {
			return (Node) root;
		}
		return null;
	}

	protected Node getNode(StructuredTextEditor textEditor) {
		StructuredTextViewer textViewer = textEditor.getTextViewer();
		if (textViewer == null) {
			return null;
		}

		StyledText textWidget = textViewer.getTextWidget();
		if (textWidget == null || textWidget.isDisposed()) {
			return null;
		}
		int offset = 0;
		try {
			offset = textWidget.getCaretOffset();
		} catch (Exception e) {
			return null;
		}
		if (offset == 0) {
			return null;
		}
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);
		if (treeNode instanceof Node) {
			return (Node) treeNode;
		}
		return null;
	}

	protected Problem createProblem(StructuredTextEditor fTextEditor, Node node, String msg, int type) {

		int startOffset = 0;
		if (node instanceof IDOMNode) {
			startOffset = ((IDOMNode) node).getStartOffset();
		}
		int endOffset = 0;
		if (node instanceof IDOMNode) {
			endOffset = ((IDOMNode) node).getEndOffset();
		}
		int line = 0;
		try {
			StructuredTextViewer textViewer = fTextEditor.getTextViewer();
			if (textViewer != null) {
				line = textViewer.getDocument().getLineOfOffset(startOffset) + 1;
			}
		} catch (BadLocationException e) {
		}
		return createProblem(msg, type, node, startOffset, endOffset, line);
	}

	protected Problem createProblem(String message, int type, Object source, int start, int end, int line) {
		Problem problem = new Problem(message, type);
		problem.setSource(source);
		problem.start = start;
		problem.end = end;
		problem.line = line;
		return problem;
	}

	protected boolean isValid(Node node) {
		if (node == null) {
			return false;
		}
		String nodeName = node.getNodeName();
		if (nodeName == null || nodeName.equals("") || nodeName.equals("#text")) {
			return false;
		}
		return true;
	}

	protected String getTagName(Node node) {
		if (!isValid(node)) {
			return null;
		}
		return node.getNodeName();
	}

	public List<Problem> checkProblems(StructuredTextEditor textEditor, String javaName) {
		List<Problem> problems = new ArrayList<Problem>();
		Node root = getRoot(textEditor);
		checkProblems(textEditor, javaName, root, problems, true);
		return problems;
	}

	// protected List<Property> findProperties(String tagName, String ns) {
	// List<Property> props = new ArrayList<Property>();
	// Metaclass metaclass = UPF.getMetaclass(tagName, ns);
	// if (metaclass != null) {
	// props.addAll(metaclass.getAllProperties());
	// props.addAll(metaclass.getAllAttachedProperties());
	// }
	// return props;
	// }

	protected abstract void checkProblems(StructuredTextEditor textEditor, String javaName, Node node, List<Problem> problems, boolean includeChildren);

	public boolean canChecked(StructuredTextEditor textEditor, String javaName) {
		return getRoot(textEditor) != null;
	}
}
