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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.palette;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.DropAdapter;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.DropContext;
import org.eclipse.e4.xwt.tools.ui.palette.ContextType;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Point;
import org.w3c.dom.Node;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class PaletteDropAdapter implements DropAdapter {

	static final int CARET_WIDTH = 2;
	protected int dropCaretOffset = -1;
	protected Designer designer;
	protected LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

	protected DropContext context;
	protected Entry entry;

	public PaletteDropAdapter(Designer designer, DropContext context) {
		this.designer = designer;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		Object selection = getSelection();
		if (!isAccept() || !(selection instanceof Entry)) {
			return;
		}

		final Template template = createTemplate(entry);
		if (template == null) {
			return;
		}

		Runnable dropRunnable = new Runnable() {
			public void run() {
				drop(template, dropCaretOffset, 0);
			}
		};
		// designer.formatWithCompound(dropRunnable);
	}

	/**
	 * @param template
	 */
	protected void drop(Template template, int dropCaretOffset, int length) {
		// IDocument document = designer.getTextViewer().getDocument();
		// ContextTypeRegistry registry =
		// XMLUIPlugin.getDefault().getTemplateContextRegistry();
		// if (registry != null) {
		// TemplateContextType type =
		// registry.getContextType(template.getContextTypeId());
		//
		// DocumentTemplateContext templateContext = new
		// DocumentTemplateContext(type, document, new Position(dropCaretOffset,
		// length));
		// if (templateContext.canEvaluate(template)) {
		// try {
		// TemplateBuffer templateBuffer = templateContext.evaluate(template);
		// String templateString = templateBuffer.getString();
		// document.replace(dropCaretOffset, length, templateString);
		//
		// StyledText styledText = designer.getTextWidget();
		// int position = getCursorOffset(templateBuffer) + dropCaretOffset;
		// styledText.setCaretOffset(position);
		// styledText.setFocus();
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// }
		// }
		// }
	}

	protected Template createTemplate(Entry entry) {
		return new Template(entry.getName(), "", getContextType().getName(), entry.getContent(), true);
	};

	protected int getCursorOffset(TemplateBuffer buffer) {
		TemplateVariable[] variables = buffer.getVariables();
		for (int i = 0; i != variables.length; i++) {
			TemplateVariable variable = variables[i];
			if (variable.getType().equals(GlobalTemplateVariables.Cursor.NAME))
				return variable.getOffsets()[0];
		}

		return buffer.getString().length();
	}

	public void dragOver(DropTargetEvent event) {
		// if (!isAccept() || !(getSelection() instanceof Entry)) {
		// return;
		// }
		// this.entry = (Entry) getSelection();
		// Node node = getCurrentNode(event);
		// int position = context.findDropPosition(node, getScope(),
		// getContextType(), getCursor(event));
		// if (position < 0) {
		// event.detail = DND.DROP_NONE;
		// } else {
		// StyledText styledText = designer.getTextWidget();
		// setDropCaretOffset(position);
		// refreshCaret(styledText, getDropCaretOffset());
		// event.detail = DND.DROP_COPY;
		// }
	}

	protected Node getCurrentNode(DropTargetEvent event) {
		// StructuredTextViewer textViewer =
		// designer.getTextEditor().getTextViewer();
		// return StructuredTextHelper.getNode(textViewer, getCursor(event));
		return null;
	}

	protected int getCursor(DropTargetEvent event) {
		// StructuredTextViewer textViewer =
		// designer.getTextEditor().getTextViewer();
		// return StructuredTextHelper.getOffsetAtPoint(textViewer, new
		// Point(event.x, event.y));
		return 0;
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
		// StyledText styledText = designer.getTextWidget();
		// if (getDropCaretOffset() != -1) {
		// refreshCaret(styledText, getDropCaretOffset());
		// }
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
	public void setDesigner(Designer editor) {
		this.designer = editor;
	}

	/**
	 * @return the editor
	 */
	public Designer getDesigner() {
		return designer;
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
	protected boolean isAccept(Object selection) {
		if (selection instanceof Entry) {
			return true;
		}
		return false;
	};

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.dnd.DropAdapter#getContextType()
	 */
	public ContextType getContextType() {
		return entry.getContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.dnd.DropAdapter#getScope()
	 */
	public String getScope() {
		return entry.getScope();
	}
}
