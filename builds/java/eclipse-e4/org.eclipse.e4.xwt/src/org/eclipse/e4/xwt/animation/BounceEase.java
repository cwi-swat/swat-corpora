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
public class BounceEase extends EasingFunctionBase {
	private int bounces = 5;
	private double bounciness = 2;

	public int getBounces() {
		return bounces;
	}

	public void setBounces(int bounces) {
		this.bounces = bounces;
	}

	public double getBounciness() {
		return bounciness;
	}

	public void setBounciness(double bounciness) {
		this.bounciness = bounciness;
	}

	public double ease(double normalizedTime) {
		switch (getEasingMode()) {
		case EaseIn:
			return easeValue(normalizedTime, getBounces(), getBounciness());
		case EaseOut:
			return 1 - easeValue(1 - normalizedTime, getBounces(),
					getBounciness());
		case EaseInOut:
			normalizedTime /= 0.5;
			if (normalizedTime < 1)
				return easeValue(normalizedTime, getBounces(), getBounciness()) / 2;
			return (2 - easeValue(2 - normalizedTime, getBounces(),
					getBounciness())) / 2;
		default:
			throw new XWTException(getEasingMode().name() + " is supported.");
		}
	}

	protected static double easeValue(double current, int bounces, double bounciness) {
		if (current == 0)
			return 0;
		if (current == 1)
			return 1;
		double angle = (Math.PI) * (bounces + 1);
		double s = Math.asin(1) / angle;
		return Math.pow(2, 10 * (current -= 1)) * Math.abs(Math
				.sin((current - s) * angle));
	}
}
