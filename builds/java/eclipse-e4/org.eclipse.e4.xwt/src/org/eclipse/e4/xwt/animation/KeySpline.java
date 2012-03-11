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
import org.eclipse.e4.xwt.graphics.Point;

public class KeySpline {
	private Point controlPoint1;
	private Point controlPoint2;

	public Point getControlPoint1() {
		return controlPoint1;
	}

	public void setControlPoint1(Point controlPoint1) {
		this.controlPoint1 = controlPoint1;
	}

	public Point getControlPoint2() {
		return controlPoint2;
	}

	public void setControlPoint2(Point controlPoint2) {
		this.controlPoint2 = controlPoint2;
	}
		
	public static KeySpline fromString(String content) {
		String[] segments = content.split("[, \t\n\r\f]");
		if (segments.length == 4) {
			double[] values = new double[segments.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = Double.parseDouble(segments[i]);
			}
			KeySpline keySpline = new KeySpline();
			keySpline.setControlPoint1(new Point(values[0], values[1]));
			keySpline.setControlPoint2(new Point(values[2], values[3]));
			return keySpline;
		}
		throw new XWTException("Wrong format: " + content);
	}
}
