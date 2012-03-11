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
package org.eclipse.e4.xwt.tools.ui.designer.commands;

import org.eclipse.gef.commands.Command;

/**
 * This is a No Operation command. There are times when a command must be returned and none being returned is considered an error. This can be used in those situations where nothing is to be be done.
 * 
 * Use the INSTANCE static field for all usages. There is no need for more than one.
 */

public class NoOpCommand extends Command {

	public static final NoOpCommand INSTANCE = new NoOpCommand();

	private NoOpCommand() {
	}

	public boolean canExecute() {
		return true;
	}

	public void execute() {
	}

	public void redo() {
	}

	public boolean canUndo() {
		return true;
	}

	public void undo() {
	}

}
