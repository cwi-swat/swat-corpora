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

import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class TextSectionFilter extends BasicFilter {

	protected boolean select(XamlNode node) {
		if (super.select(node)){
			return XWTUtility.getProperty(node, "text") != null;
		}
		return super.select(node);
	}

}
