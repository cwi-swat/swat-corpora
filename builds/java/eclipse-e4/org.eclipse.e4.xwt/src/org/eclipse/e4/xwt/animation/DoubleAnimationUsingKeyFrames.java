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

public class DoubleAnimationUsingKeyFrames extends DoubleAnimationBase {
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
	private boolean cumulative = false;
	
	/**
	 * The <code>additive</code> property specifies whether you want the output value of 
	 * an animation added to the starting value (base value) of an animated property. You 
	 * can use the <code>additive</code> property with most basic animations and most key 
	 * frame animations. 
	 * 
	 * For more information, see Animation Overview and Key-Frame Animations Overview.
	 */
	private boolean additive = false;
	private DoubleKeyFrame[] keyFrames = DoubleKeyFrame.EMPTY_ARRAY;
	
	public DoubleKeyFrame[] getKeyFrames() {
		return keyFrames;
	}

	public void setKeyFrames(DoubleKeyFrame[] keyFrames) {
		this.keyFrames = keyFrames;
	}

	public boolean isCumulative() {
		return cumulative;
	}

	public void setCumulative(boolean cumulative) {
		this.cumulative = cumulative;
	}

	public boolean isAdditive() {
		return additive;
	}

	public void setAdditive(boolean additive) {
		this.additive = additive;
	}

	protected void updateTimeline(ITimeline timeline, Object target) {
		super.updateTimeline(timeline, target);
//		for (DoubleKeyFrame doubleKeyFrame : getKeyFrames()) {
//		doubleKeyFrame.start(timeline, target);
	}
}
