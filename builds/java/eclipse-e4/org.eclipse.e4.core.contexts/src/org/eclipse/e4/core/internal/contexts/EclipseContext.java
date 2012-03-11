/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.core.internal.contexts;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.contexts.RunAndTrack;
import org.eclipse.e4.core.internal.contexts.osgi.ContextDebugHelper;

/**
 * This implementation assumes that all contexts are of the class EclipseContext. The external
 * methods of it are exposed via IEclipseContext.
 */
public class EclipseContext implements IEclipseContext {

	/**
	 * A context key identifying the parent context.
	 */
	public static final String PARENT = "parentContext"; //$NON-NLS-1$

	/**
	 * A context key (value "debugString") identifying a value to use in debug statements for a
	 * context. A computed value can be used to embed more complex information in debug statements.
	 */
	public static final String DEBUG_STRING = "debugString"; //$NON-NLS-1$

	static class Scheduled {

		public TrackableComputationExt runnable;
		public ContextChangeEvent event;

		public Scheduled(TrackableComputationExt runnable, ContextChangeEvent event) {
			this.runnable = runnable;
			this.event = event;
		}

		public int hashCode() {
			return 31 * (31 + event.hashCode()) + runnable.hashCode();
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Scheduled other = (Scheduled) obj;
			if (!event.equals(other.event))
				return false;
			return runnable.equals(other.runnable);
		}
	}

	static ThreadLocal<Computation> currentComputation = new ThreadLocal<Computation>();

	private Map<String, Set<Computation>> listeners = Collections.synchronizedMap(new HashMap<String, Set<Computation>>());

	final Map<String, ValueComputation> localValueComputations = Collections.synchronizedMap(new HashMap<String, ValueComputation>());
	final Map<String, Object> localValues = Collections.synchronizedMap(new HashMap<String, Object>());

	private final ILookupStrategy strategy;

	private ArrayList<String> modifiable;

	private List<Computation> waiting; // list of Computations; null for all non-root entries

	private Set<WeakReference<EclipseContext>> children = new HashSet<WeakReference<EclipseContext>>();

	private Set<IContextDisposalListener> notifyOnDisposal = new HashSet<IContextDisposalListener>();

	/**
	 * A context key (value "activeChildContext") that identifies another {@link IEclipseContext}
	 * that is a child of the context. The meaning of active is up to the application.
	 */
	public static final String ACTIVE_CHILD = "activeChildContext"; //$NON-NLS-1$

	static private final IEclipseContextDebugger debugAddOn = ContextDebugHelper.getDebugger();

	public EclipseContext(IEclipseContext parent, ILookupStrategy strategy) {
		this.strategy = strategy;
		setParent(parent);
		if (parent == null)
			waiting = Collections.synchronizedList(new ArrayList<Computation>());
		if (debugAddOn != null)
			debugAddOn.notify(this, IEclipseContextDebugger.EventType.CONSTRUCTED, null);
	}

	public Set<EclipseContext> getChildren() {
		if (children.size() == 0)
			return null;
		Set<EclipseContext> result = new HashSet<EclipseContext>(children.size());
		synchronized (children) {
			for (Iterator<WeakReference<EclipseContext>> i = children.iterator(); i.hasNext();) {
				EclipseContext referredContext = i.next().get();
				if (referredContext == null) {
					i.remove();
					continue;
				}
				result.add(referredContext);
			}
		}
		return result;
	}

	public boolean containsKey(String name) {
		return containsKey(name, false);
	}

