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
package org.eclipse.e4.xwt.tools.ui.designer.databinding;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.internal.utils.UserData;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTTools;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ObservableUtil {

	private static final Map<Object, Observable> cache = new HashMap<Object, Observable>(1);

	public static Observable getObservable(Object source) {
		if (source == null) {
			return null;
		}
		Observable observable = cache.get(source);
		if (observable == null) {
			observable = createObservable(source, null);
		}
		return observable;
	}

	private static Observable createObservable(Object source, Observable parent) {
		if (!(source instanceof Viewer)) {
			Viewer viewer = UserData.getLocalViewer(source);
			if (viewer != null) {
				parent = createObservable(viewer, parent);
			}
		}
		Observable observable = new Observable(source, parent);
		if (source instanceof Widget) {
			Widget[] children = SWTTools.getChildren((Widget) source);
			for (Widget widget : children) {
				createObservable(widget, observable);
			}
		}
		cache.put(source, observable);
		return observable;
	}

}
