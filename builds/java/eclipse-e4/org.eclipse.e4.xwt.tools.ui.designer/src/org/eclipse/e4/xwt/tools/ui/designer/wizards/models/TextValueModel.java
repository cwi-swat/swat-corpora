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
package org.eclipse.e4.xwt.tools.ui.designer.wizards.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.e4.xwt.tools.ui.designer.wizards.TableViewerListener;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 */
public class TextValueModel extends TextValueElement {

	private Vector content;

	private List listeners = new ArrayList();

	public TextValueModel() {
		content = new Vector();
	}

	public void add(Object element) {
		content.add(element);
		if (element instanceof TextValueElement) {
			((TextValueElement) element).setParent(this);
		}
	}

	public void remove(Object element) {
		content.remove(element);
	}

	public Object[] elements() {
		return content.toArray();
	}

	public void addPropertyChangeListener(TableViewerListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removePropertyChangeListener(TableViewerListener listener) {
		listeners.remove(listener);
	}

	/**
	 * key changed listener.
	 */
	void keyChanged(TextValueEntry textValue) {
		Iterator iter = listeners.iterator();
		while (iter.hasNext())
			((TableViewerListener) iter.next()).valueChanged(textValue);
	}
}
