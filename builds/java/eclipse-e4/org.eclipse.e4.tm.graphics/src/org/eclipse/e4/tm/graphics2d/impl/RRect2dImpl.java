/**
 * <copyright>
 * </copyright>
 *
 * $Id: RRect2dImpl.java,v 1.1 2009/08/27 09:15:03 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d.impl;

import org.eclipse.e4.tm.graphics.util.Dimension;

import org.eclipse.e4.tm.graphics2d.Graphics2dPackage;
import org.eclipse.e4.tm.graphics2d.RRect2d;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RRect2d</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.RRect2dImpl#getCornerSize <em>Corner Size</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RRect2dImpl extends Rect2dImpl implements RRect2d {
	/**
	 * The default value of the '{@link #getCornerSize() <em>Corner Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCornerSize()
	 * @generated
	 * @ordered
	 */
	protected static final Dimension CORNER_SIZE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCornerSize() <em>Corner Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCornerSize()
	 * @generated
	 * @ordered
	 */
	protected Dimension cornerSize = CORNER_SIZE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RRect2dImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Graphics2dPackage.Literals.RRECT2D;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dimension getCornerSize() {
		return cornerSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCornerSize(Dimension newCornerSize) {
		Dimension oldCornerSize = cornerSize;
		cornerSize = newCornerSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.RRECT2D__CORNER_SIZE, oldCornerSize, cornerSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Graphics2dPackage.RRECT2D__CORNER_SIZE:
				return getCornerSize();
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
			case Graphics2dPackage.RRECT2D__CORNER_SIZE:
				setCornerSize((Dimension)newValue);
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
			case Graphics2dPackage.RRECT2D__CORNER_SIZE:
				setCornerSize(CORNER_SIZE_EDEFAULT);
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
			case Graphics2dPackage.RRECT2D__CORNER_SIZE:
				return CORNER_SIZE_EDEFAULT == null ? cornerSize != null : !CORNER_SIZE_EDEFAULT.equals(cornerSize);
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
		result.append(" (cornerSize: ");
		result.append(cornerSize);
		result.append(')');
		return result.toString();
	}

} //RRect2dImpl
