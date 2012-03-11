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
package org.eclipse.e4.emf.databinding.javascript;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.e4.emf.ecore.javascript.EmfContext;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.provider.IDisposable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ComputedJavascriptValue extends AbstractObservableValue {

	private Scriptable scope;
	private String source;
	
	// Scriptable.NOT_FOUND means no value
	private Object result = Scriptable.NOT_FOUND;
	
	private Notifier notifier = null;
		
	protected Object executeScript() {
		Context context = Context.enter();
		try {
			EmfContext.startAddingDependencies();
			result = context.evaluateString(scope, source, "<script>", -1, null);
			notifier = EmfContext.stopAddingDependencies();
			notifier.eAdapters().add(new AdapterImpl() {
				public void notifyChanged(Notification notification) {
					if (notifier instanceof IDisposable) {
						((IDisposable)notifier).dispose();
					}
					// copy the old value
					final Object oldValue = result;
					result = Scriptable.NOT_FOUND;
					// Fire the "dirty" event. This implementation recomputes the new value lazily.
					// Logic copied from ComputedValue
					fireValueChange(new ValueDiff() {
						public Object getOldValue() {
							return oldValue;
						}
						public Object getNewValue() {
							return getValue();
						}
					});
				}
			});
		} catch (Exception e) {
		} finally {
			Context.exit();
		}
		return result;
	}

	protected Object doGetValue() {
		if (result == Scriptable.NOT_FOUND) {
			executeScript();
		}
		return (result != Scriptable.NOT_FOUND ? result : null);
	}

	public Object getValueType() {
		return null;
	}
}
