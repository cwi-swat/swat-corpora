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

import org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline.TreeItemEditPart;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class TreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof XamlDocument) {
			XamlElement rootElement = ((XamlDocument) model).getRootElement();
			return createEditPart(context, rootElement);
		}
		return new TreeEditPart(model);
	}

	private class TreeEditPart extends TreeItemEditPart {

		public TreeEditPart(Object model) {
			super(model, new OutlineContentProvider(),
					new OutlineLabelProvider());
		}

		protected void createEditPolicies() {
			super.createEditPolicies();
		}
	}
}
