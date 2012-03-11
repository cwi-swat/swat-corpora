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
package org.eclipse.e4.tm.builder;

import org.eclipse.emf.ecore.resource.Resource;

public interface IBuilder {
	public void build(Resource res, Object context);
	public void dispose();
	public void addBuilderListener(IBuilderListener listener);
	public void removeBuilderListener(IBuilderListener listener);
}
