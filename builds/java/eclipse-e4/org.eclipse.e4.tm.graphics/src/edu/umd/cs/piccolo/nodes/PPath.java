/*
 * Copyright (c) 2002-@year@, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the University of Maryland nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Piccolo was written at the Human-Computer Interaction Laboratory www.cs.umd.edu/hcil by Jesse Grosjean
 * under the supervision of Ben Bederson. The Piccolo website is www.cs.umd.edu/hcil/piccolo.
 */
package edu.umd.cs.piccolo.nodes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PUtil;

/**
 * <b>PPath</b> is a wrapper around a org.eclipse.swt.graphics.Path. The
 * setBounds method works by scaling the path to fit into the specified
 * bounds. This normally works well, but if the specified base bounds
 * get too small then it is impossible to expand the path shape again since
 * all its numbers have tended to zero, so application code may need to take
 * this into consideration. 
 * <P>
 * One option that applications have is to call <code>startResizeBounds</code> before
 * starting an interaction that may make the bounds very small, and calling 
 * <code>endResizeBounds</code> when this interaction is finished. When this is done
 * PPath will use a copy of the original path to do the resizing so the numbers
 * in the path wont loose resolution.
 * <P>
 * This class also provides methods for constructing common shapes using a 
 * general path.
 * <P>
 * @version 1.0
 * @author Jesse Grosjean
 */
public class PPath extends PNode {
	
	/** 
	 * The property name that identifies a change of this node's stroke paint
	 * (see {@link #getStrokePaint getStrokePaint}). Both old and new value will
	 * be set correctly to Paint objects in any property change event.
	 */
	public static final String PROPERTY_STROKE_PAINT = "strokePaint";
    public static final int PROPERTY_CODE_STROKE_PAINT = 1 << 16;

	/** 
	 * The property name that identifies a change of this node's stroke (see
	 * {@link #getStroke getStroke}). Both old and new value will be set
	 * correctly to Stroke objects in any property change event.
	 */
	public static final String PROPERTY_STROKE = "stroke";
    public static final int PROPERTY_CODE_STROKE = 1 << 17;

	/** 
	 * The property name that identifies a change of this node's path (see
	 * {@link #getPathReference getPathReference}).  In any property change
	 * event the new value will be a reference to this node's path,  but old
	 * value will always be null.
	 */
	public static final String PROPERTY_PATH = "path";
    public static final int PROPERTY_CODE_PATH = 1 << 18;
	
	private static final PAffineTransform TEMP_TRANSFORM = new PAffineTransform();
	private static final LineAttributes DEFAULT_STROKE = new LineAttributes(1.0f);
	private static final RGB DEFAULT_STROKE_PAINT = new RGB(0, 0, 0);
	
	private transient Path path;
	private transient Path resizePath;
	private transient LineAttributes stroke;
	private transient boolean updatingBoundsFromPath;
	private Color strokePaint;

	private void initPath() {
		setPaint(createColor(SWT.COLOR_WHITE));
		updateBoundsFromPath();
	}
	
	public static PPath createRectangle(float x, float y, float width, float height) {
		PPath result = new PPath();
		result.path.addRectangle(x, y, width, height);
		result.initPath();
		return result;
	}

	public static PPath createRoundedRectangle(float x, float y, float width, float height, float radius) {
		PPath result = new PPath();
		float r = radius * 2;
//		float r2 = r * 2;
		result.path.moveTo(x + r, y);
		result.path.lineTo(x + width - r, y);
//		result.path.addArc(x + width - r2, y, r2, r2, 90, -90);
		result.path.quadTo(x + width, y, x + width, y + r);
		result.path.lineTo(x + width, y + height - r);
//		result.path.addArc(x + width - r2, y + height - r2, r2, r2, 0, -90);
		result.path.quadTo(x + width, y + height, x + width - r, y + height);
		result.path.lineTo(x + r, y + height);
//		result.path.addArc(x, y + height - r2, r2, r2, 270, -90);
		result.path.quadTo(x, y + height, x, y + height - r);
		result.path.lineTo(x, y + r);
//		result.path.addArc(x, y, r2, r2, 180, -90);
		result.path.quadTo(x, y, x + r, y);
		result.initPath();
		return result;
	}
		
	public static PPath createEllipse(float x, float y, float width, float height) {
		PPath result = new PPath();
		result.path.addArc(x, y, width, height, 0, 360);
		result.initPath();
		return result;
	}
	
