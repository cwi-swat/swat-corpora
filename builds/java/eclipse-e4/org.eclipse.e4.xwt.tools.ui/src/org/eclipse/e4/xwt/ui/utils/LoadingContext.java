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
package org.eclipse.e4.xwt.ui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.eclipse.e4.xwt.IConstants;

public class LoadingContext extends org.eclipse.e4.xwt.DefaultLoadingContext {
	String namespace = IConstants.XWT_NAMESPACE;
	protected ArrayList<WeakReference<PropertyChangeListener>> changeSupport = new ArrayList<WeakReference<PropertyChangeListener>>();

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void addPropertyChangeListener(PropertyChangeListener changeListener) {
		for (WeakReference<PropertyChangeListener> reference : changeSupport) {
			if (reference.get() == changeListener) {
				return;
			}
		}
		changeSupport.add(new WeakReference<PropertyChangeListener>(changeListener));
	}

	public void removePropertyChangeListener(PropertyChangeListener changeListener) {
		for (WeakReference<PropertyChangeListener> reference : changeSupport) {
			if (reference.get() == changeListener) {
				changeSupport.remove(reference);
				return;
			}
		}
	}

	public void firePropertyChangeListener(PropertyChangeEvent event) {
		for (WeakReference<PropertyChangeListener> reference : changeSupport) {
			PropertyChangeListener listener = reference.get();
			if (listener != null) {
				listener.propertyChange(event);
			}
		}
	}
}
