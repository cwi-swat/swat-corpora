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
package org.eclipse.e4.emf.ecore.javascript.functions;

import org.eclipse.e4.emf.ecore.javascript.AsyncSupport;
import org.eclipse.e4.emf.ecore.javascript.EmfContext;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.provider.IDisposable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class BindingApply extends ApplyFunction {

	public BindingApply(AsyncSupport asyncSupport) {
		super(asyncSupport);
	}

	private abstract class DependencyHandler extends AdapterImpl implements Runnable {
		
		private Notifier notifier;
		
		public DependencyHandler(Notifier notifier) {
			super();
			this.notifier = notifier;
		}

		public void notifyChanged(Notification notification) {
			if (notifier instanceof IDisposable) {
				((IDisposable)notifier).dispose();
			}
			BindingApply.this.runAsync(this);
		}
	}
	
	protected int getAdditionalArity() {
		return 1;
	}

	private /* transient */ Function setterFunction = null;
	
	protected Object[] getFunArgs(Object[] args) {
		setterFunction = typeCheckArgument(args, 1, Function.class);
		return copyArgs(args, 2);
	}

	protected Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Function getterFunction, final Object funArgs[]) {
		return call(cx, scope, thisObj, getterFunction, setterFunction, funArgs);
	}

	protected Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Function getterFunction, final Function setterFunction, final Object funArgs[]) {
		EmfContext.startAddingDependencies();
		final Object result = getterFunction.call(cx, scope, thisObj, funArgs);
		final Notifier notifier = EmfContext.stopAddingDependencies();
		Object[] setterArgs = new Object[funArgs.length + 1];
		System.arraycopy(funArgs, 0, setterArgs, 0, funArgs.length);
		setterArgs[funArgs.length] = result;
		setterFunction.call(cx, scope, thisObj, setterArgs);
		notifier.eAdapters().add(new DependencyHandler(notifier) {
			public void run() {
				call(cx, scope, thisObj, getterFunction, setterFunction, funArgs);
			}
		});
		return result;
	}
}
