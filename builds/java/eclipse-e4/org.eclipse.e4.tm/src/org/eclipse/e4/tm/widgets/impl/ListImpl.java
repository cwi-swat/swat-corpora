/**
 * <copyright>
 * </copyright>
 *
 * $Id: ListImpl.java,v 1.5 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import java.util.Collection;

import org.eclipse.e4.tm.widgets.AbstractSelection;
import org.eclipse.e4.tm.widgets.IndexSelection;
import org.eclipse.e4.tm.widgets.List;
import org.eclipse.e4.tm.widgets.WidgetsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListImpl#getSelectionEvent <em>Selection Event</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListImpl#getSelectionIndex <em>Selection Index</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListImpl#getSelectionIndices <em>Selection Indices</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListImpl#getItems <em>Items</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ListImpl extends ControlImpl implements List {
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
	 * The default value of the '{@link #getSelectionIndex() <em>Selection Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SELECTION_INDEX_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getSelectionIndex() <em>Selection Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionIndex()
	 * @generated
	 * @ordered
	 */
	protected int selectionIndex = SELECTION_INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSelectionIndices() <em>Selection Indices</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> selectionIndices;

	/**
	 * The cached value of the '{@link #getItems() <em>Items</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getItems()
	 * @generated
	 * @ordered
	 */
	protected EList<String> items;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WidgetsPackage.Literals.LIST;
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
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST__SELECTION_EVENT, oldSelectionEvent, selectionEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSelectionIndex() {
		return selectionIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectionIndex(int newSelectionIndex) {
		int oldSelectionIndex = selectionIndex;
		selectionIndex = newSelectionIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST__SELECTION_INDEX, oldSelectionIndex, selectionIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getSelectionIndices() {
		if (selectionIndices == null) {
			selectionIndices = new EDataTypeUniqueEList<Integer>(Integer.class, this, WidgetsPackage.LIST__SELECTION_INDICES);
		}
		return selectionIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getItems() {
		if (items == null) {
			items = new EDataTypeEList<String>(String.class, this, WidgetsPackage.LIST__ITEMS);
		}
		return items;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WidgetsPackage.LIST__SELECTION_EVENT:
				return getSelectionEvent();
			case WidgetsPackage.LIST__SELECTION_INDEX:
				return getSelectionIndex();
			case WidgetsPackage.LIST__SELECTION_INDICES:
				return getSelectionIndices();
			case WidgetsPackage.LIST__ITEMS:
				return getItems();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WidgetsPackage.LIST__SELECTION_EVENT:
				setSelectionEvent(newValue);
				return;
			case WidgetsPackage.LIST__SELECTION_INDEX:
				setSelectionIndex((Integer)newValue);
				return;
			case WidgetsPackage.LIST__SELECTION_INDICES:
				getSelectionIndices().clear();
				getSelectionIndices().addAll((Collection<? extends Integer>)newValue);
				return;
			case WidgetsPackage.LIST__ITEMS:
				getItems().clear();
				getItems().addAll((Collection<? extends String>)newValue);
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
			case WidgetsPackage.LIST__SELECTION_EVENT:
				setSelectionEvent(SELECTION_EVENT_EDEFAULT);
				return;
			case WidgetsPackage.LIST__SELECTION_INDEX:
				setSelectionIndex(SELECTION_INDEX_EDEFAULT);
				return;
			case WidgetsPackage.LIST__SELECTION_INDICES:
				getSelectionIndices().clear();
				return;
			case WidgetsPackage.LIST__ITEMS:
				getItems().clear();
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
			case WidgetsPackage.LIST__SELECTION_EVENT:
				return SELECTION_EVENT_EDEFAULT == null ? selectionEvent != null : !SELECTION_EVENT_EDEFAULT.equals(selectionEvent);
			case WidgetsPackage.LIST__SELECTION_INDEX:
				return selectionIndex != SELECTION_INDEX_EDEFAULT;
			case WidgetsPackage.LIST__SELECTION_INDICES:
				return selectionIndices != null && !selectionIndices.isEmpty();
			case WidgetsPackage.LIST__ITEMS:
				return items != null && !items.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == AbstractSelection.class) {
			switch (derivedFeatureID) {
				case WidgetsPackage.LIST__SELECTION_EVENT: return WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT;
				default: return -1;
			}
		}
		if (baseClass == IndexSelection.class) {
			switch (derivedFeatureID) {
				case WidgetsPackage.LIST__SELECTION_INDEX: return WidgetsPackage.INDEX_SELECTION__SELECTION_INDEX;
				case WidgetsPackage.LIST__SELECTION_INDICES: return WidgetsPackage.INDEX_SELECTION__SELECTION_INDICES;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == AbstractSelection.class) {
			switch (baseFeatureID) {
				case WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT: return WidgetsPackage.LIST__SELECTION_EVENT;
				default: return -1;
			}
		}
		if (baseClass == IndexSelection.class) {
			switch (baseFeatureID) {
				case WidgetsPackage.INDEX_SELECTION__SELECTION_INDEX: return WidgetsPackage.LIST__SELECTION_INDEX;
				case WidgetsPackage.INDEX_SELECTION__SELECTION_INDICES: return WidgetsPackage.LIST__SELECTION_INDICES;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(", selectionIndex: ");
		result.append(selectionIndex);
		result.append(", selectionIndices: ");
		result.append(selectionIndices);
		result.append(", items: ");
		result.append(items);
		result.append(')');
		return result.toString();
	}

} //ListImpl
