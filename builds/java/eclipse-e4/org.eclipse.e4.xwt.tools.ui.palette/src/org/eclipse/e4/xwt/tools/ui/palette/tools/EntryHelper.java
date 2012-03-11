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
package org.eclipse.e4.xwt.tools.ui.palette.tools;

import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.Initializer;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class EntryHelper {

	public static Object getNewObject(Entry entry) {
		if (entry == null) {
			return null;
		}
		Initializer initializer = entry.getInitializer();
		if (initializer != null) {
			return initializer.parse(entry);
		}
		return null;
	}

	public static Object getNewObject(CreateRequest createReq) {
		if (createReq == null) {
			return null;
		}
		Object newObject = createReq.getNewObject();
		if (newObject instanceof Entry) {
			Object creatingObj = getNewObject((Entry) newObject);
			if (creatingObj != null) {
				newObject = creatingObj;
			}
		}
		return newObject;
	}
}
