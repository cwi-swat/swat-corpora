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
import org.eclipse.e4.tm.layouts.LayoutsPackage;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Control;

public class LayoutDataBinder extends SwtBinder implements IBinder {

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		super.invalidateFeature(eObject, feature, object, isEvent);
		EObject parent = eObject;
		while (parent != null) {
			if (WidgetsPackage.eINSTANCE.getControl().isInstance(parent)) {
				return;
			} else if (LayoutsPackage.eINSTANCE.getLayoutData().isInstance(parent)) {
				Control control = getParent(parent, Control.class);
				if (control != null) {
					control.getParent().layout();
				}
				return;
			}
			parent = parent.eContainer();
		}
	}
}
