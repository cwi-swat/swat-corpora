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

import org.eclipse.e4.xwt.tools.ui.designer.core.util.image.ImageCollectedRunnable;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.image.ImageCollector;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jin.liu(jin.liu@soyatec.com)
 */
public class ControlInfo extends WidgetInfo {

	public ControlInfo(Object visualObject, boolean isRoot) {
		super(visualObject, isRoot);
	}

	public void refreshImage() {
		if (!isRoot()) {
			return;
		}
		Widget widget = getVisualObject();
		if (widget == null || widget.isDisposed() || !(widget instanceof Control)) {
			return;
		}
		Control control = (Control) widget;
		ImageCollector.collectImage(control, new ImageCollectedRunnable() {
			public void imageNotCollected() {
			}

			public void imageCollected(Image image) {
				imageSupport.fireImageChanged(image);
			}
		});
	}
}
