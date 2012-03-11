/**
 * 
 */
package org.eclipse.e4.emf.ecore.javascript;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.NotifierImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IDisposable;

public class DependencyTracker extends NotifierImpl implements IDisposable {

	private List<EObjectFeatureDependency> attachedDependencies = new ArrayList<EObjectFeatureDependency>();
	private List<EObjectFeatureDependency> newDependencies = null;

	void startAddingDependencies() {
		newDependencies = new ArrayList<EObjectFeatureDependency>();
	}

	Notifier stopAddingDependencies() {
		if (newDependencies != null) {
			for (EObjectFeatureDependency dependency: newDependencies) {
				dependency.setNotifier(this);
				attachedDependencies.add(dependency);
			}
			newDependencies = null;
		}
		return this;
	}

	void addDependency(EObject eObject, EStructuralFeature feature, Object value) {
		if (newDependencies != null) {
			EObjectFeatureDependency dependency = new EObjectFeatureDependency(eObject, feature);
			newDependencies.add(dependency);
		}
	}

	public void dispose() {
		for (Iterator<EObjectFeatureDependency> it = attachedDependencies.iterator(); it.hasNext();) {
			EObjectFeatureDependency dependency = (EObjectFeatureDependency)it.next();
			dependency.dispose();
		}
		attachedDependencies.clear();
		if (newDependencies != null) {
			for (Iterator<EObjectFeatureDependency> it = newDependencies.iterator(); it.hasNext();) {
				EObjectFeatureDependency dependency = (EObjectFeatureDependency)it.next();
				dependency.dispose();
			}
			newDependencies = null;
		}
	}
}
