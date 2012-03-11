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

public class Transform extends AbstractCloneable {

	public double a11, a12, b1, a21, a22, b2;

	public Transform() {
		this(1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
	}

	public Transform(double a11, double a21, double a12, double a22, double b1, double b2) {
		this.a11 = a11;
		this.a12 = a12;
		this.a21 = a21;
		this.a22 = a22;
		this.b1 = b1;
		this.b2 = b2;
	}
	public Transform(Transform t) {
		this(t.a11, t.a21, t.a12, t.a22, t.b1, t.b2);
	}
	public Transform(double[] t) {
		this(t[0], t[1], t[2], t[3], (t.length > 5 ? t[4] : 0.0), (t.length > 5 ? t[5] : 0.0));
	}

	public Transform(float a11, float a21, float a12, float a22, float b1, float b2) {
		this.a11 = a11;
		this.a12 = a12;
		this.a21 = a21;
		this.a22 = a22;
		this.b1 = b1;
		this.b2 = b2;
	}
	public Transform(float[] t) {
		this(t[0], t[1], t[2], t[3], (t.length > 4 ? t[4] : 0.0), (t.length > 4 ? t[5] : 0.0));
	}

	private static String toStringPrefix = ""; // "[Transform: ";
	private static String toStringSuffix = ""; // "]";
	
	public String toString() {
		return toStringPrefix + "x'=" + a11 + "*x+" + a12 + "*y+" + b1 + ";y'=" + a12 + "*x+" + a22 + "*y+" + b2 + toStringSuffix;
	}
	
	public static Transform valueOf(String s) {
		double[] doubles = Util.valueOf(s, toStringPrefix, "(;?(x|y)'=)|(\\*(x|y)\\+)", toStringSuffix);
		return new Transform(doubles[0], doubles[3], doubles[1], doubles[4], doubles[2], doubles[5]);
	}

	//

	public double getTranslateX() {
		return b1;
	}
	public double getTranslateY() {
		return b2;
	}
	public double getScaleX() {
		return a11;
	}
	public double getScaleY() {
		return a22;
	}
	public double getShearX() {
		return a12;
	}
	public double getShearY() {
		return a21;
	}

	public void getMatrix(double[] t) {
		t[0] = a11;
		t[1] = a21;
		t[2] = a12;
		t[3] = a22;
		if (t.length > 5) {
			t[4] = b1;
			t[5] = b2;
		}
	}

	public Transform setTransform(double a11, double a21, double a12, double a22, double b1, double b2) {
		this.a11 = a11;
		this.a12 = a12;
		this.a21 = a21;
		this.a22 = a22;
		this.b1 = b1;
		this.b2 = b2;
		return this;
	}
	public Transform setTransform(Transform t) {
		return setTransform(t.a11, t.a21, t.a12, t.a22, t.b1, t.b2);
	}

	public Transform setToIdentity() {
		return setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
	}
	public Transform setToTranslation(double dx, double dy) {
		return setTransform(1.0, 0.0, 0.0, 1.0, dx, dy);
	}
	public Transform setToScale(double sx, double sy) {
		return setTransform(sx, 0.0, 0.0, sy, 0.0, 0.0);
	}
	public Transform setToRotation(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		return setTransform(cos, sin, -sin, cos, 0.0, 0.0);
	}
	public Transform setToRotation(double radians, double x, double y) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		return setTransform(cos, sin, -sin, cos, x - x * cos + y * sin, y - x * sin - y * cos);
	}

	//

	public static Transform concatenate(Transform t1, Transform t2, Transform dest) {
		if (dest == null) {
			dest = new Transform();
		}
		return dest.setTransform(
				t1.a11 * t2.a11 + t1.a12 * t2.a21,
				t1.a21 * t2.a11 + t1.a22 * t2.a21,
				t1.a11 * t2.a12 + t1.a12 * t2.a22,
				t1.a21 * t2.a12 + t1.a22 * t2.a22,
				t1.a11 * t2.b1  + t1.a12 * t2.b2 + t1.b1,
				t1.a21 * t2.b1  + t1.a22 * t2.b2 + t1.b2
		);
	}

	public Transform concatenate(Transform t) {
		return concatenate(this, t, this);
	}
	public Transform preConcatenate(Transform t) {
		return concatenate(t, this, this);
	}

	private static Transform tTemp = new Transform();

	public Transform translate(double dx, double dy) {
		return concatenate(tTemp.setToTranslation(dx, dy));
	}
	public Transform scale(double sx, double sy) {
		return concatenate(tTemp.setToScale(sx, sy));
	}
	public Transform rotate(double radians) {
		return concatenate(tTemp.setToRotation(radians));
	}
	public Transform rotate(double radians, double x, double y) {
		return concatenate(tTemp.setToRotation(radians, x, y));
	}

