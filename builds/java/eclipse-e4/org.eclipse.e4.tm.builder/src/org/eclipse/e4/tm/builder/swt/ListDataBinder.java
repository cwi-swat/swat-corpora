/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder.swt;

import org.eclipse.e4.tm.builder.IBinder;
import org.eclipse.e4.tm.builder.jface.ListDataContentProvider;
import org.eclipse.e4.tm.util.ListData;
import org.eclipse.emf.ecore.EObject;

public class ListDataBinder extends ControlBinder implements IBinder {

	protected Object create(EObject control) {
		return new ListDataContentProvider((ListData)control);
	}
}
