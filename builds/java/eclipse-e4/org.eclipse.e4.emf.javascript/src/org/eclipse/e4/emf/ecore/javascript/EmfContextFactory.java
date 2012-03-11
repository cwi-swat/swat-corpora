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
package org.eclipse.e4.emf.ecore.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class EmfContextFactory extends ContextFactory
{
	private static EmfContextFactory singleton;

	static EmfContextFactory getEmfContextFactory() {
		if (EmfContextFactory.singleton == null) {
			EmfContextFactory.singleton = new EmfContextFactory();
		}
		return EmfContextFactory.singleton;
	}

	static {
		// Initialize GlobalFactory with custom factory
		ContextFactory.initGlobal(EmfContextFactory.getEmfContextFactory());
	}

	// Override makeContext()
	protected Context makeContext()	{
		return new EmfContext();
	}
}
