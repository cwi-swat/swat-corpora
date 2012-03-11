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

import org.eclipse.e4.xwt.metadata.IProperty;

public class Storyboard extends ParallelTimeline {
	private String targetName;
	private IProperty targetProperty;
	
	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public IProperty getTargetProperty() {
		return targetProperty;
	}

	public void setTargetProperty(IProperty targetProperty) {
		this.targetProperty = targetProperty;
	}
}