	public static PPath createLine(float x1, float y1, float x2, float y2) {
		PPath result = new PPath();
		result.path.moveTo(x1, y1);
		result.path.lineTo(x2, y2);
		result.initPath();
		return result;
	}
	
	public static PPath createPolyline(Point[] points) {
		PPath result = new PPath();
		result.setPathToPolyline(points, false);
		result.initPath();
		return result;
	}

	public static PPath createPolyline(float[] xp, float[] yp) {
		PPath result = new PPath();
		result.setPathToPolyline(xp, yp, false);
		result.initPath();
		return result;
	}
		
	public static PPath createPolygon(Point[] points) {
		PPath result = new PPath();
		result.setPathToPolyline(points, true);
		result.initPath();
		return result;
	}
	
	public static PPath createPolygon(float[] xp, float[] yp) {
		PPath result = new PPath();
		result.setPathToPolyline(xp, yp, true);
		result.initPath();
		return result;
	}
	
	public PPath() {
		super();
		strokePaint = createColor(DEFAULT_STROKE_PAINT);
		stroke = DEFAULT_STROKE;
		path = new Path(Display.getCurrent());
	}

	public PPath(Path path) {
		this();
		this.path.addPath(path);
		initPath();
	}
	
	public void dispose() {
		super.dispose();
		path.dispose();
		path = null;
	}
	
	//****************************************************************
	// Stroke
	//****************************************************************
	
	public Color getStrokePaint() {
		return strokePaint;
	}

	public void setStrokePaint(Color aPaint) {
		Color old = strokePaint;
		strokePaint = aPaint;
		invalidatePaint();
		firePropertyChange(PROPERTY_CODE_STROKE_PAINT ,PROPERTY_STROKE_PAINT, old, strokePaint);
	}
	
	public LineAttributes getStroke() {
		return stroke;
	}

	public void setStroke(LineAttributes aStroke) {
		LineAttributes old = stroke;
		stroke = aStroke;
		updateBoundsFromPath();
		invalidatePaint();
		firePropertyChange(PROPERTY_CODE_STROKE ,PROPERTY_STROKE, old, stroke);
	}
		
	//****************************************************************
	// Bounds
	//****************************************************************
		
	public void startResizeBounds() {
		resizePath = new Path(Display.getCurrent());
	}

	public void endResizeBounds() {
		resizePath = null;
	}
				
	private static float[] boundsTemp = new float[4];

	public static void adjustForStroke(Rectangle dest, LineAttributes stroke) {
		double outset = (stroke != null ? stroke.width : 0) / 2;
		dest.setRect(dest.x - outset, dest.y - outset, dest.width + outset * 2 + 1, dest.height + outset * 2 + 1);
	}
	
	private Rectangle getPathBounds(Path path, Rectangle dest, LineAttributes stroke) {
		path.getBounds(boundsTemp);
		if (dest == null) {
			dest = new Rectangle();
		}
		dest.setRect(boundsTemp[0], boundsTemp[1], boundsTemp[2], boundsTemp[3]);
		adjustForStroke(dest, stroke);
		return dest;
	}
		
	private void resetPath() {
		Device device = path.getDevice();
		path.dispose();
		path = new Path(device);
	}
	
	/**
	 * Set the bounds of this path. This method works by scaling the path 
	 * to fit into the specified bounds. This normally works well, but if 
	 * the specified base bounds get too small then it is impossible to 
	 * expand the path shape again since all its numbers have tended to zero, 
	 * so application code may need to take this into consideration.
	 */
	protected void internalUpdateBounds(double x, double y, double width, double height) {
		if (updatingBoundsFromPath) return;
		if (path == null) return;
		
		if (resizePath != null) {
			resetPath();
			path.addPath(resizePath);
		}

		Rectangle pathBounds = getPathBounds(path, null, null);
			
		Rectangle pathStrokeBounds = getPathBoundsWithStroke();
		double strokeOutset = Math.max(pathStrokeBounds.getWidth() - pathBounds.getWidth(), 
										pathStrokeBounds.getHeight() - pathBounds.getHeight());
		
		x += strokeOutset / 2;
		y += strokeOutset / 2;
		width -= strokeOutset;
		height -= strokeOutset;
		
		double scaleX = (width == 0 || pathBounds.getWidth() == 0) ? 1 : width / pathBounds.getWidth();
		double scaleY = (height == 0 || pathBounds.getHeight() == 0) ? 1 : height / pathBounds.getHeight();
		
		TEMP_TRANSFORM.setToIdentity();
		TEMP_TRANSFORM.translate(x, y);
		TEMP_TRANSFORM.scale(scaleX, scaleY);
		TEMP_TRANSFORM.translate(-pathBounds.getX(), -pathBounds.getY());
		//TODO
//		path.transform(TEMP_TRANSFORM);
	}
	
