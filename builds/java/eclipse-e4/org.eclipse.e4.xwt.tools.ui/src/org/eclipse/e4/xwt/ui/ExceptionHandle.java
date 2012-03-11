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
package org.eclipse.e4.xwt.ui;

import org.eclipse.jface.dialogs.MessageDialog;

public class ExceptionHandle {

	static public void handle(Exception e, String message) {
		if (e.getMessage() != null) {
			message += "\n" + e.getMessage();
		}
		MessageDialog.openError(XWTUIPlugin.getShell(), "Erreur: ", message);
	}
}
