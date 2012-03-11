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
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class TabBinder extends ControlBinder implements IBinder {

	protected Composite getSwtComposite(EObject control) {
		Composite composite = super.getParent(control, Composite.class);
		if (! (composite instanceof TabFolder)) {
			throw new IllegalStateException("Parent of Tab must be a TabFolder, but was " + composite);
		}
		return null;
	}

	public Object adapt(Object value, Class c) {
		if (value instanceof TabItem){
			Control control = ((TabItem)value).getControl();
			if (c.isInstance(control)) {
				return control;
			}
		} else if (value instanceof Composite && c.isAssignableFrom(TabItem.class)) {
			return getTabItem((Composite)value);
		}
		return super.adapt(value, c);
	}

	protected Exception copyFeatureValue2Property(EObject eObject, Object value, EStructuralFeature feature, Object object, boolean isInit) {
		EClass owner = feature.getEContainingClass();
		if (WidgetsPackage.eINSTANCE.getControl().isSuperTypeOf(owner) && (! (object instanceof Control))) {
			object = adapt(object, Control.class);
		}
		return super.copyFeatureValue2Property(eObject, value, feature, object, isInit);
	}

	private TabItem getTabItem(Composite tab) {
		TabFolder tabFolder = (TabFolder)tab.getParent();
		TabItem[] tabItems = tabFolder.getItems();
		for (int i = 0; i < tabItems.length; i++) {
			if (tabItems[i].getControl() == tab) {
				return tabItems[i];
			}
		}
		return null;
	}

	protected Object create(EObject control) {
		TabFolder tabFolder = super.getParent(control, TabFolder.class);
		Composite tab = new Composite(tabFolder, SWT.NONE);
		int swtStyle = getSwtStyle(control);
		TabItem tabItem = new TabItem(tabFolder, swtStyle);
		tabItem.setControl(tab);
		return tabItem;
	}
}
