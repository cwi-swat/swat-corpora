/**
 * 
 */
package org.eclipse.e4.emf.ecore.javascript;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.mozilla.javascript.Context;

public class EmfContext extends Context {

	private DependencyTracker dependencyTracker = null;

	public static void startAddingDependencies() {
		Context cx = Context.enter();
		if (cx instanceof EmfContext) {
			EmfContext emfContext = (EmfContext)cx;
			if (emfContext.dependencyTracker == null) {
				emfContext.dependencyTracker = new DependencyTracker();
			}
			emfContext.dependencyTracker.startAddingDependencies();
		}
	}

	public static void noteDependency(EObject eObject, EStructuralFeature feature, Object value) {
		Context cx = Context.getCurrentContext();
		if (cx instanceof EmfContext) {
			EmfContext emfContext = (EmfContext)cx;
			if (emfContext.dependencyTracker != null) {
				emfContext.dependencyTracker.addDependency(eObject, feature, value);
			}
		}
	}

	public static Notifier stopAddingDependencies() {
		Context cx = Context.getCurrentContext();
		if (cx instanceof EmfContext) {
			EmfContext emfContext = (EmfContext)cx;
			DependencyTracker tracker = emfContext.dependencyTracker;
			if (tracker != null) {
				emfContext.dependencyTracker = null;
				return tracker.stopAddingDependencies();
			}
		}
		return null;
	}

	void clearDependencies() {
		DependencyTracker tracker = dependencyTracker;
		if (tracker != null) {
			dependencyTracker = null;
			tracker.dispose();
		}
	}
}
