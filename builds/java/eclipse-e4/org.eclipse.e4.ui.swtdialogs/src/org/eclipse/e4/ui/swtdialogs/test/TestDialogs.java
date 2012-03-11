/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.ui.swtdialogs.test;

import org.eclipse.e4.ui.swtdialogs.Dialogs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This isn't a proper unit test since there's no way to drive the standard
 * platform common dialogs from straight SWT and we didn't feel like
 * implementing a robot just for these tests.
 * <p>
 * To run, simply execute this class as a Java application.
 */
public class TestDialogs {
	public static void main(String[] args) {
		showDialogs();	// Creates an invisible shell to display each dialog
		
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.open();
		shell.setLayout(new GridLayout());
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Show dialogs");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Dialogs will be centered on top of application shell
				showDialogs();
			}
		});
		shell.layout();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private static void showDialogs() {
		Dialogs.errorDialogMessage("Big Bad errur!");
		Dialogs.infoDialogMessage("Stuff");
		Dialogs.warningDialogMessage("Look out");
	}
}