	//

	private static Point transform(double x, double y, Point dest, Transform t, boolean delta) {
		if (dest == null) {
			dest = new Point();
		}
		dest.x = x * t.a11 + y * t.a12 + (delta ? 0.0 : t.b1);
		dest.y = x * t.a21 + y * t.a22 + (delta ? 0.0 : t.b2);
		return dest;
	}
	public Point transform(Point p, Point dest) {
		return transform(p.x, p.y, dest, this, false);
	}
	public Point deltaTransform(Point p, Point dest) {
		return transform(p.x, p.y, dest, this, true);
	}

	private static Point pTemp = new Point();

	private void transform(double[] src, int srcPos, double[] dest, int destPos, int numPoints, boolean delta) {
		if (dest == null) {
			dest = new double[destPos + numPoints * 2];
		}
		System.arraycopy(src, srcPos, dest, destPos, numPoints * 2);
		for (int i = 0; i < numPoints; i += 2) {
			pTemp.x = dest[destPos + i];
			pTemp.y = dest[destPos + i + 1];
			transform(pTemp.x, pTemp.y, pTemp, this, delta);
			dest[destPos + i]     = pTemp.x;
			dest[destPos + i + 1] = pTemp.y;
		}
	}

	public void transform(double[] src, int srcPos, double[] dest, int destPos, int numPoints) {
		transform(src, srcPos, dest, destPos, numPoints, false);
	}

	public void deltaTransform(double[] src, int srcPos, double[] dest, int destPos, int numPoints) {
		transform(src, srcPos, dest, destPos, numPoints, true);
	}

	//

	/*
	 * x = [a22 * (x'- b1) - a12 * (y' - b2)] / (a11 * a22 - a12 * a21) 
	 * y = [a11 * (y'- b2) - a21 * (x' - b1)] / (a11 * a22 - a12 * a21) 
	 */

	private static Transform invert(Transform t, Transform dest) throws Exception {
		double det = t.a11 * t.a22 - t.a12 * t.a21;
		if (Math.abs(det) < Double.MIN_VALUE) {
			throw new Exception(t + " is not invertible");
		}
		
		if (dest == null) { 
			dest = new Transform();
		}
		return dest.setTransform(t.a22 / det, -t.a21 / det, -t.a12 / det, t.a11 / det, (t.a12 * t.b2 - t.a22 * t.b1) / det, (t.a21 * t.b1 - t.a11 * t.b2) / det);
	}
	
	public Transform createInverse() throws Exception {
		return invert(this, new Transform());
	}
	
	private Point inverseTransform(double x, double y, Point dest, Transform t) throws Exception {
		double det = t.a11 * t.a22 - t.a12 * t.a21;
		if (Math.abs(det) < Double.MIN_VALUE) {
			throw new Exception(t + " is not invertible");
		}
		double dx = x - t.b1, dy = y - t.b2;
		if (dest == null) { 
			dest = new Point();
		}
		dest.x = (t.a22 * dx - t.a12 * dy) / det; 
		dest.y = (t.a11 * dy - t.a21 * dx) / det;
		return dest;
	}
	public Point inverseTransform(Point p, Point dest) throws Exception {
		return inverseTransform(p.x, p.y, dest, this);
	}
	public void inverseTransform(double[] src, int srcPos, double[] dest, int destPos, int numPoints) throws Exception {
		if (dest == null) {
			dest = new double[destPos + numPoints * 2];
		}
		System.arraycopy(src, srcPos, dest, destPos, numPoints * 2);
		for (int i = 0; i < numPoints; i += 2) {
			pTemp.x = dest[destPos + i];
			pTemp.y = dest[destPos + i + 1];
			inverseTransform(pTemp.x, pTemp.y, pTemp, this);
			dest[destPos + i]     = pTemp.x;
			dest[destPos + i + 1] = pTemp.y;
		}
	}

	public final static int
	TYPE_IDENTITY = 0,
	TYPE_TRANSLATION = 1,
	TYPE_UNIFORM_SCALE = 2,
	TYPE_GENERAL_SCALE = 4
	;

	public int getType() {
		int bits = TYPE_IDENTITY;
		if (b1 != 0.0 || b2 == 0.0) {
			bits |= TYPE_TRANSLATION;
		}
		if (a11 != 1.0 || a22 != 1.0) {
			bits |= (a11 == a22 ? TYPE_UNIFORM_SCALE : TYPE_GENERAL_SCALE);
		}
		return bits;
	}
}
