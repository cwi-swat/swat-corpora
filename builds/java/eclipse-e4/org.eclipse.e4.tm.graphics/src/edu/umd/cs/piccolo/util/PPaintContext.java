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
package edu.umd.cs.piccolo.util;

import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.e4.tm.graphics.util.Transform;
import org.eclipse.swt.graphics.GC;

import edu.umd.cs.piccolo.PCamera;

/**
* <b>PPaintContext</b> is used by piccolo nodes to paint themselves on the screen.
 * PPaintContext wraps a Graphics2D to implement painting.
 * <P>
 * @version 1.0
 * @author Jesse Grosjean
 */
public class PPaintContext {

	public static final int LOW_QUALITY_RENDERING = 0;
	public static final int HIGH_QUALITY_RENDERING = 1;
	
//	public static FontRenderContext RENDER_QUALITY_LOW_FRC = new FontRenderContext(null, false, true);
//	public static FontRenderContext RENDER_QUALITY_HIGH_FRC = new FontRenderContext(null, true, true);	
	public static PPaintContext CURRENT_PAINT_CONTEXT;
	
	private GC graphics;
	protected PStack compositeStack;
	protected PStack clipStack;
	protected PStack localClipStack;
	protected PStack cameraStack;
	protected PStack transformStack;
	protected int renderQuality;
		
	public PPaintContext(GC aGraphics) {
		super();
		graphics = aGraphics;
		compositeStack = new PStack();
		clipStack = new PStack();
		localClipStack = new PStack();
		cameraStack = new PStack();
		transformStack = new PStack();
//		renderQuality = HIGH_QUALITY_RENDERING;
		
		org.eclipse.swt.graphics.Rectangle clip = aGraphics.getClipping();
		if (clip == null) {
			clip = new org.eclipse.swt.graphics.Rectangle(
				-Integer.MAX_VALUE / 2, 
				-Integer.MAX_VALUE / 2, 
				Integer.MAX_VALUE,
				Integer.MAX_VALUE);
			aGraphics.setClipping(clip);
		}
		
		localClipStack.push(new Rectangle(clip.x, clip.y, clip.width, clip.height));
		
		CURRENT_PAINT_CONTEXT = this;
	}
	
	public GC getGraphics() {
		return graphics;
	}

	//****************************************************************
	// Context Attributes.
	//****************************************************************
	
	public Rectangle getLocalClip() {
		return (Rectangle) localClipStack.peek();
	}
	
	private static double[] tempDoubles4 = new double[4];	
	private static org.eclipse.swt.graphics.Transform tempSwtTransform = new org.eclipse.swt.graphics.Transform(null);
	private static float[] tempFloats6 = new float[6];
	private static Transform tempTransform = new Transform();
	
	public double getScale() {
		tempDoubles4[0] = 0;//x1
		tempDoubles4[1] = 0;//y1
		tempDoubles4[2] = 1;//x2
		tempDoubles4[3] = 0;//y2
		Transform t = getTransform(graphics, tempTransform);
		t.transform(tempDoubles4, 0, tempDoubles4, 0, 2);
		return Point.distance(tempDoubles4[0], tempDoubles4[1], tempDoubles4[2], tempDoubles4[3]);
	}
	
	public static Transform getTransform(GC graphics, Transform dest) {
		if (dest == null) {
			dest = new Transform();
		}
		graphics.getTransform(tempSwtTransform);
		tempSwtTransform.getElements(tempFloats6);
		return dest.setTransform(tempFloats6[0], tempFloats6[2], tempFloats6[1], tempFloats6[3], tempFloats6[4], tempFloats6[5]);
	}

	public static org.eclipse.swt.graphics.Transform setTransform(Transform transform, GC graphics) {
		org.eclipse.swt.graphics.Transform swtTransform = new org.eclipse.swt.graphics.Transform(null, (float)transform.a11, (float)transform.a12, (float)transform.a21, (float)transform.a22, (float)transform.b1, (float)transform.b2);
		graphics.setTransform(swtTransform);
		return swtTransform;
	}
	
	//****************************************************************
	// Context Attribute Stacks. attributes that can be pushed and
	// popped.
	//****************************************************************

	public void pushCamera(PCamera aCamera) {
		cameraStack.push(aCamera);
	}
	
	public void popCamera(PCamera aCamera) {
		cameraStack.pop();
	}

	public PCamera getCamera() {
		return (PCamera) cameraStack.peek();
	}
	
	public void pushClip(Rectangle aClip) {
		org.eclipse.swt.graphics.Rectangle currentClip = graphics.getClipping();
		clipStack.push(new Rectangle(currentClip.x, currentClip.y, currentClip.width, currentClip.height));

		Rectangle newLocalClip = new Rectangle(aClip);
		Rectangle.intersect(getLocalClip(), newLocalClip, newLocalClip);		

		graphics.setClipping((int)newLocalClip.x, (int)newLocalClip.y, (int)newLocalClip.width, (int)newLocalClip.height);
		
		localClipStack.push(newLocalClip);
	}
	
	public void popClip(Rectangle aClip) {
		Rectangle newClip = (Rectangle) clipStack.pop();
		graphics.setClipping((int)newClip.x, (int)newClip.y, (int)newClip.width, (int)newClip.height);
		localClipStack.pop();
	}

	public void pushTransparency(float transparency) {
		if (transparency == 1) {
			return;
		}
		int current = graphics.getAlpha();
		compositeStack.push(current);

		graphics.setAlpha((int)(transparency * 255));
	}

	public void popTransparency(float transparency) {
		if (transparency == 1) {
			return;
		}
		int c = (Integer) compositeStack.pop();
		graphics.setAlpha(c);
	}

	public void pushTransform(PAffineTransform aTransform) {
		if (aTransform == null) return;
		Rectangle newLocalClip = (Rectangle) getLocalClip().clone();
		aTransform.inverseTransform(newLocalClip, newLocalClip);
		Transform transform = getTransform(graphics, tempTransform);
		transformStack.push(new Transform(transform));
		localClipStack.push(newLocalClip);
		setTransform(transform.concatenate(aTransform), graphics);
	}

	public void popTransform(PAffineTransform aTransform) {
		if (aTransform == null) return;
		Transform transform = (Transform)transformStack.pop();
		graphics.setTransform(new org.eclipse.swt.graphics.Transform(graphics.getDevice(), (float)transform.a11, (float)transform.a12, (float)transform.a21, (float)transform.a22, (float)transform.b1, (float)transform.b2));
		localClipStack.pop();
	}

	//****************************************************************
	// Render Quality.
	//****************************************************************/
	
	/**
	 * Return the render quality used by this paint context.
	 */
	public int getRenderQuality() {
		return renderQuality;
	}
	
	/**
	 * Set the rendering hints for this paint context. The render quality is most
	 * often set by the rendering PCanvas. Use PCanvas.setRenderQuality() and
	 * PCanvas.setInteractingRenderQuality() to set these values.
	 * 
	 * @param requestedQuality supports PPaintContext.HIGH_QUALITY_RENDERING or PPaintContext.LOW_QUALITY_RENDERING
	 */
	public void setRenderQuality(int requestedQuality) {
		renderQuality = requestedQuality;
		
		switch (renderQuality) {
			case HIGH_QUALITY_RENDERING:
//				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//				graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON); 			
				break;

			case LOW_QUALITY_RENDERING:
//				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//				graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
//				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
//				graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
				break;				
		}
	}	
}
