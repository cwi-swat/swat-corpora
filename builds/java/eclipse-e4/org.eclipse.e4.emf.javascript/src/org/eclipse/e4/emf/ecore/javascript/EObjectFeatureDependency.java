/**
 * 
 */
package org.eclipse.e4.emf.ecore.javascript;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IDisposable;

class EObjectFeatureDependency extends AdapterImpl implements IDisposable {

	private EObject eObject;
	private EStructuralFeature feature;

	public EObjectFeatureDependency(EObject eObject, EStructuralFeature feature) {
		super();
		this.eObject = eObject;
		this.feature = feature;
	}

	public void dispose() {
		setNotifier(null);
		Notifier notifier = eObject;
		eObject = null;
		feature = null;
		notifier.eAdapters().remove(this);
	}
	
	protected boolean isRelevantNotification(Notification notification) {
		return notification.getNotifier() == eObject && notification.getFeature() == feature && notification.getEventType() != Notification.REMOVING_ADAPTER;
	}

	private Notifier notifier;

	public void notifyChanged(Notification notification) {
		if (isRelevantNotification(notification)) {
			if (notifier != null) {
				notifier.eNotify(notification);
			}
		}
	}
	
	void setNotifier(Notifier notifier) {
		this.notifier = notifier;
		eObject.eAdapters().add(this);
	}
}