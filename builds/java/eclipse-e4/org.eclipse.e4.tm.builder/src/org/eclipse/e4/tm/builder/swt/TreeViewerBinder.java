/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder.swt;

import org.eclipse.e4.tm.builder.IBinder;
import org.eclipse.e4.tm.builder.jface.LabeledLabelProvider;
import org.eclipse.e4.tm.util.Labeled;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.e4.tm.widgets.TreeViewer;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class TreeViewerBinder extends ControlBinder implements IBinder {

	protected boolean shouldntHandleFeature(EStructuralFeature feature) {
		if (feature == WidgetsPackage.eINSTANCE.getTreeViewer_ViewProvider()) {
			return true;
		}
		return super.shouldntHandleFeature(feature);
	}

	protected void update(EObject control, EStructuralFeature feature, Object object, boolean isInit) {
		if (feature == WidgetsPackage.eINSTANCE.getTreeViewer_ContentProvider() && (! (object instanceof Viewer))) {
			update(control, feature, adapt(object, Viewer.class), isInit);
		} else {
			super.update(control, feature, object, isInit);
		}
	}

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		if (eObject instanceof TreeViewer && feature == UtilPackage.eINSTANCE.getObjectData_DataObject()) {
			updateViewerContent((TreeViewer)eObject, context.getObject(eObject, Viewer.class));
		}
		super.invalidateFeature(eObject, feature, object, isEvent);
	}

	private void updateViewerContent(TreeViewer treeViewer, Viewer viewer) {
		viewer.setInput(treeViewer.getDataObject());
	}

	protected Object create(EObject control) {
		Composite parent = super.getParent(control, Composite.class);
		org.eclipse.jface.viewers.TreeViewer viewer = new org.eclipse.jface.viewers.TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setLabelProvider(new LabeledLabelProvider((Labeled)((TreeViewer)control).getViewProvider()));
		Tree tree = viewer.getTree();
		tree.setData(Viewer.class.getName(), viewer);
		return tree;
	}
}
