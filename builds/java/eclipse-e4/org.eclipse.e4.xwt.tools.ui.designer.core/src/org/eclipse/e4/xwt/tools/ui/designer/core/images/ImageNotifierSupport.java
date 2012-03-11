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
package org.eclipse.e4.xwt.tools.ui.designer.core.images;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.graphics.Image;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class ImageNotifierSupport {
	protected ListenerList imageListeners = null;

	public synchronized void addImageListener(IImageListener aListener) {
		if (imageListeners == null)
			imageListeners = new ListenerList(ListenerList.IDENTITY);
		imageListeners.add(aListener);
	}

	public synchronized void fireImageChanged(Image image) {
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
		Object[] listeners = imageListeners.getListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				((IImageListener) listeners[i]).imageChanged(image);
			}
		}
	}

	public synchronized boolean hasImageListeners() {
		return imageListeners != null && !imageListeners.isEmpty();
	}

	public synchronized void removeImageListener(IImageListener aListener) {
		if (imageListeners != null)
			imageListeners.remove(aListener);
	}

}
