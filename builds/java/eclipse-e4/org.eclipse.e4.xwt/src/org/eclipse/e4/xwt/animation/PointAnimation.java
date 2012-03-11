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
import org.eclipse.e4.xwt.animation.interpolator.PointPropertyInterpolator;
import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.TridentConfig;

/**
 * 
 * @author yyang
 */
public class PointAnimation extends AnimationTimeline {
	private Point from;
	private Point to;
	private Point by;
	private IEasingFunction easingFunction;

	static {
		TridentConfig.getInstance().addPropertyInterpolator(new PointPropertyInterpolator());
	}

	public IEasingFunction getEasingFunction() {
		return easingFunction;
	}

	public void setEasingFunction(IEasingFunction easingFunction) {
		this.easingFunction = easingFunction;
	}

	public Point getFrom() {
		return from;
	}

	public void setFrom(Point from) {
		this.from = from;
	}

	public Point getTo() {
		return to;
	}

	public void setTo(Point to) {
		this.to = to;
	}
	
	protected void updateTimeline(ITimeline timeline, Object target) {
		super.updateTimeline(timeline, target);
		if (timeline instanceof TridentTimeline) {
			TridentTimeline tridentTimeline = (TridentTimeline) (timeline);
			Point from = getFrom();
			Point to = getTo();
			if (from == null && to == null) {
				from = (Point) getCacheValue();
				to = (Point) getCurrentValue(target);
				if (from.x == 0 && from.y == 0) {
					setCacheValue(to);
					throw new XWTException("action ignored");
				}
				if (from != null && from.equals(to)) {
					throw new XWTException("action ignored");
				}
			}
			tridentTimeline.addPropertyToInterpolate(getTargetProperty(), from, to);
			tridentTimeline.setEasingFunction(getEasingFunction());
		}
	}
}
