/**
 * <copyright>
 * </copyright>
 *
 * $Id: ObjectSelectionImpl.java,v 1.1 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import java.util.Collection;

import org.eclipse.e4.tm.widgets.ObjectSelection;
import org.eclipse.e4.tm.widgets.WidgetsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Object Selection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ObjectSelectionImpl#getSelectedObject <em>Selected Object</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ObjectSelectionImpl#getSelectedObjects <em>Selected Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ObjectSelectionImpl extends AbstractSelectionImpl implements ObjectSelection {
	/**
	 * The default value of the '{@link #getSelectedObject() <em>Selected Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectedObject()
	 * @generated
	 * @ordered
	 */
	protected static final Object SELECTED_OBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSelectedObject() <em>Selected Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectedObject()
	 * @generated
	 * @ordered
	 */
	protected Object selectedObject = SELECTED_OBJECT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSelectedObjects() <em>Selected Objects</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectedObjects()
	 * @generated
	 * @ordered
	 */
	protected EList<Object> selectedObjects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ObjectSelectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WidgetsPackage.Literals.OBJECT_SELECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getSelectedObject() {
		return selectedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectedObject(Object newSelectedObject) {
		Object oldSelectedObject = selectedObject;
		selectedObject = newSelectedObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECT, oldSelectedObject, selectedObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Object> getSelectedObjects() {
		if (selectedObjects == null) {
			selectedObjects = new EDataTypeUniqueEList<Object>(Object.class, this, WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECTS);
		}
		return selectedObjects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECT:
				return getSelectedObject();
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECTS:
				return getSelectedObjects();
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
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECT:
				setSelectedObject(newValue);
				return;
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECTS:
				getSelectedObjects().clear();
				getSelectedObjects().addAll((Collection<? extends Object>)newValue);
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
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECT:
				setSelectedObject(SELECTED_OBJECT_EDEFAULT);
				return;
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECTS:
				getSelectedObjects().clear();
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
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECT:
				return SELECTED_OBJECT_EDEFAULT == null ? selectedObject != null : !SELECTED_OBJECT_EDEFAULT.equals(selectedObject);
			case WidgetsPackage.OBJECT_SELECTION__SELECTED_OBJECTS:
				return selectedObjects != null && !selectedObjects.isEmpty();
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
		result.append(" (selectedObject: ");
		result.append(selectedObject);
		result.append(", selectedObjects: ");
		result.append(selectedObjects);
		result.append(')');
		return result.toString();
	}

} //ObjectSelectionImpl
