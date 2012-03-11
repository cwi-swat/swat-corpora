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
package org.eclipse.e4.xwt.tools.ui.designer.policies.layout;

import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.NewNonResizeEditPolicy;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class FillLayoutEditPolicy extends RowLayoutEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.xwt.editpolicies.RowLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		NewNonResizeEditPolicy childEditPolicy = new NewNonResizeEditPolicy(false);
		childEditPolicy.setDragAllowed(true);
		return childEditPolicy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.designer.policies.RowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		EditPart host = getHost();
		if (host != null && host instanceof CompositeEditPart) {
			Layout layout = ((CompositeEditPart) host).getLayout();
			return layout != null && layout instanceof FillLayout && ((FillLayout) layout).type == SWT.HORIZONTAL;
		}
		return super.isHorizontal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.policies.layout.RowLayoutEditPolicy#getType()
	 */
	public LayoutType getType() {
		return LayoutType.FillLayout;
	}
}
