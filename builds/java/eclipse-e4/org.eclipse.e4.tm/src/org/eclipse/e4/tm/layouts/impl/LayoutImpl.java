/**
 * <copyright>
 * </copyright>
 *
 * $Id: LayoutImpl.java,v 1.5 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.layouts.impl;

import org.eclipse.e4.tm.layouts.Layout;
import org.eclipse.e4.tm.layouts.LayoutData;
import org.eclipse.e4.tm.layouts.LayoutsPackage;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Layout</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class LayoutImpl<T extends LayoutData> extends EObjectImpl implements Layout<T> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LayoutImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LayoutsPackage.Literals.LAYOUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public T createLayoutData() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

} //LayoutImpl
