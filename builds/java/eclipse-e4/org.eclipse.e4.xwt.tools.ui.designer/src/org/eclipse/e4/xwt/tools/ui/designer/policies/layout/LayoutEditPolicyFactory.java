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
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class LayoutEditPolicyFactory {

	public static LayoutEditPolicy NULL_LAYOUT = new NullLayoutEditPolicy();

	public static ILayoutEditPolicy getLayoutEditPolicy(CompositeEditPart editPart) {
		LayoutType type = LayoutsHelper.getLayoutType(editPart);
		switch (type) {
		case GridLayout:
			return new GridLayoutEditPolicy();
		case FillLayout:
			return new FillLayoutEditPolicy();
		case RowLayout:
			return new RowLayoutEditPolicy();
		case FormLayout:
			return new FormLayoutEditPolicy();
		case StackLayout:
		default:
			return new NullLayoutEditPolicy();
		}
	}
}
