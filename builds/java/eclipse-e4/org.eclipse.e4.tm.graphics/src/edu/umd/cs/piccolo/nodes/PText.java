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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * <b>PText</b> is a multi-line text node. The text will flow to base
 * on the width of the node's bounds.
 * <P>
 * @version 1.1
 * @author Jesse Grosjean
 */
public class PText extends PNode {
	
	/** 
	 * The property name that identifies a change of this node's text (see
	 * {@link #getText getText}). Both old and new value will be set in any
	 * property change event. 
	 */
	public static final String PROPERTY_TEXT = "text";
    public static final int PROPERTY_CODE_TEXT = 1 << 19;
	
	/** 
	 * The property name that identifies a change of this node's font (see
	 * {@link #getFont getFont}).  Both old and new value will be set in any
	 * property change event.
	 */
	public static final String PROPERTY_FONT = "font";
    public static final int PROPERTY_CODE_FONT = 1 << 20;

	public static Font DEFAULT_FONT = new Font(Display.getCurrent(), "Helvetica", 12, SWT.NORMAL);
	public static double DEFAULT_GREEK_THRESHOLD = 5.5;
	
	private String text;
	private Color textPaint;
	private Font font;
	protected double greekThreshold = DEFAULT_GREEK_THRESHOLD;
	private float justification = 0.0f;
	private boolean constrainHeightToTextHeight = true;
	private boolean constrainWidthToTextWidth = true;
	private transient String[] lines;
    
	public PText() {
		super();
		setTextPaint(createColor(SWT.COLOR_BLACK));
	}

	public PText(String aText) {
		this();
		setText(aText);
	}
	
	/**
	 * Return the justificaiton of the text in the bounds.
	 * @return float
	 */
	public float getJustification() {
		return justification;
	}

	/**
     * Sets the justificaiton of the text in the bounds.
	 * @param just
	 */	
	public void setJustification(float just) {
		justification = just;
		recomputeLayout();
	}

	/**
	 * Get the paint used to paint this nodes text.
	 * @return Color
	 */
	public Color getTextPaint() {
		return textPaint;
	}

	/**
	 * Set the paint used to paint this node's text background.
	 * @param textPaint
	 */		
	public void setTextPaint(Color textPaint) {
		this.textPaint = textPaint;
		invalidatePaint();
	}

    public boolean isConstrainWidthToTextWidth() {
        return constrainWidthToTextWidth;
    }

	/**
	 * Controls whether this node changes its width to fit the width 
	 * of its text. If flag is true it does; if flag is false it doesn't
	 */
	public void setConstrainWidthToTextWidth(boolean constrainWidthToTextWidth) {
		this.constrainWidthToTextWidth = constrainWidthToTextWidth;
		recomputeLayout();
	}

    public boolean isConstrainHeightToTextHeight() {
        return constrainHeightToTextHeight;
    }

	/**
	 * Controls whether this node changes its height to fit the height 
	 * of its text. If flag is true it does; if flag is false it doesn't
	 */
	public void setConstrainHeightToTextHeight(boolean constrainHeightToTextHeight) {
		this.constrainHeightToTextHeight = constrainHeightToTextHeight;
		recomputeLayout();
	}

	/**
	 * Returns the current greek threshold. When the screen font size will be below
	 * this threshold the text is rendered as 'greek' instead of drawing the text
	 * glyphs.
	 */
	public double getGreekThreshold() {
		return greekThreshold;
	}

	/**
	 * Sets the current greek threshold. When the screen font size will be below
	 * this threshold the text is rendered as 'greek' instead of drawing the text
	 * glyphs.
	 * 
	 * @param threshold minimum screen font size.
	 */
	public void setGreekThreshold(double threshold) {
		greekThreshold = threshold;
		invalidatePaint();
	}
		
	public String getText() {
		return text;
	}

	/**
	 * Set the text for this node. The text will be broken up into multiple
	 * lines based on the size of the text and the bounds width of this node.
	 */
	public void setText(String aText) {
		String old = text;
		text = aText;
		lines = null;
		recomputeLayout();
		invalidatePaint();
		firePropertyChange(PROPERTY_CODE_TEXT, PROPERTY_TEXT, old, text);
	}
	
