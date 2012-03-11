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
import org.eclipse.gef.requests.CreateConnectionRequest;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BindingCreateCommand extends Command {

	private CreateConnectionRequest request;

	public BindingCreateCommand(CreateConnectionRequest request) {
		setLabel("Create Binding");
		this.request = request;
	}

	public void execute() {
		// TODO:
		System.out.println(request);
	}
}
