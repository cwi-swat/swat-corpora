/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.workbench.modeling;

import java.util.Collections;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.IServiceConstants;

/**
 *
 */
public class ExpressionContext implements IEvaluationContext {

	private boolean allowActivation = false;
	public IEclipseContext eclipseContext;

	public ExpressionContext(IEclipseContext eclipseContext) {
		this.eclipseContext = eclipseContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#getParent()
	 */
	public IEvaluationContext getParent() {
		IEclipseContext parent = eclipseContext.getParent();
		return parent == null ? null : new ExpressionContext(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#getRoot()
	 */
	public IEvaluationContext getRoot() {
		IEclipseContext current = eclipseContext;
		IEclipseContext parent = current.getParent();
		while (parent != null) {
			current = parent;
			parent = current.getParent();
		}
		if (current == eclipseContext) {
			return this;
		}
		return new ExpressionContext(current);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#setAllowPluginActivation(boolean)
	 */
	public void setAllowPluginActivation(boolean value) {
		allowActivation = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#getAllowPluginActivation()
	 */
	public boolean getAllowPluginActivation() {
		return allowActivation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#getDefaultVariable()
	 */
	public Object getDefaultVariable() {
		Object sel = eclipseContext.getActiveLeaf().get(IServiceConstants.ACTIVE_SELECTION);
		return sel == null ? Collections.EMPTY_LIST : sel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#addVariable(java.lang.String,
	 * java.lang.Object)
	 */
	public void addVariable(String name, Object value) {
		eclipseContext.set(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#removeVariable(java.lang.String)
	 */
	public Object removeVariable(String name) {
		Object obj = eclipseContext.getLocal(name);
		eclipseContext.remove(name);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#getVariable(java.lang.String)
	 */
	public Object getVariable(String name) {
		Object obj = eclipseContext.getActiveLeaf().get(name);
		return obj == null ? IEvaluationContext.UNDEFINED_VARIABLE : obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IEvaluationContext#resolveVariable(java.lang.String,
	 * java.lang.Object[])
	 */
	public Object resolveVariable(String name, Object[] args) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}
}
