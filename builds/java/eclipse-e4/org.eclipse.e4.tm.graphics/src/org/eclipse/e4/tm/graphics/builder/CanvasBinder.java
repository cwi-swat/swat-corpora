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
package org.eclipse.e4.tm.graphics.builder;

import org.eclipse.e4.tm.builder.swt.ControlBinder;
import org.eclipse.e4.tm.graphics2d.Canvas;
import org.eclipse.e4.tm.graphics2d.Graphics2dPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.PRoot;

public class CanvasBinder extends ControlBinder {

	private final static Class<?>[] constructorParameterTypes = new Class[]{org.eclipse.swt.widgets.Composite.class, int.class};

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		if (eObject instanceof Canvas && feature == Graphics2dPackage.eINSTANCE.getCanvas_Layers()) {
		}
		super.invalidateFeature(eObject, feature, object, isEvent);
	}

	public final static String CONTROL_DATA_URI_KEY = "modelUri";

	public <T> T adapt(Object value, Class<T> c) {
		if (value instanceof Composite) {
			PCanvas pCanvas = (PCanvas)((Composite)value).getData(PCanvas.PCANVAS_CONTROL_DATA_KEY);
			if (pCanvas == null) {
				return null;
			}
			if (c == PCanvas.class) {
				return (T)pCanvas;
			} else if (c == PRoot.class) {
				return (T)pCanvas.getRoot();
			} else if (c == PNode.class || c == PCamera.class) {
				return (T)pCanvas.getCamera();
			}
		}
		return super.adapt(value, c);
	}

	protected Object create(EObject control) {
		Composite parent = (Composite)super.create(control);
		if (control instanceof Canvas) {
			PCanvas canvas = new PCanvas(parent);
		}
		return parent;
	}
}
