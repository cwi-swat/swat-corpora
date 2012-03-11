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

import java.util.ArrayList;

import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.Palette;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * entry content provider class (tree structure)
 * 
 * 
 * @author jliu (jin.liu@soyatec.com)
 */
public class EntryContentProvider implements IPaletteContentProvider {

	static Object[] EMPTY = new Object[0];

	public Object[] getChildren(Object parent) {
		EList<EObject> elements = null;
		if (parent instanceof EObject) {
			EObject object = (EObject) parent;
			elements = object.eContents();
		} else if (parent instanceof Resource) {
			Resource resource = (Resource) parent;
			EList<EObject> contents = resource.getContents();
			if (contents.size() > 0) {
				Object element = contents.get(0);
				if (element instanceof Palette) {
					Palette palette = (Palette) element;
					elements = palette.eContents();
				}
			}
		}

		if (elements != null) {
			ArrayList<EObject> arrayList = new ArrayList<EObject>();
			for (EObject object : elements) {
				if (object instanceof Entry) {
					arrayList.add(object);
				}
			}
			return arrayList.toArray();
		}
		return EMPTY;
	}

}
