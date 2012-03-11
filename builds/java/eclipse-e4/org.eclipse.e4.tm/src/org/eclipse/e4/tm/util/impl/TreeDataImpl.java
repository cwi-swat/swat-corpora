/**
 * <copyright>
 * </copyright>
 *
 * $Id: TreeDataImpl.java,v 1.1 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.impl;

import org.eclipse.e4.tm.util.TreeData;
import org.eclipse.e4.tm.util.UtilPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tree Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.impl.TreeDataImpl#getParentDataObject <em>Parent Data Object</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.util.impl.TreeDataImpl#isLeaf <em>Leaf</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TreeDataImpl extends ListDataImpl implements TreeData {
	/**
	 * The default value of the '{@link #getParentDataObject() <em>Parent Data Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentDataObject()
	 * @generated
	 * @ordered
	 */
	protected static final Object PARENT_DATA_OBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParentDataObject() <em>Parent Data Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentDataObject()
	 * @generated
	 * @ordered
	 */
	protected Object parentDataObject = PARENT_DATA_OBJECT_EDEFAULT;

	/**
	 * The default value of the '{@link #isLeaf() <em>Leaf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLeaf()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LEAF_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLeaf() <em>Leaf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLeaf()
	 * @generated
	 * @ordered
	 */
	protected boolean leaf = LEAF_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TreeDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UtilPackage.Literals.TREE_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getParentDataObject() {
		return parentDataObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentDataObject(Object newParentDataObject) {
		Object oldParentDataObject = parentDataObject;
		parentDataObject = newParentDataObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.TREE_DATA__PARENT_DATA_OBJECT, oldParentDataObject, parentDataObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeaf(boolean newLeaf) {
		boolean oldLeaf = leaf;
		leaf = newLeaf;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.TREE_DATA__LEAF, oldLeaf, leaf));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UtilPackage.TREE_DATA__PARENT_DATA_OBJECT:
				return getParentDataObject();
			case UtilPackage.TREE_DATA__LEAF:
				return isLeaf();
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
			case UtilPackage.TREE_DATA__PARENT_DATA_OBJECT:
				setParentDataObject(newValue);
				return;
			case UtilPackage.TREE_DATA__LEAF:
				setLeaf((Boolean)newValue);
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
			case UtilPackage.TREE_DATA__PARENT_DATA_OBJECT:
				setParentDataObject(PARENT_DATA_OBJECT_EDEFAULT);
				return;
			case UtilPackage.TREE_DATA__LEAF:
				setLeaf(LEAF_EDEFAULT);
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
			case UtilPackage.TREE_DATA__PARENT_DATA_OBJECT:
				return PARENT_DATA_OBJECT_EDEFAULT == null ? parentDataObject != null : !PARENT_DATA_OBJECT_EDEFAULT.equals(parentDataObject);
			case UtilPackage.TREE_DATA__LEAF:
				return leaf != LEAF_EDEFAULT;
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
		result.append(" (parentDataObject: ");
		result.append(parentDataObject);
		result.append(", leaf: ");
		result.append(leaf);
		result.append(')');
		return result.toString();
	}

} //TreeDataImpl
