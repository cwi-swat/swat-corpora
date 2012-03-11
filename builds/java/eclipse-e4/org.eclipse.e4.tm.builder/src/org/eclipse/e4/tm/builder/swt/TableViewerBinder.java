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
import org.eclipse.e4.tm.builder.jface.LabeledTableLabelProvider;
import org.eclipse.e4.tm.util.Labeled;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.e4.tm.widgets.TableViewer;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableViewerBinder extends ControlBinder implements IBinder {

	protected boolean shouldntHandleFeature(EStructuralFeature feature) {
		if (feature == WidgetsPackage.eINSTANCE.getTableViewer_ViewProviders()) {
			return true;
		} 
		return super.shouldntHandleFeature(feature);
	}

	protected void update(EObject control, EStructuralFeature feature, Object object, boolean isInit) {
		if (feature == WidgetsPackage.eINSTANCE.getTableViewer_ContentProvider() && (! (object instanceof Viewer))) {
			update(control, feature, adapt(object, Viewer.class), isInit);
		} else {
			super.update(control, feature, object, isInit);
		}
	}

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		if (eObject instanceof TableViewer && feature == UtilPackage.eINSTANCE.getObjectData_DataObject()) {
			updateViewerContent((TableViewer)eObject, context.getObject(eObject, Viewer.class));
		}
		super.invalidateFeature(eObject, feature, object, isEvent);
	}

	private void updateViewerContent(TableViewer tableViewer, Viewer viewer) {
		viewer.setInput(tableViewer.getDataObject());
		Table table = ((org.eclipse.jface.viewers.TableViewer)viewer).getTable();
		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			table.getColumn(i).pack();
		}
	}

	protected Object create(EObject control) {
		Composite parent = super.getParent(control, Composite.class);
		org.eclipse.jface.viewers.TableViewer viewer = new org.eclipse.jface.viewers.TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		LabeledTableLabelProvider labelProvider = new LabeledTableLabelProvider();
		boolean headerVisible = true;
		int columnCount = ((TableViewer)control).getViewProviders().size(), columnNum = 0;
		for (Control viewProvider: ((TableViewer)control).getViewProviders()) {
			LabeledLabelProvider columnLabelProvider = new LabeledLabelProvider((Labeled)viewProvider);
			labelProvider.addLabelProvider(columnLabelProvider);
			TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
			TableColumn tableColumn = column.getColumn();
			String headerText = null;
			try {
				headerText = columnLabelProvider.getText(null);
			} catch (Exception e) {
			}
			tableColumn.setText(String.valueOf(++columnNum));
			if (headerText != null) {
				//				tableColumn.setText(headerText);
			} else {
				headerVisible = false;
			}
			tableColumn.setResizable(true);
			tableColumn.setMoveable(true);
		}
		viewer.setLabelProvider(labelProvider);
		Table table = viewer.getTable();
		table.setHeaderVisible(headerVisible);
		table.setLinesVisible(true);
		table.setData(Viewer.class.getName(), viewer);
		return table;
	}
}
