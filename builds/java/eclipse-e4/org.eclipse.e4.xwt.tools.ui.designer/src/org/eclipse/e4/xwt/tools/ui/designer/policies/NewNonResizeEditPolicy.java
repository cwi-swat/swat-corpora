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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.core.parts.tools.SelectionHandle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.tools.SelectEditPartTracker;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class NewNonResizeEditPolicy extends NonResizableEditPolicy {

	private boolean displayNonHandles;

	public NewNonResizeEditPolicy(boolean displayNonHandles) {
		this.displayNonHandles = displayNonHandles;
		setDragAllowed(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#createSelectionHandles()
	 */
	protected List<Handle> createSelectionHandles() {
		List<Handle> list = new ArrayList<Handle>();
		GraphicalEditPart host = (GraphicalEditPart) getHost();
		if (displayNonHandles) {
			if (isDragAllowed()) {
				NonResizableHandleKit.addHandles(host, list);
			} else {
				NonResizableHandleKit.addHandles(host, list, new SelectEditPartTracker(getHost()), SharedCursors.ARROW);
			}
		} else {
			if (isDragAllowed()) {
				NonResizableHandleKit.addMoveHandle(host, list);
			}
			list.add(createSelectionHandle(host));
		}
		return list;
	}

	protected AbstractHandle createSelectionHandle(GraphicalEditPart host) {
		return new SelectionHandle(host);
	}

}
