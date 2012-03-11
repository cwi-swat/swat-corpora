/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.debug.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.e4.languages.javascript.jsdi.BooleanValue;
import org.eclipse.e4.languages.javascript.jsdi.StackFrameReference;
import org.eclipse.e4.languages.javascript.jsdi.ThreadReference;
import org.eclipse.e4.languages.javascript.jsdi.Value;
import org.eclipse.e4.languages.javascript.jsdi.event.Event;
import org.eclipse.e4.languages.javascript.jsdi.event.EventSet;
import org.eclipse.e4.languages.javascript.jsdi.event.StepEvent;
import org.eclipse.e4.languages.javascript.jsdi.event.SuspendEvent;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequestManager;
import org.eclipse.e4.languages.javascript.jsdi.request.StepRequest;
import org.eclipse.e4.languages.javascript.jsdi.request.SuspendRequest;
import org.eclipse.osgi.util.NLS;

/**
 * A JSDI thread.
 * 
 * JSDI threads act as their own event listener for suspend and step events and are called out to from JSDI breakpoints to handle suspending at a breakpoint.
 * 
 * @since 1.0
 */
public class JSDIThread extends JSDIDebugElement implements IThread, IJSDIEventListener {

	/**
	 * Constant for no stack frames
	 * 
	 * @see #getStackFrames()
	 */
	static final IStackFrame[] NO_STACK_FRAMES = new IStackFrame[0];

	/**
	 * Constant for no breakpoints
	 * 
	 * @see #getBreakpoints()
	 */
	static final IBreakpoint[] NO_BREAKPOINTS = new IBreakpoint[0];

	/**
	 * State text meta-data for thread naming
	 */
	public static final String RUNNING_STATUS = "running"; //$NON-NLS-1$
	public static final String SUSPENDED_STATUS = "suspended"; //$NON-NLS-1$
	public static final String ZOMBIE_STATUS = "zombie"; //$NON-NLS-1$

	// states
	private static final int UNKNOWN = 0;
	private static final int SUSPENDED = 1;
	private static final int RUNNING = 2;
	private static final int STEPPING = 3;
	private static final int TERMINATED = 4;

	/**
	 * Stack frames, or <code>null</code> if none.
	 */
	private List frames = null;

	/**
	 * Breakpoints or empty if none.
	 */
	private ArrayList breakpoints = new ArrayList(4);

	/**
	 * Current state
	 */
	private int state = UNKNOWN;

	/**
	 * The underlying {@link ThreadReference} for this thread
	 */
	private final ThreadReference thread;

	/**
	 * Constructor
	 * 
	 * @param target
	 *            the target the thread belongs to
	 * @param thread
	 *            the underlying {@link ThreadReference}
	 */
	public JSDIThread(JSDIDebugTarget target, ThreadReference thread) {
		super(target);
		this.thread = thread;
		this.state = thread.isSuspended() ? SUSPENDED : RUNNING;
	}

