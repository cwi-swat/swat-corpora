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
import org.eclipse.e4.tm.widgets.ListViewer;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class ListViewerBinder extends ControlBinder implements IBinder {

	protected boolean shouldntHandleFeature(EStructuralFeature feature) {
		if (feature == WidgetsPackage.eINSTANCE.getListViewer_ViewProvider()) {
			return true;
		}
		return super.shouldntHandleFeature(feature);
	}

	protected void update(EObject control, EStructuralFeature feature, Object object, boolean isInit) {
		if (feature == WidgetsPackage.eINSTANCE.getListViewer_ContentProvider() && (! (object instanceof Viewer))) {
			update(control, feature, adapt(object, Viewer.class), isInit);
		} else {
			super.update(control, feature, object, isInit);
		}
	}

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		if (eObject instanceof ListViewer && feature == UtilPackage.eINSTANCE.getObjectData_DataObject()) {
			updateViewerContent((ListViewer)eObject, context.getObject(eObject, Viewer.class));
		}
		super.invalidateFeature(eObject, feature, object, isEvent);
	}

	private void updateViewerContent(ListViewer listViewer, Viewer viewer) {
		viewer.setInput(listViewer.getDataObject());
//		viewer.getControl().getParent().layout();
	}

	protected Object create(EObject control) {
		Composite parent = super.getParent(control, Composite.class);
		org.eclipse.jface.viewers.ListViewer viewer = new org.eclipse.jface.viewers.ListViewer(parent, SWT.BORDER);
		viewer.setLabelProvider(new LabeledLabelProvider((Labeled)((ListViewer)control).getViewProvider()));
		List list = viewer.getList();
		list.setData(Viewer.class.getName(), viewer);
		return list;
	}
}
