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
package org.eclipse.e4.xwt.tools.ui.palette.tools;

import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.CreationTool;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class PaletteCreationTool extends CreationTool {
	@Override
	protected Request createTargetRequest() {
		CreateRequest request = new PaletteCreateRequest();
		request.setFactory(getFactory());
		return request;
	}
}
