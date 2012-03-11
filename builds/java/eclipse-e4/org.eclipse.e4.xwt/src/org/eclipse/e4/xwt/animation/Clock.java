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

public class Clock {
	protected Clock parent;
	protected boolean isPaused = false;
	protected Duration naturalDuration;
	protected Timeline currentTime;
	protected double currentGlobalSpeed;
	protected TimeSpan currentGlobalTime;
	protected int currentIteration;
	protected double currentProgress;
	protected ClockState currentState;
	protected ClockController controller;
	protected boolean hasControllableRoot;

	public Clock getParent() {
		return parent;
	}
	public void setParent(Clock parent) {
		this.parent = parent;
	}
	public boolean isPaused() {
		return isPaused;
	}
	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
	public Duration getNaturalDuration() {
		return naturalDuration;
	}
	public void setNaturalDuration(Duration naturalDuration) {
		this.naturalDuration = naturalDuration;
	}
	public Timeline getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(Timeline currentTime) {
		this.currentTime = currentTime;
	}
	public double getCurrentGlobalSpeed() {
		return currentGlobalSpeed;
	}
	public void setCurrentGlobalSpeed(double currentGlobalSpeed) {
		this.currentGlobalSpeed = currentGlobalSpeed;
	}
	public TimeSpan getCurrentGlobalTime() {
		return currentGlobalTime;
	}
	public void setCurrentGlobalTime(TimeSpan currentGlobalTime) {
		this.currentGlobalTime = currentGlobalTime;
	}
	public int getCurrentIteration() {
		return currentIteration;
	}
	public void setCurrentIteration(int currentIteration) {
		this.currentIteration = currentIteration;
	}
	public double getCurrentProgress() {
		return currentProgress;
	}
	public void setCurrentProgress(double currentProgress) {
		this.currentProgress = currentProgress;
	}
	public ClockState getCurrentState() {
		return currentState;
	}
	public void setCurrentState(ClockState currentState) {
		this.currentState = currentState;
	}
	public ClockController getController() {
		return controller;
	}
	public void setController(ClockController controller) {
		this.controller = controller;
	}
	public boolean isHasControllableRoot() {
		return this.hasControllableRoot;
	}
	public void setHasControllableRoot(boolean hasControllableRoot) {
		this.hasControllableRoot = hasControllableRoot;
	}	
}
