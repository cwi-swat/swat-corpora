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
package org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.layout.FormData;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FormLayoutData {

	public FormData data;
	public Rectangle bounds;

	public FormLayoutData(FormData data, Rectangle bounds) {
		this.data = data;
		this.bounds = bounds;
	}

	public FormData getData() {
		return data;
	}

	public Rectangle getBounds() {
		return bounds;
	}

}