	/**
	 * @return the status text for the thread
	 */
	private String statusText() {
		if (thread.status() == ThreadReference.THREAD_STATUS_ZOMBIE) {
			return ZOMBIE_STATUS;
		} else if (state == SUSPENDED) {
			if (breakpoints.size() > 0) {
				try {
					JSDIBreakpoint breakpoint = (JSDIBreakpoint) breakpoints.get(0);
					if (breakpoint instanceof JSDIScriptLoadBreakpoint) {
						return NLS.bind(ModelMessages.JSDIThread_suspended_loading_script, breakpoint.getScriptPath());
					}
				} catch (CoreException ce) {
					// TODO log this
					ce.printStackTrace();
				}
			}
			return SUSPENDED_STATUS;
		}
		return RUNNING_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getBreakpoints()
	 */
	public IBreakpoint[] getBreakpoints() {
		if (this.breakpoints.isEmpty()) {
			return NO_BREAKPOINTS;
		}
		return (IBreakpoint[]) this.breakpoints.toArray(new IBreakpoint[this.breakpoints.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getName()
	 */
	public String getName() throws DebugException {
		// TODO NLS this
		return NLS.bind("Thread [{0}] ({1})", new String[] { thread.name(), statusText() }); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getPriority()
	 */
	public int getPriority() throws DebugException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getStackFrames()
	 */
	public IStackFrame[] getStackFrames() throws DebugException {
		if (!isSuspended()) {
			return NO_STACK_FRAMES;
		}
		if (this.frames == null) {
			this.frames = new ArrayList();
			List threadFrames = this.thread.frames();
			for (Iterator iterator = threadFrames.iterator(); iterator.hasNext();) {
				StackFrameReference stackFrame = (StackFrameReference) iterator.next();
				JSDIStackFrame jsdiStackFrame = createJSDIStackFrame(stackFrame);
				this.frames.add(jsdiStackFrame);
			}
		}
		return (IStackFrame[]) this.frames.toArray(new IStackFrame[this.frames.size()]);
	}

	/**
	 * Delegate method to create a {@link JSDIStackFrame}
	 * 
	 * @param stackFrame
	 * @return a new {@link JSDIStackFrame}
	 */
	JSDIStackFrame createJSDIStackFrame(StackFrameReference stackFrame) {
		return new JSDIStackFrame(this, stackFrame);
	}

	/**
	 * Clears out old stack frames after resuming.
	 */
	private synchronized void clearFrames() {
		if (this.frames != null) {
			this.frames.clear();
			this.frames = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getTopStackFrame()
	 */
	public IStackFrame getTopStackFrame() throws DebugException {
		IStackFrame[] stackFrames = getStackFrames();
		if (stackFrames != null && stackFrames.length > 0) {
			return stackFrames[0];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#hasStackFrames()
	 */
	public boolean hasStackFrames() throws DebugException {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canResume()
	 */
	public synchronized boolean canResume() {
		return state == SUSPENDED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canSuspend()
	 */
	public synchronized boolean canSuspend() {
		return state == RUNNING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#isSuspended()
	 */
	public synchronized boolean isSuspended() {
		return this.state == SUSPENDED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#resume()
	 */
	public synchronized void resume() throws DebugException {
		if (getDebugTarget().isSuspended()) {
			getDebugTarget().resume();
		} else {
			resume(true);
		}
	}

	/**
	 * Callback for the owning target to tell the thread to suspend
	 */
	public synchronized void targetResume() {
		resume(false);
	}

	/**
	 * Performs the actual resume of the thread
	 * 
	 * @param fireevent
	 */
	void resume(boolean fireevent) {
		if (canResume()) {
			this.thread.resume();
			markResumed();
			if (fireevent) {
				fireResumeEvent(DebugEvent.CLIENT_REQUEST);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#suspend()
	 */
	public synchronized void suspend() throws DebugException {
		if (canSuspend()) {
			EventRequestManager requestManager = this.thread.virtualMachine().eventRequestManager();
			SuspendRequest suspendRequest = requestManager.createSuspendRequest(this.thread);
			suspendRequest.setEnabled(true);
			getJSDITarget().addJSDIEventListener(this, suspendRequest);
			this.thread.suspend();
		}
	}

	/**
	 * Call-back from a breakpoint that has been hit
	 * 
	 * @param breakpoint
	 * @param vote
	 * @return if the thread should suspended
	 */
	public boolean suspendForBreakpoint(JSDIBreakpoint breakpoint, boolean vote) {
		addBreakpoint(breakpoint);
		try {
			String condition = breakpoint.getCondition();
			if (condition != null) {
				// evaluate it
				// TODO This method has the negative effect that the frames will be loaded
				// for the underlying thread to do the evaluation
				// Ideally we should have an evaluation engine like JDT
				List frames = this.thread.frames();
				if (frames.isEmpty()) {
					return false;
				}
				Value value = ((StackFrameReference) frames.get(0)).evaluate(condition);
				if (breakpoint.isConditionSuspendOnTrue()) {
					return suspendForValue(value);
				} else {
					return !suspendForValue(value);
				}
			}
		} catch (CoreException ce) {
			// TODO log this
		}
		return true;
	}

	/**
	 * If the thread should suspend based on the given {@link Value}. Currently only suspend when the value is non-null and a {@link BooleanValue} that has the value <code>true</code>
	 * 
	 * @param value
	 * @return true if the thread should suspend false otherwise
	 */
	private boolean suspendForValue(Value value) {
		return value instanceof BooleanValue && ((BooleanValue) value).value();
	}

	/**
	 * Call-back from {@link JSDIBreakpoint#eventSetComplete(Event, JSDIDebugTarget, boolean, EventSet)} to handle suspending / cleanup
	 * 
	 * @param breakpoint
	 * @param suspend
	 *            if the thread should suspend
	 * @param eventSet
	 */
	public void suspendForBreakpointComplete(JSDIBreakpoint breakpoint, boolean suspend, EventSet eventSet) {
		// TODO clean up after voting - when added - and handle state / policy changes
		if (suspend) {
			try {
				if (breakpoint.getSuspendPolicy() == JSDIBreakpoint.SUSPEND_THREAD) {
					markSuspended();
				} else {
					getDebugTarget().suspend();
				}
				fireSuspendEvent(DebugEvent.BREAKPOINT);
			} catch (CoreException ce) {
				// TODO log this and do not suspend
			}
		}
	}

	/**
	 * Adds the given breakpoint to the collection for this thread
	 * 
	 * @param breakpoint
	 * @return if the breakpoint added removed an existing entry
	 */
	public boolean addBreakpoint(JSDIBreakpoint breakpoint) {
		synchronized (this.breakpoints) {
			return this.breakpoints.add(breakpoint);
		}
	}

	/**
	 * Removes the breakpoint from the cached collection of breakpoints
	 * 
	 * @param breakpoint
	 * @return if the breakpoint was removed
	 */
	public boolean removeBreakpoint(JSDIBreakpoint breakpoint) {
		synchronized (this.breakpoints) {
			return this.breakpoints.remove(breakpoint);
		}
	}

	/**
	 * Sets the state of the thread to {@link #SUSPENDED}
	 */
	synchronized void markSuspended() {
		this.state = SUSPENDED;
	}

	/**
	 * Sets the state of the thread to {@link #RUNNING} and clears any cached stack frames
	 */
	synchronized void markResumed() {
		this.state = RUNNING;
		clearFrames();
		this.breakpoints.clear();
	}

	/**
	 * Sets the state of the thread to {@link #TERMINATED}
	 */
	synchronized void markTerminated() {
		this.state = TERMINATED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepInto()
	 */
	public boolean canStepInto() {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepOver()
	 */
	public boolean canStepOver() {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepReturn()
	 */
	public boolean canStepReturn() {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#isStepping()
	 */
	public synchronized boolean isStepping() {
		return this.state == STEPPING;
	}

	/**
	 * Sends a step request and fires a step event if successful.
	 * 
	 * @param stepAction
	 *            step command to send
	 * @param eventDetail
	 *            debug event detail to fire
	 * @throws DebugException
	 *             if request is not successful
	 */
	private synchronized void step(int step, int debugEvent) throws DebugException {
		if (canResume()) {
			EventRequestManager requestManager = this.thread.virtualMachine().eventRequestManager();
			StepRequest stepRequest = requestManager.createStepRequest(this.thread, step);
			stepRequest.setEnabled(true);
			getJSDITarget().addJSDIEventListener(this, stepRequest);
			this.thread.resume();
			this.state = STEPPING;
			clearFrames();
			fireResumeEvent(debugEvent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepInto()
	 */
	public void stepInto() throws DebugException {
		step(StepRequest.STEP_INTO, DebugEvent.STEP_INTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepOver()
	 */
	public void stepOver() throws DebugException {
		step(StepRequest.STEP_OVER, DebugEvent.STEP_OVER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepReturn()
	 */
	public void stepReturn() throws DebugException {
		step(StepRequest.STEP_OUT, DebugEvent.STEP_RETURN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		return getDebugTarget().canTerminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public synchronized boolean isTerminated() {
		return this.state == TERMINATED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public synchronized void terminate() throws DebugException {
		this.state = TERMINATED;
		getJSDITarget().terminate();
	}

	/**
	 * Returns if the underlying {@link ThreadReference} of this thread matches the given {@link ThreadReference} using pointer equality
	 * 
	 * @param thread
	 * @return true if the {@link ThreadReference}s are the same
	 */
	public boolean matches(ThreadReference thread) {
		return this.thread == thread;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.IJSDIEventListener#eventSetComplete(org.eclipse.e4.languages.javascript.jsdi.event.Event, org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, boolean, org.eclipse.e4.languages.javascript.jsdi.event.EventSet)
	 */
	public void eventSetComplete(Event event, JSDIDebugTarget target, boolean suspend, EventSet eventSet) {
		if (event instanceof SuspendEvent) {
			SuspendEvent suspendEvent = (SuspendEvent) event;
			ThreadReference threadReference = suspendEvent.thread();
			if (threadReference == this.thread) {
				fireSuspendEvent(DebugEvent.CLIENT_REQUEST);
			}
			EventRequestManager requestManager = thread.virtualMachine().eventRequestManager();
			requestManager.deleteEventRequest(event.request());
			getJSDITarget().removeJSDIEventListener(this, event.request());
		}

		if (event instanceof StepEvent) {
			StepEvent stepEvent = (StepEvent) event;
			ThreadReference threadReference = stepEvent.thread();
			if (threadReference == this.thread) {
				fireSuspendEvent(DebugEvent.STEP_END);
			}
			EventRequestManager requestManager = this.thread.virtualMachine().eventRequestManager();
			requestManager.deleteEventRequest(event.request());
			getJSDITarget().addJSDIEventListener(this, event.request());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.IJSDIEventListener#handleEvent(org.eclipse.e4.languages.javascript.jsdi.event.Event, org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, boolean, org.eclipse.e4.languages.javascript.jsdi.event.EventSet)
	 */
	public synchronized boolean handleEvent(Event event, JSDIDebugTarget target, boolean suspendVote, EventSet eventSet) {
		if (event instanceof SuspendEvent) {
			SuspendEvent suspendEvent = (SuspendEvent) event;
			ThreadReference threadReference = suspendEvent.thread();
			if (threadReference == this.thread) {
				markSuspended();
			}
			return false;
		}
		if (event instanceof StepEvent) {
			StepEvent stepEvent = (StepEvent) event;
			ThreadReference threadReference = stepEvent.thread();
			if (threadReference == this.thread) {
				markSuspended();
			}
			return false;
		}
		return false;
	}
}
