/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.animation.internal;

import org.pushingpixels.trident.TimelineScenario.TimelineScenarioActor;

public interface ITimeline extends TimelineScenarioActor {
	public Object getTarget();
	
	public void playReverse();

	public void end();

	public void abort();

	public void cancel();

	public void pause();

	public void resume();
	
	public void addStateChangedRunnable(Runnable runnable);
	
	public void removeStateChangedRunnable(Runnable runnable);
}
