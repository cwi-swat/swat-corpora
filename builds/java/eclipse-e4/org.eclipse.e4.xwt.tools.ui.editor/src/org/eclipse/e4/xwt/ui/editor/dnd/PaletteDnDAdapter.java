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

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.e4.xwt.ui.editor.XWTEditor;
import org.eclipse.e4.xwt.vex.VEXTextEditorHelper;
import org.eclipse.e4.xwt.vex.toolpalette.ContextType;
import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Node;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class PaletteDnDAdapter extends DnDAdapterImpl {

	private Map<String, String> name2content = new HashMap<String, String>();
	private Entry entry;

	/**
	 * @param editor
	 */
	public PaletteDnDAdapter(XWTEditor editor) {
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#createTemplate(java.lang.Object)
	 */
	@Override
	protected Template createTemplate(Object selection) {
		return new Template(entry.getName(), "", getContextType().getName(), entry.getContent(), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#getContextType()
	 */
	public ContextType getContextType() {
		return entry.getContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#getScope()
	 */
	public String getScope() {
		return entry.getScope();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		if (isAccept()) {
			// update layout and layoutData.
			updateLayoutEntry(entry, event.x, event.y);
			updateLayoutDataEntry(entry, event.x, event.y);
		}
	}

	protected void updateLayoutEntry(Entry entry, int x, int y) {

		StructuredTextEditor textEditor = getEditor().getTextEditor();
		StructuredTextViewer textViewer = textEditor.getTextViewer();
		int cursor = VEXTextEditorHelper.getOffsetAtPoint(textViewer, new Point(x, y));
		Node node = VEXTextEditorHelper.getNode(textViewer, cursor);
		while (node.getNodeType() == IDOMNode.TEXT_NODE) {
			node = node.getParentNode();
		}
		String nodeName = node.getNodeName();
		String name = entry.getName();
		// String tagName = null;
		if (nodeName != null && name.toLowerCase().endsWith("layout")) {
			String content = name2content.get(name);
			if (content == null) {
				name2content.put(name, content = entry.getContent());
			}
			String source = "Composite";
			StringTokenizer stk = new StringTokenizer(content, "<>");
			while (stk.hasMoreTokens()) {
				String nextToken = stk.nextToken();
				if (nextToken.endsWith(".layout")) {
					source = nextToken.substring(0, nextToken.lastIndexOf("."));
					break;
				}
			}
			String newContent = content.replace(source, nodeName);
			entry.setContent(newContent);
		}
	}

	protected void updateLayoutDataEntry(Entry entry, int x, int y) {
		StructuredTextEditor textEditor = getEditor().getTextEditor();
		StructuredTextViewer textViewer = textEditor.getTextViewer();
		int cursor = VEXTextEditorHelper.getOffsetAtPoint(textViewer, new Point(x, y));
		Node node = VEXTextEditorHelper.getNode(textViewer, cursor);
		while (node.getNodeType() == IDOMNode.TEXT_NODE) {
			node = node.getParentNode();
		}
		String nodeName = node.getNodeName();
		String name = entry.getName();
		String tagName = null;
		if (nodeName != null && name.toLowerCase().endsWith("data")) {
			if (nodeName.indexOf(":") != -1) {
				tagName = nodeName.substring(nodeName.lastIndexOf(":") + 1);
			} else {
				tagName = nodeName;
			}
			String content = name2content.get(name);
			if (content == null) {
				name2content.put(name, content = entry.getContent());
			}
			String newContent = content.replace("Control", tagName);
			entry.setContent(newContent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#isAccept(java.lang.Object)
	 */
	public boolean isAccept(Object obj) {
		if (obj instanceof Entry) {
			this.entry = (Entry) obj;
			return true;
		}
		return false;
	}

}
