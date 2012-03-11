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
package org.eclipse.e4.xwt.vex.palette.part;

import org.eclipse.e4.xwt.vex.PalletteSelectionTransfer;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.dnd.DropTargetEvent;

public class ToolTransferDropTargetListener extends AbstractTransferDropTargetListener {

	public ToolTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer, PalletteSelectionTransfer.getInstance());
		setEnablementDeterminedByCommand(false);
	}

	@Override
	protected void updateTargetRequest() {

	}

	@Override
	public boolean isEnabled(DropTargetEvent event) {
		System.out.println(event.toString());
		return true;
		// return super.isEnabled(event);
	}

}
