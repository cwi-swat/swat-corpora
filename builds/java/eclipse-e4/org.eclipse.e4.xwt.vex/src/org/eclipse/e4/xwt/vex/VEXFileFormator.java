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

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.sse.ui.internal.FormatProcessorsExtensionReader;

/**
 * @author yyang
 */
public class VEXFileFormator {
	public void format(IDocument document, String contentId) {
		format(document, contentId, 0, document.getLength());
	}

	public void format(IDocument document, String contentId, int offset, int length) {
		try {
			IStructuredFormatProcessor processor = getFormatProcessor(contentId);
			if (processor != null) {
				processor.formatDocument(document, offset, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	protected IStructuredFormatProcessor getFormatProcessor(String contentTypeId) {
		return FormatProcessorsExtensionReader.getInstance().getFormatProcessor(contentTypeId);
	}
}
