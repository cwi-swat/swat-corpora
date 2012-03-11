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
package org.eclipse.e4.xwt.tools.ui.designer.editor.text.assist;

import org.eclipse.e4.xwt.tools.ui.designer.dialogs.ColorChooser;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ColorSelectionCompletionProposal extends XWTSelectionCompletionProposal {

	public ColorSelectionCompletionProposal() {
		super("\"(Choose Color...)\"", 0, 0, 0, 0, null, "(Choose Color...)", null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.editor.text.assist.XWTSelectionCompletionProposal#apply(org.eclipse.jface.text.IDocument)
	 */
	public void apply(IDocument document) {
		ColorChooser dialog = new ColorChooser(new Shell(), null);
		if (dialog.open() == Window.OK) {
			String color = dialog.getColor();
			if (color == null || color.equals("")) {
				return;
			}
			setReplacementString("\"" + color + "\"");
			super.apply(document);
		}
	}
}
