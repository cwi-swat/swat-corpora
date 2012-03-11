/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.       *
 * All rights reserved. This program and the accompanying materials            *
 * are made available under the terms of the Eclipse Public License v1.0       *
 * which accompanies this distribution, and is available at                    *
 * http://www.eclipse.org/legal/epl-v10.html                                   *  
 * Contributors:                                                               *  
 *     Soyatec - initial API and implementation                                * 
 *******************************************************************************/
package org.eclipse.e4.xwt.ui.editor.dnd;

import org.eclipse.e4.xwt.ui.editor.XWTEditor;
import org.eclipse.e4.xwt.vex.VEXContext;
import org.eclipse.e4.xwt.vex.VEXTextEditorHelper;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.ui.internal.XMLUIPlugin;
import org.w3c.dom.Node;

/**
 * @author jliu jin.liu@soyatec.com
 */
public abstract class DnDAdapterImpl implements DnDAdapter {

	static final int CARET_WIDTH = 2;
	private int dropCaretOffset = -1;
	private XWTEditor editor;
	private LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

	/**
	 * 
	 */
	public DnDAdapterImpl(XWTEditor editor) {
		this.setEditor(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		Object selection = getSelection();
		if (!isAccept()) {
			return;
		}

		Template template = createTemplate(selection);
		if (template == null) {
			return;
		}
		drop(template, getDropCaretOffset(), 0);
	}

	/**
	 * @param template
	 */
	protected void drop(Template template, int dropCaretOffset, int length) {
		IDocument document = editor.getTextEditor().getTextViewer().getDocument();
		ContextTypeRegistry registry = XMLUIPlugin.getDefault().getTemplateContextRegistry();
		if (registry != null) {
			TemplateContextType type = registry.getContextType(template.getContextTypeId());

			DocumentTemplateContext templateContext = new DocumentTemplateContext(type, document, new Position(dropCaretOffset, length));
			if (templateContext.canEvaluate(template)) {
				try {
					TemplateBuffer templateBuffer = templateContext.evaluate(template);
					String templateString = templateBuffer.getString();
					document.replace(dropCaretOffset, length, templateString);

					StyledText styledText = editor.getTextWidget();
					int position = getCursorOffset(templateBuffer) + dropCaretOffset;
					styledText.setCaretOffset(position);
					styledText.setFocus();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * @param selection
	 *            TODO
	 * @return
	 */
	protected abstract Template createTemplate(Object selection);

	private int getCursorOffset(TemplateBuffer buffer) {
		TemplateVariable[] variables = buffer.getVariables();
		for (int i = 0; i != variables.length; i++) {
			TemplateVariable variable = variables[i];
			if (variable.getType().equals(GlobalTemplateVariables.Cursor.NAME))
				return variable.getOffsets()[0];
		}

		return buffer.getString().length();
	}

	public void dragOver(DropTargetEvent event) {
		if (!isAccept()) {
			return;
		}
		VEXContext context = editor.getContext();

		Node node = getCurrentNode(event);
		int position = context.findDropPosition(node, getScope(), getContextType(), getCursor(event));
		if (position < 0) {
			event.detail = DND.DROP_NONE;
		} else {
			StyledText styledText = editor.getTextWidget();
			setDropCaretOffset(position);
			refreshCaret(styledText, getDropCaretOffset());
		}
	}

	protected Node getCurrentNode(DropTargetEvent event) {
		StructuredTextViewer textViewer = editor.getTextEditor().getTextViewer();
		return VEXTextEditorHelper.getNode(textViewer, getCursor(event));
	}

	protected int getCursor(DropTargetEvent event) {
		StructuredTextViewer textViewer = editor.getTextEditor().getTextViewer();
		return VEXTextEditorHelper.getOffsetAtPoint(textViewer, new Point(event.x, event.y));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
		StyledText styledText = editor.getTextWidget();
		if (getDropCaretOffset() != -1) {
			refreshCaret(styledText, getDropCaretOffset());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {

	}

	protected void refreshCaret(StyledText text, int newOffset) {
		if (newOffset != -1) {
			Point newPos = text.getLocationAtOffset(newOffset);
			int newHeight = text.getLineHeight(newOffset);
			text.redraw(newPos.x, newPos.y, CARET_WIDTH, newHeight, false);
		}
	}

	/**
	 * @param editor
	 *            the editor to set
	 */
	public void setEditor(XWTEditor editor) {
		this.editor = editor;
	}

	/**
	 * @return the editor
	 */
	public XWTEditor getEditor() {
		return editor;
	}

	protected Object getSelection() {
		IStructuredSelection selection = (IStructuredSelection) transfer.getSelection();
		if (selection != null) {
			return selection.getFirstElement();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#isAccept()
	 */
	public boolean isAccept() {
		Object selection = getSelection();
		if (selection == null) {
			return false;
		}
		return isAccept(selection);
	}

	/**
	 * @param selection
	 * @return
	 */
	protected abstract boolean isAccept(Object selection);

	/**
	 * @param dropCaretOffset
	 *            the dropCaretOffset to set
	 */
	public void setDropCaretOffset(int dropCaretOffset) {
		this.dropCaretOffset = dropCaretOffset;
	}

	/**
	 * @return the dropCaretOffset
	 */
	public int getDropCaretOffset() {
		return dropCaretOffset;
	}
}
