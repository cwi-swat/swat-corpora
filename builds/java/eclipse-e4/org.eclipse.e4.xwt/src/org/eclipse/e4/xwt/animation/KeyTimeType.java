/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.animation;

public enum KeyTimeType {
	/**
	 * Uniform Specifies that the allotted total time for an animation sequence
	 * is divided evenly amongst each of the key frames.
	 */
	Uniform,
	/**
	 * Percent Specifies that each KeyTime value is expressed as a percentage of
	 * the total time allotted for a given animation sequence.
	 */
	Percent,
	/**
	 * TimeSpan Specifies that each KeyTime is expressed as a TimeSpan value
	 * relative to the BeginTime of an animation sequence.
	 */
	TimeSpan,
	/**
	 * Paced Specifies that adjacent KeyFrames are each allotted a slice of time
	 * proportional to their length, respectively. The overall goal is to
	 * produce a length value that keeps the pace of the animation sequence
	 * constant.
	 */
	Paced;
}
