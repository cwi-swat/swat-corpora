/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.languages.internal.javascript.debug.ui.SWTFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * Property page for line breakpoints
 * 
 * @since 0.9
 */
public class LineBreakpointPropertyPage extends BreakpointPropertyPage {
	
	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#createTypeSpecificLabels(org.eclipse.swt.widgets.Composite)
	 */
	protected void createTypeSpecificLabels(Composite parent) {
		StringBuffer lineNumber = new StringBuffer(4);
		try {
			int lNumber = getBreakpoint().getLineNumber();
			if (lNumber > 0) {
				lineNumber.append(lNumber);
			}
		} catch (CoreException ce) {
			//TODO log this
		}
		if (lineNumber.length() > 0) {
			SWTFactory.createLabel(parent, "&Line Number:", 1); 
			Text text = SWTFactory.createText(parent, SWT.READ_ONLY | SWT.SINGLE, 1);
			text.setText(lineNumber.toString());
			text.setBackground(parent.getBackground());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#createTypeSpecificEditors(org.eclipse.swt.widgets.Composite)
	 */
	protected void createTypeSpecificEditors(Composite parent) throws CoreException {
		setTitle("Line Breakpoint");
		createConditionEditor(parent);
	}
}
