/**
 * <copyright>
 * </copyright>
 *
 * $Id: AbstractSelectionImpl.java,v 1.1 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import org.eclipse.e4.tm.widgets.AbstractSelection;
import org.eclipse.e4.tm.widgets.WidgetsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Selection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.AbstractSelectionImpl#getSelectionEvent <em>Selection Event</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractSelectionImpl extends EObjectImpl implements AbstractSelection {
	/**
	 * The default value of the '{@link #getSelectionEvent() <em>Selection Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionEvent()
	 * @generated
	 * @ordered
	 */
	protected static final Object SELECTION_EVENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSelectionEvent() <em>Selection Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionEvent()
	 * @generated
	 * @ordered
	 */
	protected Object selectionEvent = SELECTION_EVENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractSelectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WidgetsPackage.Literals.ABSTRACT_SELECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getSelectionEvent() {
		return selectionEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectionEvent(Object newSelectionEvent) {
		Object oldSelectionEvent = selectionEvent;
		selectionEvent = newSelectionEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT, oldSelectionEvent, selectionEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT:
				return getSelectionEvent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT:
				setSelectionEvent(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT:
				setSelectionEvent(SELECTION_EVENT_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT:
				return SELECTION_EVENT_EDEFAULT == null ? selectionEvent != null : !SELECTION_EVENT_EDEFAULT.equals(selectionEvent);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (selectionEvent: ");
		result.append(selectionEvent);
		result.append(')');
		return result.toString();
	}

} //AbstractSelectionImpl
