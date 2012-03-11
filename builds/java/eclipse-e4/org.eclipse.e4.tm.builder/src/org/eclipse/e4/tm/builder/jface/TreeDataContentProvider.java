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

import org.eclipse.e4.tm.util.TreeData;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class TreeDataContentProvider extends ListDataContentProvider implements ITreeContentProvider {

	public TreeDataContentProvider(TreeData treeData) {
		super(treeData);
	}

	protected void disposeListData() {
		((TreeData)listData).setParentDataObject(null);
		super.disposeListData();
		listData = null;
	}

	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object getParent(Object element) {
		prepareData(element);
		return ((TreeData)listData).getParentDataObject();
	}

	public boolean hasChildren(Object element) {
		prepareData(element);
		return (! ((TreeData)listData).isLeaf());
	}
}
