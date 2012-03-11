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

import org.eclipse.e4.tm.builder.AbstractBinder;
import org.eclipse.e4.tm.builder.IBinderContext;
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.e4.tm.graphics2d.Graphical2d;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;

public class Graphical2dBinder extends AbstractBinder implements IPropertyChangeListener {

	public <T> T adapt(Object value, Class<T> c) {
		if (c == Display.class && value instanceof PNode) {
			return (T)((PNode)value).getDisplay();
		}
		return super.adapt(value, c);
	}

	protected Object create(EObject node) {
		PNode pNode = (PNode)super.create(node);
		PCanvas canvasParent = getParent(node, PCanvas.class);
		if (pNode instanceof PLayer && canvasParent != null) {
			canvasParent.addLayer((PLayer)pNode);
		} else {
			PNode nodeParent = getParent(node, PNode.class);
			if (nodeParent != null) {
				nodeParent.addChild(pNode);
			}
		}
		pNode.addPropertyChangeListener(this);
		return pNode;
	}

	// IPropertyChangeListener
	
	public void dispose(EObject eObject, Object object, IBinderContext context) {
		if (object instanceof PNode) {
			PNode node = (PNode)object;
			node.removePropertyChangeListener(this);
			node.dispose();
		}
		super.dispose(eObject, object, context);
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof PNode && PNode.PROPERTY_BOUNDS.equals(event.getProperty())) {
			PNode node = (PNode)event.getSource();
			Graphical2d eNode = (Graphical2d)context.getEObject(node);
			Rectangle rect1 = eNode.getBounds(), rect2 = node.getBoundsReference();
			if (rect1 == null || rect1.compareTo(rect2) != 0) {
				eNode.setBounds(new Rectangle(rect2));
			}
		}
	}

	public void updateStyle(EObject eObject, Object object, IBinderContext context) {
		// TODO
	}
}
