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
package org.eclipse.e4.xwt.tools.ui.palette.page.resources;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public interface IPaletteLabelProvider {

	ImageDescriptor getSmallIcon(Object obj);

	ImageDescriptor getLargeIcon(Object obj);

	String getToolTip(Object obj);

	String getName(Object obj);

	String getContent(Object obj);
}
