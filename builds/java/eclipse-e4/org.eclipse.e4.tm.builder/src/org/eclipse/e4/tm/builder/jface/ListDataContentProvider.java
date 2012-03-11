/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder.jface;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.e4.tm.util.ListData;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ListDataContentProvider implements IStructuredContentProvider {

	protected ListData listData;
	
	public ListDataContentProvider(ListData listData) {
		this.listData = listData;
	}

	public void dispose() {
		if (listData != null) {
			disposeListData();
		}
	}

	protected void disposeListData() {
		listData.getDataObjects().clear();
		listData = null;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getElements(Object inputElement) {
		// ensure there's no left-overs from previous usage
		listData.getDataObjects().clear();
		// handle some default cases
		if (inputElement == null) {
		} else if (inputElement.getClass().isArray()) {
			addAll(Arrays.asList((Object[])inputElement));
		} else if (inputElement instanceof Collection<?>) {
			addAll((Collection<?>)inputElement);
		}
		prepareData(inputElement);
		EList<Object> dataObjects = listData.getDataObjects();
		return dataObjects.toArray(new Object[dataObjects.size()]);
	}

	private void addAll(Collection<?> col) {
		listData.getDataObjects().addAll(col);
	}

	protected void prepareData(Object inputElement) {
		listData.setDataObject(inputElement);
	}
}
