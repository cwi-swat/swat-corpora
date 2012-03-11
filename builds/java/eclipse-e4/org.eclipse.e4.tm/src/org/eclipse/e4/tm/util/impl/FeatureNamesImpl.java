/**
 * <copyright>
 * </copyright>
 *
 * $Id: FeatureNamesImpl.java,v 1.1 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.impl;

import java.util.Collection;

import org.eclipse.e4.tm.util.FeatureNames;
import org.eclipse.e4.tm.util.UtilPackage;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Names</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.impl.FeatureNamesImpl#getFeatureNames <em>Feature Names</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureNamesImpl extends EObjectImpl implements FeatureNames {
	/**
	 * The cached value of the '{@link #getFeatureNames() <em>Feature Names</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureNames()
	 * @generated
	 * @ordered
	 */
	protected EList<String> featureNames;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeatureNamesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UtilPackage.Literals.FEATURE_NAMES;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getFeatureNames() {
		if (featureNames == null) {
			featureNames = new EDataTypeUniqueEList<String>(String.class, this, UtilPackage.FEATURE_NAMES__FEATURE_NAMES);
		}
		return featureNames;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UtilPackage.FEATURE_NAMES__FEATURE_NAMES:
				return getFeatureNames();
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
			case UtilPackage.FEATURE_NAMES__FEATURE_NAMES:
				getFeatureNames().clear();
				getFeatureNames().addAll((Collection<? extends String>)newValue);
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
			case UtilPackage.FEATURE_NAMES__FEATURE_NAMES:
				getFeatureNames().clear();
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
			case UtilPackage.FEATURE_NAMES__FEATURE_NAMES:
				return featureNames != null && !featureNames.isEmpty();
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
		result.append(" (featureNames: ");
		result.append(featureNames);
		result.append(')');
		return result.toString();
	}

} //FeatureNamesImpl
