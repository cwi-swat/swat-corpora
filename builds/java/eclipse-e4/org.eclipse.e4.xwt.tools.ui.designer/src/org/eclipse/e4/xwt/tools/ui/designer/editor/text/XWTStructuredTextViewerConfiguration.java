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
package org.eclipse.e4.xwt.tools.ui.designer.editor.text;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.core.text.IStructuredPartitions;
import org.eclipse.wst.xml.core.text.IXMLPartitions;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.eclipse.wst.xml.ui.internal.contentassist.NoRegionContentAssistProcessor;

public class XWTStructuredTextViewerConfiguration extends StructuredTextViewerConfigurationXML {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML#getContentAssistProcessors(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	protected IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) {
		IContentAssistProcessor[] processors = null;
		if ((partitionType.equals(IStructuredPartitions.DEFAULT_PARTITION))
				|| (partitionType.equals(IXMLPartitions.XML_DEFAULT))) {
			processors = new IContentAssistProcessor[] { new XWTContentAssistProcessor() };
		} else if (partitionType
				.equals(IStructuredPartitions.UNKNOWN_PARTITION)) {
			processors = new IContentAssistProcessor[] { new NoRegionContentAssistProcessor() };
		}
		return processors;
	}
}
