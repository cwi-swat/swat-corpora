/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Brian de Alwis - port to Eclipse 4
 ******************************************************************************/

package org.eclipse.e4.ui.workbench.renderers.swt.cocoa;

import javax.inject.Named;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.internal.cocoa.NSWindow;
import org.eclipse.swt.widgets.Shell;

/**
 * @since 4.1
 */
public abstract class AbstractWindowHandler {
	@CanExecute
	public boolean canExecute(
			@Optional @Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		if (shell == null) {
			return false;
		}
		NSWindow window = shell.view.window();
		if (window == null) {
			return false;
		}
		return !window.isMiniaturized();
	}
}
