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

import org.eclipse.e4.xwt.animation.internal.ITimeline;

/**
 * 
 * @author yyang
 */
public abstract class Timeline {
	static public Timeline[] EMPTY_ARRAY = new Timeline[0];

	protected int desiredFrameRate = 0;
	protected double accelerationRatio = 0;
	protected boolean autoReverse = false;
	protected TimeSpan beginTime = null;
	protected double decelerationRatio = 0;
	protected Duration duration = Duration.getAutomatic();
	protected FillBehavior fillBehavior = FillBehavior.HoldEnd;
	protected String name;
	protected double speedRatio = 0;
	protected RepeatBehavior repeatBehavior = RepeatBehavior.once;

	public int getDesiredFrameRate() {
		return desiredFrameRate;
	}

	public void setDesiredFrameRate(int desiredFrameRate) {
		this.desiredFrameRate = desiredFrameRate;
	}

	public double getAccelerationRatio() {
		return accelerationRatio;
	}

	public void setAccelerationRatio(double accelerationRatio) {
		this.accelerationRatio = accelerationRatio;
	}

	public boolean isAutoReverse() {
		return autoReverse;
	}

	public void setAutoReverse(boolean autoReverse) {
		this.autoReverse = autoReverse;
	}

	public TimeSpan getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(TimeSpan beginTime) {
		this.beginTime = beginTime;
	}

	public double getDecelerationRatio() {
		return decelerationRatio;
	}

	public void setDecelerationRatio(double decelerationRatio) {
		this.decelerationRatio = decelerationRatio;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public FillBehavior getFillBehavior() {
		return fillBehavior;
	}

	public void setFillBehavior(FillBehavior fillBehavior) {
		this.fillBehavior = fillBehavior;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSpeedRatio() {
		return speedRatio;
	}

	public void setSpeedRatio(double speedRatio) {
		this.speedRatio = speedRatio;
	}

	public RepeatBehavior getRepeatBehavior() {
		return repeatBehavior;
	}

	public void setRepeatBehavior(RepeatBehavior repeatBehavior) {
		this.repeatBehavior = repeatBehavior;
	}

	protected void updateTimeline(ITimeline timeline, Object target) {
	}

	protected void initialize(Object target) {
	}

	protected void endFinalize(Object target) {
	}
	
	protected Object findTarget(Object target) {
		return target;
	}
}
