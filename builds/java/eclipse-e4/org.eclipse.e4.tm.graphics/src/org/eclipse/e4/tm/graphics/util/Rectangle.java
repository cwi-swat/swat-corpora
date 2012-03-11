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


public class Rectangle extends AbstractCloneable implements Comparable {

	public double x, y, width, height;

	public Rectangle() {
		this(0.0, 0.0, 0.0, 0.0);
	}

	public Rectangle(double x, double y, double width, double height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Rectangle(Rectangle rect) {
		this(rect.x, rect.y, rect.width, rect.height);
	}

	private static String toStringPrefix = ""; // "[Rectangle: ";
	private static String toStringSuffix = ""; // "]";

	public String toString() {
		return toStringPrefix + x + "," + y + ";" + width + "x" + height + toStringSuffix;
	}

	public static Rectangle valueOf(String s) {
		double[] doubles = Util.valueOf(s, toStringPrefix, "[;,x]", toStringSuffix);
		double x = (doubles.length > 1 ? doubles[0] : 0.0);
		double y = (doubles.length > 1 ? doubles[1] : 0.0);
		double width  = (doubles.length > 3 ? doubles[2] : 0.0);
		double height = (doubles.length > 3 ? doubles[3] : 0.0);
		return new Rectangle(x, y, width, height);
	}

	//
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}

	public boolean isEmpty() {
		return width <= 0.0 || height <= 0.0;
	}

	public void setRect(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setRect(Rectangle rect) {
		setRect(rect.x, rect.y, rect.width, rect.height);
	}

	public void add(double x, double y) {
		double x1 = Math.min(this.x, x);
		double x2 = Math.max(this.x + this.width, x);
		double y1 = Math.min(this.y, y);
		double y2 = Math.max(this.y + this.height, y);
		setRect(x1, y1, x2 - x1, y2 - y1);
	}
	public void add(Point p) {
		add(p.x, p.y);
	}
	public void add(Rectangle rect) {
		double x1 = Math.min(this.x, rect.x);
		double x2 = Math.max(this.x + this.width, rect.x + rect.width);
		double y1 = Math.min(this.y, rect.y);
		double y2 = Math.max(this.y + this.height, rect.y + rect.height);
		setRect(x1, y1, x2 - x1, y2 - y1);
	}

	public double getMinX() {
		return x;
	}
	public double getMaxX() {
		return x + width;
	}
	public double getMinY() {
		return y;
	}
	public double getMaxY() {
		return y + height;
	}
	public double getCenterX() {
		return x + width / 2;
	}
	public double getCenterY() {
		return y + height / 2;
	}

	//

	public boolean contains(double x, double y) {
		return (x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height);
	}
	public boolean contains(Point p) {
		return contains(p.x, p.y);
	}
	public boolean contains(Rectangle rect) {
		return contains(rect.x, rect.y) && contains(rect.x + rect.width, rect.y + rect.height);
	}

	public static void intersect(Rectangle rect1, Rectangle rect2, Rectangle dest) {
		double x1 = Math.max(rect1.x, rect2.x);
		double y1 = Math.max(rect1.y, rect2.y);
		double x2 = Math.min(rect1.x + rect1.width, rect2.x + rect2.width);
		double y2 = Math.min(rect1.y + rect1.height, rect2.y + rect2.height);
		dest.setRect(x1, y1, x2 - x1, y2 - y1);
	}

	private static Rectangle rTemp = new Rectangle();
	
	public boolean intersects(Rectangle rect) {
		intersect(this, rect, rTemp);
		return (! rTemp.isEmpty());
	}
	
    // Comparable
    
	public int compareTo(Object o) {
		if (o instanceof Rectangle) {
			Rectangle rect = (Rectangle)o;
			double diff = rect.width - width;
			if (diff == 0.0) {
				diff = rect.height - height;
			}
			if (diff == 0.0) {
				diff = rect.x - x;
			}
			if (diff == 0.0) {
				diff = rect.y - y;
			}
			return (int)Math.signum(diff);
		}
		throw new IllegalArgumentException("Cannot compare " + this + " with " + o);
	}
}
