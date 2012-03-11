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
import java.util.Collection;

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.ChangeCommand;

public abstract class JavascriptCommand extends ChangeCommand {

	protected JavascriptSupport javascriptSupport;

	protected JavascriptCommand(JavascriptSupport javascriptSupport) {
		super(new ArrayList<Notifier>());
		this.javascriptSupport = javascriptSupport;
	}

	public JavascriptSupport getJavascriptSupport() {
		return javascriptSupport;
	}

	protected abstract Collection<EObject> getEObjects();

	public void execute() {
		notifiers.addAll(getEObjects());
		super.execute();
	}
}
