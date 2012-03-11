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
package org.eclipse.e4.xwt.animation.interpolator;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.interpolator.PropertyInterpolator;

public class PointPropertyInterpolator implements PropertyInterpolator<Point>{
	private Point value = new Point(0, 0);
	
	public PointPropertyInterpolator() {
	}
	
	public Class<?> getBasePropertyClass() {
		return Point.class;
	}
	
	public Point interpolate(Point from, Point to, float timelinePosition) {
		double x = from.x + (to.x - from.x) * timelinePosition;
		double y = from.y + (to.y - from.y) * timelinePosition;
		value.x = (int)x;
		value.y = (int)y;
		return value;
	}
}
