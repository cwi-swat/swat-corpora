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
package org.eclipse.e4.xwt.ui.utils;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class DisplayUtil {

	public static void open(IFile file) {
		if (file != null) {
			IJavaProject javaProject = JavaCore.create(file.getProject());
			if (!javaProject.exists()) {
				return;
			}
			URI uri = file.getLocationURI();
			try {
				XWT.setLoadingContext(ProjectContext.getContext(javaProject));
				Object widget = XWT.load(uri.toURL());
				if (!(widget instanceof Control)) {
					throw new XWTException("Root element must be a control.");
				}
				Shell shell = ((Control)widget).getShell();
				shell.pack();
				shell.open();
				while (!shell.isDisposed())
					if (!shell.getDisplay().readAndDispatch())
						shell.getDisplay().sleep();
			} catch (Exception e) {
				e.printStackTrace();
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				}
				throw new RuntimeException(e);
			}
		}
	}
}
