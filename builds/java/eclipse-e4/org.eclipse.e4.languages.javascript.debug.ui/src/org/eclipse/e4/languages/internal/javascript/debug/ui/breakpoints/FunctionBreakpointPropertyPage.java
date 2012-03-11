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
import org.eclipse.e4.languages.javascript.debug.model.JSDIFunctionBreakpoint;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


/**
 * Property page for function breakpoints
 * 
 * @since 0.9
 */
public class FunctionBreakpointPropertyPage extends BreakpointPropertyPage {

	private Button entry = null, exit = null; 

	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#createTypeSpecificEditors(org.eclipse.swt.widgets.Composite)
	 */
	protected void createTypeSpecificEditors(Composite parent) throws CoreException {
		setTitle("Function Breakpoint");
		createConditionEditor(parent);
		this.entry = SWTFactory.createCheckButton(parent, 
				"Suspend when execution e&nters the function.", 
				null, 
				getFunctionBreakpoint().isEntry(), 
				1);
		this.exit = SWTFactory.createCheckButton(parent, 
				"Suspend when execution e&xits the function.", 
				null, 
				getFunctionBreakpoint().isExit(), 
				1);
	}

	/**
	 * @return the {@link JSDIFunctionBreakpoint} this page is opened on
	 */
	JSDIFunctionBreakpoint getFunctionBreakpoint() {
		return (JSDIFunctionBreakpoint) getBreakpoint();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints.BreakpointPropertyPage#doStore()
	 */
	protected void doStore() throws CoreException {
		JSDIFunctionBreakpoint breakpoint = getFunctionBreakpoint();
		breakpoint.setEntry(this.entry.getSelection());
		breakpoint.setExit(this.exit.getSelection());
		super.doStore();
	}
}
