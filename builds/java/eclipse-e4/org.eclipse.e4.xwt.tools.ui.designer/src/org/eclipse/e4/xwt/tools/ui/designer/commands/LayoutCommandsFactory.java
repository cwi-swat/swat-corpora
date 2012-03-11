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

import java.util.Collections;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ForwardedRequest;

/**
 * @author jliu jin.liu@soyatec.com
 */
public abstract class LayoutCommandsFactory {
	private EditPart host;

	public LayoutCommandsFactory(EditPart host) {
		this.host = host;
	}

	public EditPart getHost() {
		return host;
	}

	public XamlNode getModel() {
		return (XamlNode) host.getModel();
	}

	public Command getDeleteCommand(Request request) {
		if (!(request instanceof ForwardedRequest)) {
			return null;
		}
		EditPart sender = ((ForwardedRequest) request).getSender();
		Object model = sender.getModel();
		if (isRoot(model)) {
			return null;
		}
		return getDeleteCommand(Collections.singletonList(sender));
	}

	private boolean isRoot(Object model) {
		return model != null && model instanceof XamlElement && ((XamlElement) model).eContainer() instanceof XamlDocument;
	}

	public Command getDeleteCommand(List deleteObjects) {
		if (deleteObjects == null || deleteObjects.isEmpty()) {
			return null;
		}
		return new DeleteCommand(deleteObjects);
	}

}
