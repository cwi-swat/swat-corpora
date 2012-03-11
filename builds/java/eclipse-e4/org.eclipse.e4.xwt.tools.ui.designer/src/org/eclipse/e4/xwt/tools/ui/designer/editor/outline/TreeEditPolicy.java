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
package org.eclipse.e4.xwt.tools.ui.designer.editor.outline;

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline.TreeItemEditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class TreeEditPolicy extends TreeItemEditPolicy {

	protected Command getDeleteCommand(Request req) {
		return super.getDeleteCommand(req);
	}

	protected Command getAddCommand(ChangeBoundsRequest request) {
		return super.getAddCommand(request);
	}

	protected Command getCreateCommand(CreateRequest request) {
		return super.getCreateCommand(request);
	}

	protected Command getOrphanChildrenCommand(GroupRequest req) {
		return super.getOrphanChildrenCommand(req);
	}

	protected Command getMoveChildrenCommand(ChangeBoundsRequest request) {
		return super.getMoveChildrenCommand(request);
	}
}
