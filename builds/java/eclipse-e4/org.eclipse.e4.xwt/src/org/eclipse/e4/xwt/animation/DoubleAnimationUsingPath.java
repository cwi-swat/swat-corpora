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

public class DoubleAnimationUsingPath extends DoubleAnimationBase {

	protected void updateTimeline(ITimeline timeline, Object target) {
		super.updateTimeline(timeline, target);
//		for (DoubleKeyFrame doubleKeyFrame : getKeyFrames()) {
//		doubleKeyFrame.start(timeline, target);
	}
}
