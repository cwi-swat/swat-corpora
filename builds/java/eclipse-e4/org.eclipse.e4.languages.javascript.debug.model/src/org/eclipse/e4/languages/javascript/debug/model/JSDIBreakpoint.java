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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.Breakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.e4.languages.javascript.jsdi.ScriptReference;
import org.eclipse.e4.languages.javascript.jsdi.event.BreakpointEvent;
import org.eclipse.e4.languages.javascript.jsdi.event.Event;
import org.eclipse.e4.languages.javascript.jsdi.event.EventSet;
import org.eclipse.e4.languages.javascript.jsdi.event.ScriptLoadEvent;
import org.eclipse.e4.languages.javascript.jsdi.request.BreakpointRequest;
import org.eclipse.e4.languages.javascript.jsdi.request.EventRequest;
import org.eclipse.e4.languages.javascript.jsdi.request.ScriptLoadRequest;

/**
 * Abstract representation of a JSDI breakpoint
 * 
 * @since 1.0
 */
public abstract class JSDIBreakpoint extends Breakpoint implements ILineBreakpoint, IJSDIEventListener {

	/**
	 * The suspend policy for the breakpoint
	 */
	public static final String SUSPEND_POLICY = "SUSPEND_POLICY"; //$NON-NLS-1$

	/**
	 * Breakpoint attribute for the path of the script
	 */
	public static final String SCRIPT_PATH = "SCRIPT_PATH"; //$NON-NLS-1$

	/**
	 * The type name within the script
	 */
	public static final String TYPE_NAME = "TYPE_NAME"; //$NON-NLS-1$

	/**
	 * The hit count set in the breakpoint
	 */
	public static final String HIT_COUNT = "HIT_COUNT"; //$NON-NLS-1$

	/**
	 * The condition for the breakpoint
	 */
	public static final String CONDITION = "CONDITION"; //$NON-NLS-1$

	/**
	 * The total count of all of the targets this breakpoint is installed in
	 */
	public static final String INSTALL_COUNT = "INSTALL_COUNT"; //$NON-NLS-1$

	/**
	 * If the condition is enabled for the breakpoint or not - allows us to keep conditions even if they are not used
	 */
	public static final String CONDITION_ENABLED = "CONDITION_ENABLED"; //$NON-NLS-1$

	/**
	 * If the breakpoint should suspend when the condition evaluates to true
	 */
	public static final String CONDITION_SUSPEND_ON_TRUE = "CONDITION_SUSPEND_ON_TRUE"; //$NON-NLS-1$

	/**
	 * JSDT member handle
	 */
	public static final String HANDLE = "HANDLE"; //$NON-NLS-1$

	/**
	 * Suspend policy for suspending the current thread of execution
	 */
	public static final int SUSPEND_THREAD = 1;
	/**
	 * Suspend policy for suspending the current target
	 */
	public static final int SUSPEND_TARGET = 2;

	private HashSet targets = null;
	private HashMap requestspertarget = new HashMap(4);

	/**
	 * The id of the attribute for a method signature
	 */
	public static final String FUNCTION_SIGNAURE = "org.eclipse.e4.languages.javascript.methodsignature"; //$NON-NLS-1$

	/**
	 * The id of the attribute for the function name
	 */
	public static final String FUNCTION_NAME = "org.eclipse.e4.languages.javascript.functionname"; //$NON-NLS-1$

	/**
	 * Registered marker id for a JSDI line breakpoint
	 */
	public static final String JSDI_LINE_BREAKPOINT = "org.eclipse.e4.languages.javascript.line.breakpoint.marker"; //$NON-NLS-1$

	/**
	 * Registered marker id for a JSDI function breakpoint
	 */
	public static final String JSDI_FUNCTION_BREAKPOINT = "org.eclipse.e4.languages.javascript.function.breakpoint.marker"; //$NON-NLS-1$

	/**
	 * Registered marker id for a JSDI script load breakpoint
	 */
	public static final String JSDI_SCRIPT_LOAD_BREAKPOINT = "org.eclipse.e4.languages.javascript.scriptload.breakpoint.marker"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.Breakpoint#setMarker(org.eclipse.core.resources.IMarker)
	 */
	public void setMarker(IMarker marker) throws CoreException {
		super.setMarker(marker);
		// reset the install count
		setAttribute(INSTALL_COUNT, 0);
	}

