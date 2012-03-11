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
package org.eclipse.e4.xwt.tools.ui.designer.core.problems;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Node;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class DesignerProblemChecker implements IProblemChecker {

	private Designer designer;

	protected Problem createError(Node node, String msg) {
		return createProblem(node, msg, Problem.ERROR);
	}

	protected Problem createProblem(Node node, String msg, int type) {
		int startOffset = 0;
		if (node instanceof IDOMNode) {
			startOffset = ((IDOMNode) node).getStartOffset();
		}
		int endOffset = 0;
		if (node instanceof IDOMNode) {
			endOffset = ((IDOMNode) node).getEndOffset();
		}
		int line = 0;
		// try {
		// StructuredTextViewer textViewer = designer.getTextViewer();
		// if (textViewer != null) {
		// line = textViewer.getDocument().getLineOfOffset(startOffset) + 1;
		// }
		// } catch (BadLocationException e) {
		// }
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

	protected IDOMDocument getTextDocument(IDocument doc) {
		IStructuredModel model = StructuredModelManager.getModelManager().getExistingModelForRead(doc);
		if (model != null && model instanceof IDOMModel) {
			return ((IDOMModel) model).getDocument();
		}

		return null;
	}

	public Designer getDesigner() {
		return designer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.problems.IProblemChecker#isAdapterFor(java.lang.Object)
	 */
	public boolean isAdapterFor(Object type) {
		boolean isAdapter = type != null && type instanceof Designer;
		if (isAdapter) {
			designer = (Designer) type;
		}
		return isAdapter;
	}

}
