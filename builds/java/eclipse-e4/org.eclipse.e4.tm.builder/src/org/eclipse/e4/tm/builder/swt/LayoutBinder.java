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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

public class LayoutBinder extends SwtBinder implements IBinder {

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		super.invalidateFeature(eObject, feature, object, isEvent);
		if (LayoutsPackage.eINSTANCE.getLayout().isSuperTypeOf(feature.getEContainingClass())) {
			Composite composite = getParent(eObject, Composite.class);
			if (composite != null) {
				composite.layout();
			}
		}
	}
}
