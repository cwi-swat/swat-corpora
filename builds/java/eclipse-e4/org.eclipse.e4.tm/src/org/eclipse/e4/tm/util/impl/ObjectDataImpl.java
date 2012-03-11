/**
 * <copyright>
 * </copyright>
 *
 * $Id: ObjectDataImpl.java,v 1.1 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.impl;

import org.eclipse.e4.tm.util.ObjectData;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Object Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.impl.ObjectDataImpl#getDataObject <em>Data Object</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ObjectDataImpl extends ScriptedImpl implements ObjectData {
	/**
	 * The default value of the '{@link #getDataObject() <em>Data Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataObject()
	 * @generated
	 * @ordered
	 */
	protected static final Object DATA_OBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataObject() <em>Data Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataObject()
	 * @generated
	 * @ordered
	 */
	protected Object dataObject = DATA_OBJECT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ObjectDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UtilPackage.Literals.OBJECT_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getDataObject() {
		return dataObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataObject(Object newDataObject) {
		Object oldDataObject = dataObject;
		dataObject = newDataObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.OBJECT_DATA__DATA_OBJECT, oldDataObject, dataObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UtilPackage.OBJECT_DATA__DATA_OBJECT:
				return getDataObject();
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
			case UtilPackage.OBJECT_DATA__DATA_OBJECT:
				setDataObject(newValue);
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
			case UtilPackage.OBJECT_DATA__DATA_OBJECT:
				setDataObject(DATA_OBJECT_EDEFAULT);
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
			case UtilPackage.OBJECT_DATA__DATA_OBJECT:
				return DATA_OBJECT_EDEFAULT == null ? dataObject != null : !DATA_OBJECT_EDEFAULT.equals(dataObject);
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
		result.append(" (dataObject: ");
		result.append(dataObject);
		result.append(')');
		return result.toString();
	}

} //ObjectDataImpl
