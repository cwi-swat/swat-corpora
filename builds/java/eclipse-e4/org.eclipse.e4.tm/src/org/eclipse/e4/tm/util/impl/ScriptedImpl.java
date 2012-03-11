/**
 * <copyright>
 * </copyright>
 *
 * $Id: ScriptedImpl.java,v 1.1 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.impl;

import org.eclipse.e4.tm.util.Scripted;
import org.eclipse.e4.tm.util.UtilPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scripted</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.impl.ScriptedImpl#getScriptSource <em>Script Source</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ScriptedImpl extends EObjectImpl implements Scripted {
	/**
	 * The default value of the '{@link #getScriptSource() <em>Script Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScriptSource()
	 * @generated
	 * @ordered
	 */
	protected static final String SCRIPT_SOURCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getScriptSource() <em>Script Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScriptSource()
	 * @generated
	 * @ordered
	 */
	protected String scriptSource = SCRIPT_SOURCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScriptedImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UtilPackage.Literals.SCRIPTED;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getScriptSource() {
		return scriptSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScriptSource(String newScriptSource) {
		String oldScriptSource = scriptSource;
		scriptSource = newScriptSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UtilPackage.SCRIPTED__SCRIPT_SOURCE, oldScriptSource, scriptSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UtilPackage.SCRIPTED__SCRIPT_SOURCE:
				return getScriptSource();
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
			case UtilPackage.SCRIPTED__SCRIPT_SOURCE:
				setScriptSource((String)newValue);
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
			case UtilPackage.SCRIPTED__SCRIPT_SOURCE:
				setScriptSource(SCRIPT_SOURCE_EDEFAULT);
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
			case UtilPackage.SCRIPTED__SCRIPT_SOURCE:
				return SCRIPT_SOURCE_EDEFAULT == null ? scriptSource != null : !SCRIPT_SOURCE_EDEFAULT.equals(scriptSource);
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
		result.append(" (scriptSource: ");
		result.append(scriptSource);
		result.append(')');
		return result.toString();
	}

} //ScriptedImpl
