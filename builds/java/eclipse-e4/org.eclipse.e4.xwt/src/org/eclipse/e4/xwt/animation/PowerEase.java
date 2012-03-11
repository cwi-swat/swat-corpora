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

import org.eclipse.e4.xwt.XWTException;

/**
 * 
 * @author yyang
 */
public class PowerEase extends EasingFunctionBase {
	private double power = 2;

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double ease(double normalizedTime) {
		switch (getEasingMode()) {
		case EaseIn:
			return Math.pow(normalizedTime, getPower());
		case EaseOut:
			return 1 - Math.pow(1 - normalizedTime, getPower());
		case EaseInOut:
			normalizedTime /= 0.5;
			if (normalizedTime < 1)
				return Math.pow(normalizedTime, getPower()) / 2;
			return (2 - Math.pow(2 - normalizedTime, getPower())) / 2;
		default:
			throw new XWTException(getEasingMode().name() + " is supported.");
		}
	}
}
