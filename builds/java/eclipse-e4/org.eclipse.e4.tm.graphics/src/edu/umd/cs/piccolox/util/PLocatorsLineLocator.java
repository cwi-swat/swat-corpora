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
 * <b>PLocatorsLineLocator</b> ...
 * <P>
 * @version 1.0
 * @author Hallvard Traetteberg
 */
public class PLocatorsLineLocator extends PAbstractLineLocator {
	
	protected PLocator locator1, locator2;

	public PLocatorsLineLocator(PLocator locator1, PLocator locator2, double lineAlignment) {
		super(lineAlignment);
		this.locator1 = locator1;
		this.locator2 = locator2;
	}

	//

	public double getLineStartX() {
		return locator1.locateX();
	}
	public double getLineStartY() {
		return locator1.locateY();
	}
	public double getLineEndX() {
		return locator2.locateY();
	}
	public double getLineEndY() {
		return locator2.locateY();
	}
	
	//
	
	public static PLocatorsLineLocator createStartLocator(PLocator locator1, PLocator locator2) {
		return new PLocatorsLineLocator(locator1, locator2, START);
	}
	
	public static PLocatorsLineLocator createMiddleLocator(PLocator locator1, PLocator locator2) {
		return new PLocatorsLineLocator(locator1, locator2, MIDDLE);
	}

	public static PLocatorsLineLocator createEndLocator(PLocator locator1, PLocator locator2) {
		return new PLocatorsLineLocator(locator1, locator2, END);
	}
}