	/**
	 * Add this breakpoint to the breakpoint manager, or sets it as unregistered.
	 */
	protected void register(boolean register) throws CoreException {
		DebugPlugin plugin = DebugPlugin.getDefault();
		if (plugin != null && register) {
			plugin.getBreakpointManager().addBreakpoint(this);
		} else {
			setRegistered(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IBreakpoint#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return JSDIDebugModel.MODEL_ID;
	}

	/**
	 * Returns whether this breakpoint should be "skipped". Breakpoints are skipped if the breakpoint manager is disabled and the breakpoint is registered with the manager
	 * 
	 * @return whether this breakpoint should be skipped
	 */
	public boolean shouldSkipBreakpoint() throws CoreException {
		DebugPlugin plugin = DebugPlugin.getDefault();
		return plugin != null && isRegistered() && !plugin.getBreakpointManager().isEnabled();
	}

	/**
	 * Adds the given debug target to the listing of targets that this breakpoint cares about
	 * 
	 * @param target
	 * @throws CoreException
	 */
	public void addToTarget(JSDIDebugTarget target) throws CoreException {
		if (target.isTerminated() || shouldSkipBreakpoint()) {
			return;
		}
		// Add script load handler
		ScriptLoadRequest request = target.getEventRequestManager().createScriptLoadRequest();
		request.setEnabled(isEnabled());
		registerRequest(target, request);

		// Add to all loaded scripts
		String script = getScriptPath();
		if (script == null) {
			return;
		}
		script = new Path(script).lastSegment();
		List/* ScriptReference */scripts = target.allScriptsByName(script);
		boolean success = true;
		for (Iterator iter = scripts.iterator(); iter.hasNext();) {
			success &= createRequest(target, (ScriptReference) iter.next());
		}
		if (success) {
			if (this.targets == null) {
				this.targets = new HashSet();
			}
			this.targets.add(target);
		}
	}

	/**
	 * Creates a request for the given script in the given target
	 * 
	 * @param target
	 *            the target to register with
	 * @param script
	 *            the script we want to set the breakpoint in
	 * @return true if a new request was created false otherwise
	 * @throws CoreException
	 */
	protected abstract boolean createRequest(JSDIDebugTarget target, ScriptReference script) throws CoreException;

	/**
	 * Configures the request with attributes from the breakpoint
	 * 
	 * @param request
	 * @throws CoreException
	 */
	protected void configureRequest(BreakpointRequest request) throws CoreException {
		request.addHitCountFilter(getHitCount());
		request.addConditionFilter(getCondition());
		// TODO add thread filters
	}

	/**
	 * Removes the given debug target from the listing of targets this breakpoint cares about
	 * 
	 * @param target
	 */
	public void removeFromTarget(JSDIDebugTarget target) {
		List requests = getRequests(target);
		EventRequest request = null;
		for (Iterator iter = requests.iterator(); iter.hasNext();) {
			try {
				request = (EventRequest) iter.next();
				if (target.isAvailable()) {
					target.getEventRequestManager().deleteEventRequest(request);
				}
			} finally {
				deregisterRequest(target, request);
			}
		}
		synchronized (this.requestspertarget) {
			this.requestspertarget.remove(target);
		}
		if (this.targets == null) {
			return;
		}
		this.targets.remove(target);
	}

	/**
	 * Recreate this breakpoint in all targets
	 * 
	 * @throws CoreException
	 */
	protected void recreateBreakpoint() throws CoreException {
		DebugPlugin plugin = DebugPlugin.getDefault();
		if (plugin != null) {
			IDebugTarget[] targets = plugin.getLaunchManager().getDebugTargets();
			for (int i = 0; i < targets.length; i++) {
				// TODO could also ask for the adapter
				if (targets[i].getModelIdentifier().equals(JSDIDebugModel.MODEL_ID)) {
					recreateBreakpointFor((JSDIDebugTarget) targets[i]);
				}
			}
		}
	}

	/**
	 * Recreates this breakpoint in the given debug target
	 * 
	 * @param target
	 * @throws CoreException
	 */
	void recreateBreakpointFor(JSDIDebugTarget target) throws CoreException {
		if (target.isAvailable() && target.getBreakpoints().contains(this)) {
			removeFromTarget(target);
			addToTarget(target);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.Breakpoint#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) throws CoreException {
		recreateBreakpoint();
		super.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILineBreakpoint#getLineNumber()
	 */
	public int getLineNumber() throws CoreException {
		return ensureMarker().getAttribute(IMarker.LINE_NUMBER, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILineBreakpoint#getCharStart()
	 */
	public int getCharStart() throws CoreException {
		return ensureMarker().getAttribute(IMarker.CHAR_START, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILineBreakpoint#getCharEnd()
	 */
	public int getCharEnd() throws CoreException {
		return ensureMarker().getAttribute(IMarker.CHAR_END, -1);
	}

	/**
	 * Sets the suspend policy for this breakpoint
	 * 
	 * @param policy
	 * @throws CoreException
	 */
	public void setSuspendPolicy(int policy) throws CoreException {
		ensureMarker().setAttribute(SUSPEND_POLICY, policy);
	}

	/**
	 * Returns the suspend policy for this breakpoint, default suspend policy is to suspend the thread
	 * 
	 * @return the suspend policy
	 * @see #SUSPEND_THREAD
	 * @see #SUSPEND_TARGET
	 * @throws CoreException
	 */
	public int getSuspendPolicy() throws CoreException {
		return ensureMarker().getAttribute(SUSPEND_POLICY, SUSPEND_THREAD);
	}

	/**
	 * Allows the enabled state of the condition to be set
	 * 
	 * @param enabled
	 * @throws CoreException
	 */
	public void setConditionEnabled(boolean enabled) throws CoreException {
		ensureMarker().setAttribute(CONDITION_ENABLED, enabled);
	}

	/**
	 * Returns if the condition for this breakpoint is enabled or not
	 * 
	 * @return if the condition is enabled
	 * @throws CoreException
	 */
	public boolean isConditionEnabled() throws CoreException {
		return ensureMarker().getAttribute(CONDITION_ENABLED, false) && getCondition() != null;
	}

	/**
	 * Returns if the breakpoint should suspend when the assigned condition evaluates to 'true'.
	 * 
	 * @return true if the breakpoint should suspend when the condition is true
	 * @throws CoreException
	 */
	public boolean isConditionSuspendOnTrue() throws CoreException {
		return ensureMarker().getAttribute(CONDITION_SUSPEND_ON_TRUE, true);
	}

	/**
	 * Sets the given condition for the breakpoint
	 * 
	 * @param condition
	 * @throws CoreException
	 */
	public void setCondition(String condition) throws CoreException {
		if (condition != null && condition.length() == 0) {
			condition = null;
		}
		ensureMarker().setAttribute(CONDITION, condition);
		recreateBreakpoint();
	}

	/**
	 * Sets if the condition for this breakpoint will suspend when it evaluates to true.
	 * 
	 * @param suspendontrue
	 * @throws CoreException
	 */
	public void setConditionSuspendOnTrue(boolean suspendontrue) throws CoreException {
		if (suspendontrue != isConditionSuspendOnTrue()) {
			ensureMarker().setAttribute(CONDITION_SUSPEND_ON_TRUE, suspendontrue);
			recreateBreakpoint();
		}
	}

	/**
	 * Returns the condition set for this breakpoint or <code>null</code>
	 * 
	 * @return the condition
	 * @throws CoreException
	 */
	public String getCondition() throws CoreException {
		return ensureMarker().getAttribute(CONDITION, null);
	}

	/**
	 * Returns if this breakpoint is currently installed in any targets
	 * 
	 * @return true if this breakpoint is installed in any targets, false otherwise
	 * @throws CoreException
	 */
	public boolean isInstalled() throws CoreException {
		return ensureMarker().getAttribute(INSTALL_COUNT, 0) > 0;
	}

	/**
	 * Increments the install count of this breakpoint
	 */
	protected void incrementInstallCount() throws CoreException {
		int count = getInstallCount();
		ensureMarker().setAttribute(INSTALL_COUNT, count + 1);
	}

	/**
	 * Returns the <code>INSTALL_COUNT</code> attribute of this breakpoint or 0 if the attribute is not set.
	 */
	public int getInstallCount() throws CoreException {
		return ensureMarker().getAttribute(INSTALL_COUNT, 0);
	}

	/**
	 * Decrements the install count of this breakpoint.
	 */
	protected void decrementInstallCount() throws CoreException {
		int count = getInstallCount();
		if (count > 0) {
			ensureMarker().setAttribute(INSTALL_COUNT, count - 1);
		}
	}

	/**
	 * Sets the given hit count for the breakpoint, throws an {@link IllegalArgumentException} if the given count is less than 1.
	 * 
	 * @param count
	 * @throws CoreException
	 * @throws IllegalArgumentException
	 *             if count &lt; 1
	 */
	public void setHitCount(int count) throws CoreException, IllegalArgumentException {
		if (count < 0) {
			// TODO NLS this
			throw new IllegalArgumentException("A breakpoint hit count must be greater than -1"); //$NON-NLS-1$
		}
		if (count != getHitCount()) {
			ensureMarker().setAttribute(HIT_COUNT, count);
			recreateBreakpoint();
		}
	}

	/**
	 * Returns the hit count set for this breakpoint or -1 if no hit count has been set.
	 * 
	 * @return the hit count
	 * @throws CoreException
	 */
	public int getHitCount() throws CoreException {
		return ensureMarker().getAttribute(HIT_COUNT, -1);
	}

	/**
	 * Returns the name of the script as it was set when the breakpoint was created
	 * 
	 * @return the name of the script this breakpoint was created on
	 * @throws CoreException
	 */
	public String getScriptPath() throws CoreException {
		return ensureMarker().getAttribute(SCRIPT_PATH, null);
	}

	/**
	 * Returns the type name that the breakpoint is set within. When <code>null</code> is returned the breakpoint is set in the top level type i.e. the root source
	 * 
	 * @return the typeName
	 */
	public String getTypeName() throws CoreException {
		return ensureMarker().getAttribute(TYPE_NAME, null);
	}

	/**
	 * Registers the given request for this breakpoint for the given target
	 * 
	 * @param target
	 * @param request
	 */
	protected void registerRequest(JSDIDebugTarget target, EventRequest request) {
		addRequestForTarget(target, request);
		if (!(request instanceof ScriptLoadRequest)) {
			try {
				incrementInstallCount();
			} catch (CoreException ce) {
				// TODO log this
			}
		}

	}

	protected synchronized void addRequestForTarget(JSDIDebugTarget target, EventRequest request) {
		ArrayList requests = getRequests(target);
		if (requests.isEmpty()) {
			synchronized (this.requestspertarget) {
				this.requestspertarget.put(target, requests);
			}
		}
		requests.add(request);
		target.addJSDIEventListener(this, request);
	}

	/**
	 * Returns any existing requests associated with the given target
	 * 
	 * @param target
	 * @return list of requests for the given target
	 */
	protected ArrayList getRequests(JSDIDebugTarget target) {
		ArrayList list = null;
		synchronized (this.requestspertarget) {
			list = (ArrayList) this.requestspertarget.get(target);
		}
		if (list == null) {
			list = new ArrayList(2);
		}
		return list;
	}

	/**
	 * Remove this breakpoint as an event listener
	 * 
	 * @param target
	 * @param request
	 */
	protected void deregisterRequest(JSDIDebugTarget target, EventRequest request) {
		target.removeJSDIEventListener(this, request);
		if (!(request instanceof ScriptLoadRequest)) {
			try {
				decrementInstallCount();
			} catch (CoreException ce) {
				// TODO log this
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.IJSDIEventListener#handleEvent(org.eclipse.e4.languages.javascript.jsdi.event.Event, org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, boolean, org.eclipse.e4.languages.javascript.jsdi.event.EventSet)
	 */
	public boolean handleEvent(Event event, JSDIDebugTarget target, boolean suspendVote, EventSet eventSet) {
		// get the thread and suspend it
		if (event instanceof BreakpointEvent) {
			if (skipEvent((BreakpointEvent) event)) {
				return true;
			}
			JSDIThread thread = target.findThread(((BreakpointEvent) event).thread());
			if (thread != null) {
				return !thread.suspendForBreakpoint(this, suspendVote);
			}
		}
		if (event instanceof ScriptLoadEvent) {
			ScriptLoadEvent sevent = (ScriptLoadEvent) event;
			ScriptReference script = sevent.script();
			try {
				// TODO need something fancier in the future
				if (new Path(getScriptPath()).equals(script.sourceURI().getPath())) {
					createRequest(target, sevent.script());
				}
			} catch (CoreException ce) {
				// TODO log this
				ce.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * If the breakpoint event should be skipped - i.e. if the thread should resume and ignore the event
	 * 
	 * @param event
	 * @return true if the event should be skipped, false otherwise
	 */
	boolean skipEvent(BreakpointEvent event) {
		try {
			Path root = new Path(getScriptPath());
			if (root.segmentCount() == 1) {
				return false;
			}
			ScriptReference script = event.location().scriptReference();
			return !getScriptPath().equals(script.sourceURI().getPath());
		} catch (CoreException ce) {
			// TODO log this
		}
		return true;
	}

	/**
	 * Returns if the type names for the breakpoint are equal or not. Two <code>null</code> type names are considered to be equal.
	 * 
	 * @param tname1
	 * @param tname2
	 * @return true if the type names are equal, false otherwise
	 */
	boolean typeNamesEqual(String tname1, String tname2) {
		if (tname1 == null && tname2 == null) {
			return true;
		}
		return tname1 != null && tname1.equals(tname2);
	}

	/**
	 * Custom comparison to avoid the leading separator issue from saying the paths are not equal
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	boolean pathsEqual(IPath p1, IPath p2) {
		if (p1.segmentCount() == p2.segmentCount()) {
			String[] segments = p1.segments();
			for (int i = 0; i < segments.length; i++) {
				if (!segments[i].equals(p2.segment(i))) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.languages.javascript.debug.model.IJSDIEventListener#eventSetComplete(org.eclipse.e4.languages.javascript.jsdi.event.Event, org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget, boolean, org.eclipse.e4.languages.javascript.jsdi.event.EventSet)
	 */
	public void eventSetComplete(Event event, JSDIDebugTarget target, boolean suspend, EventSet eventSet) {
		if (event instanceof BreakpointEvent) {
			JSDIThread thread = target.findThread(((BreakpointEvent) event).thread());
			if (thread != null) {
				thread.suspendForBreakpointComplete(this, suspend, eventSet);
			}
		}
	}
}
