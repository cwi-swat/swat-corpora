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
package org.eclipse.e4.xwt.tools.ui.designer.policies;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.RowLayoutEditPolicy;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author yahong.song@soyatec.com
 */
public class ExpandBarEditPolicy extends RowLayoutEditPolicy {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.policies.layout.RowLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart, org.eclipse.gef.EditPart)
	 */
	protected Command createAddCommand(EditPart child, EditPart after) {
		return null;
	}

	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return super.createMoveChildCommand(child, after);
	}

	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NewResizableEditPolicy(PositionConstants.NONE, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.policies.layout.RowLayoutEditPolicy #getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		return super.getCreateCommand(request);
	}

	protected boolean isHorizontal() {
		return false;
	}
}
