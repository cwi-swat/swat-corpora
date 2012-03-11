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

import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DefaultCreateCommand extends AbstractCreateCommand {

	/**
	 * @param parent
	 * @param createRequest
	 */
	public DefaultCreateCommand(EditPart parent, CreateRequest createRequest) {
		super(parent, createRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.commands.AbstractCreateCommand#preExecute(org.soyatec.tools.designer.xaml.XamlNode, org.eclipse.gef.requests.CreateRequest)
	 */
	protected void preExecute(XamlNode newNode, CreateRequest createRequest) {
		// Do nothing here.
	}

}
