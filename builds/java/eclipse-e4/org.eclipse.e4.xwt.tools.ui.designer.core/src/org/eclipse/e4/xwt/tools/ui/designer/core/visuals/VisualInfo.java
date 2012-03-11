/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.core.visuals;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.images.IImageListener;
import org.eclipse.e4.xwt.tools.ui.designer.core.images.ImageNotifierSupport;

/**
 * @author jin.liu(jin.liu@soyatec.com)
 */
public abstract class VisualInfo implements IVisualInfo {

	protected Object visualObject;
	protected ImageNotifierSupport imageSupport = new ImageNotifierSupport();

	private boolean isRoot;

	public VisualInfo(Object visualObject, boolean isRoot) {
		this.visualObject = visualObject;
		this.isRoot = isRoot;
	}

	public Object getVisualObject() {
		return visualObject;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public Rectangle getBounds() {
		return new Rectangle();
	}

	public Rectangle getClientArea() {
		return getBounds();
	}

	public void setVisualObject(Object visualObject) {
		this.visualObject = visualObject;
	}

	public void addImageListener(IImageListener listener) {
		imageSupport.addImageListener(listener);
	}

	public void removeImageListener(IImageListener listener) {
		imageSupport.removeImageListener(listener);
	}

	public boolean hasImageListeners() {
		return imageSupport.hasImageListeners();
	}
}
