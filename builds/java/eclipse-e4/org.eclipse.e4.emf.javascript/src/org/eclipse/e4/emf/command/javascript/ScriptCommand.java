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
import org.eclipse.emf.ecore.EObject;

public class ScriptCommand extends JavascriptCommand {

	public ScriptCommand(JavascriptSupport javascriptSupport) {
		super(javascriptSupport);
	}

	private Object scope;

	public Object getScope() {
		return scope;
	}

	public void setScope(Object scope) {
		this.scope = scope;
	}

	private String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	private Collection<EObject> eObjects = new ArrayList<EObject>();

	protected Collection<EObject> getEObjects() {
		return eObjects;
	}

	public void addEObject(EObject eObject) {
		eObjects.add(eObject);
	}

	public void doExecute() {
		javascriptSupport.evaluate(getScript(), getScope(), true);
	}
}
