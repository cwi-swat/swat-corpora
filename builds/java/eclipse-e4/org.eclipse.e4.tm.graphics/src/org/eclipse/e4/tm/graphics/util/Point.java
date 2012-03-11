/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.graphics.util;


public class Point extends AbstractCloneable implements Comparable {

	public double x, y;

	public Point() {
		this(0.0, 0.0);
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public Point(Point point) {
		this(point.x, point.y);
	}

	private static String toStringPrefix = ""; // "[Point: ";
	private static String toStringSuffix = ""; // "]";

	public String toString() {
		return toStringPrefix + x + "," + y + toStringSuffix;
	}

	public static Point valueOf(String s) {
		double[] doubles = Util.valueOf(s, toStringPrefix, "[;,]", toStringSuffix);
		double x = (doubles.length > 1 ? doubles[0] : 0.0);
		double y = (doubles.length > 1 ? doubles[1] : 0.0);
		return new Point(x, y);
	}

	//
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setLocation(Point point) {
		setLocation(point.x, point.y);
	}
	
	//
	
    public static double distance(double x1, double y1, double x2, double y2) {
    	double dx = x2 - x1, dy = y2 - y1;
    	return Math.sqrt(dx * dx + dy * dy);
    }
    public double distance(double x, double y) {
    	return distance(this.x, this.y, x, y);
    }
    public double distance(Point p) {
    	return distance(p.x, p.y);
    }

    // Comparable
    
	public int compareTo(Object o) {
		if (o instanceof Point) {
			Point p = (Point)o;
			double diff = p.x - x;
			if (diff == 0.0) {
				diff = p.y - y;
			}
			return (int)Math.signum(diff);
		}
		throw new IllegalArgumentException("Cannot compare " + this + " with " + o);
	}
}
