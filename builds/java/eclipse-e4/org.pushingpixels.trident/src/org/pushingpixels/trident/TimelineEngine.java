/*
 * Copyright (c) 2005-2009 Trident Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Trident Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.trident;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.TimelineScenario.TimelineScenarioState;
import org.pushingpixels.trident.callback.RunOnUIThread;

/**
 * The Trident timeline engine. This is the main entry point to play
 * {@link Timeline}s and {@link TimelineScenario}s. Use the
 * {@link #getInstance()} method to get the timeline engine.
 * 
 * @author Kirill Grouchnikov
 */
class TimelineEngine {
	/**
	 * Debug mode indicator. Set to <code>true</code> to have trace messages on
	 * console.
	 */
	public static boolean DEBUG_MODE = false;

	/**
	 * Single instance of <code>this</code> class.
	 */
	private static TimelineEngine instance;

	/**
	 * All currently running timelines.
	 */
	private Set<Timeline> runningTimelines;

	// // private Set<Runnable> callbacks;
	//
	// private Queue<Runnable> waitingTimelineScenarios;
	//
	// private Map<TimelineScenario, Runnable> waitingTimelineScenariosMap;

	enum TimelineOperationKind {
		PLAY, CANCEL, RESUME, SUSPEND, ABORT, END
	}

	class TimelineOperation {
		public TimelineOperationKind operationKind;

		Runnable operationRunnable;

		public TimelineOperation(TimelineOperationKind operationKind,
				Runnable operationRunnable) {
			this.operationKind = operationKind;
			this.operationRunnable = operationRunnable;
		}
	}

	// private Queue<Timeline> waitingTimelines;
	//
	// private Map<Timeline, List<TimelineOperation>> waitingTimelinesMap;

	/**
	 * All currently executing timelines. Key is {@link Long} (corresponds to
	 * the {@link TimelineState#id}, value is {@link TimelineKind} - which is a
	 * key in {@link #trackedTimelines}.
	 */
	// private Map<Long, TimelineKind> runningTimelines;
	private Set<TimelineScenario> runningScenarios;

	/**
	 * ID of the current loop iteration.
	 */
	// long currLoopId;
	long lastIterationTimeStamp;

	/**
	 * List of global timeline tracker callbacks.
	 */
	// private List<GlobalTimelineCallback> globalCallbackList;
	// private boolean nothingTracked;

	/**
	 * Identifies a main object and an optional secondary ID.
	 * 
	 * @author Kirill Grouchnikov
	 */
	static class FullObjectID {
		/**
		 * Main object for the timeline.
		 */
		public Object mainObj;

		/**
		 * ID to distinguish between different sub-components of
		 * {@link #mainObj}. For example, the tabbed pane uses this field to
		 * make tab-specific animations.
		 */
		@SuppressWarnings("unchecked")
		public Comparable subID;

