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

public class Dimension extends AbstractCloneable {

	public double width, height;

	public Dimension() {
		this(0.0, 0.0);
	}

	public Dimension(double width, double height) {
		super();
		this.width = width;
		this.height = height;
	}
	public Dimension(Dimension dim) {
		this(dim.width, dim.height);
	}
	
	private static String toStringPrefix = ""; // "[Dimension: ";
	private static String toStringSuffix = ""; // "]";

	public String toString() {
		return toStringPrefix + width + "x" + height + toStringSuffix;
	}

	public static Dimension valueOf(String s) {
		double[] doubles = Util.valueOf(s, toStringPrefix, "[;,x]", toStringSuffix);
		double width = (doubles.length > 1 ? doubles[0] : 0.0);
		double height = (doubles.length > 1 ? doubles[1] : 0.0);
		return new Dimension(width, height);
	}

	//
	
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}

	public boolean isEmpty() {
		return width == 0.0 || height == 0.0;
	}

	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public void setSize(Dimension dim) {
		setSize(dim.width, dim.height);
	}

    // Comparable
    
	public int compareTo(Object o) {
		if (o instanceof Dimension) {
			Dimension rect = (Dimension)o;
			double diff = rect.width - width;
			if (diff == 0.0) {
				diff = rect.height - height;
			}
			return (int)Math.signum(diff);
		}
		throw new IllegalArgumentException("Cannot compare " + this + " with " + o);
	}
}
