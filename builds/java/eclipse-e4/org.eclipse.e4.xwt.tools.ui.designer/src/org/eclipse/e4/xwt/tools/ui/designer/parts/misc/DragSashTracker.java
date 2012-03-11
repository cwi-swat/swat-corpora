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
package org.eclipse.e4.xwt.tools.ui.designer.parts.misc;

import org.eclipse.e4.xwt.tools.ui.designer.commands.ChangeWeightsCommand;
import org.eclipse.e4.xwt.tools.ui.designer.parts.SashEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.SashFormEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.graphics.Cursor;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class DragSashTracker extends DragEditPartsTracker {

	public DragSashTracker(SashEditPart sourceEditPart) {
		super(sourceEditPart);
	}

	protected Command getCommand() {
		ChangeBoundsRequest targetRequest = (ChangeBoundsRequest) getTargetRequest();
		EditPart parent = getSourceEditPart().getParent();
		if (parent instanceof SashFormEditPart) {
			return new ChangeWeightsCommand((SashFormEditPart) parent,
					targetRequest);
		}
		return super.getCommand();
	}

	protected Cursor getDefaultCursor() {
		SashEditPart editPart = (SashEditPart) getSourceEditPart();
		return editPart.getDefaultCursor();
	}
}
