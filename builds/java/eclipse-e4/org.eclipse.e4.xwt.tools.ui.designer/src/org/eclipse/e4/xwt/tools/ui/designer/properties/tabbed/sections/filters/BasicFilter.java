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
package org.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.filters;

import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class BasicFilter implements IFilter {

	public boolean select(Object toTest) {
		XamlNode node = null;
		if (toTest instanceof XamlNode) {
			node = (XamlNode) toTest;
		} else if (toTest instanceof EditPart) {
			Object model = ((EditPart) toTest).getModel();
			return select(model);
		}
		return select(node);
	}

	protected boolean select(XamlNode node) {
		return node != null;
	}

}
