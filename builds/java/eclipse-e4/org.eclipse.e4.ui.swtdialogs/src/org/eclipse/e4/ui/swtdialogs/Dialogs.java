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
package org.eclipse.e4.ui.swtdialogs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Displays the standard message and error dialogs more simply than doing it
 * by hand.
 */
public class Dialogs {
	private static Shell tempShell;

	/**
	 * Helper method for a standard ERROR MessageDialog
	 * 
	 * @param message
	 *            Error message to display to user
	 */
	public static void errorDialogMessage(final String message) {
		runOnDisplayThread(new Runnable() {
			public void run() {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", message);
			}
		});
	}

	/**
	 * Helper method for a standard WARNING MessageDialog
	 * 
	 * @param message
	 *            Warning message to display to user
	 */
	public static void warningDialogMessage(final String message) {
		runOnDisplayThread(new Runnable() {
			public void run() {
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Warning", message);
			}
		});
	}

	/**
	 * Helper method to show a standard INFO MessageDialog
	 * 
	 * @param message
	 *            Message to display to user
	 */
	public static void infoDialogMessage(final String message) {
		runOnDisplayThread(new Runnable() {
			public void run() {
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Info", message);
			}
		});
	}

	/**
	 * Ensure the specified Runnable runs on the Display thread.
	 * <p>
	 * If the Display or a default Shell does not exist, they are created. If a
	 * default Shell has to be created, it will automatically be disposed after
	 * the Runnable runs. If we are not already running on the Display thread
	 * (for any reason), the Runnable will be asyncExec'd on the Display and
	 * will run at the first reasonable time, but *after* this method
	 * terminates. If we are already running on the Display thread, the Runnable
	 * will be executed directly/synchronously.
	 * 
	 * @param runnable
	 *            the Runnable to execute on the Display thread.
	 */
	public static void runOnDisplayThread(final Runnable runnable) {
		Display display = getDisplay();
		
		if (display.getThread() == Thread.currentThread()) {
			//getActiveShell ensures that a shell exists, we don't actually care what it is 
			//but it just needs to be there. If you want the active shell (after calling getActiveShell() 
			//do it using standard SWT (i.e Display.getCurrent().getActiveShell() )			
			getActiveShell();
			runnable.run();
			disposeTempShell();
		} else {
			Runnable wrapped = new Runnable() {
				public void run() {
					getActiveShell();
					runnable.run();
					disposeTempShell();					
				}
			};
			
			display.asyncExec(wrapped);
		}
	}

	private static Shell getActiveShell() {
		tempShell=null;
		Display display = getDisplay();
		Shell shell = display.getActiveShell();
		if (shell != null) {
			return shell;
		}
		tempShell = new Shell(display);
		return tempShell;
	}

	private static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}
	
	protected static void disposeTempShell() {
		if (tempShell != null) {
			tempShell.dispose();
			tempShell = null;
		}
	}
}
