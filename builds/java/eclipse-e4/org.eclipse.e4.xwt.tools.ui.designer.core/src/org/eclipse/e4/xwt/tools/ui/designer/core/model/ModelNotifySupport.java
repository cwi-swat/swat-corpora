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
package org.eclipse.e4.xwt.tools.ui.designer.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class ModelNotifySupport implements IModelNotify {

	private List<ModelChangeListener> listeners;

	// private List<Notification> eventsQueue;

	public void addModelListener(ModelChangeListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<ModelChangeListener>();
		}
		listeners.add(listener);
	}

	public boolean hasListener(ModelChangeListener listener) {
		return listeners != null && listeners.contains(listener);
	}

	public void removeModelListener(ModelChangeListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public void dispatchEvent(Notification event) {
		if (listeners != null) {
			for (ModelChangeListener l : listeners) {
				l.notifyChanged(event);
			}
		}
	}

	public void dispose() {
		if (listeners != null) {
			listeners.clear();
		}
		listeners = null;
	}
}
