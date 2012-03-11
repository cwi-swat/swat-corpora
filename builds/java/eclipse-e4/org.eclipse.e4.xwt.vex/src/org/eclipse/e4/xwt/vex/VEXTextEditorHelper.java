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

import java.lang.reflect.Method;

import org.eclipse.e4.xwt.vex.toolpalette.ContextType;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.w3c.dom.Node;

public class VEXTextEditorHelper {
	public static int getOffsetAtPoint(ITextViewer textViewer, Point absolutePosition) {
		StyledText styledText = textViewer.getTextWidget();
		Point relativePosition = styledText.toControl(absolutePosition);

		try {
			Method method = StyledText.class.getDeclaredMethod("getOffsetAtPoint", int.class, int.class, int[].class, boolean.class);
			method.setAccessible(true);

			int[] trailing = new int[1];
			int widgetOffset = (Integer) method.invoke(styledText, relativePosition.x, relativePosition.y, trailing, false);
			widgetOffset += trailing[0];
			return widgetOffset;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static IDOMNode getCurrentNode(ITextViewer textViewer, Point absolutePosition) {
		return getNode(textViewer, getOffsetAtPoint(textViewer, absolutePosition));
	}

	public static IDOMNode getNode(ITextViewer textViewer, int documentPosition) {
		return (IDOMNode) ContentAssistUtils.getNodeAt(textViewer, documentPosition);
	}

	/**
	 * find the car
	 * 
	 * @param textViewer
	 * @param contextType
	 * @return
	 */
	public static int findContextTypeOffset(ITextViewer textViewer, ContextType contextType) {
		int documentPosition = textViewer.getTextWidget().getCaretOffset();
		return findContextTypeOffset(textViewer, documentPosition, contextType);
	}

	public static int findContextTypeOffset(ITextViewer textViewer, int documentPosition, ContextType contextType) {
		Node node = getNode(textViewer, documentPosition);
		ITextRegion completionRegion = getCompletionRegion(documentPosition, node);
		System.out.println(completionRegion.getStart() + ", " + completionRegion.getEnd() + ", " + completionRegion.getLength());
		String regionType = completionRegion.getType();

		return documentPosition;
	}

	public static ContextType getCurrentContextType(ITextViewer textViewer) {
		int documentPosition = textViewer.getTextWidget().getCaretOffset();
		return getContextType(textViewer, documentPosition);
	}

	public static ContextType getContextType(ITextViewer textViewer, int documentPosition) {
		Node node = getNode(textViewer, documentPosition);
		ITextRegion completionRegion = getCompletionRegion(documentPosition, node);
		System.out.println(completionRegion.getStart() + ", " + completionRegion.getEnd() + ", " + completionRegion.getLength());
		String regionType = completionRegion.getType();

		IStructuredDocumentRegion sdRegion = ContentAssistUtils.getStructuredDocumentRegion(textViewer, documentPosition);
		return ContextType.NONE;
	}

	static public ITextRegion getCompletionRegion(int documentPosition, Node domnode) {
		if (domnode == null) {
			return null;
		}

		ITextRegion region = null;
		int offset = documentPosition;
		IStructuredDocumentRegion flatNode = null;
		IDOMNode node = (IDOMNode) domnode;

		if (node.getNodeType() == Node.DOCUMENT_NODE) {
			if (node.getStructuredDocument().getLength() == 0) {
				return null;
			}
			ITextRegion result = node.getStructuredDocument().getRegionAtCharacterOffset(offset).getRegionAtCharacterOffset(offset);
			while (result == null) {
				offset--;
				result = node.getStructuredDocument().getRegionAtCharacterOffset(offset).getRegionAtCharacterOffset(offset);
			}
			return result;
		}

		IStructuredDocumentRegion startTag = node.getStartStructuredDocumentRegion();
		IStructuredDocumentRegion endTag = node.getEndStructuredDocumentRegion();

		// Determine if the offset is within the start
		// IStructuredDocumentRegion, end IStructuredDocumentRegion, or
		// somewhere within the Node's XML content.
		if ((startTag != null) && (startTag.getStartOffset() <= offset) && (offset < startTag.getStartOffset() + startTag.getLength())) {
			flatNode = startTag;
		} else if ((endTag != null) && (endTag.getStartOffset() <= offset) && (offset < endTag.getStartOffset() + endTag.getLength())) {
			flatNode = endTag;
		}

		if (flatNode != null) {
			// the offset is definitely within the start or end tag, continue
			// on and find the region
			region = getCompletionRegion(offset, flatNode);
		} else {
			// the docPosition is neither within the start nor the end, so it
			// must be content
			flatNode = node.getStructuredDocument().getRegionAtCharacterOffset(offset);
			// (pa) ITextRegion refactor
			// if (flatNode.contains(documentPosition)) {
			if ((flatNode.getStartOffset() <= documentPosition) && (flatNode.getEndOffset() >= documentPosition)) {
				// we're interesting in completing/extending the previous
				// IStructuredDocumentRegion if the current
				// IStructuredDocumentRegion isn't plain content or if it's
				// preceded by an orphan '<'
				if ((offset == flatNode.getStartOffset()) && (flatNode.getPrevious() != null) && (((flatNode.getRegionAtCharacterOffset(documentPosition) != null) && (flatNode.getRegionAtCharacterOffset(documentPosition).getType() != DOMRegionContext.XML_CONTENT)) || (flatNode.getPrevious().getLastRegion().getType() == DOMRegionContext.XML_TAG_OPEN) || (flatNode.getPrevious().getLastRegion().getType() == DOMRegionContext.XML_END_TAG_OPEN))) {
					// Is the region also the start of the node? If so, the
					// previous IStructuredDocumentRegion is
					// where to look for a useful region.
					region = flatNode.getPrevious().getLastRegion();
				} else if (flatNode.getEndOffset() == documentPosition) {
					region = flatNode.getLastRegion();
				} else {
					region = flatNode.getFirstRegion();
				}
			} else {
				// catch end of document positions where the docPosition isn't
				// in a IStructuredDocumentRegion
				region = flatNode.getLastRegion();
			}
		}

		return region;
	}

	static protected ITextRegion getCompletionRegion(int offset, IStructuredDocumentRegion sdRegion) {
		ITextRegion region = sdRegion.getRegionAtCharacterOffset(offset);
		if (region == null) {
			return null;
		}

		if (sdRegion.getStartOffset(region) == offset) {
			// The offset is at the beginning of the region
			if ((sdRegion.getStartOffset(region) == sdRegion.getStartOffset()) && (sdRegion.getPrevious() != null) && (!sdRegion.getPrevious().isEnded())) {
				// Is the region also the start of the node? If so, the
				// previous IStructuredDocumentRegion is
				// where to look for a useful region.
				region = sdRegion.getPrevious().getRegionAtCharacterOffset(offset - 1);
			} else {
				// Is there no separating whitespace from the previous region?
				// If not,
				// then that region is the important one
				ITextRegion previousRegion = sdRegion.getRegionAtCharacterOffset(offset - 1);
				if ((previousRegion != null) && (previousRegion != region) && (previousRegion.getTextLength() == previousRegion.getLength())) {
					region = previousRegion;
				}
			}
		} else {
			// The offset is NOT at the beginning of the region
			if (offset > sdRegion.getStartOffset(region) + region.getTextLength()) {
				// Is the offset within the whitespace after the text in this
				// region?
				// If so, use the next region
				ITextRegion nextRegion = sdRegion.getRegionAtCharacterOffset(sdRegion.getStartOffset(region) + region.getLength());
				if (nextRegion != null) {
					region = nextRegion;
				}
			} else {
				// Is the offset within the important text for this region?
				// If so, then we've already got the right one.
			}
		}

		// valid WHITE_SPACE region handler (#179924)
		if ((region != null) && (region.getType() == DOMRegionContext.WHITE_SPACE)) {
			ITextRegion previousRegion = sdRegion.getRegionAtCharacterOffset(sdRegion.getStartOffset(region) - 1);
			if (previousRegion != null) {
				region = previousRegion;
			}
		}

		return region;
	}

}
