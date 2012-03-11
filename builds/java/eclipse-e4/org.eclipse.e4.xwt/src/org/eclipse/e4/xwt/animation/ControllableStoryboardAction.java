/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.animation;

import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.core.TriggerAction;
import org.eclipse.e4.xwt.internal.utils.UserData;

public abstract class ControllableStoryboardAction extends TriggerAction {
	private String beginStoryboardName;
	private Storyboard storyboard;
	
	protected Storyboard getStoryboard() {
		return storyboard;
	}

	/**
	 * Getter of the property <tt>BeginStoryboardName</tt>
	 * 
	 * @return Returns the beginStoryboardName.
	 * @uml.property name="BeginStoryboardName"
	 */
	public String getBeginStoryboardName() {
		return beginStoryboardName;
	}

	/**
	 * Setter of the property <tt>BeginStoryboardName</tt>
	 * 
	 * @param BeginStoryboardNameProperty
	 *            The beginStoryboardName to set.
	 * @uml.property name="BeginStoryboardName"
	 */
	public void setBeginStoryboardName(String beginStoryboardName) {
		this.beginStoryboardName = beginStoryboardName;
	}
	
	@Override
	public void initialize(Object target) {
		String beginStoryboardName = getBeginStoryboardName();
		if (beginStoryboardName == null || beginStoryboardName.length() == 0) {
			return;
		}
		Object element = UserData.findElementByName(target, beginStoryboardName);
		if (element instanceof BeginStoryboard) {
			BeginStoryboard beginStoryboard = (BeginStoryboard) element;
			storyboard = beginStoryboard.getStoryboard();
		}
		else {
			throw new XWTException("NeginStoryboardName " + beginStoryboardName + " is not found.");
		}
	}
	
	public void endFinalize(Object target) {
		storyboard.endFinalize(target);
	}
	
}
