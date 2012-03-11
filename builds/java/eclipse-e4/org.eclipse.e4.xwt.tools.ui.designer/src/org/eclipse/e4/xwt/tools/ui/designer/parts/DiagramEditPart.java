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
package org.eclipse.e4.xwt.tools.ui.designer.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.core.parts.AbstractDiagramEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.LayoutEditPolicyFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.gef.EditPolicy;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class DiagramEditPart extends AbstractDiagramEditPart {

	public DiagramEditPart(XamlDocument document) {
		super(document);
	}

	/**
	 * @see org.AbstractDiagramEditPart.xaml.ve.editor.editparts.DiagramGraphicalEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		removeEditPolicy(EditPolicy.LAYOUT_ROLE);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, LayoutEditPolicyFactory.NULL_LAYOUT);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		XamlDocument model = (XamlDocument) getModel();
		if (model.getRootElement() != null) {
			return Collections.singletonList(model.getRootElement());
		}
		return super.getModelChildren();
	}

	public String toString() {
		return "";
	}
}
