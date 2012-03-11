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
public class ElasticEase extends EasingFunctionBase {
	private int oscillations = 3;
	private double springiness = 3;

	public int getOscillations() {
		return oscillations;
	}

	public void setOscillations(int oscillations) {
		this.oscillations = oscillations;
	}

	public double getSpringiness() {
		return springiness;
	}

	public void setSpringiness(double springiness) {
		this.springiness = springiness;
	}

	public double ease(double normalizedTime) {
		switch (getEasingMode()) {
		case EaseIn:
			return easeValue(normalizedTime, getOscillations(),
					getSpringiness());
		case EaseOut:
			return 1 - easeValue(1 - normalizedTime, getOscillations(),
					getSpringiness());
		case EaseInOut:
			normalizedTime /= 0.5;
			if (normalizedTime < 1)
				return easeValue(normalizedTime, getOscillations(),
						getSpringiness()) / 2;
			return (2 - easeValue(2 - normalizedTime, getOscillations(),
					getSpringiness())) / 2;
		default:
			throw new XWTException(getEasingMode().name() + " is supported.");
		}
	}

	protected double easeValue(double current, int oscillations,
			double springiness) {
		if (current == 0)
			return 0;
		if (current == 1)
			return 1;
		double s = Math.asin(1) / ((2 * Math.PI) * oscillations);
		return -(Math.pow(2, 10 * (current -= 1)) * Math.sin((current - s)
				* (2 * Math.PI) * oscillations));
	}
}
