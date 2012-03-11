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

import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.animation.internal.ITimeline;
import org.eclipse.e4.xwt.animation.internal.TridentTimeline;

public class DoubleAnimation extends DoubleAnimationBase {
	private Double by;
	private Double from;
	private Double to;
	private IEasingFunction easingFunction;
	
	/**
	 * The <code>additive</code> property specifies whether you want the output value of 
	 * an animation added to the starting value (base value) of an animated property. You 
	 * can use the <code>additive</code> property with most basic animations and most key 
	 * frame animations. 
	 * 
	 * For more information, see Animation Overview and Key-Frame Animations Overview.
	 */
	protected boolean additive = false;
	
	/**
	 * Use the <code>cumulative</code> property to accumulate base values of an animation 
	 * across repeating cycles. For example, if you set an animation to repeat 9 times 
	 * (RepeatBehavior = “9x”) and you set the property to animate between 10 and 15 
	 * (From = 10 To = 15), the property animates from 10 to 15 during the first cycle, 
	 * from 15 to 20 during the second cycle, from 20 to 25 during the third cycle, 
	 * and so on. Hence, each animation cycle uses the ending animation value from the 
	 * previous animation cycle as its base value.
	 * 
	 * You can use the <code>cumulative</code> property with most basic animations and 
	 * most key frame animations. 
	 * 
	 * For more information, see Animation Overview and Key-Frame Animations Overview.
	 */
	protected boolean cumulative = false;

	public DoubleAnimation() {
	}
	
	public DoubleAnimation(Double toValue, Duration duration) {
		setTo(toValue);
		setDuration(duration);
	}
	
	public DoubleAnimation(Double fromValue, Double toValue, Duration duration) {
		setTo(toValue);
		setFrom(fromValue);
		setDuration(duration);
	}
	
	public DoubleAnimation(Double toValue, Duration duration, FillBehavior fillBehavior) {
		setTo(toValue);
		setDuration(duration);
		setFillBehavior(fillBehavior);
	}
	
	public DoubleAnimation(Double fromValue, Double toValue, Duration duration, FillBehavior fillBehavior) {
		setTo(toValue);
		setFrom(fromValue);
		setDuration(duration);
		setFillBehavior(fillBehavior);
	}

	public IEasingFunction getEasingFunction() {
		return easingFunction;
	}

	public void setEasingFunction(IEasingFunction easingFunction) {
		this.easingFunction = easingFunction;
	}

	public Double getBy() {
		return by;
	}

	public void setBy(Double by) {
		this.by = by;
	}

	public Double getFrom() {
		return from;
	}

	public void setFrom(Double from) {
		this.from = from;
	}

	public Double getTo() {
		return to;
	}

	public void setTo(Double to) {
		this.to = to;
	}

	public boolean isAdditive() {
		return additive;
	}

	public void setAdditive(boolean additive) {
		this.additive = additive;
	}

	public boolean isCumulative() {
		return cumulative;
	}

	public void setCumulative(boolean cumulative) {
		this.cumulative = cumulative;
	}
	
	protected void updateTimeline(ITimeline timeline, Object target) {
		super.updateTimeline(timeline, target);
		if (timeline instanceof TridentTimeline) {
			TridentTimeline tridentTimeline = (TridentTimeline) (timeline);
			Double from = getFrom();
			Double to = getTo();
			if (from == null && to == null) {
				from = (Double) getCacheValue();
				to = (Double) getCurrentValue(target);
				if (from != null && from.equals(to)) {
					throw new XWTException("action ignored");
				}
			}
			tridentTimeline.addPropertyToInterpolate(getTargetProperty(), from, to);
			tridentTimeline.setEasingFunction(getEasingFunction());
		}
	}
}
