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
package org.eclipse.e4.xwt.tools.ui.designer.core.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;

/**
 * @author bo.zhou
 */
public class DisplayUtil {
	public static void asyncExec(Runnable runnable) {
		asyncExec(getDisplay(), runnable);
	}

	public static void asyncExec(Display display, Runnable runnable) {
		check(display).asyncExec(runnable);
	}

	private static Display check(Display display) {
		if (display == null) {
			display = getDisplay();
		}
		if (display == null) {
			throw new SWTException(SWT.ERROR_INVALID_ARGUMENT, "Invalid thread access");
		}
		return display;
	}

	public static void syncExec(Runnable runnable) {
		if (runnable != null) {
			getDisplay().syncExec(runnable);
		}
	}

	public static void syncExec(Display display, Runnable runnable) {
		check(display).syncExec(runnable);
	}

	public static void timerExec(int milliseconds, Runnable runnable) {
		if (runnable != null) {
			getDisplay().timerExec(milliseconds, runnable);
		}
	}

	public static void timerExec(Display display, int milliseconds, Runnable runnable) {
		check(display).timerExec(milliseconds, runnable);
	}

	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		if (display == null) {
			display = new Display();
		}
		return display;
	}
}