		/**
		 * Creates a new object ID.
		 * 
		 * @param mainObj
		 *            The main object.
		 * @param subID
		 *            ID to distinguish between different sub-components of
		 *            <code>mainObj</code>. Can be <code>null</code>.
		 */
		@SuppressWarnings("unchecked")
		public FullObjectID(Object mainObj, Comparable subID) {
			this.mainObj = mainObj;
			this.subID = subID;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			int result = this.mainObj.hashCode();
			if (this.subID != null)
				result &= (this.subID.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			if (obj instanceof FullObjectID) {
				FullObjectID cid = (FullObjectID) obj;
				try {
					boolean result = (this.mainObj == cid.mainObj);
					if (this.subID == null) {
						result = result && (cid.subID == null);
					} else {
						result = result
								&& (this.subID.compareTo(cid.subID) == 0);
					}
					return result;
				} catch (Exception exc) {
					return false;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return this.mainObj.getClass().getSimpleName() + ":" + this.subID;
		}
	}

	/**
	 * The timeline thread.
	 */
	TridentAnimationThread animatorThread;

	private BlockingQueue<Runnable> callbackQueue;

	private TimelineCallbackThread callbackThread;

	class TridentAnimationThread extends Thread {
		public TridentAnimationThread() {
			super();
			this.setName("Trident pulse source thread");
			this.setDaemon(true);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public final void run() {
			TridentConfig.PulseSource pulseSource = TridentConfig.getInstance()
					.getPulseSource();
			lastIterationTimeStamp = System.currentTimeMillis();
			while (true) {
				pulseSource.waitUntilNextPulse();
				updateTimelines();
				// engine.currLoopId++;
			}
		}

		@Override
		public void interrupt() {
			System.err.println("Interrupted");
			super.interrupt();
		}
	}

	/**
	 * The timeline thread class.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private class TimelineEngineThread extends Thread {
		/**
		 * Creates the main trident thread.
		 */
		public TimelineEngineThread() {
		}

	}

	private class TimelineCallbackThread extends Thread {
		public TimelineCallbackThread() {
			super();
			this.setName("Trident callback thread");
			this.setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				try {
					Runnable runnable = callbackQueue.take();
					runnable.run();
					Thread.sleep(0);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	/**
	 * Simple constructor. Defined private for singleton.
	 * 
	 * @see #getInstance()
	 */
	private TimelineEngine() {
		// this.globalCallbackList = new ArrayList<GlobalTimelineCallback>();

		// this.waitingTimelines = new ConcurrentLinkedQueue<Timeline>();
		// this.waitingTimelinesMap = new ConcurrentHashMap<Timeline,
		// List<TimelineOperation>>();
		// this.waitingTimelineScenarios = new
		// ConcurrentLinkedQueue<Runnable>();
		// this.waitingTimelineScenariosMap = new
		// ConcurrentHashMap<TimelineScenario, Runnable>();

		this.runningTimelines = new HashSet<Timeline>();
		this.runningScenarios = new HashSet<TimelineScenario>();

		// this.currLoopId = 0;
		this.callbackQueue = new LinkedBlockingQueue<Runnable>();
		// this.animatorThread = this.getAnimatorThread();
		this.callbackThread = this.getCallbackThread();
	}

	/**
	 * Gets singleton instance.
	 * 
	 * @return Singleton instance.
	 */
	public synchronized static TimelineEngine getInstance() {
		if (TimelineEngine.instance == null) {
			TimelineEngine.instance = new TimelineEngine();
		}
		return TimelineEngine.instance;
	}

	/**
	 * Updates all timelines that are currently registered with
	 * <code>this</code> tracker.
	 */
	void updateTimelines() {
		synchronized (LOCK) {
			if ((this.runningTimelines.size() == 0)
					&& (this.runningScenarios.size() == 0)) {
				this.lastIterationTimeStamp = System.currentTimeMillis();
				return;
			}

			long passedSinceLastIteration = (System.currentTimeMillis() - this.lastIterationTimeStamp);
			if (passedSinceLastIteration < 0) {
				// ???
				passedSinceLastIteration = 0;
			}
			if (DEBUG_MODE) {
				System.out.println("Elapsed since last iteration: "
						+ passedSinceLastIteration + "ms");
			}

			// System.err.println("Periodic update on "
			// + this.runningTimelines.size() + " timelines; "
			// + passedSinceLastIteration + " ms passed since last");
			for (Iterator<Timeline> itTimeline = this.runningTimelines
					.iterator(); itTimeline.hasNext();) {
				Timeline timeline = itTimeline.next();
				if (timeline.getState() == TimelineState.SUSPENDED)
					continue;

				boolean timelineWasInReadyState = false;
				if (timeline.getState() == TimelineState.READY) {
					if ((timeline.timeUntilPlay - passedSinceLastIteration) > 0) {
						// still needs to wait in the READY state
						timeline.timeUntilPlay -= passedSinceLastIteration;
						continue;
					}

					// can go from READY to PLAYING
					timelineWasInReadyState = true;
					timeline.popState();
					this.callbackCallTimelineStateChanged(timeline,
							TimelineState.READY);
				}

				boolean hasEnded = false;
				if (DEBUG_MODE) {
					System.out.println("Processing " + timeline.id + "["
							+ timeline.mainObject.getClass().getSimpleName()
							+ "] from " + timeline.durationFraction
							+ ". Callback - "
							+ (timeline.callback == null ? "no" : "yes"));
				}
				// Component comp = entry.getKey();

				// at this point, the timeline must be playing
				switch (timeline.getState()) {
				case PLAYING_FORWARD:
					if (!timelineWasInReadyState) {
						timeline.durationFraction = timeline.durationFraction
								+ (float) passedSinceLastIteration
								/ (float) timeline.duration;
					}
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					if (DEBUG_MODE) {
						System.out
								.println("Timeline position: "
										+ ((long) (timeline.durationFraction * timeline.duration))
										+ "/" + timeline.duration + " = "
										+ timeline.durationFraction);
					}
					if (timeline.durationFraction > 1.0f) {
						timeline.durationFraction = 1.0f;
						timeline.timelinePosition = 1.0f;
						if (timeline.isLooping) {
							boolean stopLoopingAnimation = timeline.toCancelAtCycleBreak;
							int loopsToLive = timeline.repeatCount;
							if (loopsToLive > 0) {
								loopsToLive--;
								stopLoopingAnimation = stopLoopingAnimation
										|| (loopsToLive == 0);
								timeline.repeatCount = loopsToLive;
							}
							if (stopLoopingAnimation) {
								// end looping animation
								hasEnded = true;
								itTimeline.remove();
							} else {
								if (timeline.repeatBehavior == Timeline.RepeatBehavior.REVERSE) {
									timeline
											.replaceState(TimelineState.PLAYING_REVERSE);
									if (timeline.cycleDelay > 0) {
										timeline.pushState(TimelineState.READY);
										timeline.timeUntilPlay = timeline.cycleDelay;
									}
									this.callbackCallTimelineStateChanged(
											timeline,
											TimelineState.PLAYING_FORWARD);
								} else {
									timeline.durationFraction = 0.0f;
									timeline.timelinePosition = 0.0f;
									if (timeline.cycleDelay > 0) {
										timeline.pushState(TimelineState.READY);
										timeline.timeUntilPlay = timeline.cycleDelay;
										this.callbackCallTimelineStateChanged(
												timeline,
												TimelineState.PLAYING_FORWARD);
									} else {
										// it's still playing forward, but lets
										// the app code know
										// that the new loop has begun
										this.callbackCallTimelineStateChanged(
												timeline,
												TimelineState.PLAYING_FORWARD);
									}
								}
							}
						} else {
							hasEnded = true;
							itTimeline.remove();
						}
					}
					break;
				case PLAYING_REVERSE:
					if (!timelineWasInReadyState) {
						timeline.durationFraction = timeline.durationFraction
								- (float) passedSinceLastIteration
								/ (float) timeline.duration;
					}
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					// state.timelinePosition = state.timelinePosition
					// - stepFactor
					// * state.fadeStep.getNextStep(state.timelineKind,
					// state.timelinePosition,
					// state.isPlayingForward, state.isLooping);
					if (DEBUG_MODE) {
						System.out
								.println("Timeline position: "
										+ ((long) (timeline.durationFraction * timeline.duration))
										+ "/" + timeline.duration + " = "
										+ timeline.durationFraction);
					}
					if (timeline.durationFraction < 0) {
						timeline.durationFraction = 0.0f;
						timeline.timelinePosition = 0.0f;
						if (timeline.isLooping) {
							boolean stopLoopingAnimation = timeline.toCancelAtCycleBreak;
							int loopsToLive = timeline.repeatCount;
							if (loopsToLive > 0) {
								loopsToLive--;
								stopLoopingAnimation = stopLoopingAnimation
										|| (loopsToLive == 0);
								timeline.repeatCount = loopsToLive;
							}
							if (stopLoopingAnimation) {
								// end looping animation
								hasEnded = true;
								itTimeline.remove();
							} else {
								timeline
										.replaceState(TimelineState.PLAYING_FORWARD);
								if (timeline.cycleDelay > 0) {
									timeline.pushState(TimelineState.READY);
									timeline.timeUntilPlay = timeline.cycleDelay;
								}
								this.callbackCallTimelineStateChanged(timeline,
										TimelineState.PLAYING_REVERSE);
							}
						} else {
							hasEnded = true;
							itTimeline.remove();
						}
					}
					break;
				default:
					throw new IllegalStateException("Timeline cannot be in "
							+ timeline.getState() + " state");
				}
				if (hasEnded) {
					if (DEBUG_MODE) {
						System.out.println("Ending " + timeline.id + " on "
								// + timeline.timelineKind.toString()
								+ " in state " + timeline.getState().name()
								+ " at position " + timeline.durationFraction);
					}
					TimelineState oldState = timeline.getState();
					timeline.replaceState(TimelineState.DONE);
					this.callbackCallTimelineStateChanged(timeline, oldState);
					timeline.popState();
					if (timeline.getState() != TimelineState.IDLE) {
						throw new IllegalStateException(
								"Timeline should be IDLE at this point");
					}
					this.callbackCallTimelineStateChanged(timeline,
							TimelineState.DONE);
				} else {
					if (DEBUG_MODE) {
						System.out.println("Calling " + timeline.id + " on "
						// + timeline.timelineKind.toString() + " at "
								+ timeline.durationFraction);
					}
					this.callbackCallTimelinePulse(timeline);
				}
			}

			if (this.runningScenarios.size() > 0) {
				// System.err.println(Thread.currentThread().getName()
				// + " : updating");
				for (Iterator<TimelineScenario> it = this.runningScenarios
						.iterator(); it.hasNext();) {
					TimelineScenario scenario = it.next();
					if (scenario.state == TimelineScenarioState.DONE) {
						it.remove();
						this.callbackCallTimelineScenarioEnded(scenario);
						continue;
					}
					Set<TimelineScenario.TimelineScenarioActor> readyActors = scenario
							.getReadyActors();
					if (readyActors != null) {
						// if (readyActors.size() > 0)
						// System.out.println("Scenario : " + scenario.state +
						// ":"
						// + readyActors.size());
						for (TimelineScenario.TimelineScenarioActor readyActor : readyActors) {
							readyActor.play();
						}
					}
				}
			}
			// System.err.println("Periodic update done");

			// this.nothingTracked = (this.runningTimelines.size() == 0);
			this.lastIterationTimeStamp = System.currentTimeMillis();
		}
	}

	private void callbackCallTimelineStateChanged(final Timeline timeline,
			final TimelineState oldState) {
		final TimelineState newState = timeline.getState();
		final float durationFraction = timeline.durationFraction;
		final float timelinePosition = timeline.timelinePosition;
		Runnable callbackRunnable = new Runnable() {
			public void run() {
				boolean shouldRunOnUIThread = false;
				Class<?> clazz = timeline.callback.getClass();
				while ((clazz != null) && !shouldRunOnUIThread) {
					shouldRunOnUIThread = clazz
							.isAnnotationPresent(RunOnUIThread.class);
					clazz = clazz.getSuperclass();
				}
				if (shouldRunOnUIThread && (timeline.uiToolkitHandler != null)) {
					timeline.uiToolkitHandler.runOnUIThread(
							timeline.mainObject, new Runnable() {
								public void run() {
									timeline.callback.onTimelineStateChanged(
											oldState, newState,
											durationFraction, timelinePosition);
								}
							});
				} else {
					timeline.callback.onTimelineStateChanged(oldState,
							newState, durationFraction, timelinePosition);
				}
			}
		};
		// callbackRunnable.run();
		this.callbackQueue.add(callbackRunnable);

		// if (globalCallbackList.size() == 0)
		// return;
		// // if (!timeline.timelineKind.isCore)
		// // return;
		//
		// for (GlobalTimelineCallback globalCallback : globalCallbackList) {
		// globalCallback.onTimelineStateChanged(timeline, oldState);
		// }
	}

	private void callbackCallTimelinePulse(final Timeline timeline) {
		final float durationFraction = timeline.durationFraction;
		final float timelinePosition = timeline.timelinePosition;
		Runnable callbackRunnable = new Runnable() {
			public void run() {
				boolean shouldRunOnUIThread = false;
				Class<?> clazz = timeline.callback.getClass();
				while ((clazz != null) && !shouldRunOnUIThread) {
					shouldRunOnUIThread = clazz
							.isAnnotationPresent(RunOnUIThread.class);
					clazz = clazz.getSuperclass();
				}
				if (shouldRunOnUIThread && (timeline.uiToolkitHandler != null)) {
					timeline.uiToolkitHandler.runOnUIThread(
							timeline.mainObject, new Runnable() {
								public void run() {
									// System.err.println("Timeline @"
									// + timeline.hashCode());
									timeline.callback.onTimelinePulse(
											durationFraction, timelinePosition);
								}
							});
				} else {
					// System.err.println("Timeline @" + timeline.hashCode());
					timeline.callback.onTimelinePulse(durationFraction,
							timelinePosition);
				}
			}
		};
		// callbackRunnable.run();
		this.callbackQueue.add(callbackRunnable);

		// if (globalCallbackList.size() == 0)
		// return;
		// // if (!timeline.timelineKind.isCore)
		// // return;
		//
		// for (GlobalTimelineCallback globalCallback : globalCallbackList) {
		// globalCallback.onTimelinePulse(timeline);
		// }
	}

	private void callbackCallTimelineScenarioEnded(
			final TimelineScenario timelineScenario) {
		// boolean shouldRunOnUIThread = false;
		// Class<?> clazz = timelineScenario.callback.getClass();
		// while ((clazz != null) && !shouldRunOnUIThread) {
		// shouldRunOnUIThread = clazz
		// .isAnnotationPresent(RunOnUIThread.class);
		// clazz = clazz.getSuperclass();
		// }
		// if (shouldRunOnUIThread && (timeline.uiToolkitHandler != null)) {
		// timeline.uiToolkitHandler.runOnUIThread(new Runnable() {
		// public void run() {
		// timelineScenario.callback.onTimelineScenarioDone();
		// }
		// });
		// } else {
		Runnable callbackRunnable = new Runnable() {
			public void run() {
				timelineScenario.callback.onTimelineScenarioDone();
			}
		};
		this.callbackQueue.offer(callbackRunnable);
		// }
	}

	/**
	 * Returns an existing running timeline that matches the specified
	 * parameters.
	 * 
	 * @param timelineKind
	 *            Timeline kind.
	 * @param object
	 *            Component.
	 * @param secondaryId
	 *            Secondary id. Relevant for such components as tabbed panes
	 *            (where animation is performed on different tabs).
	 * @return An existing running timeline that matches the specified
	 *         parameters.
	 */
	private Timeline getRunningTimeline(Timeline timeline) {
		synchronized (LOCK) {
			if (this.runningTimelines.contains(timeline))
				return timeline;
			//
			return null;
			// return this.getRunningTimeline(//timeline.timelineKind,
			// timeline.mainObject, timeline.secondaryId);
		}
	}

	/**
	 * Returns an existing running timeline that matches the specified
	 * parameters.
	 * 
	 * @param timelineKind
	 *            Timeline kind.
	 * @param mainObject
	 *            Main object.
	 * @param secondaryId
	 *            Secondary id. Relevant for such components as tabbed panes
	 *            (where animation is performed on different tabs).
	 * @return An existing running timeline that matches the specified
	 *         parameters.
	 */
	// private Timeline getRunningTimeline(//TimelineKind timelineKind,
	// Object mainObject, Comparable<?> secondaryId) {
	// synchronized (LOCK) {
	// // if (timelineKind == null)
	// // return null;
	// FullObjectID fullObjectID = new FullObjectID(mainObject,
	// secondaryId);
	// for (Timeline running : this.runningTimelines) {
	// if (//(running.timelineKind == timelineKind)
	// //&&
	// (running.fullObjectID.equals(fullObjectID)))
	// return running;
	// }
	// return null;
	// }
	// }
	/**
	 * Adds the specified timeline.
	 * 
	 * @param timeline
	 *            Timeline to add.
	 */
	private void addTimeline(Timeline timeline) {
		synchronized (LOCK) {
			FullObjectID cid = new FullObjectID(timeline.mainObject,
					timeline.secondaryId);
			timeline.fullObjectID = cid;
			this.runningTimelines.add(timeline);
			// this.nothingTracked = false;
			if (DEBUG_MODE) {
				System.out.println("Added (" + timeline.id + ") on "
						+ timeline.fullObjectID + "]. Fade "
						// + timeline.timelineKind.toString() + " with state "
						+ timeline.getState().name() + ". Callback - "
						+ (timeline.callback == null ? "no" : "yes"));
			}
		}
	}

	void play(Timeline timeline, boolean reset, long msToSkip) {
		synchronized (LOCK) {
			getAnimatorThread();

			// if
			// (!TimelineConfigurationManager.getInstance().animationsAllowed(
			// timeline.timelineKind, timeline.mainObject)) {
			// TimelineState oldState = timeline.getState();
			// timeline.durationFraction = 1.0f;
			// timeline.timelinePosition = 1.0f;
			// while (timeline.getState() != TimelineState.IDLE)
			// timeline.popState();
			// timeline.pushState(TimelineState.DONE);
			// this.callbackCallTimelineStateChanged(timeline, oldState);
			// timeline.popState();
			// this.callbackCallTimelineStateChanged(timeline,
			// TimelineState.DONE);
			// return;
			// }

			// see if it's already tracked
			Timeline existing = this.getRunningTimeline(timeline);
			if (existing == null) {
				TimelineState oldState = timeline.getState();
				timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
				if (timeline.timeUntilPlay < 0) {
					timeline.durationFraction = (float) -timeline.timeUntilPlay
							/ (float) timeline.duration;
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					timeline.timeUntilPlay = 0;
				} else {
					timeline.durationFraction = 0.0f;
					timeline.timelinePosition = 0.0f;
				}
				timeline.pushState(TimelineState.PLAYING_FORWARD);
				timeline.pushState(TimelineState.READY);
				this.addTimeline(timeline);

				this.callbackCallTimelineStateChanged(timeline, oldState);
			} else {
				TimelineState oldState = existing.getState();
				if (oldState == TimelineState.READY) {
					// the timeline remains READY, but after that it will be
					// PLAYING_FORWARD
					existing.popState();
					existing.replaceState(TimelineState.PLAYING_FORWARD);
					existing.pushState(TimelineState.READY);
				} else {
					// change the timeline state
					existing.replaceState(TimelineState.PLAYING_FORWARD);
					if (oldState != existing.getState()) {
						this.callbackCallTimelineStateChanged(timeline,
								oldState);
					}
				}
				if (reset) {
					existing.durationFraction = 0.0f;
					existing.timelinePosition = 0.0f;
					this.callbackCallTimelinePulse(existing);
				}
			}
			// return existing.id;
		}
	}

	void playScenario(TimelineScenario scenario) {
		synchronized (LOCK) {
			getAnimatorThread();
			Set<TimelineScenario.TimelineScenarioActor> readyActors = scenario
					.getReadyActors();

			// System.err.println(Thread.currentThread().getName() +
			// " : adding");
			this.runningScenarios.add(scenario);
			for (TimelineScenario.TimelineScenarioActor readyActor : readyActors) {
				readyActor.play();
			}
		}
	}

	void playReverse(Timeline timeline, boolean reset, long msToSkip) {
		synchronized (LOCK) {
			getAnimatorThread();
			if (timeline.isLooping) {
				throw new IllegalArgumentException(
						"Timeline must not be marked as looping");
			}

			// if
			// (!TimelineConfigurationManager.getInstance().animationsAllowed(
			// timeline.timelineKind, timeline.mainObject)) {
			// TimelineState oldState = timeline.getState();
			// timeline.durationFraction = 0.0f;
			// timeline.timelinePosition = 0.0f;
			// while (timeline.getState() != TimelineState.IDLE)
			// timeline.popState();
			// timeline.replaceState(TimelineState.DONE);
			// this.callbackCallTimelineStateChanged(timeline, oldState);
			// timeline.popState();
			// this.callbackCallTimelineStateChanged(timeline,
			// TimelineState.DONE);
			// return;
			// }

			// see if it's already tracked
			Timeline existing = this.getRunningTimeline(timeline);
			if (existing == null) {
				TimelineState oldState = timeline.getState();
				timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
				if (timeline.timeUntilPlay < 0) {
					timeline.durationFraction = 1.0f
							- (float) -timeline.timeUntilPlay
							/ (float) timeline.duration;
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					timeline.timeUntilPlay = 0;
				} else {
					timeline.durationFraction = 1.0f;
					timeline.timelinePosition = 1.0f;
				}
				timeline.pushState(TimelineState.PLAYING_REVERSE);
				timeline.pushState(TimelineState.READY);

				this.addTimeline(timeline);
				this.callbackCallTimelineStateChanged(timeline, oldState);
			} else {
				TimelineState oldState = existing.getState();
				if (oldState == TimelineState.READY) {
					// the timeline remains READY, but after that it will be
					// PLAYING_REVERSE
					existing.popState();
					existing.replaceState(TimelineState.PLAYING_REVERSE);
					existing.pushState(TimelineState.READY);
				} else {
					// change the timeline state
					existing.replaceState(TimelineState.PLAYING_REVERSE);
					if (oldState != existing.getState()) {
						this.callbackCallTimelineStateChanged(timeline,
								oldState);
					}
				}
				if (reset) {
					existing.durationFraction = 1.0f;
					existing.timelinePosition = 1.0f;
					this.callbackCallTimelinePulse(existing);
				}
				// timelineState.callback = timeline.callback;
			}
		}
		// /return existing.id;
	}

	void playLoop(Timeline timeline, long msToSkip) {
		synchronized (LOCK) {
			getAnimatorThread();
			if (!timeline.isLooping) {
				throw new IllegalArgumentException(
						"Timeline must be marked as looping");
			}

			// if
			// (!TimelineConfigurationManager.getInstance().animationsAllowed(
			// timeline.timelineKind, timeline.mainObject)) {
			// TimelineState oldState = timeline.getState();
			// timeline.durationFraction = 0.0f;
			// timeline.timelinePosition = 0.0f;
			// while (timeline.getState() != TimelineState.IDLE)
			// timeline.popState();
			// timeline.replaceState(TimelineState.DONE);
			// this.callbackCallTimelineStateChanged(timeline, oldState);
			// timeline.popState();
			// this.callbackCallTimelineStateChanged(timeline,
			// TimelineState.DONE);
			// return;
			// }

			// see if it's already tracked
			Timeline existing = this.getRunningTimeline(timeline);
			if (existing == null) {
				TimelineState oldState = timeline.getState();
				timeline.timeUntilPlay = timeline.initialDelay - msToSkip;
				if (timeline.timeUntilPlay < 0) {
					timeline.durationFraction = (float) -timeline.timeUntilPlay
							/ (float) timeline.duration;
					timeline.timelinePosition = timeline.ease
							.map(timeline.durationFraction);
					timeline.timeUntilPlay = 0;
				} else {
					timeline.durationFraction = 0.0f;
					timeline.timelinePosition = 0.0f;
				}
				timeline.pushState(TimelineState.PLAYING_FORWARD);
				timeline.pushState(TimelineState.READY);
				timeline.toCancelAtCycleBreak = false;

				this.addTimeline(timeline);
				this.callbackCallTimelineStateChanged(timeline, oldState);
			} else {
				// timelineState.isLooping = true;
				// timelineState.repeatBehavior = timeline.repeatBehavior;
				// timelineState.callback = timeline.callback;
				existing.toCancelAtCycleBreak = false;
				existing.repeatCount = timeline.repeatCount;
			}
		}
		// return existing.id;
	}

	/**
	 * Checks whether the specified object is animated by one of the currently
	 * executed timelines tracked by this engine.
	 * 
	 * @param object
	 *            Object.
	 * @param timelineKind
	 *            Timeline kind.
	 * @return <code>true</code> if the specified object is being tracked by
	 *         <code>this</code> tracker, <code>false</code> otherwise.
	 */
	// public boolean isTracked(Object object, TimelineKind timelineKind) {
	// return this.isTracked(object, null, timelineKind);
	// }
	/**
	 * Checks whether the specified object is animated by one of the currently
	 * executed timelines tracked by this engine.
	 * 
	 * @param object
	 *            Object.
	 * @param secondaryId
	 *            Secondary id. Relevant for such components as tabbed panes
	 *            (where animations are performed on different tabs).
	 * @param timelineKind
	 *            Timeline kind.
	 * @return <code>true</code> if the specified object is being tracked by
	 *         <code>this</code> tracker, <code>false</code> otherwise.
	 */
	// public boolean isTracked(Object object, int secondaryId,
	// TimelineKind timelineKind) {
	// return this.isTracked(object, new Integer(secondaryId), timelineKind);
	// }
	/**
	 * Checks whether the specified object is animated by one of the currently
	 * executed timelines tracked by this engine.
	 * 
	 * @param object
	 *            Object.
	 * @param secondaryId
	 *            Secondary id. Relevant for such components as tabbed panes
	 *            (where animations are performed on different tabs). May be
	 *            <code>null</code>.
	 * @param timelineKind
	 *            Timeline kind.
	 * @return <code>true</code> if the specified object is being tracked by
	 *         <code>this</code> tracker, <code>false</code> otherwise.
	 */
	// public boolean isTracked(Object object, Comparable<?> secondaryId,
	// TimelineKind timelineKind) {
	// if (this.nothingTracked)
	// return false;
	//
	// Timeline running = getRunningTimeline(timelineKind, object, secondaryId);
	// return (running != null);
	// }
	/**
	 * Checks whether the specified object is animated by one of the currently
	 * executed timelines tracked by this engine.
	 * 
	 * @param object
	 *            Object.
	 * @param secondaryId
	 *            Secondary id. Relevant for such components as tabbed panes
	 *            (where animations are performed on different tabs). May be
	 *            <code>null</code>.
	 * @param timelineKind
	 *            Timeline kind.
	 * @param timelineState
	 *            Required timeline state.
	 * @return <code>true</code> if the specified object is being tracked by
	 *         <code>this</code> tracker, <code>false</code> otherwise.
	 */
	// public boolean isTracked(Object object, Comparable<?> secondaryId,
	// TimelineKind timelineKind, TimelineState timelineState) {
	// if (this.nothingTracked)
	// return false;
	//
	// Timeline running = getRunningTimeline(timelineKind, object, secondaryId);
	// if (running == null)
	// return false;
	//
	// return (running.getState() == timelineState);
	// }
	/**
	 * Returns the timeline position for the specified object. The result will
	 * be in 0.0-1.0 range.
	 * 
	 * @param object
	 *            Object.
	 * @param timelineKind
	 *            Fade kind.
	 * @return The timeline position for the specified object. For objects that
	 *         are not tracked (when {@link #isTracked(Object, TimelineKind)}
	 *         returns <code>false</code>), value 0 (zero) is returned.
	 */
	// public float getTimelinePosition(Object object, TimelineKind
	// timelineKind) {
	// synchronized (LOCK) {
	// return this.getTimelinePosition(object, null, timelineKind);
	// }
	// }
	/**
	 * Returns the timeline position for the specified object. The result will
	 * be in 0.0-1.0 range.
	 * 
	 * @param object
	 *            Object.
	 * @param secondaryId
	 *            Secondary id. Relevant for such components as tabbed panes
	 *            (where animations are performed on different tabs).
	 * @param timelineKind
	 *            Timeline kind.
	 * @return The timeline position for the specified object. For objects that
	 *         are not tracked (when
	 *         {@link #isTracked(Object, int, TimelineKind)} returns
	 *         <code>false</code>), value 0 (zero) is returned.
	 */
	// public float getTimelinePosition(Object object, int secondaryId,
	// TimelineKind timelineKind) {
	// synchronized (LOCK) {
	// return this.getTimelinePosition(object, new Integer(secondaryId),
	// timelineKind);
	// }
	// }
	/**
	 * Returns the timeline position for the specified object. The result will
	 * be in 0.0-1.0 range.
	 * 
	 * @param object
	 *            Object.
	 * @param secondaryId
	 *            Secondary id. Relevant for such components as tabbed panes,
	 *            lists, tables, trees and others (where animations are
	 *            performed on different tabs). May be <code>null</code>.
	 * @param timelineKind
	 *            Timeline kind.
	 * @return The timeline position for the specified object. For objects that
	 *         are not tracked (when
	 *         {@link #isTracked(Object, Comparable, TimelineKind)} returns
	 *         <code>false</code>), value 0 (zero) is returned.
	 */
	// public float getTimelinePosition(Object object, Comparable<?>
	// secondaryId,
	// TimelineKind timelineKind) {
	// synchronized (LOCK) {
	// if (this.nothingTracked)
	// return 0;
	//
	// Timeline runningTimeline = getRunningTimeline(timelineKind, object,
	// secondaryId);
	// if (runningTimeline == null)
	// return 0;
	//
	// return runningTimeline.timelinePosition;
	// }
	// }
	/**
	 * Stops tracking of all timelines. Note that this function <b>does not</b>
	 * stop the timeline engine thread ({@link #animatorThread}) and the
	 * timeline callback thread ({@link #callbackThread}).
	 */
	public void cancelAllTimelines() {
		synchronized (LOCK) {
			getAnimatorThread();
			for (Timeline timeline : this.runningTimelines) {
				TimelineState oldState = timeline.getState();
				while (timeline.getState() != TimelineState.IDLE)
					timeline.popState();
				timeline.pushState(TimelineState.CANCELLED);
				this.callbackCallTimelineStateChanged(timeline, oldState);
				timeline.popState();
				this.callbackCallTimelineStateChanged(timeline,
						TimelineState.CANCELLED);
			}
			this.runningTimelines.clear();
			this.runningScenarios.clear();
			// this.waitingTimelines.clear();
			// this.waitingTimelinesMap.clear();
		}
	}

	/**
	 * Returns an instance of the animator thread.
	 * 
	 * @return The animator thread.
	 */
	private TridentAnimationThread getAnimatorThread() {
		if (this.animatorThread == null) {
			this.animatorThread = new TridentAnimationThread();
			this.animatorThread.start();
		}
		return this.animatorThread;
	}

	/**
	 * Returns an instance of the callback thread.
	 * 
	 * @return The animator thread.
	 */
	private TimelineCallbackThread getCallbackThread() {
		if (this.callbackThread == null) {
			this.callbackThread = new TimelineCallbackThread();
			this.callbackThread.start();
		}
		return this.callbackThread;
	}

	/**
	 * Cancels the specified timeline instance.
	 * 
	 * @param timeline
	 *            Timeline to cancel.
	 */
	private void cancelTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			this.runningTimelines.remove(timeline);
			TimelineState oldState = timeline.getState();
			while (timeline.getState() != TimelineState.IDLE)
				timeline.popState();
			timeline.pushState(TimelineState.CANCELLED);
			this.callbackCallTimelineStateChanged(timeline, oldState);
			timeline.popState();
			this.callbackCallTimelineStateChanged(timeline,
					TimelineState.CANCELLED);
		}
	}

	/**
	 * Ends the specified timeline instance.
	 * 
	 * @param timeline
	 *            Timeline to end.
	 */
	private void endTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			this.runningTimelines.remove(timeline);
			TimelineState oldState = timeline.getState();
			float endPosition = timeline.timelinePosition;
			while (timeline.getState() != TimelineState.IDLE) {
				TimelineState state = timeline.popState();
				if (state == TimelineState.PLAYING_FORWARD)
					endPosition = 1.0f;
				if (state == TimelineState.PLAYING_REVERSE)
					endPosition = 0.0f;
			}
			timeline.durationFraction = endPosition;
			timeline.timelinePosition = endPosition;
			timeline.pushState(TimelineState.DONE);
			this.callbackCallTimelineStateChanged(timeline, oldState);
			timeline.popState();
			this.callbackCallTimelineStateChanged(timeline, TimelineState.DONE);
		}
	}

	/**
	 * Cancels the specified timeline instance.
	 * 
	 * @param timeline
	 *            Timeline to cancel.
	 */
	private void abortTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			this.runningTimelines.remove(timeline);
			while (timeline.getState() != TimelineState.IDLE)
				timeline.popState();
		}
	}

