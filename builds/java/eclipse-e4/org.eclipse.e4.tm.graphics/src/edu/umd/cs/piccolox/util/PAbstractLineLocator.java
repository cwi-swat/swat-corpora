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
 */package edu.umd.cs.piccolox.util;

import edu.umd.cs.piccolo.util.PLocator;

 /**
  * <b>PAbstractLineLocator</b> ...
  * <P>
  * @version 1.0
  * @author Hallvard Traetteberg
  */
public abstract class PAbstractLineLocator extends PLocator {
	
	private double lineAlignment, alongOffset, perpendicularOffset;
	
	public final static double START = 0.0f, MIDDLE = 0.5f, CENTER = MIDDLE, END = 1.0f;
	
	public PAbstractLineLocator(double lineAlignment) {
		super();
		this.lineAlignment = lineAlignment;
	}

	//
	
	public void setLineOffset(double lineAlignment) {
		this.lineAlignment = lineAlignment;
	}

	public void setLineOffset(double alongOffset, double perpendicularOffset) {
		this.alongOffset = alongOffset;
		this.perpendicularOffset = perpendicularOffset;
	}
	
	//

	public abstract double getLineStartX();
	public abstract double getLineStartY();
	public abstract double getLineEndX();
	public abstract double getLineEndY();
	
	public double locateX() {
		double x = getLineStartX(), y = getLineStartY(), dx = getLineEndX() - x, dy = getLineEndY() - y;
		double e = Math.sqrt(dx * dx + dy * dy);
		return x + dx * lineAlignment + alongOffset * dx / e - perpendicularOffset * dy / e + offsetX;
	}

	public double locateY() {
		double x = getLineStartX(), y = getLineStartY(), dx = getLineEndX() - x, dy = getLineEndY() - y;
		double e = Math.sqrt(dx * dx + dy * dy);
		return y + dy * lineAlignment + alongOffset * dy / e + perpendicularOffset * dx / e + offsetY;
	}
}