	public Rectangle getPathBoundsWithStroke() {
		return getPathBounds(path, null, stroke);
	}
			
	public void updateBoundsFromPath() {
		updatingBoundsFromPath = true;
		if (path == null) {
			resetBounds();
		} else {
			setBounds(getPathBoundsWithStroke());
		}
		updatingBoundsFromPath = false;
	}
	
	//****************************************************************
	// Painting
	//****************************************************************
	
	protected void paint(PPaintContext paintContext) {
//		System.out.println("PPath: Painting " + this);
		GC g2 = paintContext.getGraphics();
		Color p = getPaint();
		
		if (p != null) {
			g2.setBackground(p);
			g2.fillPath(path);
		}

		if (stroke != null && strokePaint != null) {
			g2.setForeground(strokePaint);
			g2.setLineAttributes(stroke);
			g2.drawPath(path);
		}
	}
		
	//****************************************************************
	// Path Support set org.eclipse.swt.graphics.Path documentation for more
	// information on using these methods.
	//****************************************************************

	public Path getPathReference() {
		return path;
	}
	
	public void moveTo(float x, float y) {
		path.moveTo(x, y);
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}
	
	public void lineTo(float x, float y) {
		path.lineTo(x, y);
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}

	public void quadTo(float x1, float y1, float x2, float y2) {
		path.quadTo(x1, y1, x2, y2);
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}

	public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
		path.cubicTo(x1, y1, x2, y2, x3, y3);
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}
	
	public void append(Path path) {
		path.addPath(path);
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}
	
	public void setPathTo(Path path) {
		resetPath();
		append(path);
	}

	public void setPathToRectangle(float x, float y, float width, float height) {
		resetPath();
		path.addRectangle(x, y, width, height);
	}

	public void setPathToEllipse(float x, float y, float width, float height) {
		resetPath();
		path.addArc(x, y, width, height, 0, 360);
	}

	public void setPathToPolyline(Point[] points, boolean close) {
		resetPath();
		path.moveTo((float)points[0].getX(), (float)points[0].getY());
		for (int i = 1; i < points.length; i++) {
			path.lineTo((float)points[i].getX(), (float)points[i].getY());
		}
		if (close) {
			path.close();
		}
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();		
	}

	public void setPathToPolyline(float[] xp, float[] yp, boolean close) {
		resetPath();
		path.moveTo(xp[0], yp[0]);
		for (int i = 1; i < xp.length; i++) {
			path.lineTo(xp[i], yp[i]);
		}
		if (close) {
			path.close();
		}
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}
	
	public void closePath() {
		path.close();
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}
	
	public void reset() {
		resetPath();
		firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path);
		updateBoundsFromPath();
		invalidatePaint();
	}
	
	//****************************************************************
	// Serialization
	//****************************************************************
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		PUtil.writeStroke(stroke, out); 	   
		PUtil.writePath(path, out); 	   
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();		
		stroke = PUtil.readStroke(in);
		path = PUtil.readPath(in);		
	}
	
	//****************************************************************
	// Debugging - methods for debugging
	//****************************************************************
	
	/**
	 * Returns a string representing the state of this node. This method is
	 * intended to be used only for debugging purposes, and the content and
	 * format of the returned string may vary between implementations. The
	 * returned string may be empty but may not be <code>null</code>.
	 *
	 * @return  a string representation of this node's state
	 */
	protected String paramString() {
		StringBuffer result = new StringBuffer();

		result.append("path=" + (path == null ? "null" : path.toString()));
		result.append(",stroke=" + (stroke == null ? "null" : stroke.toString()));
		result.append(",strokePaint=" + (strokePaint == null ? "null" : strokePaint.toString()));
		result.append(',');
		result.append(super.paramString());

		return result.toString();
	}	
}
