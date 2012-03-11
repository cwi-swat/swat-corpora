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
package org.eclipse.e4.xwt.tools.ui.designer.dialogs;

import org.eclipse.e4.xwt.tools.ui.designer.editor.event.EventHandler;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewHandlerDialog extends TitleAreaDialog {
	private EventHandler eventHandler;
	private String handlerName;

	public NewHandlerDialog(EventHandler eventHandler, String initialValue) {
		super(new Shell());
		this.eventHandler = eventHandler;
		this.handlerName = initialValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Handler Dialog");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite control = new Composite(composite, SWT.NONE);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		control.setLayout(new GridLayout(2, false));
		Label label = new Label(control, SWT.NONE);
		label.setText("Handler:");

		final Text text = new Text(control, SWT.BORDER);
		text.setText(this.handlerName);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handlerName = text.getText();
				if (handlerName == null) {
					setErrorMessage("Handler name is null.");
				} else if (eventHandler.exist(handlerName)) {
					setErrorMessage("Handler existing for current xwt presentation.");
				} else {
					setErrorMessage(null);
				}
			}
		});
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		setTitle("New Event Handler");
		setMessage("Create a new event handler for current XWT presentation.");
		return composite;
	}

	public String getHandlerName() {
		return handlerName;
	}

}
