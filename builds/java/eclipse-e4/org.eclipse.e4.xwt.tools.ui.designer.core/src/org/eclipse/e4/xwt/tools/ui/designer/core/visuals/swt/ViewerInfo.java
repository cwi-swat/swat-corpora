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
package org.eclipse.e4.xwt.tools.ui.designer.core.visuals.swt;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.VisualInfo;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

/**
 * @author jin.liu(jin.liu@soyatec.com)
 */
public class ViewerInfo extends VisualInfo {

	private Viewer viewer;

	private ControlInfo delegate;

	public ViewerInfo(Viewer visualObject, boolean isRoot) {
		super(visualObject, isRoot);
		this.viewer = visualObject;
		if (visualObject != null) {
			Control control = visualObject.getControl();
			if (control != null) {
				delegate = new ControlInfo(control, isRoot);
			}
		}
	}

	public Viewer getViewer() {
		return viewer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.tools.ui.designer.core.visuals.VisualInfo#getBounds()
	 */
	public Rectangle getBounds() {
		if (delegate != null) {
			return delegate.getBounds();
		}
		return super.getBounds();
	}

	public void refreshImage() {
		if (delegate != null) {
			delegate.refreshImage();
		}
	}

}
