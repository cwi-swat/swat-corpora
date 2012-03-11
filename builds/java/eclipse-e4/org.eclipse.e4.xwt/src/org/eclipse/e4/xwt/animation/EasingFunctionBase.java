/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.animation;

public abstract class EasingFunctionBase implements IEasingFunction {
	private EasingMode easingMode = EasingMode.EaseOut; 
	
	public EasingMode getEasingMode() {
		return easingMode;
	}

	public void setEasingMode(EasingMode easingMode) {
		this.easingMode = easingMode;
	}

	final public float map(float durationFraction) {
		return (float) ease(durationFraction);
	}
}
