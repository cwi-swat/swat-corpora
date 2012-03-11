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
import org.eclipse.e4.xwt.animation.internal.TridentTimeline;

/**
 * 
 * @author yyang
 */
public class IntAnimation extends AnimationTimeline {
	private Integer from;
	private Integer by;
	private Integer to;

	private IEasingFunction easingFunction;

	public IEasingFunction getEasingFunction() {
		return easingFunction;
	}

	public void setEasingFunction(IEasingFunction easingFunction) {
		this.easingFunction = easingFunction;
	}

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public Integer getBy() {
		return by;
	}

	public void setBy(Integer by) {
		this.by = by;
	}

	protected void updateTimeline(ITimeline timeline, Object target) {
		super.updateTimeline(timeline, target);
		if (timeline instanceof TridentTimeline) {
			TridentTimeline tridentTimeline = (TridentTimeline) (timeline);
			Integer from = getFrom();
			Integer to = getTo();
			if (from == null && to == null) {
				from = (Integer) getCacheValue();
				to = (Integer) getCurrentValue(target);
				if (from != null && from.equals(to)) {
					return;
				}
			}
			tridentTimeline.addPropertyToInterpolate(getTargetProperty(), from,
					to);
			tridentTimeline.setEasingFunction(getEasingFunction());
		}
	}
}
