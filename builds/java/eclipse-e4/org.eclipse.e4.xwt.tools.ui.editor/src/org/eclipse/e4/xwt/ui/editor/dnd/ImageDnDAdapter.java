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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.ui.editor.XWTEditor;
import org.eclipse.e4.xwt.utils.PathHelper;
import org.eclipse.e4.xwt.vex.VEXContext;
import org.eclipse.e4.xwt.vex.dom.DomHelper;
import org.eclipse.e4.xwt.vex.toolpalette.ContextType;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.NamedNodeMap;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ImageDnDAdapter extends DnDAdapterImpl {

	private static List<String> supportedImageExts = new ArrayList<String>();
	private static List<String> supportedImageAttrs = new ArrayList<String>();
	static {
		supportedImageExts.add("png");
		supportedImageExts.add("gif");
		supportedImageExts.add("bmp");
	}
	static {
		supportedImageAttrs.add("image");
		supportedImageAttrs.add("backgroundImage");
	}
	private IFile image;
	private IFile editorInput;
	private IDOMNode acceptAttr;

	/**
	 * @param editor
	 */
	public ImageDnDAdapter(XWTEditor editor) {
		super(editor);
		editorInput = ((FileEditorInput) editor.getEditorInput()).getFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#createTemplate(java.lang.Object)
	 */
	protected Template createTemplate(Object selection) {
		String pattern = computePath();
		if (acceptAttr == null) {
			pattern = "image=\"" + pattern + "\"";
		}
		return new Template("image", "", getContextType().getName(), pattern, true);
	}

	/**
	 * @return
	 */
	private String computePath() {
		if (editorInput != null) {
			return PathHelper.getRelativePath(editorInput.getLocation().toString(), image.getLocation().toString());
		}
		return image.getLocation().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#isAccept(java.lang.Object)
	 */
	protected boolean isAccept(Object selection) {
		if (selection instanceof IFile) {
			String ext = ((IFile) selection).getFileExtension();
			boolean contains = supportedImageExts.contains(ext);
			if (contains) {
				image = (IFile) selection;
			}
			return contains;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void drop(DropTargetEvent event) {
		Object selection = getSelection();
		if (!isAccept()) {
			return;
		}

		Template template = createTemplate(selection);
		if (template == null) {
			return;
		}
		String nodeValue = acceptAttr == null ? "" : acceptAttr.getNodeValue();
		drop(template, getDropCaretOffset(), nodeValue == null ? 0 : nodeValue.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOver(DropTargetEvent event) {
		if (!isAccept()) {
			return;
		}
		IDOMNode node = (IDOMNode) getCurrentNode(event);
		int cursor = getCursor(event);
		VEXContext context = getEditor().getContext();
		int position = context.findDropPosition(node, getScope(), getContextType(), cursor);
		if (position >= 0) {
			NamedNodeMap attributes = node.getAttributes();
			if (attributes != null) {
				acceptAttr = getAttr(node, cursor);
				StyledText styledText = getEditor().getTextWidget();
				if (acceptAttr == null && hasImageFeature(node)) {
					setDropCaretOffset(cursor);
					refreshCaret(styledText, getDropCaretOffset());
					return;
				} else if (acceptAttr != null && supportedImageAttrs.contains(acceptAttr.getNodeName())) {
					setDropCaretOffset(getInsertion(acceptAttr, cursor));
					refreshCaret(styledText, getDropCaretOffset());
					return;
				}
			}
		}
		event.detail = DND.DROP_NONE;
	}

	/**
	 * @param node
	 * @return
	 */
	private boolean hasImageFeature(IDOMNode node) {
		boolean hasImageAttr = getImageAttr(node) == null;
		String nodeName = node.getNodeName();
		IMetaclass metaclass = XWT.getMetaclass(nodeName, DomHelper.lookupNamespaceURI(node));
		if (metaclass != null) {
			hasImageAttr &= metaclass.findProperty("image") != null;
		}
		return hasImageAttr;
	}

	/**
	 * @param acceptAttr
	 * @param cursor
	 * @return
	 */
	private int getInsertion(IDOMNode acceptAttr, int cursor) {
		if (acceptAttr == null) {
			return cursor;
		}
		return acceptAttr.getStartOffset() + acceptAttr.getNodeName().length() + 2;
	}

	private IDOMNode getAttr(IDOMNode node, int cursor) {
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			return null;
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			IDOMNode attr = (IDOMNode) attributes.item(i);
			int startOffset = attr.getStartOffset();
			int endOffset = attr.getEndOffset();
			if (cursor >= startOffset && cursor < endOffset) {
				return attr;
			}
		}
		return null;
	}

	private IDOMNode getImageAttr(IDOMNode node) {
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			return null;
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			IDOMNode attr = (IDOMNode) attributes.item(i);
			if (supportedImageAttrs.contains(attr.getNodeName())) {
				return attr;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#getContextType()
	 */
	public ContextType getContextType() {
		return ContextType.XML_ATTRIBUTE_VALUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#getScope()
	 */
	public String getScope() {
		return null;
	}

}
