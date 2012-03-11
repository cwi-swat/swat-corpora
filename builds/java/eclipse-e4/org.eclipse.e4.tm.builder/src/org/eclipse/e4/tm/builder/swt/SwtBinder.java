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

import org.eclipse.e4.tm.builder.AbstractBinder;
import org.eclipse.e4.tm.builder.AbstractBuilder;
import org.eclipse.e4.tm.builder.IBinder;
import org.eclipse.e4.tm.builder.IBinderContext;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public abstract class SwtBinder extends AbstractBinder implements IBinder {

	protected Object convertValue(EStructuralFeature feature, Object value) {
		String constantName = String.valueOf(value).toUpperCase();
		Class<?> constants = SWT.class;
		String constantClass = EcoreUtil.getAnnotation(feature, SwtBuilder.getAnnotationUri(), "constantClass");
		if (constantClass != null) {
			try {
				constants = context.convert(constantClass, Class.class);
			} catch (Exception e) {
				throw new IllegalArgumentException("Conversion from " + value + " failed: " + e.getMessage(), e);
			}
		}
		if (feature.getEType() == EcorePackage.eINSTANCE.getEString()) {
			String type = EcoreUtil.getAnnotation(feature, SwtBuilder.getAnnotationUri(), "type");
			if ("int".equals(type)) {
				value = SwtBuilder.getStaticField(constants, constantName, Integer.class, SWT.DEFAULT);
			} else if (type != null) {
				try {
					Class<?> typeClass = context.convert(type, Class.class);
					value = SwtBuilder.getStaticField(constants, constantName, typeClass, null);
				} catch (Exception e) {
					throw new IllegalArgumentException("Conversion from " + value + " to " + type + " failed: " + e.getMessage(), e);
				}
			}
		} else if (feature.getEType() == org.eclipse.e4.tm.swt.styles.StylesPackage.eINSTANCE.getSwtConstant()) {
			value = SwtBuilder.getStaticField(constants, constantName, Integer.class, SWT.DEFAULT);
		}
		return value;
	}

	protected void setDefaultLayout(Composite swtComposite) {
		swtComposite.setLayout(getDefaultLayout());
	}

	protected Layout getDefaultLayout() {
		return new FillLayout();
	}

	protected void updateInvalidFeature(EObject eObject, EStructuralFeature feature, Object object) {
		super.updateInvalidFeature(eObject, feature, object);
		if (eObject.eContainer() instanceof org.eclipse.e4.tm.widgets.Composite) {
			org.eclipse.e4.tm.widgets.Composite comp = (org.eclipse.e4.tm.widgets.Composite)eObject.eContainer();
			if (eObject == comp.getLayout()) {
				((Composite)context.getObject(comp, Composite.class)).layout();
			}
		}
		String invalidateStyle = AbstractBuilder.getClassAnnotation(feature.getEContainingClass(), AbstractBinder.ANNOTATION_URI, "invalidateStyle", null);
		if (invalidateStyle != null) {
			EObject parent = eObject;
			while (parent != null) {
				if (parent instanceof Styled) {
					context.updateStyle((Styled)parent);
					break;
				}
			}
		}
	}
	
	//
	
	public void updateStyle(EObject eObject, Object object, IBinderContext context) {
	}
}
