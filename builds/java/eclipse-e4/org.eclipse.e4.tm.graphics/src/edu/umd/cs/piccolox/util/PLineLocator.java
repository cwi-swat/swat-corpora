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

import org.eclipse.e4.tm.graphics.util.Point;

import edu.umd.cs.piccolox.nodes.PLine;

/**
 * <b>PLineLocator</b> ...
 * <P>
 * @version 1.0
 * @author Hallvard Traetteberg
 */
public class PLineLocator extends PAbstractLineLocator {
	
	protected PLine line;

	private int start, end;
	
	public PLineLocator(PLine line, int start, int end, double alongAlignment) {
		super(alongAlignment);
		this.line = line;
		this.start = start;
		this.end = end;
	}

	public PLineLocator(PLine line, int segment, double alongAlignment) {
		super(alongAlignment);
		this.line = line;
		if (segment < 0) {
			this.start = segment - 1;
		} else {
			this.start = segment;
		}
		this.end = this.start + 1;
	}
	
	//
	
	public PLine getLine() {
		return line;
	}
	
	public void setLine(PLine line) {
		this.line = line;
	}
	
	//

	private static Point tempPoint = new Point();
	
	private Point getSegmentPoint(int num) {
		Points points = line.getLineReference();
		if (num < 0) {
			num = points.getPointCount() + num;
		}
		if (num >= 0 && num < points.getPointCount()) {
			points.getPoint(num, tempPoint);
		} else {
			tempPoint.setLocation(0.0, 0.0);
		}
		return tempPoint;
	}
	
	public double getLineStartX() {
		return getSegmentPoint(start).getX();
	}
	public double getLineStartY() {
		return getSegmentPoint(start).getY();
	}
	public double getLineEndX() {
		return getSegmentPoint(end).getX();
	}
	public double getLineEndY() {
		return getSegmentPoint(end).getY();
	}
	
	//
	
	public static PLineLocator createStartLocator(PLine line, int start, int end) {
		return new PLineLocator(line, start, end, START);
	}
	public static PLineLocator createStartLocator(PLine line, int segment) {
		return new PLineLocator(line, segment, START);
	}
	
	public static PLineLocator createMiddleLocator(PLine line, int start, int end) {
		return new PLineLocator(line, start, end, MIDDLE);
	}
	public static PLineLocator createMiddleLocator(PLine line, int segment) {
		return new PLineLocator(line, segment, MIDDLE);
	}

	public static PLineLocator createEndLocator(PLine line, int start, int end) {
		return new PLineLocator(line, start, end, END);
	}
	public static PLineLocator createEndLocator(PLine line, int segment) {
		return new PLineLocator(line, segment, END);
	}
}
