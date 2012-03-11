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
package org.eclipse.e4.xwt.vex.palette;

import java.util.ArrayList;

import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPalette;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author yyang
 * 
 */
public class EntryContentProvider implements ITreeContentProvider {
	static Object[] EMPTY = new Object[0];

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		EList<EObject> elements = null;
		if (inputElement instanceof EObject) {
			EObject object = (EObject) inputElement;
			elements = object.eContents();
		} else if (inputElement instanceof Resource) {
			Resource resource = (Resource) inputElement;
			EList<EObject> contents = resource.getContents();
			if (contents.size() > 0) {
				Object element = contents.get(0);
				if (element instanceof ToolPalette) {
					ToolPalette toolPalette = (ToolPalette) element;
					elements = toolPalette.eContents();
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

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return getElements(element).length > 0;
	}
}
