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

import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.RGB;

import edu.umd.cs.piccolo.PNode;
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
public class PRect extends PNode {
	
	/** 
	 * The property name that identifies a change of this node's stroke paint
	 * (see {@link #getStrokePaint getStrokePaint}). Both old and new value will
	 * be set correctly to Paint objects in any property change event.
	 */
	public static final String PROPERTY_STROKE_PAINT = "strokePaint";
    public static final int PROPERTY_CODE_STROKE_PAINT = 1 << 16;

	private static final RGB DEFAULT_STROKE_PAINT = new RGB(0, 0, 0);

	/** 
	 * The property name that identifies a change of this node's stroke (see
	 * {@link #getStroke getStroke}). Both old and new value will be set
	 * correctly to Stroke objects in any property change event.
	 */
	public static final String PROPERTY_STROKE = "stroke";
    public static final int PROPERTY_CODE_STROKE = 1 << 17;

	private static final LineAttributes DEFAULT_STROKE = new LineAttributes(1.0f);

	private LineAttributes stroke;
	private Color strokePaint;
	
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
		invalidatePaint();
		firePropertyChange(PROPERTY_CODE_STROKE ,PROPERTY_STROKE, old, stroke);
	}
		
	public PRect() {
		strokePaint = createColor(DEFAULT_STROKE_PAINT);
		stroke = DEFAULT_STROKE;
	}
	
	//****************************************************************
	// Bounds
	//****************************************************************
	
	private Rectangle resizeBounds = null;
	
	public void startResizeBounds() {
		resizeBounds = new Rectangle(getBoundsReference());
	}

	public void endResizeBounds() {
		resizeBounds = null;
	}
	
	//****************************************************************
	// Painting
	//****************************************************************
	
	protected void paint(PPaintContext paintContext) {
		GC g2 = paintContext.getGraphics();
		Color p = getPaint();
		Rectangle r = getBoundsReference();
		
		if (p != null) {
			g2.setBackground(p);
			fillRect(g2, (int)(r.x + stroke.width / 2), (int)(r.y + stroke.width / 2), (int)(r.width - stroke.width), (int)(r.height - stroke.width));
		}

		if (stroke != null && strokePaint != null) {
			g2.setForeground(strokePaint);
			g2.setLineAttributes(stroke);
			drawRect(g2, (int)(r.x + stroke.width / 2), (int)(r.y + stroke.width / 2), (int)(r.width - stroke.width), (int)(r.height - stroke.width));
		}
	}

	protected void fillRect(GC g2, int x, int y, int width, int height) {
		g2.fillRectangle(x, y, width, height);
	}
	protected void drawRect(GC g2, int x, int y, int width, int height) {
		g2.drawRectangle(x, y, width, height);
	}
	
	//****************************************************************
	// Serialization
	//****************************************************************
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		PUtil.writeStroke(stroke, out); 	   
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();		
		stroke = PUtil.readStroke(in);
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

		result.append("stroke=" + (stroke == null ? "null" : stroke.toString()));
		result.append(",strokePaint=" + (strokePaint == null ? "null" : strokePaint.toString()));
		result.append(',');
		result.append(super.paramString());

		return result.toString();
	}	
}