	/**
	 * Returns the font of this PText.
	 * @return the font of this PText.
	 */ 
	public Font getFont() {
		if (font == null) {
			font = DEFAULT_FONT;
		}
		return font;
	}
	
	/**
	 * Set the font of this PText. Note that in Piccolo if you want to change
	 * the size of a text object it's often a better idea to scale the PText
	 * node instead of changing the font size to get that same effect. Using
	 * very large font sizes can slow performance.
	 */
	public void setFont(Font aFont) {
		Font old = font;
		font = aFont;
		lines = null;
		recomputeLayout();
		invalidatePaint();
		firePropertyChange(PROPERTY_CODE_FONT, PROPERTY_FONT, old, font);
	}

	private GC fontMetricsGc = null;
	
	/**
	 * Compute the bounds of the text wrapped by this node. The text layout
	 * is wrapped based on the bounds of this node.
	 */
	public void recomputeLayout() {
		List linesList = new ArrayList();
		double textWidth = 0;
		double textHeight = 0;

		if (text != null && text.length() > 0) {		
			
			if (fontMetricsGc == null) {
				fontMetricsGc = new GC(Display.getCurrent());
			}
			fontMetricsGc.setFont(font);
			FontMetrics fontMetrics = fontMetricsGc.getFontMetrics();
			int spacing = fontMetricsGc.stringExtent(" ").x;
			float availableWidth = constrainWidthToTextWidth ? Float.MAX_VALUE : (float) getWidth();

			lines = text.split("\\s");
			StringBuilder line = new StringBuilder();
			for (int pos = 0, width = 0; true; pos++) {
				// new token, add single space to line
				if (line.length() > 0) {
					line.append(" ");
					width += spacing;
				}
				org.eclipse.swt.graphics.Point extent = (pos < lines.length ? fontMetricsGc.stringExtent(lines[pos]) : null);
				// if new line collected
				if (pos >= lines.length || (width + extent.x >= availableWidth && line.length() > 0)) {
					linesList.add(line.toString());
					textWidth = Math.max(textWidth, width);
					textHeight += fontMetrics.getHeight();
					line.setLength(0);
					width = 0;
					if (pos >= lines.length) {
						break;
					}
				}
				line.append(lines[pos]);
				width += extent.x;
			}
			lines = (String[])linesList.toArray(new String[linesList.size()]);
		}
					
		if (constrainWidthToTextWidth || constrainHeightToTextHeight) {
			double newWidth = getWidth(), newHeight = getHeight();
			
			if (constrainWidthToTextWidth) {
				newWidth = textWidth;
			}
			if (constrainHeightToTextHeight) {
				newHeight = textHeight;
			}
			super.setBounds(getX(), getY(), newWidth, newHeight);
		}	
	}
		
	protected void paint(PPaintContext paintContext) {		
		super.paint(paintContext);
		GC g2 = paintContext.getGraphics();
		FontMetrics fontMetrics = g2.getFontMetrics();
		float screenFontSize = fontMetrics.getHeight() * (float) paintContext.getScale();
		if (textPaint != null && screenFontSize > greekThreshold) {
			float x = (float) getX();
			float y = (float) getY();
			float bottomY = (float) getHeight() + y;
			
			if (lines == null) {
				recomputeLayout();
				repaint();
				return;
			}

			g2.setForeground(textPaint);
			
			for (int i = 0; i < lines.length; i++) {
                String tl = lines[i];
				if (bottomY < y) {
					return;
				}
                float offset = (float) (getWidth() - g2.stringExtent(tl).x) * justification;
                g2.drawString(tl, (int)(x + offset), (int)y);
	
                y += fontMetrics.getHeight();
			}
		}
	}
	
	protected void internalUpdateBounds(double x, double y, double width, double height) {
		recomputeLayout();
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

		result.append("text=" + (text == null ? "null" : text));
		result.append(",font=" + (font == null ? "null" : font.toString()));
		result.append(',');
		result.append(super.paramString());

		return result.toString();
	}
}