	/**
	 * Suspends the specified timeline instance.
	 * 
	 * @param timeline
	 *            Timeline to suspend.
	 */
	private void suspendTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			TimelineState oldState = timeline.getState();
			if ((oldState != TimelineState.PLAYING_FORWARD)
					&& (oldState != TimelineState.PLAYING_REVERSE)
					&& (oldState != TimelineState.READY)) {
				return;
			}
			timeline.pushState(TimelineState.SUSPENDED);
			this.callbackCallTimelineStateChanged(timeline, oldState);
		}
	}

	/**
	 * Resume the specified timeline instance.
	 * 
	 * @param timeline
	 *            Timeline to resume.
	 */
	private void resumeTimeline(Timeline timeline) {
		getAnimatorThread();
		if (this.runningTimelines.contains(timeline)) {
			TimelineState oldState = timeline.getState();
			if (oldState != TimelineState.SUSPENDED)
				return;
			timeline.popState();
			this.callbackCallTimelineStateChanged(timeline, oldState);
		}
	}

	/**
	 * Returns all objects that are currently under the specified timeline kind.
	 * 
	 * @param timelineKind
	 *            Timeline kind.
	 * @return Non-<code>null</code> set of all objects that are currently under
	 *         the specified timeline kind.
	 */
	// public Set<Object> getAllObjectsByFadeKind(TimelineKind timelineKind) {
	// synchronized (LOCK) {
	// Set<Object> result = new HashSet<Object>();
	// for (Timeline runningTimeline : this.runningTimelines) {
	// result.add(runningTimeline.mainObject);
	// }
	// return result;
	// }
	// }
	// /*
	// * Returns the ID of the current loop iteration.
	// *
	// * @return ID of the current loop iteration.
	// */
	// public long getCurrLoopId() {
	// return currLoopId;
	// }
	//
	/**
	 * Registers the specified application callback for tracking core timeline
	 * events.
	 * 
	 * @param callback
	 *            Application callback for tracking core timeline events.
	 * @see TimelineConfigurationManager#addGlobalFadeTrackerCallback(GlobalTimelineCallback)
	 * @see #removeGlobalCallback(GlobalTimelineCallback)
	 */
	// public void addGlobalCallback(GlobalTimelineCallback callback) {
	// this.globalCallbackList.add(callback);
	// }
	/**
	 * Removes the specified application callback for tracking core timeline
	 * events.
	 * 
	 * @param callback
	 *            Application callback to remove.
	 * @see #addGlobalFadeTrackerCallback(GlobalTimelineCallback)
	 */
	// public void removeGlobalCallback(GlobalTimelineCallback callback) {
	// this.globalCallbackList.remove(callback);
	// }
	// void enqueueTimelineOperation(Timeline timeline,
	// TimelineOperationKind operationKind, Runnable operationRunnable) {
	// synchronized (LOCK) {
	// this.getAnimatorThread();
	// if (!this.waitingTimelinesMap.containsKey(timeline))
	// this.waitingTimelinesMap.put(timeline,
	// new ArrayList<TimelineOperation>());
	// this.waitingTimelinesMap.get(timeline).add(
	// new TimelineOperation(operationKind, operationRunnable));
	// this.waitingTimelines.add(timeline);
	// }
	// }

	void runTimelineOperation(Timeline timeline,
			TimelineOperationKind operationKind, Runnable operationRunnable) {
		synchronized (LOCK) {
			this.getAnimatorThread();
			switch (operationKind) {
			case CANCEL:
				this.cancelTimeline(timeline);
				return;
			case END:
				this.endTimeline(timeline);
				return;
			case RESUME:
				this.resumeTimeline(timeline);
				return;
			case SUSPEND:
				this.suspendTimeline(timeline);
				return;
			case ABORT:
				this.abortTimeline(timeline);
				return;
			}
			operationRunnable.run();
		}
	}

	void runTimelineScenario(TimelineScenario timelineScenario,
			Runnable timelineScenarioRunnable) {
		synchronized (LOCK) {
			this.getAnimatorThread();
			timelineScenarioRunnable.run();
		}
		// this.waitingTimelineScenarios.add(timelineScenarioRunnable);
		// this.waitingTimelineScenariosMap.put(timelineScenario,
		// timelineScenarioRunnable);
	}

	static final Object LOCK = new Object();
}
