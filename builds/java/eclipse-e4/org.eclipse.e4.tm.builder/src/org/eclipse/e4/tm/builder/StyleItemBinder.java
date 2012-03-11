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
package org.eclipse.e4.tm.builder;

import org.eclipse.e4.tm.styles.StyleItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class StyleItemBinder extends AbstractBinder {

	protected Object create(EObject eObject) {
		StyleItem styleItem = (StyleItem)eObject; 
		Class<?> c = getToolkitClass(styleItem, true);
		Object value = null; // context.getObject(styleItem, c);
		try {
			value = context.convert(styleItem.getSource(), c);
			context.putObject(styleItem, value);
		} catch (Exception e) {
		}
		if (value != null) {
			return value;
		}
		return super.create(eObject);
	}

	protected boolean shouldntHandleFeature(EStructuralFeature feature) {
		return true;
	}
}
