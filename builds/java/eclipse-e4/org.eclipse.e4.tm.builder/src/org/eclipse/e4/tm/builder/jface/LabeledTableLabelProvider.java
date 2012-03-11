/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder.jface;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;

public class LabeledTableLabelProvider implements ITableLabelProvider, ILabelProviderListener {

	private List<ILabelProvider> labelProviders = new ArrayList<ILabelProvider>();

	public void clearLabelProviders() {
		labelProviders.clear();
	}
	public void addLabelProvider(ILabelProvider labelProvider) {
		labelProviders.add(labelProvider);
	}
	
	public boolean isLabelProperty(Object element, String property) {
		for (IBaseLabelProvider labelProvider : labelProviders) {
			if (labelProvider.isLabelProperty(element, property)) {
				return true;
			}
		}
		return false;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		if (labelProviders.size() > columnIndex) {
			return labelProviders.get(columnIndex).getImage(element);
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (labelProviders.size() > columnIndex) {
			return labelProviders.get(columnIndex).getText(element);
		}
		return null;
	}

	public void dispose() {
		labelProviders.clear();
		labelProviders = null;
		listeners.clear();
		listeners = null;
	}
	
	private List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();

	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}
	
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		for (ILabelProviderListener listener : listeners) {
			listener.labelProviderChanged(event);
		}
	}
}