	public boolean containsKey(String name, boolean localOnly) {
		if (isSetLocally(name))
			return true;
		if (localOnly)
			return false;
		IEclipseContext parent = getParent();
		if (parent != null && parent.containsKey(name))
			return true;
		if (strategy != null) {
			if (strategy.containsKey(name, this))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.core.services.context.IEclipseContext#dispose()
	 */
	public void dispose() {
		// dispose of child contexts first
		EclipseContext[] currentChildren = null;
		synchronized (children) {
			if (children.size() > 0) {
				Set<EclipseContext> localCopy = new HashSet<EclipseContext>(children.size());
				for (WeakReference<EclipseContext> childContextRef : children) {
					EclipseContext childContext = childContextRef.get();
					if (childContext != null)
						localCopy.add(childContext);
				}
				currentChildren = new EclipseContext[localCopy.size()];
				localCopy.toArray(currentChildren);
				children.clear(); // just in case
			}
		}
		if (currentChildren != null) {
			for (EclipseContext childContext : currentChildren) {
				childContext.dispose();
			}
		}

		ContextChangeEvent event = new ContextChangeEvent(this, ContextChangeEvent.DISPOSE, null, null, null);
		List<Scheduled> scheduled = new ArrayList<Scheduled>();
		Set<Computation> allComputations = new HashSet<Computation>();
		for (Set<Computation> computations : listeners.values()) {
			allComputations.addAll(computations);
		}
		listeners.clear();
		for (Computation computation : allComputations) {
			computation.handleInvalid(event, scheduled);
		}
		processScheduled(scheduled);

		synchronized (notifyOnDisposal) {
			for (IContextDisposalListener listener : notifyOnDisposal) {
				listener.disposed(this);
			}
		}

		if (strategy != null)
			strategy.dispose();
		localValueComputations.clear();

		// if this was the parent's active child, deactivate it
		EclipseContext parent = getParent();
		if (parent != null) {
			if (this == parent.getActiveChild())
				parent.set(ACTIVE_CHILD, null);
		}

		localValues.clear();

		if (parent != null)
			parent.removeChild(this);

		if (debugAddOn != null)
			debugAddOn.notify(this, IEclipseContextDebugger.EventType.DISPOSED, null);
	}

	public Object get(String name) {
		return internalGet(this, name, false);
	}

	public Object getLocal(String name) {
		return internalGet(this, name, true);
	}

	public Object internalGet(EclipseContext originatingContext, String name, boolean local) {
		trackAccess(name);
		if (this == originatingContext) {
			ValueComputation valueComputation = localValueComputations.get(name);
			if (valueComputation != null) {
				return valueComputation.get();
			}
		}

		Object result = null;
		// 1. try for local value
		if (localValues.containsKey(name)) {
			result = localValues.get(name);
			if (result == null)
				return null;
		} else if (strategy != null) // 2. try the local strategy
			result = strategy.lookup(name, originatingContext);

		// if we found something, compute the concrete value and return
		if (result != null) {
			if (result instanceof IContextFunction) {
				ValueComputation valueComputation = new ValueComputation(this, originatingContext, name, ((IContextFunction) result));
				originatingContext.localValueComputations.put(name, valueComputation);

				// the cached value depends on all entries with this name and all parent relationships
				// between the originating context and this context, inclusive
				for (EclipseContext step = originatingContext; step != null; step = step.getParent()) {
					valueComputation.addDependency(step, name);
					if (step == this)
						break;
					valueComputation.addDependency(step, PARENT);
				}
				result = valueComputation.get();
			}
			return result;
		}
		// 3. delegate to parent
		if (!local) {
			IEclipseContext parent = (IEclipseContext) getLocal(PARENT);
			if (parent != null) {
				return ((EclipseContext) parent).internalGet(originatingContext, name, local);
			}
		}
		return null;
	}

	/**
	 * The given name has been modified or removed in this context. Invalidate all local value
	 * computations and listeners that depend on this name.
	 */
	public void invalidate(String name, int eventType, Object oldValue, List<Scheduled> scheduled) {
		if (DebugHelper.DEBUG_NAMES)
			System.out.println("[context] invalidating \"" + name + "\" on " + toString()); //$NON-NLS-1$ //$NON-NLS-2$
		removeLocalValueComputations(name);
		handleInvalid(name, eventType, oldValue, scheduled);
	}

	/**
	 * The value of the given name has changed in this context. This either means the value has been
	 * changed directly, or the value is a function that has been invalidated (one of the function's
	 * dependencies has changed).
	 */
	void handleInvalid(String name, int eventType, Object oldValue, List<Scheduled> scheduled) {
		Set<Computation> computations = listeners.remove(name);
		if (computations == null)
			return;
		ContextChangeEvent event = new ContextChangeEvent(this, eventType, null, name, oldValue);
		for (Computation computation : computations) {
			computation.handleInvalid(event, scheduled);
		}
	}

	private boolean isSetLocally(String name) {
		trackAccess(name);
		return localValues.containsKey(name);
	}

	public void remove(String name) {
		if (isSetLocally(name)) {
			Object oldValue = localValues.remove(name);
			List<Scheduled> scheduled = new ArrayList<Scheduled>();
			invalidate(name, ContextChangeEvent.REMOVED, oldValue, scheduled);
			processScheduled(scheduled);
		}
	}

	/**
	 * Removes all local value computations associated with the given name.
	 * @param name The name to remove
	 */
	public void removeLocalValueComputations(String name) {
		synchronized (localValueComputations) {
			ValueComputation removed = localValueComputations.remove(name);
			if (removed != null)
				removed.stopListening(null, name);
		}
	}

	public void runAndTrack(final RunAndTrack runnable) {
		ContextChangeEvent event = new ContextChangeEvent(this, ContextChangeEvent.INITIAL, null, null, null);
		TrackableComputationExt computation = new TrackableComputationExt(runnable, this);
		computation.update(event);
	}

	protected void processScheduled(List<Scheduled> scheduledList) {
		HashSet<Scheduled> sent = new HashSet<Scheduled>(scheduledList.size());
		for (Iterator<Scheduled> i = scheduledList.iterator(); i.hasNext();) {
			Scheduled scheduled = i.next();
			// don't send the same event twice
			if (!sent.add(scheduled))
				continue;
			scheduled.runnable.update(scheduled.event);
		}
	}

	public void set(String name, Object value) {
		if (DebugHelper.DEBUG_NAMES)
			System.out.println("[context] set(" + name + ',' + value + ")" + " on " + toString());//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if (PARENT.equals(name)) {
			setParent((IEclipseContext) value);
			return;
		}
		boolean containsKey = localValues.containsKey(name);
		Object oldValue = localValues.put(name, value);
		if (!containsKey || value != oldValue) {
			List<Scheduled> scheduled = new ArrayList<Scheduled>();
			invalidate(name, ContextChangeEvent.ADDED, oldValue, scheduled);
			processScheduled(scheduled);
		}
	}

	public void modify(String name, Object value) {
		List<Scheduled> scheduled = new ArrayList<Scheduled>();
		if (!internalModify(name, value, scheduled))
			set(name, value);
		processScheduled(scheduled);
	}

	public boolean internalModify(String name, Object value, List<Scheduled> scheduled) {
		boolean containsKey = localValues.containsKey(name);
		if (containsKey) {
			if (!checkModifiable(name)) {
				String tmp = "Variable " + name + " is not modifiable in the context " + toString(); //$NON-NLS-1$ //$NON-NLS-2$
				throw new IllegalArgumentException(tmp);
			}
			Object oldValue = localValues.put(name, value);
			if (value != oldValue)
				invalidate(name, ContextChangeEvent.ADDED, oldValue, scheduled);
			return true;
		}

		EclipseContext parent = getParent();
		if (parent != null)
			return parent.internalModify(name, value, scheduled);
		return false;
	}

	public EclipseContext getParent() {
		trackAccess(PARENT);
		return (EclipseContext) localValues.get(PARENT);
	}

	public void setParent(IEclipseContext parent) {
		EclipseContext parentContext = (EclipseContext) localValues.get(PARENT);
		if (parent == parentContext)
			return; // no-op
		if (parentContext != null)
			parentContext.removeChild(this);
		List<Scheduled> scheduled = new ArrayList<Scheduled>();
		handleReparent((EclipseContext) parent, scheduled);
		localValues.put(PARENT, parent);
		if (parent != null)
			((EclipseContext) parent).addChild(this);
		processScheduled(scheduled);
		return;
	}

	/**
	 * Returns a string representation of this context for debugging purposes only.
	 */
	public String toString() {
		Object debugString = localValues.get(DEBUG_STRING);
		return debugString instanceof String ? ((String) debugString) : "Anonymous Context"; //$NON-NLS-1$
	}

	private void trackAccess(String name) {
		Computation computation = currentComputation.get();
		if (computation != null) {
			computation.addDependency(this, name);
		}
	}

	public void declareModifiable(String name) {
		if (name == null)
			return;
		if (modifiable == null)
			modifiable = new ArrayList<String>(3);
		modifiable.add(name);
		if (localValues.containsKey(name))
			return;
		localValues.put(name, null);
	}

	private boolean checkModifiable(String name) {
		if (modifiable == null)
			return false;
		for (Iterator<String> i = modifiable.iterator(); i.hasNext();) {
			String candidate = i.next();
			if (candidate.equals(name))
				return true;
		}
		return false;
	}

	public void removeListenersTo(Object object) {
		if (object == null)
			return;
		ContextChangeEvent event = new ContextChangeEvent(this, ContextChangeEvent.UNINJECTED, new Object[] {object}, null, null);
		// TBD computation here removes listeners. We should do that inside this method instead
		Set<Computation> computations = getListeners();
		Computation[] ls = computations.toArray(new Computation[computations.size()]);
		for (Computation computation : ls) {
			if (computation instanceof TrackableComputationExt)
				((TrackableComputationExt) computation).update(event);
		}
	}

	private void handleReparent(EclipseContext newParent, List<Scheduled> scheduled) {
		// TBD should we lock waiting list while doing reparent?
		// Add "boolean inReparent" on the root context and process right away?
		processWaiting();
		// 1) everybody who depends on me: I need to collect combined list of names injected
		Set<String> tmp = listeners.keySet(); // clone internal name list
		Set<String> usedNames = new HashSet<String>(tmp.size());
		usedNames.addAll(tmp);

		// 2) for each used name:
		for (Iterator<String> i = usedNames.iterator(); i.hasNext();) {
			String name = i.next();
			if (localValues.containsKey(name))
				continue; // it is a local value
			Object oldValue = get(name);
			Object newValue = (newParent != null) ? newParent.get(name) : null;
			if (oldValue != newValue)
				invalidate(name, ContextChangeEvent.ADDED, oldValue, scheduled);
		}
		localValueComputations.clear();
	}

	public void processWaiting() {
		// traverse to the root node
		EclipseContext parent = getParent();
		if (parent != null) {
			parent.processWaiting();
			return;
		}
		if (waiting == null)
			return;
		// create update notifications
		Computation[] ls = waiting.toArray(new Computation[waiting.size()]);
		waiting.clear();
		ContextChangeEvent event = new ContextChangeEvent(this, ContextChangeEvent.UPDATE, null, null, null);
		for (int i = 0; i < ls.length; i++) {
			if (ls[i] instanceof TrackableComputationExt)
				((TrackableComputationExt) ls[i]).update(event);
		}
	}

	public void addWaiting(Computation cp) {
		// traverse to the root node
		EclipseContext parent = getParent();
		if (parent != null) {
			parent.addWaiting(cp);
			return;
		}
		if (waiting == null) // could happen on re-parent
			waiting = Collections.synchronizedList(new ArrayList<Computation>());
		waiting.add(cp);
	}

	protected EclipseContext getRoot() {
		EclipseContext current = this;
		EclipseContext root;
		do {
			root = current;
			current = current.getParent();
		} while (current != null);
		return root;
	}

	public void addChild(EclipseContext childContext) {
		synchronized (children) {
			children.add(new WeakReference<EclipseContext>(childContext));
		}
	}

	public void removeChild(EclipseContext childContext) {
		synchronized (children) {
			for (Iterator<WeakReference<EclipseContext>> i = children.iterator(); i.hasNext();) {
				EclipseContext referredContext = i.next().get();
				if (referredContext == null) {
					i.remove();
					continue;
				}
				if (referredContext == childContext) {
					i.remove();
					return;
				}
			}
		}
	}

	public <T> T get(Class<T> clazz) {
		return clazz.cast(get(clazz.getName()));
	}

	public boolean containsKey(Class<?> clazz) {
		return containsKey(clazz.getName());
	}

	public <T> void set(Class<T> clazz, T value) {
		set(clazz.getName(), value);
	}

	public void remove(Class<?> clazz) {
		remove(clazz.getName());
	}

	public <T> T getLocal(Class<T> clazz) {
		return clazz.cast(getLocal(clazz.getName()));
	}

	public <T> void modify(Class<T> clazz, T value) {
		modify(clazz.getName(), value);
	}

	public void declareModifiable(Class<?> clazz) {
		declareModifiable(clazz.getName());
	}

	public IEclipseContext createChild() {
		return new EclipseContext(this, null); // strategies are not inherited
	}

	public IEclipseContext createChild(String name) {
		IEclipseContext result = createChild();
		result.set(DEBUG_STRING, name);
		return result;
	}

	public void notifyOnDisposal(IContextDisposalListener listener) {
		synchronized (notifyOnDisposal) {
			notifyOnDisposal.add(listener);
		}
	}

	static public ThreadLocal<Computation> localComputation() {
		return currentComputation;
	}

	public IEclipseContext getActiveChild() {
		return (EclipseContext) internalGet(this, ACTIVE_CHILD, true);
	}

	public IEclipseContext getActiveLeaf() {
		IEclipseContext activeContext = this;
		IEclipseContext child = getActiveChild();
		while (child != null) {
			activeContext = child;
			child = child.getActiveChild();
		}
		return activeContext;
	}

	public void activate() {
		EclipseContext parent = getParent();
		if (parent == null)
			return;
		if (this == parent.getActiveChild())
			return;
		parent.set(ACTIVE_CHILD, this);
	}

	public void activateBranch() {
		for (IEclipseContext i = this; i != null; i = i.getParent()) {
			i.activate();
		}
	}

	public void deactivate() {
		EclipseContext parent = getParent();
		if (parent == null)
			return;
		if (this != parent.getActiveChild())
			return; // this is not an active context; return 
		parent.set(ACTIVE_CHILD, null);
	}

	// This method is for debug only, do not use externally
	public Map<String, Object> localData() {
		Map<String, Object> result = new HashMap<String, Object>(localValues.size());
		for (String string : localValues.keySet()) {
			Object value = localValues.get(string);
			if (value instanceof IContextFunction)
				continue;
			result.put(string, value);
		}
		return result;
	}

	// This method is for debug only, do not use externally
	public Map<String, Object> localContextFunction() {
		Map<String, Object> result = new HashMap<String, Object>(localValues.size());
		for (String string : localValues.keySet()) {
			Object value = localValues.get(string);
			if (value instanceof IContextFunction)
				result.put(string, value);
		}
		return result;
	}

	// This method is for debug only, do not use externally
	public Map<String, Object> cachedCachedContextFunctions() {
		Map<String, Object> result = new HashMap<String, Object>(localValueComputations.size());
		for (String string : localValueComputations.keySet()) {
			result.put(string, localValueComputations.get(string).get());
		}
		return result;
	}

	// This method is for debug only, do not use externally
	public Set<String> getRawListenerNames() {
		Set<String> tmp = listeners.keySet(); // clone internal name list
		Set<String> usedNames = new HashSet<String>(tmp.size());
		usedNames.addAll(tmp);
		return usedNames;
	}

	// This method is for debug only, do not use externally
	public Set<Computation> getListeners(String name) {
		Set<Computation> tmp = listeners.get(name);
		Set<Computation> result = new HashSet<Computation>(tmp.size());
		result.addAll(tmp);
		return result;
	}

	public void addListener(Computation computation, Set<String> names) {
		for (String name : names) {
			if (listeners.containsKey(name)) {
				Set<Computation> existingDependencies = listeners.get(name);
				existingDependencies.add(computation);
			} else {
				Set<Computation> computations = new HashSet<Computation>();
				computations.add(computation);
				listeners.put(name, computations);
			}
		}
	}

	public void removeListener(Computation computation) {
		for (Map.Entry<String, Set<Computation>> entry : listeners.entrySet()) {
			Set<Computation> computations = entry.getValue();
			computations.remove(computation);
		}
	}

	public Set<Computation> getListeners() {
		Set<Computation> computations = new HashSet<Computation>();
		for (Map.Entry<String, Set<Computation>> entry : listeners.entrySet()) {
			computations.addAll(entry.getValue());
		}
		return computations;
	}

}
