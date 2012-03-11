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
package org.eclipse.e4.xwt.ui.workbench.views;

import org.eclipse.e4.xwt.ui.workbench.IStaticPart;

/**
 * 
 * @author yyang (yves.yang@soyatec.com)
 */
public abstract class XWTPartSwitcher extends XWTStaticPart {

	protected void refresh() {
		IStaticPart switcher = getCurrentPart();
		switchPart(switcher);
	}

	protected abstract IStaticPart getCurrentPart();

	public void switchPart(IStaticPart part) {
		refresh(part.getURL(), part.getDataContext(), part.getClassLoader());
	}
}
