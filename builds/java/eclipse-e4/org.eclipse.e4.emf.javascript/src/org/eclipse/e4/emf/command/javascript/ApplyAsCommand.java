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
package org.eclipse.e4.emf.command.javascript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.e4.emf.ecore.javascript.functions.ApplyFunction;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class ApplyAsCommand extends ApplyFunction {

	private EditingDomain editingDomain;

	public ApplyAsCommand(EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	protected Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Function function, final Object[] funArgs) {
		Collection<Notifier> eObjects = new ArrayList<Notifier>(funArgs.length);
		for (int i = 0; i < funArgs.length; i++) {
			Object funArg = Context.jsToJava(funArgs[i], Object.class);
			if (funArg instanceof EObject) {
				eObjects.add((EObject)funArg);
			}
		}
		Command command = new ChangeCommand(eObjects) {
			protected void doExecute() {
				try {
					function.call(cx, scope, thisObj, funArgs);
				} catch (RuntimeException e) {
					cx.getErrorReporter().runtimeError("Exception when applying " + function + " to " + Arrays.asList(funArgs) + ": " + e, function.toString(), -1, null, -1);
					throw e;
				}
			}
		};
		editingDomain.getCommandStack().execute(command);
		return eObjects;
	}
}
