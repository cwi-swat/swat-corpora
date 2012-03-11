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
package org.eclipse.e4.xwt.tools.ui.designer.parts.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ControlEditPart;

public class CompositeEditPartHelper {

	/**
	 * Remove popup menus, just keep the ControlEditParts
	 * 
	 * @param compositeEditPart
	 * @return
	 */
	public static List<ControlEditPart> getChildren(
			CompositeEditPart compositeEditPart) {
		List<ControlEditPart> collector = new ArrayList<ControlEditPart>();
		for (Iterator<?> iterator = compositeEditPart.getChildren().iterator(); iterator
				.hasNext();) {
			Object element = iterator.next();
			if (element instanceof ControlEditPart) {
				collector.add((ControlEditPart) element);
			}
		}
		return collector;
	}
}
