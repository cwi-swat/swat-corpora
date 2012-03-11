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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.e4.xwt.animation.RepeatBehavior;
import org.pushingpixels.trident.TimelineScenario;
import org.pushingpixels.trident.callback.TimelineScenarioCallback;

public class ScenarioTimeline extends TimelineScenario implements ITimelineGroup {
	protected TimelineScenario tridentTimelineScenario;
	protected org.eclipse.e4.xwt.animation.Timeline xwtTimeline;
	protected Object target;
	protected boolean done = false;
	private Collection<ITimeline> actors = new ArrayList<ITimeline>();
	private Collection<Runnable> stateChangedRunnables = new ArrayList<Runnable>();

	public ScenarioTimeline(org.eclipse.e4.xwt.animation.Timeline xwtTimeline, TimelineScenario tridentTimelineScenario, Object target) {
		this.xwtTimeline = xwtTimeline;
		this.target = target;
		this.tridentTimelineScenario = tridentTimelineScenario;
		this.tridentTimelineScenario.addCallback(new TimelineScenarioCallback() {
			public void onTimelineScenarioDone() {
				synchronized (ScenarioTimeline.this) {
					done = true;
					for (Runnable runnable : getStateChangedRunnables()) {
						runnable.run();
					}
				}
			}
		});
	}

	public Collection<Runnable> getStateChangedRunnables() {
		return stateChangedRunnables;
	}

	public void addStateChangedRunnable(Runnable stateChangedRunnable) {
		if (stateChangedRunnable == null) {
			return;
		}
		if (!this.stateChangedRunnables.contains(stateChangedRunnable)) {
			this.stateChangedRunnables.add(stateChangedRunnable);			
		}
	}

	public void removeStateChangedRunnable(Runnable stateChangedRunnable) {
		this.stateChangedRunnables.remove(stateChangedRunnable);
	}
	
	public Object getTarget() {
		return target;
	}

	public void play() {
		for (ITimeline timeline : actors) {
			timeline.resetDoneFlag();
			this.tridentTimelineScenario.addScenarioActor(timeline);
		}
		this.tridentTimelineScenario.play();
	}
	
	public void playLoop(RepeatBehavior behavior) {
		this.tridentTimelineScenario.playLoop();
	}

	public void end() {
		if (this.tridentTimelineScenario == null) {
			return;
		}
		this.tridentTimelineScenario.cancel();
		for (ITimeline timeline : actors) {
			timeline.end();
		}
	}

	public void cancel() {
		if (this.tridentTimelineScenario == null) {
			return;
		}
		this.tridentTimelineScenario.cancel();
		for (ITimeline timeline : actors) {
			timeline.cancel();
		}
	}

	public void abort() {
		if (this.tridentTimelineScenario == null) {
			return;
		}
		this.tridentTimelineScenario.cancel();
		for (ITimeline timeline : actors) {
			timeline.abort();
		}		
	}
	
	public void pause() {
		if (this.tridentTimelineScenario == null) {
			return;
		}
		this.tridentTimelineScenario.suspend();
	}

	public void resume() {
		if (this.tridentTimelineScenario == null) {
			return;
		}
		this.tridentTimelineScenario.resume();
	}

	public void playReverse() {
	}
	
	public boolean isDone() {
		synchronized (ScenarioTimeline.this) {
			return done;
		}
	}
	
	public void resetDoneFlag() {
		done = false;
	}
	
	public boolean supportsReplay() {
		return false;
	}
	
	public void addTimeline(ITimeline timeline) {
		actors.add(timeline);
	}
}
