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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline;

import org.eclipse.gef.Request;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.TreeContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class TreeItemEditPolicy extends TreeContainerEditPolicy {

	public Command getCommand(Request req) {
		if (req.getType() == REQ_ORPHAN_CHILDREN) {
			return getOrphanChildrenCommand((GroupRequest) req);
		} else if (req.getType() == REQ_DELETE) {
			return getDeleteCommand(req);
		} else if (req.getType() == REQ_MOVE) {
			ChangeBoundsRequest request = new ChangeBoundsRequest(
					REQ_MOVE_CHILDREN);
			request.setEditParts(getHost());
			if (req instanceof ChangeBoundsRequest) {
				request.setLocation(((ChangeBoundsRequest) req).getLocation());
			}
			return getHost().getParent().getCommand(request);
		}
		return super.getCommand(req);
	}

	protected Command getDeleteCommand(Request req) {
		return null;
	}

	protected Command getOrphanChildrenCommand(GroupRequest req) {
		return null;
	}

	protected Command getAddCommand(ChangeBoundsRequest request) {
		return null;
	}

	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	protected Command getMoveChildrenCommand(ChangeBoundsRequest request) {
		return null;
	}

	public void showTargetFeedback(Request req) {
		super.showTargetFeedback(req);
		if (req.getType().equals(REQ_MOVE) || req.getType().equals(REQ_ADD)
				|| req.getType().equals(REQ_CREATE)) {
			expandTreeItem((DropRequest) req);
		}
	}

	private void expandTreeItem(DropRequest req) {
		Widget hostWidget = ((TreeEditPart) getHost()).getWidget();
		if (hostWidget == null || hostWidget.isDisposed()
				|| !(hostWidget instanceof TreeItem)) {
			return;
		}
		TreeItem treeItem = (TreeItem) hostWidget;
		treeItem.setExpanded(true);

	}
}
