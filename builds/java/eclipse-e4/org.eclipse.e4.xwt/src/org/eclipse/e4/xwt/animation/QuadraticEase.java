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
public class QuadraticEase extends EasingFunctionBase {
	public double ease(double normalizedTime) {
		switch (getEasingMode()) {
		case EaseIn:
			return normalizedTime * normalizedTime;
		case EaseOut:
			normalizedTime = 1 - normalizedTime;
			return 1 - normalizedTime * normalizedTime;
		case EaseInOut:
			normalizedTime /= 0.5;
			if (normalizedTime < 1)
				return normalizedTime * normalizedTime / 2;
			normalizedTime = 2 - normalizedTime;
			return (2 - normalizedTime * normalizedTime) / 2;
		default:
			throw new XWTException(getEasingMode().name() + " is supported.");
		}
	}
}
