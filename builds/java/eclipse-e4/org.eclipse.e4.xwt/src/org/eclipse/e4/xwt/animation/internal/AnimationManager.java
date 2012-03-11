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
import java.util.WeakHashMap;

/**
 * This class manages all scenarios and orchestrates the scenarios execution.
 * 
 * @author yyang
 */
public class AnimationManager {
	private WeakHashMap<Object, ScenarioManager> scenarioManagers = new WeakHashMap<Object, ScenarioManager>();
	private static AnimationManager INSTANCE;
		
	/**
	 * This class manages all scenarios of an UI element. It orchestrates the scenarios execution.
	 * 
	 * @author yyang
	 */
	static class ScenarioManager {
		private Collection<ITimeline> timelines = new ArrayList<ITimeline>();

		public void play(ITimeline timeline) {
			for (ITimeline timelineScenario : timelines) {
				if (timelineScenario != timeline) {
					timelineScenario.cancel();
				}
			}
			timeline.play();
		}

		public void cancel(ITimeline timeline) {
			timeline.end();
		}
		
		public void pause(ITimeline timeline) {
			timeline.pause();
		}
		
		public void resume(ITimeline timeline) {
			timeline.resume();
		}
		
		public void playReverse(ITimeline timeline) {
			timeline.playReverse();
		}

		public void addTimeline(ITimeline timeline) {
			timelines.add(timeline);	
		}
	}
	
	public static AnimationManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AnimationManager();
		}
		return INSTANCE;
	}
	
	
	public void play(ITimeline timeline) {
		ScenarioManager manager = scenarioManagers.get(timeline.getTarget());
		if (manager != null) {
			manager.play(timeline);
		}
	}
	
	public void stop(ITimeline timeline) {
		ScenarioManager manager = scenarioManagers.get(timeline.getTarget());
		if (manager != null) {
			manager.cancel(timeline);
		}
	}
	
	public void pause(ITimeline timeline) {
		ScenarioManager manager = scenarioManagers.get(timeline.getTarget());
		if (manager != null) {
			manager.pause(timeline);
		}
	}

	public void resume(ITimeline timeline) {
		ScenarioManager manager = scenarioManagers.get(timeline.getTarget());
		if (manager != null) {
			manager.resume(timeline);
		}
	}

	public void playReverse(ITimeline timeline) {
		ScenarioManager manager = scenarioManagers.get(timeline.getTarget());
		if (manager != null) {
			manager.playReverse(timeline);
		}
	}

	public void addTimeline(ITimeline timeline) {
		ScenarioManager manager = scenarioManagers.get(timeline.getTarget());
		if (manager == null) {
			manager = new ScenarioManager();
			scenarioManagers.put(timeline.getTarget(), manager);
		}
		manager.addTimeline(timeline);	
	}
}
