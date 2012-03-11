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
package org.eclipse.e4.xwt.vex;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractCodeSynchronizer implements VEXCodeSynchronizer {
	private final Map<Node, String> node2name;
	private String oldAttrValue;

	private VEXEditor editor;

	private String cacheContent = "";

	public AbstractCodeSynchronizer(VEXEditor editor) {
		node2name = new HashMap<Node, String>();
		this.editor = editor;
	}

	public String getOldAttrValue() {
		return oldAttrValue;
	}

	public void setOldAttrValue(String oldAttrValue) {
		this.oldAttrValue = oldAttrValue;
	}

	public String getCacheContent() {
		return cacheContent;
	}

	public void setCacheContent(String cacheContent) {
		this.cacheContent = cacheContent;
	}

	public Map<Node, String> getNode2name() {
		return node2name;
	}

	public VEXEditor getEditor() {
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.soyatec.eface.tools.ui.editors.jdt.CodeManager#add(org.w3c.dom.Node, java.lang.String)
	 */
	public void add(final IDOMNode node, final String name) {
		if (node != null && name != null && !name.equals("")) {
			String oldName = node2name.get(node);
			if (oldName != null) {
				if (oldName.equals(name)) {
					return;
				} else {
					remove(node);
					add(node, name);
				}
			}
			node2name.put(node, name);
			Display display = Display.getDefault();
			if (display != null) {
				display.syncExec(new Runnable() {
					public void run() {
						String fullTypeName = getFullTypeName(node);
						if (fullTypeName == null) {
							return;
						}
						generateFields(fullTypeName, name);
						buildInitialization();
					}
				});
			}
		}
	}

	abstract protected String getFullTypeName(IDOMNode node);

	protected void updateCodeManager() {
		StructuredTextViewer textViewer = editor.getTextEditor().getTextViewer();
		int offset = textViewer.getTextWidget().getCaretOffset();
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);
		Node root = (Node) treeNode;
		while ((root != null) && (root.getParentNode() != null) && !(root.getParentNode() instanceof Document)) {
			root = root.getParentNode();
		}
		update((IDOMNode) root);
	}

	public void update(IDOMNode parentNode) {
		if (parentNode.getNodeType() == Node.TEXT_NODE) {
			return;
		}
		if (!editor.getContext().hasType(parentNode)) {
			return;
		}
		boolean findNameAttr = false;
		NamedNodeMap attributes = parentNode.getAttributes();
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			Node item = attributes.item(i);
			String nodeName = item.getNodeName();
			if ("Name".equals(nodeName)) {
				findNameAttr = true;
				if (oldAttrValue != null) {
					remove(parentNode);
				}
				add(parentNode, item.getNodeValue());
			}
		}
		if (!findNameAttr) {
			remove(parentNode);
		}
		NodeList childNodes = parentNode.getChildNodes();
		int len = childNodes.getLength();
		for (int i = 0; i < len; i++) {
			Node item = childNodes.item(i);
			update((IDOMNode) item);
		}
	}

	public boolean codeAboutToBeChanged() {
		StructuredTextViewer textViewer = editor.getTextEditor().getTextViewer();
		int offset = textViewer.getTextWidget().getCaretOffset();
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);
		Node node = (Node) treeNode;
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		IDOMNode xmlnode = (IDOMNode) node;
		if (xmlnode != null && editor.getContext().hasType(xmlnode)) {
			NamedNodeMap nodeMap = xmlnode.getAttributes();
			for (int i = nodeMap.getLength() - 1; i >= 0; i--) {
				IDOMAttr attrNode = (IDOMAttr) nodeMap.item(i);
				String attrName = attrNode.getName();
				String attrValue = attrNode.getValue();
				int startOffset = attrNode.getStartOffset();
				int endOffset = startOffset + attrName.length() + attrValue.length() + 3;
				if (offset >= startOffset && offset < endOffset) {
					oldAttrValue = attrValue;
					return true;
				}
			}
		}
		oldAttrValue = null;
		return false;
	}

	protected String updateCacheContent(IDocument newInput) {
		StringBuffer buffer = new StringBuffer();
		String content = newInput.get();
		for (char c : content.toCharArray()) {
			if (!Character.isWhitespace(c)) {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	protected boolean checkContent(String content) {
		StringBuffer buffer = new StringBuffer();

		for (char c : content.toCharArray()) {
			if (!Character.isWhitespace(c)) {
				buffer.append(c);
			}
		}
		String value = buffer.toString();

		if (value.equals(cacheContent)) {
			return true;
		}
		cacheContent = value;
		return false;
	}

	protected String resolveHostClass(Node node) {
		return null;
	}

	protected IProgressMonitor getProgressMonitor() {
		ProgressManager progresManager = (ProgressManager) editor.getTextEditor().getSite().getWorkbenchWindow().getWorkbench().getProgressService();
		IProgressMonitor monitor = progresManager.getDefaultMonitor();
		return monitor;
	}

	protected IProject getProject() {
		IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
		if (resource != null) {
			return resource.getProject();
		}
		return null;
	}
}
