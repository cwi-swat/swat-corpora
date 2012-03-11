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
package org.eclipse.e4.emf.ecore.javascript.functions;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class AdaptTo extends ApplyFunction {

	private boolean logic;
	
	public AdaptTo(boolean logic) {
		this.logic = logic;
	}

	public AdaptTo() {
		this(true);
	}
	
	public int getArity() {
		return 2; // at least 2
	}

	protected Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Function function, final Object funArgs[]) {
		Notifier notifier = typeCheckArgument(funArgs, 0, Notifier.class);
		Adapter adapter = typeCheckArgument(funArgs, 1, Adapter.class);
		boolean logic = (funArgs.length > 2 ? typeCheckArgument(funArgs, 2, Boolean.class) : this.logic);
		EList<Adapter> adapters = notifier.eAdapters();
		boolean containsAdapter = adapters.contains(adapter);
		if (logic != containsAdapter) {
			if (logic) {
				adapters.add(adapter);
			} else {
				adapters.remove(adapter);
			}
		}
		return adapters.contains(adapter);
	}
}
